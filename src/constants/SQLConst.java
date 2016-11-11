package constants;

public class SQLConst {

	/** 查询数据库中所有的表名SQL */
	public static final String SQL_QUERY_TABLE = "SELECT TABLE_NAME FROM information_schema.TABLES WHERE TABLE_SCHEMA ='DATABASE_NAME'"
			.replace("DATABASE_NAME", DbConst.DATABASE_NAME);
	// public static final String SQL_QUERY_TABLE = "SELECT TABLE_NAME FROM
	// information_schema.TABLES WHERE TABLE_NAME = 'notice_message_tb'";
	/** 查询当前表的所有字段名称和类型 */
	public static final String SQL_QUERY_COLUMN = "SELECT t.TABLE_NAME,c.COLUMN_NAME,c.COLUMN_TYPE,c.COLUMN_COMMENT FROM information_schema.`TABLES` t  LEFT JOIN information_schema.`COLUMNS` c ON t.TABLE_NAME = c.TABLE_NAME WHERE t.TABLE_SCHEMA = 'DATABASE_NAME'"
			.replace("DATABASE_NAME", DbConst.DATABASE_NAME);
	/** 查询字段类型有date或datetime类型的表 */
	public static final String SQL_QUERY_CONTAIN_DATETYPE_TABLE = "SELECT t.TABLE_NAME FROM information_schema.`TABLES` t  LEFT JOIN information_schema.`COLUMNS` c ON t.TABLE_NAME = c.TABLE_NAME WHERE t.TABLE_SCHEMA = 'DATABASE_NAME' AND (c.COLUMN_TYPE = 'date'	OR c.COLUMN_TYPE = 'datetime') GROUP BY	t.TABLE_NAME"
			.replace("DATABASE_NAME", DbConst.DATABASE_NAME);
}
