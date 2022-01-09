package de.melody.commands.admin;

import de.melody.core.Constants;
import de.melody.core.Melody;
import de.melody.entities.GuildEntity;
import de.melody.speechpackets.Language;
import de.melody.speechpackets.MessageFormatter;
import de.melody.tools.ConsoleLogger;
import de.melody.tools.Utils;
import de.melody.tools.Utils.Emoji;
import de.melody.tools.commandbuilder.CommandPermission;
import de.melody.tools.commandbuilder.CommandType;
import de.melody.tools.commandbuilder.Command;
import de.melody.tools.messenger.Messenger;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;


public class ConfigCommand implements Command{
	
	private Melody melody = Melody.INSTANCE;
	private MessageFormatter mf = melody._messageformatter;
	
	@Override 
	public void performCommand(Member member, TextChannel channel, Message message, Guild guild, GuildEntity guildentity) {
		String[] args = message.getContentDisplay().toLowerCase().split(" ");
		
		if(args.length == 1) {
			sendMainMenu(channel,guildentity.getPrefix());
		}else if(args.length == 2) {
			try {
				switch(SubCommand.valueOf(args[1].toUpperCase())) {
					case PREFIX:
						sendSubCommandMenu(guildentity.getPrefix(), channel, SubCommand.PREFIX, guildentity.getPrefix());
						break;
					case LANGUAGE:
						String languagelist = "";
						for (Language language : Language.values()) {
							languagelist = languagelist+ " `"+language.getIcon()+"` - "+language.getName()+" \n";
				        }
						sendSubCommandMenu(" `"+guildentity.getLanguage().getIcon()+"` "+guildentity.getLanguage().getName()+" ", channel, SubCommand.LANGUAGE, languagelist);
						break;
					case ANNOUNCESONGS:
						sendSubCommandMenu(Utils.getStringFromBoolean(guildentity.canAnnounceSongs()), channel, SubCommand.ANNOUNCESONGS, "on, off");
						break;
					case DJONLY:
						sendSubCommandMenu(Utils.getStringFromBoolean(guildentity.isDjOnly()), channel, SubCommand.DJONLY, "on, off");
						break;
					case DJROLES:
						sendSubCommandMenu(guildentity.getDJRolesID().toString(), channel, SubCommand.DJROLES, "@djrole");
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
				switch(SubCommand.valueOf(args[1].toUpperCase())) {
					case PREFIX:
						if(args[2].length() <= 6) {					 
							channel.sendMessage(mf.format(guild, "config.sub.prefix.succes", guildentity.getPrefix(), args[2])).queue();	
							guildentity.setPrefix(args[2]);
						}else 
							sendSubCommandMenu(guildentity.getPrefix(), channel, SubCommand.PREFIX);
						break;
						
					case LANGUAGE:
						boolean isLanguage = false;
						Language newLang = guildentity.getLanguage();
						for(Language lang : Language.values()) {
							if(lang.getName().equalsIgnoreCase(args[2]) || lang.getCode().equalsIgnoreCase(args[2]) && newLang != lang) {
			 					isLanguage = true;
								newLang = lang;
							}
						}
						if(isLanguage) {
							Language oldLang = guildentity.getLanguage();
							guildentity.setLanguage(newLang);
							channel.sendMessage(mf.format(guild, "config.sub.language.succes", oldLang.getName(),newLang.getName())).queue();	
						}else {
							String languagelist = "";
							for (Language language : Language.values()) {
								languagelist = languagelist+ " `"+language.getIcon()+"` - "+language.getName()+" \n";
					        }
							sendSubCommandMenu(" `"+guildentity.getLanguage().getIcon()+"` "+guildentity.getLanguage().getName()+" ", channel, SubCommand.LANGUAGE, languagelist);
						}
						break;
						
					case ANNOUNCESONGS:
						if(Utils.isStringValidBoolean(args[2])) {
							boolean value;
							if((value = Utils.getBooleanFromString(args[2])) != guildentity.canAnnounceSongs()) {
								if(value) {
									channel.sendMessage(mf.format(guild, "config.sub.announcesongs.succes.on")).queue();	
									guildentity.setAnnounceSongs(true);
								}else {
									channel.sendMessage(mf.format(guild, "config.sub.announcesongs.succes.off")).queue();	
									guildentity.setAnnounceSongs(false);
								}
								break;
							}
						}
						sendSubCommandMenu(Utils.getStringFromBoolean(guildentity.canAnnounceSongs()), channel, SubCommand.ANNOUNCESONGS, "on, off");
						break;
						
					case DJONLY:
						if(Utils.isStringValidBoolean(args[2])) {
							boolean value;
							if((value = Utils.getBooleanFromString(args[2])) != guildentity.isDjOnly()) {
								if(value) {
									channel.sendMessage(mf.format(guild, "config.sub.djonly.succes.on")).queue();	
									guildentity.setDjOnly(true);
								}else {
									channel.sendMessage(mf.format(guild, "config.sub.djonly.succes.off")).queue();	
									guildentity.setDjOnly(false);
								}
								break;
							}
						}
						sendSubCommandMenu(Utils.getStringFromBoolean(guildentity.isDjOnly()), channel, SubCommand.DJONLY, "on, off");
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
		for(SubCommand command : SubCommand.values()) {
			builder.addField(command.title, "`"+prefix+"config "+command.name().toLowerCase()+"`", true);
		}
		Messenger.sendMessageEmbed(channel, builder).queue();
	}
	
	private void sendSubCommandMenu(Object currentvalue, TextChannel channel, SubCommand subcommand, String customvalidsettigs) {
		Guild guild = channel.getGuild();
		EmbedBuilder builder = new EmbedBuilder();
		builder.setTitle(mf.format(guild, "config.info.submenu.title",Constants.BUILDNAME,subcommand.title));	
		builder.setDescription(mf.format(guild, "config.sub."+subcommand.name()+".info"));
		builder.addField(Emoji.CLIPBOARD+" "+mf.format(guild, "config.info.submenu.current-value"), "`"+currentvalue.toString()+"`", false);
		builder.addField(Emoji.PENCIL+" "+mf.format(guild, "config.info.submenu.usage"), "`"+melody._entityManager.getGuildEntity(guild).getPrefix()+"config " +subcommand.name().toLowerCase()+" "+subcommand.usage+"`", false);
		builder.addField(Emoji.CHECK_MARK+" "+mf.format(guild, "config.info.submenu.valid-settings"), "`"+customvalidsettigs+"`", false);
		Messenger.sendMessageEmbed(channel, builder).queue();
	}
	
	private void sendSubCommandMenu(Object currentvalue, TextChannel channel, SubCommand subcommand) {
		Guild guild = channel.getGuild();
		EmbedBuilder builder = new EmbedBuilder();
		builder.setTitle(mf.format(guild, "config.info.submenu.title",Constants.BUILDNAME,subcommand.title));	
		builder.setDescription(mf.format(guild, "config.sub."+subcommand.name()+".info"));
		builder.addField(Emoji.CLIPBOARD+" "+mf.format(guild, "config.info.submenu.current-value"), "`"+currentvalue.toString()+"`", false);
		builder.addField(Emoji.PENCIL+" "+mf.format(guild, "config.info.submenu.usage"), "`"+melody._entityManager.getGuildEntity(guild).getPrefix()+"config " +subcommand.name().toLowerCase()+" "+subcommand.usage+"`", false);
		builder.addField(Emoji.CHECK_MARK+" "+mf.format(guild, "config.info.submenu.valid-settings"), "`"+mf.format(guild, "config.sub."+subcommand.name()+".valid-settings")+"`", false);
		Messenger.sendMessageEmbed(channel, builder).queue();
	}
	
	private enum SubCommand{
		PREFIX("[new prefix]",Emoji.EXCLAMATION_MARK+" Prefix"),
		LANGUAGE("[new language]",Emoji.WHITE_FLAG+" Language"),
		ANNOUNCESONGS("[on|off]",Emoji.BELL+" Announce Songs"),
		DJONLY("[on|off]", Emoji.SILVERCD+" DJ Only"),
		DJROLES("[@djrole]", Emoji.GOLDCD+" DJRoles");
		
		String usage;
		String title;
		SubCommand(String usage, String title) {
			this.usage = usage;
			this.title = title;
		}
	}
	
	@Override
	public String[] getCommandPrefix() {
		return new String[] {"config", "c"};
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