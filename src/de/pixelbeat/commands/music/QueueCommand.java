package de.pixelbeat.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import de.pixelbeat.PixelBeat;
import de.pixelbeat.commands.types.ServerCommand;
import de.pixelbeat.music.MusicController;
import de.pixelbeat.music.Queue;
import de.pixelbeat.music.QueuedTrack;
import de.pixelbeat.speechpackets.MessageFormatter;
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
			list = "Currently playing:\n"
					+ " ["+ controller.getPlayer().getPlayingTrack().getInfo().title+"]("+controller.getPlayer().getPlayingTrack().getInfo().uri+") | Requested by: "+queue.currentplaying.getWhoQueued().getUser().getAsTag()+" \n"
					+ " \n";
			int size = 1;
			for(QueuedTrack qt : queue.getQueuelist()) {
				AudioTrack at = qt.getTrack();
				if(size <= 10 && size >= 1) {
					if(size == 1) {
						list = list + "In queue:\n";
					}
						list = list + "``"+size+"`` - ["+ at.getInfo().title+"]("+at.getInfo().uri+") | ``Requested by: "+qt.getWhoQueued().getUser().getAsTag()+"``\n"
								+ " \n";
				}
				size++;
			}
			list = list + "**"+(size-1)+" **\n";
		}else {
			list = "Oops currently i am not playing any music!";
		}
		builder.setDescription(list);
		channel.sendMessage(builder.build()).queue();
	}

}
