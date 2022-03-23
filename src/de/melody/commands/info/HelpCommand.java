package de.melody.commands.info;

import java.util.ArrayList;

import de.melody.core.Constants;
import de.melody.core.Melody;
import de.melody.tools.cmdbuilder.CommandPermission;
import de.melody.tools.cmdbuilder.CommandType;
import de.melody.tools.cmdbuilder.ServerCommand;
import de.melody.tools.entitymanager.entitys.GuildEntity;
import de.melody.tools.messenger.Messenger;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class HelpCommand extends ServerCommand{
	
	public HelpCommand() {
		super();
		setMainPermission(CommandPermission.EVERYONE);
		setDescription("Shows the help menu.");
		setPrefixes("help", "h");
		setType(CommandType.CHAT);
	}
	
	@Override
	public void performMainCMD(Member member, TextChannel channel, Message message, Guild guild, GuildEntity guildentity) {
		Messenger.sendMessageEmbed(channel, generateMenu(null, guildentity)).queue();
	}
	
	private EmbedBuilder generateMenu(String searchquery, GuildEntity guildentity) {
		EmbedBuilder builder = new EmbedBuilder();
		if(searchquery == null) {
			builder.setAuthor("Help Command", null, Constants.ICON_URL);
			
			ArrayList<String> admincmds = new ArrayList<String>();
			ArrayList<String> djcmds = new ArrayList<String>();
			ArrayList<String> everyonecmds = new ArrayList<String>();
			
			for(ServerCommand scmd : Melody.INSTANCE.cmdMan.getCommands()) {
				switch(scmd.getMainPermission()) {
					case ADMIN:
						admincmds.add("`"+scmd.getPrefixs()[0]+"`");
						break;
						
					case DJ:
						djcmds.add("`"+scmd.getPrefixs()[0]+"`");
						break;
						
					case EVERYONE:
						everyonecmds.add("`"+scmd.getPrefixs()[0]+"`");
						break;
					
					default:
						break;
				}
			}
			
			builder.addField("**Admin Commands**", admincmds.toString().replace("[", "").replace("]", ""), false);
			builder.addField("**DJ Commands**", djcmds.toString().replace("[", "").replace("]", ""), false);
			builder.addField("**Everyone Commands**", everyonecmds.toString().replace("[", "").replace("]", ""), false);
			builder.addField("**Web Dashboard**", "[View Commands]("+Constants.COMMAND_URL+"?p="+guildentity.getPrefix()+")", false);
			builder.setFooter("Type '"+guildentity.getPrefix()+"help <CommandName>' for details on a command.");
		}else {
			
		}
		return builder;
	}
}
