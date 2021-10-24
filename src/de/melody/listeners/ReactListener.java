package de.melody.listeners;

import java.util.concurrent.TimeUnit;

import de.melody.commands.music.QueueCommand;
import de.melody.core.Melody;
import de.melody.entities.reacts.QueueReaction;
import de.melody.entities.reacts.ReactionManager;
import de.melody.entities.reacts.ReactionTypes;
import de.melody.entities.reacts.TrackReaction;
import de.melody.utils.Utils.Emoji;
import de.melody.utils.Utils.IDGenerator;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ReactListener extends ListenerAdapter{
	
	@SuppressWarnings("deprecation")
	@Override
	public void onMessageReactionAdd(MessageReactionAddEvent event) {
		if(event.isFromType(ChannelType.TEXT)) {
			User user = event.getUser();
			if(!user.isBot() && event.getReactionEmote().isEmoji()) {
				Guild guild = event.getGuild();
				Long messageid = event.getMessageIdLong();
				String emoji = event.getReactionEmote().getEmoji();
				TextChannel channel = event.getTextChannel();
				ReactionManager reactionManager = Melody.INSTANCE.getEntityManager().getGuildEntity(guild).getReactionManager();
				TrackReaction tr = (TrackReaction) reactionManager.getReacton(messageid, ReactionTypes.TRACKREACTION);
				
				if(tr != null) {
					if(emoji.equals(Emoji.SPARKLING_HEART)) {
						if(Melody.INSTANCE.getEntityManager().getUserEntity(user).getFavoriteMusicId() > 0) {
				
							
							
						}else {
							user.openPrivateChannel().queue((ch) ->{
								ch.sendMessage("Token: "+IDGenerator.generateID()).queue((message) ->{
									message.delete().queueAfter(120, TimeUnit.SECONDS);
								});
							});	
						}
					}
				}
				
				QueueReaction qr;
				if((qr = (QueueReaction) reactionManager.getReacton(messageid, ReactionTypes.QUEUEREACTION)) != null) {
					switch (emoji) {
					case Emoji.BACK:
						channel.removeReactionById(messageid, emoji, user).queue();
						if(qr.removePage()) {
							channel.editMessageById(messageid, QueueCommand.loadQueueEmbed(guild,qr).build()).queue();		
						}
						break;
						
					case Emoji.RESUME:
						channel.removeReactionById(messageid, emoji, user).queue();
						if(qr.addPage()) {
							channel.editMessageById(messageid, QueueCommand.loadQueueEmbed(guild,qr).build()).queue();	
						}
						break;
					
					case Emoji.REFRESH:
						channel.removeReactionById(messageid, emoji, user).queue();
						channel.editMessageById(messageid, QueueCommand.loadQueueEmbed(guild,qr).build()).queue();
						break;
					}
				}
			}
		}
	}	
}