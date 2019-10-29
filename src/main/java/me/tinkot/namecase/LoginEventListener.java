package me.tinkot.namecase;

import java.sql.SQLException;

import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class LoginEventListener implements Listener {
	enum Case {
		PASSED, TOO_LONG, TOO_SHORT, ILLEGAL, NAMECASE
	}

	private static final Case DEFAULT_CASE = Case.PASSED
	private Config config;
	private NameCase plugin;

	public LoginEventListener(Config config, NameCase plugin) {
		this.config = config;
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerLogin(final PreLoginEvent event) throws SQLException, ClassNotFoundException {
		event.registerIntent(plugin);
		ProxyServer.getInstance().getScheduler().runAsync(plugin, new Runnable() {
			public void run() {
				
				Case reason = DEFAULT_CASE;	

				String loginName = event.getConnection().getName();
				String realName  = new DataJob().getRealName(loginName);
				
				if(loginName.length() > 16) {
					reason = Case.TOO_LONG;
				}
				
				else if(loginName.length() < 3) {
					reason = Case.TOO_SHORT;
				}
					
				else if (!loginName.matches("[a-zA-Z0-9_]*")) {
					reason = Case.ILLEGAL;
				}
				
				else if (realName == null) {
					realName = loginName;
					new DataJob().insertUser(realName);
					
				} else if (!loginName.equals(realName)) {
					reason = Case.NAMECASE;
				}
				if (reason != Case.PASSED) {
					String message;
					switch(reason) {
						case TOO_LONG:
							message = config.getString("TOO_LONG");
							break;
						case TOO_SHORT:
							message = config.getString("TOO_SHORT");
							break;
						case ILLEGAL:
							message = config.getString("ILLEGAL_CHARS");
							break;
						case NAMECASE:
							message = config.getString("NAMECASE").replace("<name>", realName).replace("<wrongname>", loginName);
							break;
						default:
							message = "unknown";
							break;
					}
					message = message.replace("\\n", "\n"); // hotfix for new line character
					
					event.setCancelReason(new TextComponent(ChatColor.translateAlternateColorCodes('&', message)));
					event.setCancelled(true);
				}
				event.completeIntent(plugin);
			}
		});
	}
}
