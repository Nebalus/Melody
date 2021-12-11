package de.melody.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import de.melody.core.Melody;
import de.melody.entities.GuildEntity;
import de.melody.music.MusicController;
import de.melody.speechpackets.MessageFormatter;
import de.melody.utils.Utils.Emoji;
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


public class LoopCommand implements ServerCommand{

	private Melody melody = Melody.INSTANCE;
	private MessageFormatter mf = melody.getMessageFormatter();
	
	@Override
	public void performCommand(Member member, TextChannel channel, Message message, Guild guild, GuildEntity guildentity) {
		GuildVoiceState state;
		if((state = guild.getSelfMember().getVoiceState()) != null && state.getChannel() != null) {
			MusicController controller = melody.playerManager.getController(guild.getIdLong());
			AudioPlayer player = controller.getPlayer();
			if(player.getPlayingTrack() != null) {
				if(controller.isLoop()) {
					controller.setLoop(false);
					Messenger.sendMessageEmbed(channel,Emoji.SINGLE_LOOP+mf.format(guild, "music.info.loop-disabled")).queue();
				}else {
					controller.setLoop(true);
					Messenger.sendMessageEmbed(channel,Emoji.SINGLE_LOOP+mf.format(guild, "music.info.loop-enabled")).queue();
				}	
			}else {
				Messenger.sendErrorMessage(channel, new ErrorMessageBuilder().setMessageFormat(guild, "music.currently-playing-null"));
			}
		}else {
			Messenger.sendErrorMessage(channel, new ErrorMessageBuilder().setMessageFormat(guild, "music.bot-not-in-vc"));
		}
	}

	@Override
	public String[] getCommandPrefix() {
		return new String[] {"loop"};
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
		return null;
	}
}