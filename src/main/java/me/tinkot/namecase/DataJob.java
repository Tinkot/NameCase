package me.tinkot.namecase;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataJob {
	private Connection connection;

	/**
	 * Create a new DataJob
	 * @param config, NameCase plugin 
	 * 
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	DataJob() {
		try {
			connect();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Let this object connect to the database
	 * 
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	private void connect() throws ClassNotFoundException, SQLException {
		Class.forName("org.sqlite.JDBC");
		String database = NameCase.getConfig().getAbsolutePluginFolder() + File.separator + "names.db";
		connection = DriverManager.getConnection("jdbc:sqlite:" + database);
	}
	
	/**
	 * Close the connection. Usually called at the end of a query
	 * 
	 * @throws SQLException 
	 */
	private void closeConnection() throws SQLException {
		connection.close();
	}
	
	/**
	 * Create table
	 * 
	 * @throws SQLException
	 */
	public void createTable() {
		try {
			String sql = "CREATE TABLE IF NOT EXISTS Players (flatName TEXT NOT NULL UNIQUE, realName TEXT NOT NULL UNIQUE)";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.execute();
			closeConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * insert a user into the database
	 * 
	 * @param name
	 * @throws SQLException
	 */
	public void insertUser(String name) {
		try {
			String sql = "INSERT INTO Players (flatName, realName) VALUES (LOWER(?), ?)";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, name);
			statement.setString(2, name);
			statement.execute();
			closeConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Get the real name of the player who logged in.
	 * 
	 * @param name
	 * @return
	 * @throws SQLException
	 */
	public String getRealName(String loginName) {
		String realName = null;
		try {
			String sql = "SELECT realName FROM Players WHERE flatName = LOWER(?) LIMIT 1";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, loginName);
			ResultSet resultset =  statement.executeQuery();
			// handling the ResultSet
			while(resultset.next()) {
				realName = resultset.getString("realName");
			}
			closeConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return realName;
	}
}
