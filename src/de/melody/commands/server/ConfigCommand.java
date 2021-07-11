package de.melody.commands.server;

import de.melody.Melody;
import de.melody.commands.types.ServerCommand;
import de.melody.entities.GuildEntity;
import de.melody.speechpackets.MessageFormatter;
import de.melody.utils.Emojis;
import de.melody.utils.Utils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class ConfigCommand implements ServerCommand{
	
	private Melody melody = Melody.INSTANCE;
	private MessageFormatter mf = melody.getMessageFormatter();
	
	@Override
	public void performCommand(Member m, TextChannel channel, Message message, Guild guild) {
		String[] args = message.getContentDisplay().split(" ");
		Long guildid = guild.getIdLong();
		GuildEntity guildentity = melody.entityManager.getGuildEntity(guildid);
		if(m.hasPermission(Permission.ADMINISTRATOR) || m.hasPermission(Permission.MANAGE_SERVER)) {
			if(args.length == 1) {
				
			}else if(args.length == 2) {
				if(args[1].equalsIgnoreCase(ConfigSubCommands.prefix.name())) {
					sendSubCommandMenu(guildentity.getPrefix(), channel, ConfigSubCommands.prefix, guildentity.getPrefix());
				}
			}else if(args.length >= 3) {
				if(args[1].equalsIgnoreCase(ConfigSubCommands.prefix.name())) {
					if(args[2].length() <= 6) {					            
						String oldPrefix = guildentity.getPrefix();
						guildentity.setPrefix(args[2]);
						channel.sendMessage("**You have updated my prefix from** `"+oldPrefix+"` **to** `"+args[2]+"`").queue();			
					}else {
						sendSubCommandMenu(guildentity.getPrefix(), channel, ConfigSubCommands.prefix, guildentity.getPrefix());
					}
				}
			}
		}else {
			Utils.sendErrorEmbled(channel,mf.format(guildid, "feedback.error.user-no-permmisions", "MANAGE_SERVER"), m);
		}
	}
	public void sendMainMenu(TextChannel channel) {
		
	}
	@SuppressWarnings("deprecation")
	public void sendSubCommandMenu(Object currentvalue, TextChannel channel,ConfigSubCommands subcommand,String prefix) {
		Long guildid = channel.getGuild().getIdLong();
		EmbedBuilder builder = new EmbedBuilder();
		builder.setColor(Melody.HEXEmbeld);
		builder.setTitle(mf.format(guildid, "config.info.submenu.title",Melody.name,subcommand.title));	
		builder.setDescription(mf.format(guildid, "config.sub."+subcommand.name()+".info"));
		builder.addField(Emojis.CLIPBOARD+" "+mf.format(guildid, "config.info.submenu.current-value"), "`"+currentvalue.toString()+"`", false);
		builder.addField(Emojis.PENCIL+" "+mf.format(guildid, "config.info.submenu.usage"), "`"+prefix+"config " +subcommand.name()+" "+subcommand.usage+"`", false);
		builder.addField(Emojis.CHECK_MARK+" "+mf.format(guildid, "config.info.submenu.valid-settings"), "`"+mf.format(guildid, "config.sub."+subcommand.name()+".valid-settings")+"`", false);
	
		channel.sendMessage(builder.build()).queue();
	}
	private enum ConfigSubCommands{
		prefix("[new prefix]",Emojis.EXCLAMATION_MARK+" Prefix");
		
		String usage;
		String title;
		private ConfigSubCommands(String usage, String title) {
			this.usage = usage;
			this.title = title;
		}
	}
}