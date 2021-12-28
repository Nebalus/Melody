package de.melody.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import de.melody.core.Constants;
import de.melody.core.Melody;
import de.melody.entities.GuildEntity;
import de.melody.music.MusicController;
import de.melody.speechpackets.MessageFormatter;
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
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class JoinCommand implements ServerCommand{
	
	private Melody melody = Melody.INSTANCE;
	private MessageFormatter mf = melody.getMessageFormatter();
	
	@Override
	public void performCommand(Member member, TextChannel channel, Message message, Guild guild, GuildEntity guildentity) {
		GuildVoiceState state;
		VoiceChannel vc;
		if((state = member.getVoiceState()) != null && (vc = state.getChannel()) != null) {
			guild.getAudioManager().openAudioConnection(vc);
			MusicController controller = melody.playerManager.getController(guild.getIdLong());
			AudioPlayer player = controller.getPlayer();
			Messenger.sendMessage(channel, mf.format(guild, "music.info.bot-join-vc", vc.getName())).queue();
			if(player.getPlayingTrack() == null) {
				controller.setAfkTime(600);
			}
		}else {
			Messenger.sendErrorMessage(channel, new ErrorMessageBuilder().setMessageFormat(guild, "music.user-not-in-vc"));	
		}	
	}

	@Override
	public void performSlashCommand(Member member, MessageChannel channel, Guild guild, GuildEntity guildentity, SlashCommandEvent event) {
		GuildVoiceState state;
		VoiceChannel vc;
		if((state = member.getVoiceState()) != null && (vc = state.getChannel()) != null) {
			guild.getAudioManager().openAudioConnection(vc);
			MusicController controller = melody.playerManager.getController(guild.getIdLong());
			AudioPlayer player = controller.getPlayer();
			event.reply(mf.format(guild, "music.info.bot-join-vc", vc.getName())).queue();
			if(player.getPlayingTrack() == null) {
				controller.setAfkTime(600);
			}
		}else {
			Messenger.sendErrorSlashMessage(event, new ErrorMessageBuilder().setMessageFormat(guild, "music.user-not-in-vc"));	
		}	
	}
	
	@Override
	public String[] getCommandPrefix() {
		return new String[] {"join","j"};
	}
	
	@Override
	public CommandType getCommandType() {
		return CommandType.BOTH;
	}
	
	@Override
	public String getCommandDescription() {
		return "Let "+Constants.BUILDNAME+" join your Voicechannel";
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
