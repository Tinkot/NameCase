package me.tinkot.namecase;

import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;

public class NameCase extends Plugin implements Listener {
	private static Config config;

	@Override
	public void onEnable() {
		config = new Config(this);
		new DataJob().createTable();
		getProxy().getPluginManager().registerListener(this, new LoginEventListener(config, this));
	}
	
	
	public static Config getConfig() {
		return config;
	}
}

