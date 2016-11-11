package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import constants.DbConst;
import constants.SQLConst;

public class DbUtils {

	/** JDBC */
	public static Connection connection = null;
	public static PreparedStatement statement = null;
	public static ResultSet rs = null;


	/**
	 * 打开数据库连接
	 */
	public static void openConn() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(DbConst.URL, DbConst.USERNAME, DbConst.PASSWORD);
		} catch (ClassNotFoundException e) {
			System.err.println("找不到驱动程序类，加载驱动失败！");
		} catch (SQLException e) {
			System.err.println("数据库连接失败,请检查参数是否正确！");
		}
	}

	/**
	 * 关闭数据库连接释放资源
	 */
	public static void closeConn() {
		try {
			if (rs != null)rs.close();
			if (statement != null)statement.close();
			if (connection != null)connection.close();
		} catch (SQLException e) { 
			e.printStackTrace();
		}
	}

	/**
	 * 获取结果集
	 */
	public static void execute(String sql) {
		try {
			openConn();
			statement = DbUtils.connection.prepareStatement(sql);
			rs = DbUtils.statement.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
