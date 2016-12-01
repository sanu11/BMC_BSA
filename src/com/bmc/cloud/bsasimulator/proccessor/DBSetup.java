package com.bmc.cloud.bsasimulator.proccessor;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.bmc.cloud.bsasimulator.internal.Constants;

public class DBSetup {
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		Class.forName(Constants.JDBC_CONNECTOR_NAME);
		Connection connection=null;
		try{
			connection=DriverManager.getConnection(Constants.USER_DB_NAME);
			Statement  statement=connection.createStatement();
			statement.executeUpdate("create table user (id integer,username string,password string)");
			statement.executeUpdate("insert into user values(1,'bmc','bladelogic')");
			statement.executeUpdate("insert into user values(2,'altoadmin','bladelogic')");
			ResultSet resultSet=statement.executeQuery("select * from user");
			System.out.println(resultSet.getString("username")+resultSet.getString("password"));
			connection.close();
			statement.close();
		}
		catch(SQLException exception){

		}

	}
}
