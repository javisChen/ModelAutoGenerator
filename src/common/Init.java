package common;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import constants.SQLConst;
import utils.DbUtils;
import utils.StringUtils;

public class Init {

	public static List<String> TABLES = null;
	public static List<String> CONTAIN_DATE_TABLES = null;
	public static List<String[]> COLUMNS = null;
	
	static {
		init();
	}
	
	private static void init() {

		TABLES = new ArrayList<String>();
		COLUMNS = new ArrayList<String[]>();
		CONTAIN_DATE_TABLES = new ArrayList<String>();
		try {
			DbUtils.execute(SQLConst.SQL_QUERY_TABLE);
			while (DbUtils.rs.next()) {
				TABLES.add(StringUtils.toCamelCase((DbUtils.rs.getString("TABLE_NAME")), 1));
			}
			DbUtils.execute(SQLConst.SQL_QUERY_COLUMN);
			while (DbUtils.rs.next()) {
				String column[] = new String[4];
				column[0] = StringUtils.toCamelCase(DbUtils.rs.getString("TABLE_NAME"), 1);
				column[1] = DbUtils.rs.getString("COLUMN_NAME");
				column[2] = StringUtils.convertToJavaType(DbUtils.rs.getString("COLUMN_TYPE"));
				column[3] = DbUtils.rs.getString("COLUMN_COMMENT");
				COLUMNS.add(column);
			}
			DbUtils.execute(SQLConst.SQL_QUERY_CONTAIN_DATETYPE_TABLE);
			while (DbUtils.rs.next()) {
				CONTAIN_DATE_TABLES.add(StringUtils.toCamelCase((DbUtils.rs.getString("TABLE_NAME")), 1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.closeConn();
		}
	}
	
	public static void main(String[] args) {
		for (String string : TABLES) {
			System.out.println(string);
		}
		for (String string : CONTAIN_DATE_TABLES) {
			System.out.println(string);
		}
		for (String[] string : COLUMNS) {
			System.out.println(string[0]);
		}
	}
}
