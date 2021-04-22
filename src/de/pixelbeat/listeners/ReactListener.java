package de.pixelbeat.listeners;

import de.pixelbeat.PixelBeat;
import de.pixelbeat.commands.music.QueueCommand;
import de.pixelbeat.entities.EntityController;
import de.pixelbeat.entities.QueueEmbed;
import de.pixelbeat.utils.Emojis;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ReactListener extends ListenerAdapter{
	
	@Override
	public void onMessageReactionAdd(MessageReactionAddEvent event) {
		if(event.isFromType(ChannelType.TEXT)) {
			Guild guild = event.getGuild();
			Long messageid = event.getMessageIdLong();
			String emoji = event.getReactionEmote().getEmoji();
			TextChannel channel = event.getTextChannel();
			User user = event.getUser();
			
			EntityController entityController = PixelBeat.INSTANCE.entityManager.getController(guild.getIdLong());
			QueueEmbed qe;
			if((qe = entityController.getQueueEmbed(messageid)) != null && !user.isBot()) {
				switch (emoji) {
				case Emojis.BACK:
					channel.removeReactionById(messageid, emoji, event.getUser()).queue();
					if(qe.removePage()) {
						channel.editMessageById(messageid, QueueCommand.loadQueueEmbed(guild,qe).build()).queue();		
					}
					break;
				case Emojis.RESUME:
					channel.removeReactionById(messageid, emoji, event.getUser()).queue();
					if(qe.addPage()) {
						channel.editMessageById(messageid, QueueCommand.loadQueueEmbed(guild,qe).build()).queue();	
					}
					break;
				
				case Emojis.EXIT:
					entityController.removeQueueEmbed(messageid);
					channel.clearReactionsById(messageid).queue();
					break;
				}
			}
		}
	}	
}