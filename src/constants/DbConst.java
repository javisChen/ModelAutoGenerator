package constants;

public class DbConst {
	
	/** 服务器地址 */
	public static final String SERVER_ADDRESS = "112.74.189.239";
	/** 数据库 */
	public static final String DATABASE_NAME = "szyt";
	/** Url */
	public static final String URL = "jdbc:mysql://server_address:3306/dbname?characterEncoding=utf-8".replace("dbname", DATABASE_NAME).replace("server_address", SERVER_ADDRESS);
	/** 用户名 */
	public static final String USERNAME = "root";
	/** 密码 */
	public static final String PASSWORD = "nixihuan";
}
