package de.melody.listeners;

import java.util.List;

import de.melody.core.Melody;
import de.melody.tools.entitymanager.entitys.GuildEntity;
import de.melody.tools.messenger.MessageFormatter;
import de.melody.tools.messenger.Messenger;
import de.melody.tools.messenger.Messenger.ErrorMessageBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public final class CommandListener extends ListenerAdapter {
	
	private Melody melody = Melody.INSTANCE;
	private MessageFormatter mf = melody.messageformatter;
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		String message = event.getMessage().getContentDisplay();
		if(event.isFromType(ChannelType.TEXT)) {
			if(!event.getAuthor().isBot()) {
				List<User> MentionedUsers = event.getMessage().getMentionedUsers();
				TextChannel channel = event.getTextChannel();
				Guild guild = event.getGuild();
				GuildEntity ge = melody.entityMan.getGuildEntity(guild);
				if(message.startsWith(ge.getPrefix())) {
					int count = 0;
					for (int i = 0; i < ge.getPrefix().length(); i++) {
						count++;
					}
					String[] args = message.substring(count).split(" ");
					if(args.length > 0){
						try {
							if(!melody.cmdMan.performServer(args[0], event.getMember(), channel, event.getMessage(), event.getGuild(), ge)) {	
								Messenger.sendErrorMessage(channel, new ErrorMessageBuilder().setMessageFormat(guild, "info.unknown-command"));
							}
						}catch(InsufficientPermissionException e) {
							Messenger.sendErrorMessage(channel, new ErrorMessageBuilder().setMessageFormat(guild, "bot-no-permmisions", e.getPermission()));
						}
					}
				}else if(MentionedUsers.contains(channel.getJDA().getSelfUser())) {
					channel.sendMessage(mf.format(guild, "feedback.info.prefix",ge.getPrefix())).queue();
				}
			}
		}
	}
}
