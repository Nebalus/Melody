package de.melody.commands.server;

import de.melody.Config;
import de.melody.Melody;
import de.melody.commands.types.ServerCommand;
import de.melody.entities.GuildEntity;
import de.melody.speechpackets.Languages;
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
		GuildEntity ge = melody.entityManager.getGuildEntity(guildid);
		if(m.hasPermission(Permission.ADMINISTRATOR) || m.hasPermission(Permission.MANAGE_SERVER)) {
			if(args.length == 1) {
				sendMainMenu(channel,ge.getPrefix());
			}else if(args.length == 2) {
				try {
					ConfigSubCommands subcommand = ConfigSubCommands.valueOf(args[1]);
					switch(subcommand) {
						case prefix:
							sendSubCommandMenu(ge.getPrefix(), channel, ConfigSubCommands.prefix, ge.getPrefix(),null);
							break;
						case language:
							String languagelist = "";
							for (Languages language : Languages.values()) {
								languagelist = languagelist+ " `"+language.getIcon()+"` - "+language.getName()+" \n";
					        }
							sendSubCommandMenu(" `"+ge.getLanguage().getIcon()+"` "+ge.getLanguage().getName()+" ", channel, ConfigSubCommands.language, ge.getPrefix(),languagelist);
							break;
						case announcesongs:
							sendSubCommandMenu(Utils.getStringFromBoolean(ge.canAnnounceSongs()), channel, ConfigSubCommands.announcesongs, ge.getPrefix(),Utils.getStringFromBoolean(true)+", "+Utils.getStringFromBoolean(false));
							break;
						case revocablecommands: 
							sendSubCommandMenu(Utils.getStringFromBoolean(ge.canRevokeCommand()), channel, ConfigSubCommands.revocablecommands, ge.getPrefix(), null);
							break;
						default:
							sendMainMenu(channel,ge.getPrefix());
							break;
					}
				}catch(IllegalArgumentException e){
					sendMainMenu(channel,ge.getPrefix());
				}
			}else if(args.length >= 3) {
				try {
					ConfigSubCommands subcommand = ConfigSubCommands.valueOf(args[1]);
					switch(subcommand) {
						case prefix:
							if(args[2].length() <= 6) {					 
								channel.sendMessage(mf.format(guildid, "config.sub.prefix.succes", ge.getPrefix(),args[2])).queue();	
								ge.setPrefix(args[2]);
							}else 
								sendSubCommandMenu(ge.getPrefix(), channel, ConfigSubCommands.prefix, ge.getPrefix(),null);
							break;
						
						case language:
							boolean isLanguage = false;
							Languages newLang = ge.getLanguage();
							for(Languages lang : Languages.values()) {
								if(lang.getName().equalsIgnoreCase(args[2]) || lang.getCode().equalsIgnoreCase(args[2]) && newLang != lang) {
									isLanguage = true;
									newLang = lang;
								}
							}
							if(isLanguage) {
								Languages oldLang = ge.getLanguage();
								ge.setLanguage(newLang);
								channel.sendMessage(mf.format(guildid, "config.sub.language.succes", oldLang.getName(),newLang.getName())).queue();	
							}else {
								String languagelist = "";
								for (Languages language : Languages.values()) {
									languagelist = languagelist+ " `"+language.getIcon()+"` - "+language.getName()+" \n";
						        }
								sendSubCommandMenu(" `"+ge.getLanguage().getIcon()+"` "+ge.getLanguage().getName()+" ", channel, ConfigSubCommands.language, ge.getPrefix(),languagelist);
							}
							break;
						case announcesongs:
							boolean value = Utils.getBooleanFromString(args[2]);
							if(Utils.isStringValidBoolean(args[2]) && value != ge.canAnnounceSongs()) {
								if(value) {
									channel.sendMessage(mf.format(guildid, "config.sub.announcesongs.succes.on")).queue();	
									ge.setAnnounceSongs(true);
								}else {
									channel.sendMessage(mf.format(guildid, "config.sub.announcesongs.succes.off")).queue();	
									ge.setAnnounceSongs(false);
								}
							}else {
								sendSubCommandMenu(Utils.getStringFromBoolean(ge.canAnnounceSongs()), channel, ConfigSubCommands.announcesongs, ge.getPrefix(),Utils.getStringFromBoolean(true)+", "+Utils.getStringFromBoolean(false));
							}
							break;
						case revocablecommands: 
							boolean value1 = Utils.getBooleanFromString(args[2]);
							if(Utils.isStringValidBoolean(args[2]) && value1 != ge.canRevokeCommand()) {
								if(value1) {
									channel.sendMessage("**I will now delete every new command**").queue();	
									ge.setRevokeCommand(true);
								}else {
									channel.sendMessage("**I will no longer delete every new command**").queue();	
									ge.setRevokeCommand(false);
								}
							}else {
								sendSubCommandMenu(Utils.getStringFromBoolean(ge.canRevokeCommand()), channel, ConfigSubCommands.revocablecommands, ge.getPrefix(),Utils.getStringFromBoolean(true)+", "+Utils.getStringFromBoolean(false));
							}
							break;
						default:
							sendMainMenu(channel,ge.getPrefix());
							break;
					}
				}catch(IllegalArgumentException e){
					sendMainMenu(channel,ge.getPrefix());
				}
			}
		}else {
			Utils.sendErrorEmbled(channel,mf.format(guildid, "feedback.error.user-no-permmisions", "MANAGE_SERVER"), m);
		}
	}
	@SuppressWarnings("deprecation")
	public void sendMainMenu(TextChannel channel, String prefix) {
		EmbedBuilder builder = new EmbedBuilder();
		builder.setColor(Config.HEXEmbeld);
		builder.setTitle("**"+Config.buildname+" Config**");	
		for(ConfigSubCommands command : ConfigSubCommands.values()) {
			builder.addField(command.title, "`"+prefix+"config "+command.name()+"`", true);
		}
		channel.sendMessage(builder.build()).queue();
	}
	
	
	@SuppressWarnings("deprecation")
	public void sendSubCommandMenu(Object currentvalue, TextChannel channel,ConfigSubCommands subcommand,String prefix,String customvalidsettigs) {
		Long guildid = channel.getGuild().getIdLong();
		EmbedBuilder builder = new EmbedBuilder();
		builder.setColor(Config.HEXEmbeld);
		builder.setTitle(mf.format(guildid, "config.info.submenu.title",Config.buildname,subcommand.title));	
		builder.setDescription(mf.format(guildid, "config.sub."+subcommand.name()+".info"));
		builder.addField(Emojis.CLIPBOARD+" "+mf.format(guildid, "config.info.submenu.current-value"), "`"+currentvalue.toString()+"`", false);
		builder.addField(Emojis.PENCIL+" "+mf.format(guildid, "config.info.submenu.usage"), "`"+prefix+"config " +subcommand.name()+" "+subcommand.usage+"`", false);
		if(customvalidsettigs == null) {
			builder.addField(Emojis.CHECK_MARK+" "+mf.format(guildid, "config.info.submenu.valid-settings"), "`"+mf.format(guildid, "config.sub."+subcommand.name()+".valid-settings")+"`", false);
		}else {
			builder.addField(Emojis.CHECK_MARK+" "+mf.format(guildid, "config.info.submenu.valid-settings"), "`"+customvalidsettigs+"`", false);
		}
		channel.sendMessage(builder.build()).queue();
	}
	private enum ConfigSubCommands{
		prefix("[new prefix]",Emojis.EXCLAMATION_MARK+" Prefix"),
		language("[new language]",Emojis.WHITE_FLAG+" Language"),
		announcesongs("[on|off]",Emojis.BELL+" Announce Songs"),
		revocablecommands("[on|off]",Emojis.FIRECRACKER+" Revocable Commands");
		
		String usage;
		String title;
		private ConfigSubCommands(String usage, String title) {
			this.usage = usage;
			this.title = title;
		}
	}
}