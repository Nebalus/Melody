package de.melody.commands.server.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import de.melody.Melody;
import de.melody.commands.types.SlashCommand;
import de.melody.music.MusicController;
import de.melody.music.MusicUtil;
import de.melody.speechpackets.MessageFormatter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.managers.AudioManager;

public class JoinCommand implements SlashCommand{
	
	private Melody melody = Melody.INSTANCE;
	private MessageFormatter mf = melody.getMessageFormatter();
	
	@Override
	public void performSlashCommand(SlashCommandEvent slash) {
		Guild guild = slash.getGuild();
		melody.entityManager.getGuildEntity(guild.getIdLong()).setChannelId(slash.getChannel().getIdLong());
		GuildVoiceState state;
		VoiceChannel vc;
		if((state = slash.getMember().getVoiceState()) != null && (vc = state.getChannel()) != null) {
			AudioManager manager = guild.getAudioManager();
			manager.openAudioConnection(vc);
			MusicController controller = melody.playerManager.getController(guild.getIdLong());
			AudioPlayer player = controller.getPlayer();
			if(player.getPlayingTrack() == null) {
				controller.setAfkTime(600);
			}
		}else {
			MusicUtil.sendEmbledErrorSlash(slash,mf.format(guild.getIdLong(), "feedback.music.user-not-in-vc"),true);		
		}
	}

	@Override
	public CommandData getCommandData() {
		return new CommandData("join", "Summons the bot to your voice channel");
	}
}
