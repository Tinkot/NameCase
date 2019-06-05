package me.Tinkot.NameCase;

import java.sql.SQLException;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class LoginEventListener implements Listener {

	public LoginEventListener() {
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerLogin(PreLoginEvent event) throws SQLException {
		String name = event.getConnection().getName();

		// To check if his name is not longer than 16 characters.
		if (name.length() > 16) {
			TextComponent message = new TextComponent("ERROR: ");
			message.setColor(ChatColor.RED);
			TextComponent reason = new TextComponent("Your username is longer than 16 characters.");
			reason.setColor(ChatColor.DARK_RED);
			message.addExtra(reason);
			// canceling the event
			event.setCancelReason(message);
			event.setCancelled(true);

		// To check if his name is not below than 3 characters.
		} else if (name.length() < 3) {
			TextComponent message = new TextComponent("ERROR: ");
			message.setColor(ChatColor.RED);
			TextComponent reason = new TextComponent("Your username is below 3 characters.");
			reason.setColor(ChatColor.DARK_RED);
			message.addExtra(reason);
			// canceling the event
			event.setCancelReason(message);
			event.setCancelled(true);

		// To check if his name does not contain illegal characters.
		} else if (!name.matches("[a-zA-Z0-9_]*")) {
			TextComponent message = new TextComponent("ERROR: ");
			message.setColor(ChatColor.RED);
			TextComponent reason = new TextComponent("Your username contains illegal characters.");
			reason.setColor(ChatColor.DARK_RED);
			message.addExtra(reason);
			// canceling the event
			event.setCancelReason(message);
			event.setCancelled(true);
		} else {
		
			// requesting name to database
			SqliteDB db = new SqliteDB();
			String realName = db.getRealName(name);
			db.closeConnection();
			// checking if name is not equal
			if (!name.equals(realName)) {
				TextComponent message = new TextComponent("ERROR: ");
				message.setColor(ChatColor.RED);
				TextComponent reason = new TextComponent("You username previously logged in using: '" + realName + "'");
				reason.setColor(ChatColor.DARK_RED);
				message.addExtra(reason);

				event.setCancelReason(message);
				event.setCancelled(true);
			}
		}
	}
}
