package de.melody.commands.server.music;

import java.util.List;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import de.melody.CommandManager.CommandType;
import de.melody.commands.types.ServerCommand;
import de.melody.core.Melody;
import de.melody.music.MusicController;
import de.melody.utils.Images;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;


public class TrackinfoCommand implements ServerCommand{
	
	@Override
	public void performCommand(Member m, TextChannel channel, Message message, Guild guild) {
		try {
			MusicController controller = Melody.INSTANCE.playerManager.getController(guild.getIdLong());
			AudioTrack audiotrack = controller.getPlayer().getPlayingTrack();
			channel.sendFile(Images.tracktopng(audiotrack.getInfo().title,audiotrack.getPosition(),audiotrack.getDuration(),audiotrack.getInfo().author,guild,controller.getQueue().currentlyPlaying().getWhoQueued()), "trackinfo.png").queue();	
		}catch (NullPointerException e) {
			channel.sendFile(Images.tracktopng(null,0,0,null,guild,null), "trackinfo.png").queue();	
		}
	}

	@Override
	public List<String> getCommandPrefix() {
		return List.of("trackinfo","ti");
	}
	@Override
	public CommandType getCommandType() {
		return CommandType.MUSIC_COMMAND;
	}
	@Override
	public boolean isSlashCommandCompatible() {
		return false;
	}
	@Override
	public String getCommandDescription() {
		return null;
	}

	@Override
	public void performSlashCommand(Member member, MessageChannel channel, Guild guild, SlashCommandEvent event) {
		
	}
}
