package de.pixelbeat.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import de.pixelbeat.PixelBeat;
import de.pixelbeat.commands.types.ServerCommand;
import de.pixelbeat.music.MusicController;
import de.pixelbeat.music.Queue;
import de.pixelbeat.music.QueuedTrack;
import de.pixelbeat.speechpackets.MessageFormatter;
import de.pixelbeat.utils.Emojis;
import de.pixelbeat.utils.Utils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class QueueCommand implements ServerCommand{

	private MessageFormatter mf = PixelBeat.INSTANCE.getMessageFormatter();
	
	@Override
	public void performCommand(Member m, TextChannel channel, Message message) {
		MusicController controller = PixelBeat.INSTANCE.playerManager.getController(channel.getGuild().getIdLong());
		EmbedBuilder builder = new EmbedBuilder();
		Queue queue = controller.getQueue();
		
		builder.setTitle("**Queue for "+channel.getGuild().getName()+"**");
		
		String list = null;
		
		if(controller.getPlayer().getPlayingTrack() != null) {
			list = mf.format(channel.getGuild().getIdLong(), "music.track.currently-playing")+"\n"
				+ " ["+ controller.getPlayer().getPlayingTrack().getInfo().title+"]("+controller.getPlayer().getPlayingTrack().getInfo().uri+") | "+mf.format(channel.getGuild().getIdLong(), "music.user.who-requested")+queue.currentplaying.getWhoQueued().getUser().getAsTag()+" \n"
				+ " \n";
			int size = 1;
			for(QueuedTrack qt : queue.getQueuelist()) {
				AudioTrack at = qt.getTrack();
				if(size <= 10 && size >= 1) {
					if(size == 1) {
						list = list + mf.format(channel.getGuild().getIdLong(), "music.queue.in-queue")+"\n";
					}
					list = list + "``"+size+".`` ``["+Utils.getTimeFormat(at.getInfo().length)+"]`` **["+ at.getInfo().title+"]("+at.getInfo().uri+")** - "+qt.getWhoQueued().getAsMention()+"\n";
				}
				size++;
			}
			builder.setFooter(mf.format(channel.getGuild().getIdLong(), "music.queue.page", 1+"/"+(((size-1) / 10) > 0 ? ((size-1) / 10) : 1)));
			
		}else {
			list = mf.format(channel.getGuild().getIdLong(), "music.info.currently-playing-null");
		}
		builder.setDescription(list);
		channel.sendMessage(builder.build()).queue((queuemessage) ->{
			queuemessage.addReaction(Emojis.BACK).queue();	
			queuemessage.addReaction(Emojis.STOP).queue();	
			queuemessage.addReaction(Emojis.RESUME).queue();	
		});
		
	}
	
}
