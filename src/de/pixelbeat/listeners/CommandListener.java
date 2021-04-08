package de.pixelbeat.listeners;

import java.util.List;

import de.pixelbeat.PixelBeat;
import de.pixelbeat.utils.Emojis;
import de.pixelbeat.utils.Misc;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CommandListener extends ListenerAdapter{

	
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		String message = event.getMessage().getContentDisplay();
		if(event.isFromType(ChannelType.TEXT)) {
			List<User> MentionedUsers = event.getMessage().getMentionedUsers();
			TextChannel channel = event.getTextChannel();
			Guild guild = event.getGuild();
			String prefix = Misc.getGuildPrefix(guild.getIdLong());
			
			if(!event.getAuthor().isBot()) {
				if(message.startsWith(prefix)) {
					int count = 0;
					for (int i = 0; i < prefix.length(); i++) {
						count++;
					}
					String[] args = message.substring(count).split(" ");
			
					if(args.length > 0){
						if(!PixelBeat.INSTANCE.getCmdMan().perform(args[0], event.getMember(), channel, event.getMessage())) {
							channel.sendMessage(event.getJDA().getEmoteById(Emojis.ANIMATED_THINKING_EMOJI).getAsMention()+" `hmm, it looks like I don't know this command. Please use "+prefix+"help to see the command list.`").queue();
						}
					}
				}else if(MentionedUsers.contains(channel.getJDA().getSelfUser())) {
					event.getChannel().sendMessage("**My prefix on this server is `"+prefix+"`**").queue();
				}
			}
		}
	}	
}
	


