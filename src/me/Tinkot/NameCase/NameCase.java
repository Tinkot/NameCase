package me.Tinkot.NameCase;

import java.io.File;
import java.sql.SQLException;

import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;

public class NameCase extends Plugin implements Listener {
	private static NameCase instance;

	public void onEnable() {
		instance = this;

		File newFolder = new File(instance.getDataFolder().getAbsolutePath());
		if (!(newFolder.exists())) {
			System.out.println(" ");
			System.out.println("      - Looks likes you're running NameCase for the first time.");
			System.out.println("      - Make sure you have the sqlite drivers installed on your bungeecord");
			System.out.println("      - Attempting to create new sqlite database for you! :)");
			System.out.println(" ");
			new File(instance.getDataFolder().getAbsolutePath()).mkdir();
			SqliteDB db = new SqliteDB();
			try {
				db.createTable();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			db.closeConnection();
		} else {
			System.out.println(" ");
			System.out.println("      - Loading NameCase from existing database");
			System.out.println("      - If you wish to regenerate the database then also delete the folder");
			System.out.println(" ");
		}

		this.getProxy().getPluginManager().registerListener((Plugin) this, (Listener) new LoginEventListener(this));
		getProxy().getPluginManager().registerListener(this, this);
	}

	public static NameCase getInstance() {
		return instance;
	}
}
