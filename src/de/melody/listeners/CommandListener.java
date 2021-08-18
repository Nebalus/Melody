package de.melody.listeners;

import java.util.List;

import de.melody.ConsoleLogger;
import de.melody.Melody;
import de.melody.speechpackets.MessageFormatter;
import de.melody.utils.Emojis;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CommandListener extends ListenerAdapter{

	private Melody melody = Melody.INSTANCE;
	private MessageFormatter mf = melody.getMessageFormatter();
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		String message = event.getMessage().getContentDisplay();
		if(event.isFromType(ChannelType.TEXT)) {
			List<User> MentionedUsers = event.getMessage().getMentionedUsers();
			TextChannel channel = event.getTextChannel();
			Guild guild = event.getGuild();
			String prefix = melody.entityManager.getGuildEntity(guild.getIdLong()).getPrefix();
			
			if(!event.getAuthor().isBot()) {
				if(message.startsWith(prefix)) {
					int count = 0;
					for (int i = 0; i < prefix.length(); i++) {
						count++;
					}
					String[] args = message.substring(count).split(" ");
			
					if(args.length > 0){
						if(!melody.getCmdMan().performServer(args[0], event.getMember(), channel, event.getMessage(), event.getGuild())) {
							channel.sendMessage(event.getJDA().getEmoteById(Emojis.ANIMATED_THINKING_EMOJI).getAsMention()+" "+mf.format(guild.getIdLong(), "feedback.info.unknown-command",prefix)).queue();
						}
					}
				}else if(MentionedUsers.contains(channel.getJDA().getSelfUser())) {
					event.getChannel().sendMessage(mf.format(guild.getIdLong(), "feedback.info.prefix",prefix)).queue();
				}
			}
		}else if(event.isFromType(ChannelType.PRIVATE)) {
			ConsoleLogger.info("Test", message);
		}
	}	
}
	


