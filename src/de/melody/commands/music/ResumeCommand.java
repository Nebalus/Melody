package de.melody.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import de.melody.core.Melody;
import de.melody.entities.GuildEntity;
import de.melody.speechpackets.MessageFormatter;
import de.melody.utils.Utils.Emoji;
import de.melody.utils.commandbuilder.CommandPermission;
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

public class ResumeCommand implements ServerCommand{

	private Melody melody = Melody.INSTANCE;
	private MessageFormatter mf = melody.getMessageFormatter();
	
	@Override
	public void performCommand(Member member, TextChannel channel, Message message, Guild guild, GuildEntity guildentity) {
		GuildVoiceState state;
		if((state = guild.getSelfMember().getVoiceState()) != null && state.getChannel() != null) {
			AudioPlayer player = melody.playerManager.getController(guild.getIdLong()).getPlayer();
			melody.playerManager.setAnounceChannelID(guild.getIdLong(), channel.getIdLong());
			if(player.isPaused()) {
				Messenger.sendMessageEmbed(channel, Emoji.RESUME+" "+mf.format(guild, "music.track.resume")).queue();		
				player.setPaused(false);
			}
		}else {
			Messenger.sendErrorMessage(channel, new ErrorMessageBuilder().setMessageFormat(guild, "music.bot-not-in-vc"));
		}
	}

	@Override
	public String[] getCommandPrefix() {
		return new String[] {"resume","unpause"};
	}
	
	@Override
	public CommandType getCommandType() {
		return CommandType.CHAT;
	}
	
	@Override
	public String getCommandDescription() {
		return null;
	}

	@Override
	public void performSlashCommand(Member member, MessageChannel channel, Guild guild, GuildEntity guildentity, SlashCommandEvent event) {
		
	}

	@Override
	public OptionData[] getCommandOptions() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public CommandPermission getMainPermmision() {
		return CommandPermission.DJ;
	}
}
