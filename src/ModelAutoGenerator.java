

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 连接mysql自动生成模型
 * @author Javis
 */
public class ModelAutoGenerator {

	/** JDBC */
	private static Connection connection = null;
	private static PreparedStatement statement = null;
	private static ResultSet rs = null;
	/** 数据库名 */
	private static final String DATABASE_NAME = "szyt";
	/** 服务器地址 */
	private static final String SERVER_ADDRESS = "112.74.72.3";
	/** url */
	private static final String URL = "jdbc:mysql://server_address:3306/dbname?characterEncoding=utf-8".replace("dbname", DATABASE_NAME).replace("server_address", SERVER_ADDRESS);
	/** username */
	private static final String USERNAME = "root";
	/** password */
	private static final String PASSWORD = "nixihuan";
	/** 查询数据库中所有的表名SQL */
//	private static final String SQL_QUERY_TABLE = "SELECT TABLE_NAME FROM information_schema.TABLES WHERE TABLE_SCHEMA = 'DATABASE_NAME'".replace("DATABASE_NAME", DATABASE_NAME);
	private static final String SQL_QUERY_TABLE = "SELECT TABLE_NAME FROM information_schema.TABLES WHERE TABLE_NAME = 'notice_message_tb'";
	/** 查询当前表的所有字段名称和类型 */
	private static final String SQL_QUERY_COLUMN = "SELECT t.TABLE_NAME,c.COLUMN_NAME,c.COLUMN_TYPE,c.COLUMN_COMMENT FROM information_schema.`TABLES` t  LEFT JOIN information_schema.`COLUMNS` c ON t.TABLE_NAME = c.TABLE_NAME WHERE t.TABLE_SCHEMA = 'DATABASE_NAME'".replace("DATABASE_NAME", DATABASE_NAME);
	/**查询字段类型有date或datetime类型的表  */
	private static final String SQL_QUERY_CONTAIN_DATETYPE_TABLE = "SELECT t.TABLE_NAME FROM information_schema.`TABLES` t  LEFT JOIN information_schema.`COLUMNS` c ON t.TABLE_NAME = c.TABLE_NAME WHERE t.TABLE_SCHEMA = 'DATABASE_NAME' AND (c.COLUMN_TYPE = 'date'	OR c.COLUMN_TYPE = 'datetime') GROUP BY	t.TABLE_NAME".replace("DATABASE_NAME", DATABASE_NAME);
	/** 项目根路径 */
	private static final String ROOT_PATH = System.getProperty("user.dir");
	/** 实体类存放位置 */
	private static final String SAVE_ENTITY_PATH = ROOT_PATH + "/src/main/java/com/szyt/web/vo";
	/** 数据访问接口存放位置 */
	private static final String SAVE_IDAO_PATH = ROOT_PATH + "/src/main/java/com/szyt/web/dao/api";
	/** MybatisMapper存放位置 */
	private static final String SAVE_MAPPER_PATH = ROOT_PATH + "/src/main/java/com/szyt/web/dao/mapper";
	/** 业务接口存放位置 */
	private static final String SAVE_ISERVICE_PATH = ROOT_PATH + "/src/main/java/com/szyt/web/service/api";
	/** 业务实现类存放位置 */
	private static final String SAVE_SERVICE_PATH = ROOT_PATH + "/src/main/java/com/szyt/web/service/impl";

	/**
	 * 生成实体类文件
	 */
	public static void generateModelFile() {

		long startTime = System.currentTimeMillis();
		List<String> tableList = getTableList();
		List<String> containsDateTypeTableList = getContainsDateTypeTableList();
		List<String[]> columnList = getColumnList();
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
	 * @param tableList
	 * @param containsDateTypeTableList
	 * @param columnList
	 * @throws IOException 
	 */
	public static void generateEntitys(List<String> tableList, List<String> containsDateTypeTableList, List<String[]> columnList) throws IOException {

		BufferedWriter bw = null;
		try {
			for (String tableName : tableList) {
				File parentFile = new File(SAVE_ENTITY_PATH);
				if (!parentFile.exists()) {
					parentFile.mkdirs();
				}
				File file = new File(SAVE_ENTITY_PATH + "/Entity.java".replace("Entity", tableName));
				file.createNewFile();
				StringBuilder builder = new StringBuilder();
				bw = new BufferedWriter(new FileWriter(file));
				builder.append("package com.szyt.web.vo;\r\n\r\n");
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
						builder.append("\tprivate type name;\r\n\r\n".replace("type", c[2]).replace("name", toCamelCase(c[1], 2)));
					}
				}
				for (String[] c : columnList) {
					if (c[0].equals(tableName)) {
						builder.append("\tpublic type getAttribute() {\r\n"
								.replace("type", c[2])
								.replace("Attribute", toCamelCase(c[1], 1)));
						builder.append("\t\treturn attribute;\r\n\t}\r\n\r\n".replace("attribute", toCamelCase(c[1], 2)));
						builder.append("\tpublic void setAttribute(paramType paramName) {\r\n"
								.replace("Attribute", toCamelCase(c[1], 1))
								.replace("paramType", c[2])
								.replace("paramName", c[1]));
						builder.append("\t\tthis.attribute = paramName;\r\n\t}\r\n\r\n".replace("attribute", toCamelCase(c[1], 2))
								.replace("paramName", c[1]));
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
	 * @param tableList
	 * @throws IOException
	 */
	public static void generateDataAccessObjectApi(List<String> tableList) throws IOException {

		BufferedWriter bw = null;
		try {
			for (String tableName : tableList) {
				File file = new File(SAVE_IDAO_PATH);
				if (!file.exists()) {
					file.mkdirs();
				}
				file = new File(SAVE_IDAO_PATH + "/IDaoNameDao.java".replace("DaoName", tableName));
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
	 * @param tableList
	 * @throws IOException
	 */
	public static void generateMyBatisMapper(List<String> tableList, List<String[]> columnList) throws IOException {

		BufferedWriter bw = null;
		try {
			for (String tableName : tableList) {
				File file = new File(SAVE_MAPPER_PATH);
				if (!file.exists()) {
					file.mkdirs();
				}
				file = new File(SAVE_MAPPER_PATH + "/ApiNameMapper.xml".replace("ApiName", tableName));
				file.createNewFile();
				bw = new BufferedWriter(new FileWriter(file));
				StringBuilder builder = new StringBuilder();
				builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n");
				builder.append("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\r\n");
				builder.append("<mapper namespace=\"com.szyt.web.dao.api.IDaoNameDao\">\r\n\r\n".replace("DaoName", tableName));
				builder.append("\t<resultMap id=\"EntityNameResultMap\" type=\"com.szyt.web.vo.EntityName\">\r\n".replace("EntityName", tableName));

				String initial = tableName.substring(0, 1).toLowerCase();
				for (String[] c : columnList) {
					if (c[0].equals(tableName)) {
						if (c[1].equals("id")) {
							builder.append("\t\t<id column=\"initialid\" property=\"id\" />\r\n".replace("initial", initial));
						} else {
							builder.append("\t\t<result column=\"columnName\" property=\"propertyName\" />\r\n".replace("columnName", initial + c[1]).replace("propertyName", toCamelCase(c[1], 2)));
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
	 * @param tableList
	 * @throws IOException
	 */
	public static void generateServiceApi(List<String> tableList) throws IOException {

		BufferedWriter bw = null;
		try {
			for (String tableName : tableList) {
				File file = new File(SAVE_ISERVICE_PATH);
				if (!file.exists()) {
					file.mkdirs();
				}
				file = new File(SAVE_ISERVICE_PATH + "/IServiceNameService.java".replace("ServiceName", tableName));
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
	 * @param tableList
	 * @throws IOException
	 */
	public static void generateService(List<String> tableList) throws IOException {

		BufferedWriter bw = null;
		try {
			for (String tableName : tableList) {
				File file = new File(SAVE_SERVICE_PATH);
				if (!file.exists()) {
					file.mkdirs();
				}
				file.createNewFile();
				file = new File(SAVE_SERVICE_PATH + "/ServiceNameServiceImpl.java".replace("ServiceName", tableName));
				bw = new BufferedWriter(new FileWriter(file));
				StringBuilder builder = new StringBuilder();
				builder.append("package com.szyt.web.service.impl;\r\n\r\n");
				builder.append("import org.springframework.stereotype.Service;\r\n\r\n");
				builder.append("import com.szyt.web.service.api.ISerivceNameService;\r\n\r\n".replace("SerivceName", tableName));
				builder.append("@Service\r\n");
				builder.append("public class ServiceNameServiceImpl implements IServiceNameService {\r\n\r\n}".replace("ServiceName", tableName).replace("IServiceName", "I" + tableName));
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
	 * 把字段类型转换成Java类型
	 * 
	 * @param type
	 * @return type
	 */
	public static String convertToJavaType(String type) {
		if (type.contains("varchar")) {
			return "String";
		} else if (type.contains("date")) {
			return "Date";
		} else if (type.contains("text")) {
			return "String";
		} else if (type.contains("int")) {
			return "int";
		} else if (type.contains("blob")) {
			return "String";
		}
		return null;
	}
	
	/**
	 * 转换成驼峰命名法
	 * @param name
	 * @param type 1.全部单词首字母大写  2.从第二个单词开始首字母大写
	 * @return
	 */
	public static String toCamelCase(String name, int type) {
		String[] words = name.split("_");
		StringBuilder result = type == 1 ? new StringBuilder() : new StringBuilder(words[0]);
		for (int i = 0; i < words.length; i++) {
			if (type == 1) {
				if (!words[i].equals("tb")) {
					result.append(words[i].substring(0, 1).toUpperCase() + words[i].substring(1, words[i].length()));
				}
			} else {
				if (i > 0) {
					result.append(words[i].substring(0, 1).toUpperCase() + words[i].substring(1, words[i].length()));
				}
			}
		}
		return result.toString();
	}
	
	/**
	 * 查询所有表名
	 * @return
	 */
	public static List<String> getTableList() {
		List<String> tables = new ArrayList<String>();
		try {
			openConnection();
			statement = connection.prepareStatement(SQL_QUERY_TABLE);
			rs = statement.executeQuery();
			while (rs.next()) {
				tables.add(toCamelCase((rs.getString("TABLE_NAME")), 1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection();
		}
		return tables;
	}

	/**
	 * 查询所有包含date或datetime类型的表
	 * 
	 * @return
	 */
	public static List<String> getContainsDateTypeTableList() {
		List<String> tables = new ArrayList<String>();
		try {
			openConnection();
			statement = connection.prepareStatement(SQL_QUERY_CONTAIN_DATETYPE_TABLE);
			rs = statement.executeQuery();
			while (rs.next()) {
				tables.add(toCamelCase((rs.getString("TABLE_NAME")), 1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection();
		}
		return tables;
	}

	/**
	 * 查询所有列名,类型
	 * 
	 * @return
	 */
	public static List<String[]> getColumnList() {
		List<String[]> columnList = new ArrayList<String[]>();
		try {
			openConnection();
			statement = connection.prepareStatement(SQL_QUERY_COLUMN);
			rs = statement.executeQuery();
			while (rs.next()) {
				String column[] = new String[4];
				column[0] = toCamelCase(rs.getString("TABLE_NAME"), 1);
				column[1] = rs.getString("COLUMN_NAME");
				column[2] = convertToJavaType(rs.getString("COLUMN_TYPE"));
				column[3] = rs.getString("COLUMN_COMMENT");
				columnList.add(column);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection();
		}
		return columnList;
	}

	/**
	 * 检查是否存在date或datetime类型的字段
	 */
	public static boolean checkDateTypeExists() {
		List<String[]> columns = getColumnList();
		for (String[] c : columns) {
			if (c[2].contains("Date")) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 打开数据库连接
	 */
	public static void openConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
		} catch (ClassNotFoundException e) {
			System.err.println("找不到驱动程序类，加载驱动失败！");
			e.printStackTrace();
		} catch (SQLException e) {
			System.err.println("数据库连接失败,请检查参数是否正确！");
			e.printStackTrace();
		}
	}

	/**
	 * 关闭数据库连接释放资源
	 */
	public static void closeConnection() {
		try {
			if (rs != null) {
				rs.close();
			}
			if (statement != null) {
				statement.close();
			}
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		ModelAutoGenerator.generateModelFile();
	}
}
