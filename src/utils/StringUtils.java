package utils;

public class StringUtils {

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
	 * 
	 * @param name
	 * @param type 1.全部单词首字母大写 2.从第二个单词开始首字母大写
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
}
