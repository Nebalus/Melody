package de.pixelbeat.listeners;

import java.util.List;

import de.pixelbeat.PixelBeat;
import de.pixelbeat.speechpackets.MessageFormatter;
import de.pixelbeat.utils.Emojis;
import de.pixelbeat.utils.Utils;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CommandListener extends ListenerAdapter{

	private MessageFormatter mf = PixelBeat.INSTANCE.getMessageFormatter();
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		String message = event.getMessage().getContentDisplay();
		if(event.isFromType(ChannelType.TEXT)) {
			List<User> MentionedUsers = event.getMessage().getMentionedUsers();
			TextChannel channel = event.getTextChannel();
			Guild guild = event.getGuild();
			String prefix = Utils.getGuildPrefix(guild.getIdLong());
			
			if(!event.getAuthor().isBot()) {
				if(message.startsWith(prefix)) {
					int count = 0;
					for (int i = 0; i < prefix.length(); i++) {
						count++;
					}
					String[] args = message.substring(count).split(" ");
			
					if(args.length > 0){
						if(!PixelBeat.INSTANCE.getCmdMan().perform(args[0], event.getMember(), channel, event.getMessage())) {
							channel.sendMessage(event.getJDA().getEmoteById(Emojis.ANIMATED_THINKING_EMOJI).getAsMention()+" "+mf.format(guild.getIdLong(), "feedback.info.unknown-command",prefix)).queue();
						}
					}
				}else if(MentionedUsers.contains(channel.getJDA().getSelfUser())) {
					event.getChannel().sendMessage(mf.format(guild.getIdLong(), "feedback.info.prefix",prefix)).queue();
				}
			}
		}
	}	
}
	


