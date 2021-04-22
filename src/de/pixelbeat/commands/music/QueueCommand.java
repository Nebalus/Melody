package de.pixelbeat.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import de.pixelbeat.PixelBeat;
import de.pixelbeat.commands.types.ServerCommand;
import de.pixelbeat.entities.QueueEmbed;
import de.pixelbeat.music.MusicController;
import de.pixelbeat.music.Queue;
import de.pixelbeat.music.QueuedTrack;
import de.pixelbeat.speechpackets.MessageFormatter;
import de.pixelbeat.utils.Emojis;
import de.pixelbeat.utils.Utils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class QueueCommand implements ServerCommand{

	private static MessageFormatter mf = PixelBeat.INSTANCE.getMessageFormatter();
	
	@Override
	public void performCommand(Member m, TextChannel channel, Message message) {
	
		
		
		channel.sendMessage("Loading...").queue((queuemessage) ->{
			queuemessage.addReaction(Emojis.BACK).queue();	
			queuemessage.addReaction(Emojis.RESUME).queue();	
			queuemessage.addReaction(Emojis.EXIT).queue();
			QueueEmbed qe = new QueueEmbed(PixelBeat.INSTANCE.playerManager.getController(channel.getGuild().getIdLong()).getQueue());
			PixelBeat.INSTANCE.entityManager.getController(channel.getGuild().getIdLong()).addQueueEmbed(queuemessage.getIdLong(), qe);
			queuemessage.editMessage(loadQueueEmbed(channel.getGuild(),qe).build()).queue();
			queuemessage.editMessage(".").queue();
		});
		
	}
	
	public static EmbedBuilder loadQueueEmbed(Guild guild, QueueEmbed qe) {
		MusicController controller = PixelBeat.INSTANCE.playerManager.getController(guild.getIdLong());
		EmbedBuilder builder = new EmbedBuilder();
		Queue queue = controller.getQueue();
		builder.setTitle("**Queue for "+guild.getName()+"**");
		
		String list = null;
		if(controller.getPlayer().getPlayingTrack() != null) {
			list = mf.format(guild.getIdLong(), "music.track.currently-playing")+"\n"
				+ " ["+ controller.getPlayer().getPlayingTrack().getInfo().title+"]("+controller.getPlayer().getPlayingTrack().getInfo().uri+") | "+mf.format(guild.getIdLong(), "music.user.who-requested")+queue.currentplaying.getWhoQueued().getUser().getAsTag()+" \n"
				+ " \n";
			int size = 1;
			for(QueuedTrack qt : queue.getQueuelist()) {
				if(size <= qe.getPage()*10 && size >= (qe.getPage()*10)-9) {
					AudioTrack at = qt.getTrack();
					if((qe.getPage()*10)-9 == 1) {
						list = list + mf.format(guild.getIdLong(), "music.queue.in-queue")+"\n";
					}
					list = list + "``"+size+".`` ``["+Utils.getTimeFormat(at.getInfo().length)+"]`` **["+ at.getInfo().title+"]("+at.getInfo().uri+")** - "+qt.getWhoQueued().getAsMention()+"\n";
				}
				size++;
			}
			builder.setFooter(mf.format(guild.getIdLong(), "music.queue.page", qe.getPage()+"/"+(((size-1) / 10) > 0 ? ((size-1) / 10) : 1)));
			
		}else {
			list = mf.format(guild.getIdLong(), "music.info.currently-playing-null");
		}
		builder.setDescription(list);
		return builder;
	}
}
