package de.nebalus.dcbots.melody.commands.info;

import java.util.ArrayList;

import de.nebalus.dcbots.melody.core.Melody;
import de.nebalus.dcbots.melody.core.constants.Settings;
import de.nebalus.dcbots.melody.core.constants.Url;
import de.nebalus.dcbots.melody.tools.cmdbuilder.CommandPermission;
import de.nebalus.dcbots.melody.tools.cmdbuilder.ServerCommand;
import de.nebalus.dcbots.melody.tools.entitymanager.entitys.GuildEntity;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class HelpCommand extends ServerCommand{
	
	public HelpCommand() {
		super("help");
		setMainPermission(CommandPermission.EVERYONE);
		setDescription("Shows the help menu.");
	}
	
	@Override
	public void performMainCMD(Member member, MessageChannel channel, Guild guild, GuildEntity guildentity, SlashCommandInteractionEvent event) {
//		String[] command = message.getContentDisplay().split(" ");
//		if(command.length == 2) {
//			Messenger.sendMessageEmbed(channel, generateMenu(command[1], guildentity)).queue();
//		}else {
//			Messenger.sendMessageEmbed(channel, generateMenu(null, guildentity)).queue();
//		}
	}
	
	private EmbedBuilder generateMenu(String searchquery, GuildEntity guildentity) {
		EmbedBuilder builder = new EmbedBuilder();
		if(searchquery == null) {
			builder.setAuthor("Help Command", null, Url.ICON.toString());
			
			ArrayList<String> admincmds = new ArrayList<String>();
			ArrayList<String> djcmds = new ArrayList<String>();
			ArrayList<String> everyonecmds = new ArrayList<String>();
			
			for(ServerCommand scmd : Melody.INSTANCE.cmdMan.getCommands()) {
				switch(scmd.getMainPermission()) {
					case ADMIN:
						admincmds.add("`"+scmd.getPrefix()+"`");
						break;
						
					case DJ:
						djcmds.add("`"+scmd.getPrefix()+"`");
						break;
						
					case EVERYONE:
						everyonecmds.add("`"+scmd.getPrefix()+"`");
						break;
					
					default:
						break;
				}
			}
			if(!admincmds.isEmpty()) {
				builder.addField("**Admin Commands**", admincmds.toString().replace("[", "").replace("]", ""), false);
			}
			if(!djcmds.isEmpty()) {
				builder.addField("**DJ Commands**", djcmds.toString().replace("[", "").replace("]", ""), false);
			}
			if(!everyonecmds.isEmpty()) {
				builder.addField("**Everyone Commands**", everyonecmds.toString().replace("[", "").replace("]", ""), false);
			}
			builder.addField("**Web Dashboard**", "[View Commands](" + Url.COMMAND.toString() + "?p=" + Settings.CMD_PREFIX + ")", false);
			builder.setFooter("Type '" + Settings.CMD_PREFIX + "help <CommandName>' for details on a command.");
		}else {
			ServerCommand cmd;
			try {
				cmd = Melody.INSTANCE.cmdMan.getCommand(searchquery);
				builder.setAuthor("Help Command: "+searchquery, null, Url.ICON.toString());
				String description;
				
				builder.setDescription("This feature is still work and progress");
				
				
			}catch(NullPointerException e) {
				return generateMenu(null, guildentity);
			}
		}
		return builder;
	}
}