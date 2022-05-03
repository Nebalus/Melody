package de.melody.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import de.melody.core.Melody;
import de.melody.entities.GuildEntity;
import de.melody.speechpackets.MessageFormatter;
import de.melody.tools.Utils.Emoji;
import de.melody.tools.commandbuilder.CommandPermission;
import de.melody.tools.commandbuilder.CommandType;
import de.melody.tools.commandbuilder.Command;
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

public class PauseCommand implements Command{

	private Melody melody = Melody.INSTANCE;
	private MessageFormatter mf = melody._messageformatter;
	
	@Override
	public void performCommand(Member member, TextChannel channel, Message message, Guild guild, GuildEntity guildentity) {
		GuildVoiceState state;
		if((state = guild.getSelfMember().getVoiceState()) != null && state.getChannel() != null) {
			AudioPlayer player = melody._playerManager.getController(guild.getIdLong()).getPlayer();
			melody._playerManager.setAnounceChannelID(guild.getIdLong(), channel.getIdLong());
			if(!player.isPaused()) {
				Messenger.sendMessageEmbed(channel, Emoji.PAUSE+" "+mf.format(guild, "music.track.pause")).queue();	
				player.setPaused(true);
			}
		}else {
			Messenger.sendErrorMessage(channel, new ErrorMessageBuilder().setMessageFormat(guild, "music.bot-not-in-vc"));
		}
	}
	
	@Override
	public void performSlashCommand(Member member, MessageChannel channel, Guild guild, GuildEntity guildentity, SlashCommandEvent event) {
		GuildVoiceState state;
		if((state = guild.getSelfMember().getVoiceState()) != null && state.getChannel() != null) {
			AudioPlayer player = melody._playerManager.getController(guild.getIdLong()).getPlayer();
			melody._playerManager.setAnounceChannelID(guild.getIdLong(), channel.getIdLong());
			if(!player.isPaused()) {
				event.replyEmbeds(Messenger.getMessageEmbed(Emoji.PAUSE+" "+mf.format(guild, "music.track.pause"))).queue();
				player.setPaused(true);
			}
		}else {
			Messenger.sendErrorSlashMessage(event, new ErrorMessageBuilder().setMessageFormat(guild, "music.bot-not-in-vc"));
		}
	}

	@Override
	public String[] getCommandPrefix() {
		return new String[] {"pause"};
	}
	@Override
	public CommandType getCommandType() {
		return CommandType.BOTH;
	}

	@Override
	public String getCommandDescription() {
		return "Pauses the currently playing track";
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
