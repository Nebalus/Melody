package de.melody.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import de.melody.core.Melody;
import de.melody.entities.GuildEntity;
import de.melody.speechpackets.MessageFormatter;
import de.melody.utils.Utils.Emoji;
import de.melody.utils.commandbuilder.CommandPermissions;
import de.melody.utils.commandbuilder.CommandType;
import de.melody.utils.commandbuilder.ServerCommand;
import de.melody.utils.messenger.Messenger;
import de.melody.utils.messenger.Messenger.ErrorMessageBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;


public class PauseCommand implements ServerCommand{

	private Melody melody = Melody.INSTANCE;
	private MessageFormatter mf = melody.getMessageFormatter();
	
	@Override
	public void performCommand(Member member, TextChannel channel, Message message, Guild guild, GuildEntity guildentity) {
		melody.getEntityManager().getGuildEntity(guild).setMusicChannelId(channel.getIdLong());
		GuildVoiceState state;
		if((state = guild.getSelfMember().getVoiceState()) != null && state.getChannel() != null) {
			AudioPlayer player = melody.playerManager.getController(guild.getIdLong()).getPlayer();
			if(!player.isPaused()) {
				Messenger.sendMessageEmbed(channel, Emoji.PAUSE+" "+mf.format(guild, "music.track.pause"));	
				player.setPaused(true);
			}
		}else {
			Messenger.sendErrorMessage(channel, new ErrorMessageBuilder().setMessageFormat(guild, "music.bot-not-in-vc"));
		}
	}
	
	@Override
	public void performSlashCommand(Member member, MessageChannel channel, Guild guild, GuildEntity guildentity, SlashCommandEvent event) {
		melody.getEntityManager().getGuildEntity(guild).setMusicChannelId(channel.getIdLong());
		GuildVoiceState state;
		if((state = guild.getSelfMember().getVoiceState()) != null && state.getChannel() != null) {
			AudioPlayer player = melody.playerManager.getController(guild.getIdLong()).getPlayer();
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
	public CommandPermissions getMainPermmision() {
		return CommandPermissions.DJ;
	}
}
