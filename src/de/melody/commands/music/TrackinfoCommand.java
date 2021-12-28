package de.melody.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import de.melody.core.Melody;
import de.melody.entities.GuildEntity;
import de.melody.music.MusicController;
import de.melody.tools.Images;
import de.melody.tools.commandbuilder.CommandPermission;
import de.melody.tools.commandbuilder.CommandType;
import de.melody.tools.commandbuilder.ServerCommand;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class TrackinfoCommand implements ServerCommand{
	
	@Override
	public void performCommand(Member member, TextChannel channel, Message message, Guild guild, GuildEntity guildentity) {
		MusicController controller = Melody.INSTANCE.playerManager.getController(guild.getIdLong());
		
		channel.sendMessage("Loading...").queue((chatmessage) ->{
			if(controller.isPlayingTrack()) {
				AudioTrack at = controller.getPlayer().getPlayingTrack();
				channel.sendFile(Images.tracktopng(at.getInfo().title,at.getPosition(),at.getDuration(),at.getInfo().author,guild,controller.getQueue().currentlyPlaying().getWhoQueued()), "trackinfo.png").queue();
				chatmessage.delete().queue();
			}else{
				channel.sendFile(Images.tracktopng(null,0,0,null,guild,null), "trackinfo.png").queue();
				chatmessage.delete().queue();
			}			
		});		
	}

	@Override
	public void performSlashCommand(Member member, MessageChannel channel, Guild guild, GuildEntity guildentity, SlashCommandEvent event) {
		MusicController controller = Melody.INSTANCE.playerManager.getController(guild.getIdLong());
		event.reply("Loading...").queue((slashmessage)->{
			if(controller.isPlayingTrack()) {
				AudioTrack at = controller.getPlayer().getPlayingTrack();
				slashmessage.editOriginal(Images.tracktopng(at.getInfo().title,at.getPosition(),at.getDuration(),at.getInfo().author,guild,controller.getQueue().currentlyPlaying().getWhoQueued()), "trackinfo.png").queue();
				slashmessage.editOriginal(" ").queue();
			}else {
				slashmessage.editOriginal(Images.tracktopng(null,0,0,null,guild,null), "trackinfo.png").queue();
				slashmessage.editOriginal(" ").queue();
			}
		});
	}
	
	@Override
	public String[] getCommandPrefix() {
		return new String[] {"trackinfo","ti","np","nowplaying"};
	}
	@Override
	public CommandType getCommandType() {
		return CommandType.BOTH;
	}

	@Override
	public String getCommandDescription() {
		return "Shows details of the song currently being played";
	}

	@Override
	public OptionData[] getCommandOptions() {
		return null;
	}
	@Override
	public CommandPermission getMainPermmision() {
		return CommandPermission.EVERYONE;
	}
}
