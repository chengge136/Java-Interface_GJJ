package com.lsfg.base;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * 数据库操作类.
 * 链接或关闭数据库.
 * @author lpeng.
 */
public class OracleConnection {
	
	private static Logger logger = Logger.getLogger(OracleConnection.class);
	
	//链接数据库
	public static Connection getConnection() throws Exception{
		InputStream in = OracleConnection.class.getClassLoader().getResourceAsStream("jdbc.properties");
		Properties properties = new Properties();
		try {
			properties.load(in);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		String oracle_url = properties.getProperty("jdbc.url");
		String oracle_user = properties.getProperty("jdbc.username");
		String oracle_pwd = properties.getProperty("jdbc.password");
		String driverClassName = properties.getProperty("jdbc.driverClassName");
		Class.forName(driverClassName);
		Connection smsTransmitConn = DriverManager.getConnection(oracle_url, oracle_user, oracle_pwd);
		
		return smsTransmitConn;
	}
	
	//关闭数据库连接
	public static void release(Statement statement, Connection con) throws SQLException{
        /**
         * Statement Connection两个连接的方法
         */
        try {
            if(statement != null){
            	statement.close();
            }
            if(con != null){
            	con.close();
            }
            logger.info("数据库关闭。。。。。");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("数据库关闭连接异常：" + e.getMessage());
        }
    }
}
