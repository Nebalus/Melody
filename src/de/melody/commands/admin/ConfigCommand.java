package de.melody.commands.admin;

import de.melody.core.Constants;
import de.melody.core.Melody;
import de.melody.entities.GuildEntity;
import de.melody.speechpackets.Languages;
import de.melody.speechpackets.MessageFormatter;
import de.melody.utils.Utils;
import de.melody.utils.Utils.Emoji;
import de.melody.utils.commandbuilder.CommandPermission;
import de.melody.utils.commandbuilder.CommandType;
import de.melody.utils.commandbuilder.ServerCommand;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import de.melody.utils.messenger.Messenger;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;


public class ConfigCommand implements ServerCommand{
	
	private Melody melody = Melody.INSTANCE;
	private MessageFormatter mf = melody.getMessageFormatter();
	
	@Override
	public void performCommand(Member member, TextChannel channel, Message message, Guild guild, GuildEntity guildentity) {
		String[] args = message.getContentDisplay().split(" ");
		if(args.length == 1) {
			sendMainMenu(channel,guildentity.getPrefix());
		}else if(args.length == 2) {
			try {
				ConfigSubCommands subcommand = ConfigSubCommands.valueOf(args[1]);
				switch(subcommand) {
					case prefix:
						sendSubCommandMenu(guildentity.getPrefix(), channel, ConfigSubCommands.prefix, guildentity.getPrefix(),null);
						break;
					case language:
						String languagelist = "";
						for (Languages language : Languages.values()) {
							languagelist = languagelist+ " `"+language.getIcon()+"` - "+language.getName()+" \n";
				        }
						sendSubCommandMenu(" `"+guildentity.getLanguage().getIcon()+"` "+guildentity.getLanguage().getName()+" ", channel, ConfigSubCommands.language, guildentity.getPrefix(),languagelist);
						break;
					case announcesongs:
						sendSubCommandMenu(Utils.getStringFromBoolean(guildentity.canAnnounceSongs()), channel, ConfigSubCommands.announcesongs, guildentity.getPrefix(),Utils.getStringFromBoolean(true)+", "+Utils.getStringFromBoolean(false));
						break;
					default:
						sendMainMenu(channel,guildentity.getPrefix());
						break;
				}
			}catch(IllegalArgumentException e){
				sendMainMenu(channel,guildentity.getPrefix());
			}
		}else if(args.length >= 3) {
			try {
				switch(ConfigSubCommands.valueOf(args[1])) {
					case prefix:
						if(args[2].length() <= 6) {					 
							channel.sendMessage(mf.format(guild, "config.sub.prefix.succes", guildentity.getPrefix(),args[2])).queue();	
							guildentity.setPrefix(args[2]);
						}else 
							sendSubCommandMenu(guildentity.getPrefix(), channel, ConfigSubCommands.prefix, guildentity.getPrefix(),null);
						break;
						
					case language:
						boolean isLanguage = false;
						Languages newLang = guildentity.getLanguage();
						for(Languages lang : Languages.values()) {
							if(lang.getName().equalsIgnoreCase(args[2]) || lang.getCode().equalsIgnoreCase(args[2]) && newLang != lang) {
								isLanguage = true;
								newLang = lang;
							}
						}
						if(isLanguage) {
							Languages oldLang = guildentity.getLanguage();
							guildentity.setLanguage(newLang);
							channel.sendMessage(mf.format(guild, "config.sub.language.succes", oldLang.getName(),newLang.getName())).queue();	
						}else {
							String languagelist = "";
							for (Languages language : Languages.values()) {
								languagelist = languagelist+ " `"+language.getIcon()+"` - "+language.getName()+" \n";
					        }
							sendSubCommandMenu(" `"+guildentity.getLanguage().getIcon()+"` "+guildentity.getLanguage().getName()+" ", channel, ConfigSubCommands.language, guildentity.getPrefix(),languagelist);
						}
						break;
					case announcesongs:
						boolean value = Utils.getBooleanFromString(args[2]);
						if(Utils.isStringValidBoolean(args[2]) && value != guildentity.canAnnounceSongs()) {
							if(value) {
								channel.sendMessage(mf.format(guild, "config.sub.announcesongs.succes.on")).queue();	
								guildentity.setAnnounceSongs(true);
							}else {
								channel.sendMessage(mf.format(guild, "config.sub.announcesongs.succes.off")).queue();	
								guildentity.setAnnounceSongs(false);
							}
						}else {
							sendSubCommandMenu(Utils.getStringFromBoolean(guildentity.canAnnounceSongs()), channel, ConfigSubCommands.announcesongs, guildentity.getPrefix(),Utils.getStringFromBoolean(true)+", "+Utils.getStringFromBoolean(false));
						}
						break;
					default:
						sendMainMenu(channel,guildentity.getPrefix());
						break;
				}
			}catch(IllegalArgumentException e){
				sendMainMenu(channel,guildentity.getPrefix());
			}
		}
	}
	private void sendMainMenu(TextChannel channel, String prefix) {
		EmbedBuilder builder = new EmbedBuilder();
		builder.setTitle("**"+Constants.BUILDNAME+" Config**");	
		for(ConfigSubCommands command : ConfigSubCommands.values()) {
			builder.addField(command.title, "`"+prefix+"config "+command.name()+"`", true);
		}
		Messenger.sendMessageEmbed(channel, builder).queue();
	}
	private void sendSubCommandMenu(Object currentvalue, TextChannel channel,ConfigSubCommands subcommand,String prefix,String customvalidsettigs) {
		Guild guild = channel.getGuild();
		EmbedBuilder builder = new EmbedBuilder();
		builder.setTitle(mf.format(guild, "config.info.submenu.title",Constants.BUILDNAME,subcommand.title));	
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
	public String[] getCommandPrefix() {
		return new String[] {"config"};
	}
	@Override
	public CommandType getCommandType() {
		return CommandType.CHAT;
	}

	@Override
	public String getCommandDescription() {
		return null;
	}

	@Override
	public void performSlashCommand(Member member, MessageChannel channel, Guild guild, GuildEntity guildentity, SlashCommandEvent event) {
		
	}
	@Override
	public OptionData[] getCommandOptions() {
		return null;
	}
	@Override
	public CommandPermission getMainPermmision() {
		return CommandPermission.ADMIN;
	}
}