package de.melody.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import de.melody.core.Melody;
import de.melody.entities.GuildEntity;
import de.melody.music.MusicController;
import de.melody.music.Queue;
import de.melody.tools.Utils.Emoji;
import de.melody.tools.commandbuilder.CommandPermission;
import de.melody.tools.commandbuilder.CommandType;
import de.melody.tools.commandbuilder.ServerCommand;
import de.melody.tools.messenger.Messenger;
import de.melody.tools.messenger.Messenger.ErrorMessageBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class StopCommand implements ServerCommand{

	private Melody melody = Melody.INSTANCE;
	
	@Override
	public void performCommand(Member member, TextChannel channel, Message message, Guild guild, GuildEntity guildentity) {
		GuildVoiceState state;
		if((state = member.getVoiceState()) != null && state.getChannel() != null) {
			MusicController controller = melody._playerManager.getController(guild.getIdLong());
			melody._playerManager.setAnounceChannelID(guild.getIdLong(), channel.getIdLong());
			AudioPlayer player = controller.getPlayer();
			if(player.getPlayingTrack() != null) {
				Queue queue = controller.getQueue();
				player.stopTrack();
				queue.clear();
				melody._playerManager.getController(guild.getIdLong()).setAfkTime(600);
				message.addReaction(Emoji.OK_HAND).queue();
			}else {
				Messenger.sendErrorMessage(channel, new ErrorMessageBuilder().setMessageFormat(guild, "music.currently-playing-null"));
			}
		}else {
			Messenger.sendErrorMessage(channel, new ErrorMessageBuilder().setMessageFormat(guild, "music.bot-not-in-vc"));
		}
	}

	@Override
	public void performSlashCommand(Member member, MessageChannel channel, Guild guild, GuildEntity guildentity, SlashCommandEvent event) {
		GuildVoiceState state;
		if((state = member.getVoiceState()) != null && state.getChannel() != null) {
			MusicController controller = melody._playerManager.getController(guild.getIdLong());
			melody._playerManager.setAnounceChannelID(guild.getIdLong(), channel.getIdLong());
			AudioPlayer player = controller.getPlayer();
			if(player.getPlayingTrack() != null) {
				Queue queue = controller.getQueue();
				player.stopTrack();
				queue.clear();
				melody._playerManager.getController(guild.getIdLong()).setAfkTime(600);
			}else {
				Messenger.sendErrorSlashMessage(event, new ErrorMessageBuilder().setMessageFormat(guild, "music.currently-playing-null"));
			}
		}else {
			Messenger.sendErrorSlashMessage(event, new ErrorMessageBuilder().setMessageFormat(guild, "music.bot-not-in-vc"));
		}
	}
	
	@Override
	public String[] getCommandPrefix() {
		return new String[] {"stop"};
	}
	
	@Override
	public CommandType getCommandType() {
		return CommandType.BOTH;
	}

	@Override
	public String getCommandDescription() {
		return "Stops the player and clears the queue";
	}

	@Override
	public OptionData[] getCommandOptions() {
		return null;
	}
	@Override
	public CommandPermission getMainPermmision() {
		return CommandPermission.DJ;
	}
}
