package de.melody.commands.server.music;

import java.util.List;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import de.melody.Melody;
import de.melody.commands.types.ServerCommand;
import de.melody.music.MusicController;
import de.melody.speechpackets.MessageFormatter;
import de.melody.utils.Utils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class SeekCommand implements ServerCommand{
	
	private Melody melody = Melody.INSTANCE;
	private MessageFormatter mf = melody.getMessageFormatter();
	
	@Override
	public void performCommand(Member m, TextChannel channel, Message message, Guild guild) {
		GuildVoiceState state;
		if((state = guild.getSelfMember().getVoiceState()) != null && state.getChannel() != null) {
			String[] args = message.getContentDisplay().split(" ");
			MusicController controller = melody.playerManager.getController(guild.getIdLong());
			if(controller.isPlayingTrack()) {
				AudioPlayer player = controller.getPlayer();
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
				Utils.sendErrorEmbled(channel, mf.format(guild, "feedback.music.currently-playing-null"),m);
		}else 
			Utils.sendErrorEmbled(channel, mf.format(guild, "feedback.music.bot-not-in-vc"), m);
	}

	@Override
	public List<String> getCommandPrefix() {
		return List.of("seek");
	}
}
