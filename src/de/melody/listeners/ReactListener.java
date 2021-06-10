package de.melody.listeners;

import java.util.concurrent.TimeUnit;

import de.melody.Melody;
import de.melody.commands.server.music.QueueCommand;
import de.melody.entities.reacts.QueueReaction;
import de.melody.entities.reacts.ReactionManager;
import de.melody.entities.reacts.ReactionTypes;
import de.melody.entities.reacts.TrackReaction;
import de.melody.utils.Emojis;
import de.melody.utils.ID_Manager;
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
			User user = event.getUser();
			if(!user.isBot()) {
				Guild guild = event.getGuild();
				Long messageid = event.getMessageIdLong();
				String emoji = event.getReactionEmote().getEmoji();
				TextChannel channel = event.getTextChannel();
				
				ReactionManager reactionManager = Melody.INSTANCE.entityManager.getGuildController(guild.getIdLong()).getReactionManager();
				
				TrackReaction tr = (TrackReaction) reactionManager.getReacton(messageid, ReactionTypes.TRACKREACTION);
				if(tr != null) {
					if(emoji.equals(Emojis.SPARKLING_HEART)) {
						if(Melody.INSTANCE.entityManager.getUserEntity(user).getFavoriteMusicId() > 0) {
				
							
							
						}else {
							user.openPrivateChannel().queue((ch) ->{
								ch.sendMessage("Token: "+ID_Manager.generateID()).queue((message) ->{
									message.delete().queueAfter(120, TimeUnit.SECONDS);
								});
							});	
						}
						channel.removeReactionById(messageid, emoji, user).queue();
					}
				}
				
				QueueReaction qr;
				if((qr = (QueueReaction) reactionManager.getReacton(messageid, ReactionTypes.QUEUEREACTION)) != null) {
					switch (emoji) {
					case Emojis.BACK:
						channel.removeReactionById(messageid, emoji, user).queue();
						if(qr.removePage()) {
							channel.editMessageById(messageid, QueueCommand.loadQueueEmbed(guild,qr).build()).queue();		
						}
						break;
						
					case Emojis.RESUME:
						channel.removeReactionById(messageid, emoji, user).queue();
						if(qr.addPage()) {
							channel.editMessageById(messageid, QueueCommand.loadQueueEmbed(guild,qr).build()).queue();	
						}
						break;
					
					case Emojis.REFRESH:
						channel.removeReactionById(messageid, emoji, user).queue();
						channel.editMessageById(messageid, QueueCommand.loadQueueEmbed(guild,qr).build()).queue();
						break;
					}
				}
			 }
		}
	}	
}