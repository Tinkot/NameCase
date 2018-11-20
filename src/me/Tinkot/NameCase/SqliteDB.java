package me.Tinkot.NameCase;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import net.md_5.bungee.api.plugin.Plugin;

public class SqliteDB extends Plugin {
	Connection c = null;
	Statement stmt = null;

	SqliteDB() {
		// try connect to DB
		try {
			Class.forName("org.sqlite.JDBC");
			String folder = NameCase.getInstance().getDataFolder().getAbsolutePath();
			String database = folder + File.separator + "names.db";
			c = DriverManager.getConnection("jdbc:sqlite:" + database);
		} catch (Exception e) {
		}
	}

	// called when folder doesn't exists
	public void createTable() throws SQLException {
		// creates the table
		this.stmt = c.createStatement();
		stmt.execute(
				"CREATE TABLE IF NOT EXISTS Players (flatName TEXT NOT NULL UNIQUE, realName	TEXT NOT NULL UNIQUE)");
	}

	// function to return real name of the player.
	// it returns their curent name if the player is new.
	public String getRealName(String name) throws SQLException {

		this.stmt = c.createStatement();
		String realName = null;

		ResultSet rs = stmt
				.executeQuery("SELECT realName FROM Players WHERE flatName = LOWER('" + name + "') LIMIT 1;");

		// will set realName to the players name if exists in db
		while (rs.next()) {
			realName = rs.getString("realName");
		}

		// if username does not exists in db then it will insert one
		if (realName == null) {
			stmt.executeUpdate(
					"INSERT INTO Players (flatName, realName) VALUES (LOWER('" + name + "'), '" + name + "')");
			realName = name;
		}
		return realName;
	}

	// close connection of the database.
	// its normal to close it after every statement when using sqlite.
	public void closeConnection() {
		try {
			c.close();
		} catch (Exception e) {
			System.out.print(e.getMessage());
		}

	}
}
