package de.melody.commands.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.melody.core.Constants;
import de.melody.entities.GuildEntity;
import de.melody.tools.commandbuilder.CommandPermission;
import de.melody.tools.commandbuilder.CommandType;
import de.melody.tools.commandbuilder.ServerCommand;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class CleanCommand implements ServerCommand{
	
	@Override
	public void performCommand(Member member, TextChannel channel, Message message, Guild guild, GuildEntity guildentity) {
		int i = Constants.MAXCLEANMESSAGES;
		List<Message> purgemessages = new ArrayList<>();
		for(Message cachemessage : channel.getIterableHistory().cache(false)) {
			if(cachemessage.getAuthor() == channel.getJDA().getSelfUser() || cachemessage.getContentRaw().startsWith(guildentity.getPrefix())) {
				purgemessages.add(cachemessage);
			}
			if(--i <= 0) break;
		}
		channel.purgeMessages(purgemessages);	
		channel.sendMessage(purgemessages.size() + " from "+Constants.MAXCLEANMESSAGES+" Messages have been deleted.").complete().delete().queueAfter(20, TimeUnit.SECONDS);
	}

	@Override
	public void performSlashCommand(Member member, MessageChannel channel, Guild guild, GuildEntity guildentity, SlashCommandEvent event) {
		
	}

	@Override
	public String[] getCommandPrefix() {
		return new String[] {"cleanup", "clean"};
	}

	@Override
	public CommandType getCommandType() {
		return CommandType.CHAT;
	}

	@Override
	public OptionData[] getCommandOptions() {
		return null;
	}

	@Override
	public String getCommandDescription() {
		return "Clears command and bot messages";
	}

	@Override
	public CommandPermission getMainPermmision() {
		return CommandPermission.ADMIN;
	}

}
