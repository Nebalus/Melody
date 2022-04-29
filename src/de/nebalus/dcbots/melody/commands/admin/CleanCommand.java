package de.nebalus.dcbots.melody.commands.admin;

import de.nebalus.dcbots.melody.tools.cmdbuilder.ServerCommand;

public class CleanCommand extends ServerCommand{
	/*
	public CleanCommand() {
		super();
		setMainPermission(CommandPermission.ADMIN);
		setPrefixes("cleanup", "clean");
		setType(CommandType.CHAT);
		setDescription("Clears command and bot messages.");
	}
	
	@Override
	public void performMainCMD(Member member, TextChannel channel, Message message, Guild guild, GuildEntity guildentity) {
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
	*/
}
