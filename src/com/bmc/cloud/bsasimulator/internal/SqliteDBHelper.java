package com.bmc.cloud.bsasimulator.internal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.bmc.cloud.bsasimulator.internal.Constants.PASSWORD;
import static com.bmc.cloud.bsasimulator.internal.Constants.USERNAME;

public class SqliteDBHelper  implements DBHelper{

	@Override
	public boolean checkCredentials(String user, String password) throws ClassNotFoundException {
		// TODO Auto-generated method stub
		if(user.equals(USERNAME) && password.equals(PASSWORD))
			return true;
		/*Class.forName("org.sqlite.JDBC");
		ResultSet resultSet=null;
		try {
			Connection connection=DriverManager.getConnection(Constants.USER_DB_PATH);
			String statment="select username ,password from user where username=?";
			PreparedStatement preparedStatement=connection.prepareStatement(statment);
			preparedStatement.setString(1,user);
			resultSet=preparedStatement.executeQuery();
			while(resultSet.next()){
				if(resultSet.getString("username").equals(Constants.USERNAME) && resultSet.getString("password").equals(Constants.PASSWORD)){
					return true;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		return false;
	}

	@Override
	public void addTodb(String str) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getVmDetails() {
		// TODO Auto-generated method stub
		return "VM DETAILS";
	}
}
