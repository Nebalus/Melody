package de.pixelbeat.commands;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import de.pixelbeat.LiteSQL;
import de.pixelbeat.PixelBeat;
import de.pixelbeat.commands.types.ServerCommand;
import de.pixelbeat.speechpackets.MessageFormatter;
import de.pixelbeat.utils.Utils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class ConfigCommand implements ServerCommand{
	
	private MessageFormatter mf = PixelBeat.INSTANCE.getMessageFormatter();
	
	@Override
	public void performCommand(Member m, TextChannel channel, Message message, Guild guild) {
		String[] args = message.getContentDisplay().split(" ");
		Long guildid = guild.getIdLong();
		if(args.length == 1) {
			
		}else if(args.length == 2) {
			if(args[1].equalsIgnoreCase("prefix")) {
				channel.sendMessage(mf.format(guildid, "feedback.info.prefix",Utils.getGuildPrefix(guildid))).queue();
			}
		}else if(args.length == 3) {
			if(args[1].equalsIgnoreCase("prefix")) {
				if(m.hasPermission(Permission.ADMINISTRATOR) | m.hasPermission(Permission.MANAGE_SERVER)) {
					if(LiteSQL.isConnected()) {
						int count = 0;
				        for (int i = 0; i < args[2].length(); i++) {
				        	count++;
				        }
				        if(count <= 6) {					            
							try {
								String oldPrefix = Utils.getGuildPrefix(guildid);
								PreparedStatement ps = LiteSQL.getConnection().prepareStatement("UPDATE `general` SET `prefix` = ? WHERE `guildid` = "+guildid);
								ps.setString(1, args[2]);
								ps.executeUpdate();
								channel.sendMessage("**You have updated my prefix from** `"+oldPrefix+"` **to** `"+args[2]+"`").queue();
							} catch (SQLException e) {
								e.printStackTrace();
							}
						}else {
							Utils.sendErrorEmbled(channel, "The prefix must be less than 6 characters. The prefix is the character that starts a command e.g `!`", m);
						}
					}else {
						Utils.sendErrorEmbled(channel,"An internal error occurred: `Could not connect to the database`", m);
					}
				}else {
					Utils.sendErrorEmbled(channel, "You don't have enough permissions **Permisions: **`MANAGE_SERVER`", m);
				}
			}
		}else if(args.length == 4) {
			
		}else if(args.length == 5) {
			
		}else if(args.length == 6) {
			
		}
	}
	public void sendHelpMenu(TextChannel channel) {
		
	}
}