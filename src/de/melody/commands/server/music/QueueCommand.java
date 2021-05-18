package de.melody.commands.server.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import de.melody.Melody;
import de.melody.commands.types.ServerCommand;
import de.melody.entities.reacts.QueueReaction;
import de.melody.music.MusicController;
import de.melody.music.Queue;
import de.melody.music.QueuedTrack;
import de.melody.speechpackets.MessageFormatter;
import de.melody.utils.Emojis;
import de.melody.utils.Utils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class QueueCommand implements ServerCommand{

	private static Melody melody = Melody.INSTANCE;
	private static MessageFormatter mf = melody.getMessageFormatter();
	
	@Override
	public void performCommand(Member m, TextChannel channel, Message message, Guild guild) {	
		channel.sendMessage("Loading...").queue((queuemessage) ->{
			queuemessage.addReaction(Emojis.BACK).queue();	
			queuemessage.addReaction(Emojis.RESUME).queue();	
			queuemessage.addReaction(Emojis.REFRESH).queue();

			QueueReaction qe = new QueueReaction(melody.playerManager.getController(guild.getIdLong()).getQueue());
			Melody.INSTANCE.entityManager.getGuildController(guild.getIdLong()).getReactionManager().addReactionMessage(queuemessage.getIdLong(), qe);
			queuemessage.editMessage(loadQueueEmbed(guild,qe).build()).queue();
			queuemessage.editMessage(".").queue();
		});		
	}
	
	public static EmbedBuilder loadQueueEmbed(Guild guild, QueueReaction qe) {
		MusicController controller = melody.playerManager.getController(guild.getIdLong());
		EmbedBuilder builder = new EmbedBuilder();
		Queue queue = controller.getQueue();
		builder.setTitle("**Queue for "+guild.getName()+"**");
		builder.setThumbnail(guild.getIconUrl());
		
		String list = null;
		if(controller.getPlayer().getPlayingTrack() != null) {
			list = mf.format(guild.getIdLong(), "music.track.currently-playing")+"\n"
				+ " ["+ controller.getPlayer().getPlayingTrack().getInfo().title+"]("+controller.getPlayer().getPlayingTrack().getInfo().uri+") | "+mf.format(guild.getIdLong(), "music.user.who-requested")+queue.currentplaying.getWhoQueued().getUser().getAsTag()+" \n"
				+ " \n";
			int size = 1;
			int page = qe.getPage()*10;
			for(QueuedTrack qt : queue.getQueuelist()) {
				if(size <= page && size >= page-9) {
					AudioTrack at = qt.getTrack();
					if(size == page-9) {
						list = list + mf.format(guild.getIdLong(), "music.queue.in-queue")+"\n";
					}
					list = list + "``"+size+".`` ``["+Utils.getTimeFormat(at.getInfo().length)+"]`` **"+ at.getInfo().title+"** - "+qt.getWhoQueued().getAsMention()+"\n";
				}
				size++;
			}
			int queuepage = queue.getQueueSize()/10;
			int maxpage = (float) queue.getQueueSize()/10 > queuepage ? queuepage+1 : queuepage;
			builder.setFooter(mf.format(guild.getIdLong(), "music.queue.page", qe.getPage()+"/"+maxpage));
			
		}else {
			list = mf.format(guild.getIdLong(), "music.info.currently-playing-null");
		}
		builder.setDescription(list);
		return builder;
	}
}
