package de.melody.commands.server.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import de.melody.Melody;
import de.melody.commands.types.ServerCommand;
import de.melody.entities.GuildEntity;
import de.melody.music.MusicController;
import de.melody.music.Queue;
import de.melody.speechpackets.MessageFormatter;
import de.melody.utils.Utils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;

public class SeekCommand implements ServerCommand{
	
	private Melody melody = Melody.INSTANCE;
	private MessageFormatter mf = melody.getMessageFormatter();
	
	@SuppressWarnings("unused")
	@Override
	public void performCommand(Member m, TextChannel channel, Message message, Guild guild) {
		GuildVoiceState state;
		VoiceChannel vc;
		if((state = guild.getSelfMember().getVoiceState()) != null && (vc = state.getChannel()) != null) {
			String[] args = message.getContentDisplay().split(" ");
			GuildEntity ge = melody.entityManager.getGuildEntity(guild.getIdLong());
			MusicController controller = melody.playerManager.getController(guild.getIdLong());
			AudioPlayer player = controller.getPlayer();
			Queue queue = controller.getQueue();
			if(queue.isPlayingTrack()) {
				if(args.length <= 1) {
				
				}else {
					String subTime = "";
					for(int i = 1; i < args.length; i++) {
						subTime = subTime +" "+args[i];
					}
					AudioTrack track = player.getPlayingTrack();
					track.setPosition(Utils.decodeTimeMillisFromString(subTime));
				}
			}else 
				Utils.sendErrorEmbled(channel, mf.format(guild.getIdLong(), "feedback.music.currently-playing-null"),m);
		}else 
			Utils.sendErrorEmbled(channel, mf.format(guild.getIdLong(), "feedback.music.bot-not-in-vc"), m);
	}
}
