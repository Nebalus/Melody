package de.melody.commands.server.music;

import java.util.List;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import de.melody.Config;
import de.melody.Melody;
import de.melody.commands.types.ServerCommand;
import de.melody.entities.reacts.QueueReaction;
import de.melody.music.MusicController;
import de.melody.music.Queue;
import de.melody.music.QueuedTrack;
import de.melody.speechpackets.MessageFormatter;
import de.melody.utils.Emoji;
import de.melody.utils.Utils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class QueueCommand implements ServerCommand{

	private static Melody melody = Melody.INSTANCE;
	private static MessageFormatter mf = melody.getMessageFormatter();
	
	@SuppressWarnings("deprecation")
	@Override
	public void performCommand(Member m, TextChannel channel, Message message, Guild guild) {	
		channel.sendMessage("Loading...").queue((queuemessage) ->{
			queuemessage.addReaction(Emoji.BACK).queue();	
			queuemessage.addReaction(Emoji.RESUME).queue();	
			queuemessage.addReaction(Emoji.REFRESH).queue();

			QueueReaction qe = new QueueReaction(melody.playerManager.getController(guild.getIdLong()).getQueue());
			Melody.INSTANCE.entityManager.getGuildController(guild.getIdLong()).getReactionManager().addReactionMessage(queuemessage.getIdLong(), qe);
			queuemessage.editMessage(loadQueueEmbed(guild,qe).build()).queue();
			queuemessage.editMessage("‏‏‎ ‎").queue();
		});		
	}
	
	public static EmbedBuilder loadQueueEmbed(Guild guild, QueueReaction qe) {
		MusicController controller = melody.playerManager.getController(guild.getIdLong());
		EmbedBuilder builder = new EmbedBuilder();
		Queue queue = controller.getQueue();
		builder.setTitle(mf.format(guild, "music.queue.from-guild",guild.getName()));
		builder.setThumbnail(guild.getIconUrl());
		builder.setColor(Config.HEXEmbeld);
		String list = null;
		if(controller.getPlayer().getPlayingTrack() != null) {
			if(queue.currentlyPlaying() != null) {
				list = mf.format(guild, "music.track.currently-playing")+"\n"
					+ " ["+ controller.getPlayer().getPlayingTrack().getInfo().title+"]("+controller.getPlayer().getPlayingTrack().getInfo().uri+") | "+mf.format(guild, "music.user.who-requested")+queue.currentlyPlaying().getWhoQueued().getUser().getAsTag()+" \n"
					+ " \n";
			}
			int size = 1;
			int page = qe.getPage()*10;
			for(QueuedTrack qt : queue.getQueuelist()) {
				if(size <= page && size >= page-9) {
					AudioTrack at = qt.getTrack();
					if(size == page-9) {
						list = list + mf.format(guild, "music.queue.in-queue")+"\n";
					}
					list = list + "``"+size+".`` ``["+Utils.getTimeFormat(at.getInfo().length)+"]`` **"+ at.getInfo().title+"** - "+qt.getWhoQueued().getAsMention()+"\n";
				}
				size++;
			}
			int queuepage = queue.getQueueSize()/10;
			int maxpage = (float) queue.getQueueSize()/10 > queuepage ? queuepage+1 : queuepage;
			builder.setFooter(mf.format(guild, "music.queue.page", qe.getPage()+"/"+maxpage));
			
		}else {
			list = mf.format(guild, "feedback.music.currently-playing-null");
		}
		builder.setDescription(list);
		return builder;
	}

	@Override
	public List<String> getCommandPrefix() {
		return List.of("queue","q");
	}
}
