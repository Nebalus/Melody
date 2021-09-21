package de.melody.commands.music;

import java.util.List;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import de.nebalus.botbuilder.command.CommandInfo;
import de.nebalus.botbuilder.command.CommandType;
import de.melody.core.Melody;
import de.melody.music.MusicController;
import de.melody.utils.Images;
import de.nebalus.botbuilder.command.ServerCommand;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;


public class TrackinfoCommand implements ServerCommand{
	
	@Override
	public void performCommand(Member m, TextChannel channel, Message message, Guild guild) {
		MusicController controller = Melody.INSTANCE.playerManager.getController(guild.getIdLong());
		if(controller.isPlayingTrack()) {
			AudioTrack at = controller.getPlayer().getPlayingTrack();
			channel.sendFile(Images.tracktopng(at.getInfo().title,at.getPosition(),at.getDuration(),at.getInfo().author,guild,controller.getQueue().currentlyPlaying().getWhoQueued()), "trackinfo.png").queue();
		}else{
			channel.sendFile(Images.tracktopng(null,0,0,null,guild,null), "trackinfo.png").queue();
		}
	}

	@Override
	public void performSlashCommand(Member member, MessageChannel channel, Guild guild, SlashCommandEvent event) {
		MusicController controller = Melody.INSTANCE.playerManager.getController(guild.getIdLong());
		if(controller.isPlayingTrack()) {
			AudioTrack at = controller.getPlayer().getPlayingTrack();
			event.reply("Loading...").queue((message)->{
				message.editOriginal(Images.tracktopng(at.getInfo().title,at.getPosition(),at.getDuration(),at.getInfo().author,guild,controller.getQueue().currentlyPlaying().getWhoQueued()), "trackinfo.png").queue();
				message.editOriginal(" ").queue();
			});
		}else {
			event.reply("Loading...").queue((message)->{
				message.editOriginal(Images.tracktopng(null,0,0,null,guild,null), "trackinfo.png").queue();
				message.editOriginal(" ").queue();
			});
		}
	}
	
	@Override
	public List<String> getCommandPrefix() {
		return List.of("trackinfo","ti","np","nowplaying");
	}
	@Override
	public CommandType getCommandType() {
		return CommandType.BOTH;
	}

	@Override
	public CommandInfo getCommandInfo() {
		return CommandInfo.INFO_COMMAND;
	}
	@Override
	public String getCommandDescription() {
		return "Shows the current playing song";
	}

	@Override
	public List<OptionData> getCommandOptions() {
		return null;
	}
}
