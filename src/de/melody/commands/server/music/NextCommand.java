package de.melody.commands.server.music;

import java.util.List;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import de.melody.Melody;
import de.melody.commands.types.ServerCommand;
import de.melody.music.MusicController;
import de.melody.music.MusicUtil;
import de.melody.music.Queue;
import de.melody.speechpackets.MessageFormatter;
import de.melody.utils.Emoji;
import de.melody.utils.Utils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class NextCommand implements ServerCommand{

	private Melody melody = Melody.INSTANCE;
	private MessageFormatter mf = melody.getMessageFormatter();
	
	@Override
	public void performCommand(Member m, TextChannel channel, Message message, Guild guild) {
		melody.entityManager.getGuildEntity(guild).setChannelId(channel.getIdLong());
		
		GuildVoiceState state;
		EmbedBuilder builder = new EmbedBuilder();
		if((state = guild.getSelfMember().getVoiceState()) != null && state.getChannel() != null) {
			MusicController controller = melody.playerManager.getController(guild.getIdLong());
			String[] args = message.getContentDisplay().split(" ");
			AudioPlayer player = controller.getPlayer();
			Queue queue = controller.getQueue();
			if(player.getPlayingTrack() != null) {
				player.stopTrack();
				builder.setDescription(Emoji.NEXT_TITLE+" "+mf.format(guild, "music.track.skip"));
				MusicUtil.sendEmbled(guild, builder);
				try {
					int i = Integer.valueOf(args[1]);
					queue.next(i);
				}catch(NumberFormatException | IndexOutOfBoundsException e) {
					queue.next(1);
				}
			}else 
				Utils.sendErrorEmbled(channel, mf.format(guild, "feedback.music.currently-playing-null"),m);
		}else
			Utils.sendErrorEmbled(channel, mf.format(guild, "feedback.music.bot-not-in-vc"), m);
	}

	@Override
	public List<String> getCommandPrefix() {
		return List.of("next","n","skip","s");
	}
}
