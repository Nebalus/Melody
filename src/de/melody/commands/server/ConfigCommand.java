package de.melody.commands.server;

import java.util.List;

import de.melody.core.Config;
import de.melody.core.Melody;
import de.melody.entities.GuildEntity;
import de.melody.speechpackets.Languages;
import de.melody.speechpackets.MessageFormatter;
import de.melody.utils.Emoji;
import de.melody.utils.Utils;
import de.nebalus.botbuilder.command.CommandInfo;
import de.nebalus.botbuilder.command.CommandType;
import de.nebalus.botbuilder.command.ServerCommand;
import de.nebalus.botbuilder.utils.Messenger;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;


public class ConfigCommand implements ServerCommand{
	
	private Melody melody = Melody.INSTANCE;
	private MessageFormatter mf = melody.getMessageFormatter();
	
	@Override
	public void performCommand(Member m, TextChannel channel, Message message, Guild guild) {
		String[] args = message.getContentDisplay().split(" ");
		GuildEntity ge = melody.entityManager.getGuildEntity(guild);
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
						default:
							sendMainMenu(channel,ge.getPrefix());
							break;
					}
				}catch(IllegalArgumentException e){
					sendMainMenu(channel,ge.getPrefix());
				}
			}else if(args.length >= 3) {
				try {
					switch(ConfigSubCommands.valueOf(args[1])) {
						case prefix:
							if(args[2].length() <= 6) {					 
								channel.sendMessage(mf.format(guild, "config.sub.prefix.succes", ge.getPrefix(),args[2])).queue();	
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
								channel.sendMessage(mf.format(guild, "config.sub.language.succes", oldLang.getName(),newLang.getName())).queue();	
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
									channel.sendMessage(mf.format(guild, "config.sub.announcesongs.succes.on")).queue();	
									ge.setAnnounceSongs(true);
								}else {
									channel.sendMessage(mf.format(guild, "config.sub.announcesongs.succes.off")).queue();	
									ge.setAnnounceSongs(false);
								}
							}else {
								sendSubCommandMenu(Utils.getStringFromBoolean(ge.canAnnounceSongs()), channel, ConfigSubCommands.announcesongs, ge.getPrefix(),Utils.getStringFromBoolean(true)+", "+Utils.getStringFromBoolean(false));
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
			Utils.sendErrorEmbled(message, mf.format(guild, "feedback.error.user-no-permmisions", "MANAGE_SERVER"), m);
		}
	}
	private void sendMainMenu(TextChannel channel, String prefix) {
		EmbedBuilder builder = new EmbedBuilder();
		builder.setTitle("**"+Config.BUILDNAME+" Config**");	
		for(ConfigSubCommands command : ConfigSubCommands.values()) {
			builder.addField(command.title, "`"+prefix+"config "+command.name()+"`", true);
		}
		Messenger.sendMessageEmbed(channel, builder).queue();
	}
	private void sendSubCommandMenu(Object currentvalue, TextChannel channel,ConfigSubCommands subcommand,String prefix,String customvalidsettigs) {
		Guild guild = channel.getGuild();
		EmbedBuilder builder = new EmbedBuilder();
		builder.setTitle(mf.format(guild, "config.info.submenu.title",Config.BUILDNAME,subcommand.title));	
		builder.setDescription(mf.format(guild, "config.sub."+subcommand.name()+".info"));
		builder.addField(Emoji.CLIPBOARD+" "+mf.format(guild, "config.info.submenu.current-value"), "`"+currentvalue.toString()+"`", false);
		builder.addField(Emoji.PENCIL+" "+mf.format(guild, "config.info.submenu.usage"), "`"+prefix+"config " +subcommand.name()+" "+subcommand.usage+"`", false);
		if(customvalidsettigs == null) {
			builder.addField(Emoji.CHECK_MARK+" "+mf.format(guild, "config.info.submenu.valid-settings"), "`"+mf.format(guild, "config.sub."+subcommand.name()+".valid-settings")+"`", false);
		}else {
			builder.addField(Emoji.CHECK_MARK+" "+mf.format(guild, "config.info.submenu.valid-settings"), "`"+customvalidsettigs+"`", false);
		}
		Messenger.sendMessageEmbed(channel, builder).queue();
	}
	private enum ConfigSubCommands{
		prefix("[new prefix]",Emoji.EXCLAMATION_MARK+" Prefix"),
		language("[new language]",Emoji.WHITE_FLAG+" Language"),
		announcesongs("[on|off]",Emoji.BELL+" Announce Songs");
		
		String usage;
		String title;
		private ConfigSubCommands(String usage, String title) {
			this.usage = usage;
			this.title = title;
		}
	}
	@Override
	public List<String> getCommandPrefix() {
		return List.of("config");
	}
	@Override
	public CommandType getCommandType() {
		return CommandType.CHAT_COMMAND;
	}

	@Override
	public CommandInfo getCommandInfo() {
		return CommandInfo.INFO_COMMAND;
	}
	@Override
	public String getCommandDescription() {
		return null;
	}

	@Override
	public void performSlashCommand(Member member, MessageChannel channel, Guild guild, SlashCommandEvent event) {
		
	}
	@Override
	public List<OptionData> getCommandOptions() {
		return null;
	}
}