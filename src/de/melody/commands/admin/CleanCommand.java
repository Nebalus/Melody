package de.melody.commands.admin;

import java.util.ArrayList;
import java.util.List;

import de.melody.core.Constants;
import de.melody.core.Melody;
import de.melody.entities.GuildEntity;
import de.melody.speechpackets.MessageFormatter;
import de.melody.utils.Utils.ConsoleLogger;
import de.melody.utils.commandbuilder.CommandInfo;
import de.melody.utils.commandbuilder.CommandType;
import de.melody.utils.commandbuilder.ServerCommand;
import de.melody.utils.messenger.Messenger;
import de.melody.utils.messenger.Messenger.ErrorMessageBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class CleanCommand implements ServerCommand{

	private Melody melody = Melody.INSTANCE;
	
	@Override
	public void performCommand(Member member, TextChannel channel, Message message, Guild guild) {
		String[] args = message.getContentDisplay().split(" ");
		GuildEntity ge = melody.getEntityManager().getGuildEntity(guild);
		int clearmessages = Constants.DEFAULTCLEANMESSAGES;
		
		if(args.length == 2) {		
			try {
				clearmessages = Integer.parseInt(args[1]);
			}catch(NumberFormatException e) {
				Messenger.sendErrorMessage(channel, new ErrorMessageBuilder().setMessageFormat(guild, "info.command-usage", getCommandPrefix().get(0)+" <1-"+Constants.MAXCLEANMESSAGES+">"));		
			}
		}
		if(clearmessages <= Constants.MAXCLEANMESSAGES && clearmessages >= 1) {
			message.delete().queue();
			int i = clearmessages;
			List<Message> purgemessages = new ArrayList<>();
			for(Message cachemessage : channel.getIterableHistory().cache(false)) {
				if(cachemessage.getAuthor() == channel.getJDA().getSelfUser() || cachemessage.getContentRaw().startsWith(ge.getPrefix())) {
					purgemessages.add(cachemessage);
				}
				if(--i <= 0) break;
			}
			channel.purgeMessages(purgemessages);	
		}else {
			Messenger.sendErrorMessage(channel, new ErrorMessageBuilder().setMessageFormat(guild, "info.command-usage", getCommandPrefix().get(0)+" <1-"+Constants.MAXCLEANMESSAGES+">"));		
		}
	}

	@Override
	public void performSlashCommand(Member member, MessageChannel channel, Guild guild, SlashCommandEvent event) {
		
	}

	@Override
	public List<String> getCommandPrefix() {
		return List.of("clean");
	}

	@Override
	public CommandType getCommandType() {
		return CommandType.CHAT_COMMAND;
	}

	@Override
	public CommandInfo getCommandInfo() {
		return CommandInfo.ADMIN_COMMAND;
	}

	@Override
	public List<OptionData> getCommandOptions() {
		return null;
	}

	@Override
	public String getCommandDescription() {
		return "Clears all messages send from the bot";
	}

}
