package de.pixelbeat.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import de.pixelbeat.PixelBeat;
import de.pixelbeat.commands.types.ServerCommand;
import de.pixelbeat.music.MusicController;
import de.pixelbeat.music.Queue;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class QueueCommand implements ServerCommand{

	@Override
	public void performCommand(Member m, TextChannel channel, Message message) {
		MusicController controller = PixelBeat.INSTANCE.playerManager.getController(channel.getGuild().getIdLong());
		Queue queue = controller.getQueue();
		EmbedBuilder builder = new EmbedBuilder();
		
		builder.setTitle("**Queue for "+channel.getGuild().getName()+"**");
		
		String list = null;
		
		if(controller.getPlayer().getPlayingTrack() != null) {
			list = "Currently playing:\n"
					+ " ["+ controller.getPlayer().getPlayingTrack().getInfo().title+"]("+controller.getPlayer().getPlayingTrack().getInfo().uri+") | Requested by: "+queue.getuserwhoqueued(0).getUser().getAsTag()+" \n"
					+ " \n";
			int size = 1;
			for(AudioTrack t : queue.getQueuelist()) {
				if(size <= 10 && size >= 1) {
					if(size == 1) {
						list = list + "In queue:\n";
					}
						list = list + "``"+size+"`` - ["+ t.getInfo().title+"]("+t.getInfo().uri+") | ``Requested by: "+queue.getuserwhoqueued(size).getUser().getAsTag()+"``\n"
								+ " \n";
				}
				size++;
			}
			list = list + "**"+(size-1)+" **\n";
		}else {
			list = "Oops im currently not playing any music!";
		}
		builder.setDescription(list);
		channel.sendMessage(builder.build()).queue();
	}

}
