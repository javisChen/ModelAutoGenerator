package core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import common.Init;
import constants.DbConst;
import constants.PathConst;
import utils.StringUtils;

/**
 * 连接mysql自动生成模型
 * @author Javis
 */
public class ModelAutoGenerator {

	/**
	 * 生成实体类文件
	 */
	public static void generateModelFile() {

		long startTime = System.currentTimeMillis();
		List<String> tableList = Init.TABLES;
		List<String> containsDateTypeTableList = Init.CONTAIN_DATE_TABLES;
		List<String[]> columnList = Init.COLUMNS;
		try {
			generateEntitys(tableList, containsDateTypeTableList, columnList);
			generateDataAccessObjectApi(tableList);
			generateMyBatisMapper(tableList, columnList);
			generateServiceApi(tableList);
			generateService(tableList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		long endTime = System.currentTimeMillis();
		System.out.println("模型生成成功！耗时：" + (endTime - startTime) / 1000 + "秒");
	}

	/**
	 * 生成实体类
	 * 
	 * @param tableList
	 * @param containsDateTypeTableList
	 * @param columnList
	 * @throws IOException
	 */
	public static void generateEntitys(List<String> tableList, List<String> containsDateTypeTableList,
			List<String[]> columnList) throws IOException {

		BufferedWriter bw = null;
		try {
			for (String tableName : tableList) {
				File parentFile = new File(PathConst.SAVE_ENTITY_PATH);
				if (!parentFile.exists()) {
					parentFile.mkdirs();
				}
				File file = new File(PathConst.SAVE_ENTITY_PATH + "/Entity.java".replace("Entity", tableName));
				file.createNewFile();
				StringBuilder builder = new StringBuilder();
				bw = new BufferedWriter(new FileWriter(file));
				builder.append("package com.szyt." + DbConst.PROJECT_NAME + ".po;\r\n\r\n");
				for (String cdttableName : containsDateTypeTableList) {
					if (cdttableName.equals(tableName)) {
						builder.append("import java.util.Date;\r\n\r\n");
					}
				}
				builder.append("public class entityName {\r\n\r\n".replace("entityName", tableName));
				for (String[] c : columnList) {
					if (c[0].equals(tableName)) {
						if (c[3] != "") {
							builder.append("\t/** comment */\r\n".replace("comment", c[3]));
						}
						builder.append("\tprivate type name;\r\n\r\n".replace("type", c[2]).replace("name",
								StringUtils.toCamelCase(c[1], 2)));
					}
				}
				for (String[] c : columnList) {
					if (c[0].equals(tableName)) {
						builder.append("\tpublic type getAttribute() {\r\n".replace("type", c[2]).replace("Attribute",
								StringUtils.toCamelCase(c[1], 1)));
						builder.append(
								"\t\treturn attribute;\r\n\t}\r\n\r\n".replace("attribute", StringUtils.toCamelCase(c[1], 2)));
						builder.append("\tpublic void setAttribute(paramType paramName) {\r\n"
								.replace("Attribute", StringUtils.toCamelCase(c[1], 1)).replace("paramType", c[2])
								.replace("paramName", c[1]));
						builder.append("\t\tthis.attribute = paramName;\r\n\t}\r\n\r\n"
								.replace("attribute", StringUtils.toCamelCase(c[1], 2)).replace("paramName", c[1]));
					}
				}
				builder.append("\tpublic EntityName() {\r\n\r\n\t}\r\n\r\n".replace("EntityName", tableName));
				builder.append("}");
				bw.write(builder.toString());
				bw.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			bw.close();
		}
	}

	/**
	 * 生成数据访问接口
	 * 
	 * @param tableList
	 * @throws IOException
	 */
	public static void generateDataAccessObjectApi(List<String> tableList) throws IOException {

		BufferedWriter bw = null;
		try {
			for (String tableName : tableList) {
				File file = new File(PathConst.SAVE_IDAO_PATH);
				if (!file.exists()) {
					file.mkdirs();
				}
				file = new File(PathConst.SAVE_IDAO_PATH + "/IDaoNameDao.java".replace("DaoName", tableName));
				file.createNewFile();
				bw = new BufferedWriter(new FileWriter(file));
				StringBuilder builder = new StringBuilder();
				builder.append("package com.szyt.web.dao.api;\r\n\r\n");
				builder.append("public interface IDaoNameDao {\r\n\r\n}".replace("DaoName", tableName));
				bw.write(builder.toString());
				bw.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			bw.close();
		}
	}

	/**
	 * 生成Mapper
	 * 
	 * @param tableList
	 * @throws IOException
	 */
	public static void generateMyBatisMapper(List<String> tableList, List<String[]> columnList) throws IOException {

		BufferedWriter bw = null;
		try {
			for (String tableName : tableList) {
				File file = new File(PathConst.SAVE_MAPPER_PATH);
				if (!file.exists()) {
					file.mkdirs();
				}
				file = new File(PathConst.SAVE_MAPPER_PATH + "/ApiNameMapper.xml".replace("ApiName", tableName));
				file.createNewFile();
				bw = new BufferedWriter(new FileWriter(file));
				StringBuilder builder = new StringBuilder();
				builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n");
				builder.append(
						"<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\r\n");
				builder.append("<mapper namespace=\"com.szyt.web.dao.api.IDaoNameDao\">\r\n\r\n".replace("DaoName",
						tableName));
				builder.append("\t<resultMap id=\"EntityNameResultMap\" type=\"com.szyt.web.vo.EntityName\">\r\n"
						.replace("EntityName", tableName));

				String initial = tableName.substring(0, 1).toLowerCase();
				for (String[] c : columnList) {
					if (c[0].equals(tableName)) {
						if (c[1].equals("id")) {
							builder.append(
									"\t\t<id column=\"initialid\" property=\"id\" />\r\n".replace("initial", initial));
						} else {
							builder.append("\t\t<result column=\"columnName\" property=\"propertyName\" />\r\n"
									.replace("columnName", initial + c[1])
									.replace("propertyName", StringUtils.toCamelCase(c[1], 2)));
						}
					}
				}
				builder.append("\t</resultMap>\r\n</mapper>");
				bw.write(builder.toString());
				bw.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			bw.close();
		}
	}

	/**
	 * 生成业务接口
	 * 
	 * @param tableList
	 * @throws IOException
	 */
	public static void generateServiceApi(List<String> tableList) throws IOException {

		BufferedWriter bw = null;
		try {
			for (String tableName : tableList) {
				File file = new File(PathConst.SAVE_ISERVICE_PATH);
				if (!file.exists()) {
					file.mkdirs();
				}
				file = new File(PathConst.SAVE_ISERVICE_PATH + "/IServiceNameService.java".replace("ServiceName", tableName));
				file.createNewFile();
				bw = new BufferedWriter(new FileWriter(file));
				StringBuilder builder = new StringBuilder();
				builder.append("package com.szyt.web.service.api;\r\n\r\n");
				builder.append("public interface IServiceNameService {\r\n\r\n}".replace("ServiceName", tableName));
				bw.write(builder.toString());
				bw.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			bw.close();
		}
	}

	/**
	 * 生成业务实现类
	 * 
	 * @param tableList
	 * @throws IOException
	 */
	public static void generateService(List<String> tableList) throws IOException {

		BufferedWriter bw = null;
		try {
			for (String tableName : tableList) {
				File file = new File(PathConst.SAVE_SERVICE_PATH);
				if (!file.exists()) {
					file.mkdirs();
				}
				file.createNewFile();
				file = new File(PathConst.SAVE_SERVICE_PATH + "/ServiceNameServiceImpl.java".replace("ServiceName", tableName));
				bw = new BufferedWriter(new FileWriter(file));
				StringBuilder builder = new StringBuilder();
				builder.append("package com.szyt.web.service.impl;\r\n\r\n");
				builder.append("import org.springframework.stereotype.Service;\r\n\r\n");
				builder.append("import com.szyt.web.service.api.ISerivceNameService;\r\n\r\n".replace("SerivceName",
						tableName));
				builder.append("@Service\r\n");
				builder.append("public class ServiceNameServiceImpl implements IServiceNameService {\r\n\r\n}"
						.replace("ServiceName", tableName).replace("IServiceName", "I" + tableName));
				bw.write(builder.toString());
				bw.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			bw.close();
		}
	}

	/**
	 * 检查是否存在date或datetime类型的字段
	 */
	public static boolean checkDateTypeExists() {
		List<String[]> columns = Init.COLUMNS;
		for (String[] c : columns) {
			if (c[2].contains("Date")) {
				return true;
			}
		}
		return false;
	}

	public static void main(String[] args) {
		ModelAutoGenerator.generateModelFile();
	}
}
