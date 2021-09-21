package de.melody.commands.music;

import java.util.List;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import de.nebalus.botbuilder.command.CommandInfo;
import de.nebalus.botbuilder.command.CommandType;
import de.melody.core.Melody;
import de.melody.entities.GuildEntity;
import de.melody.music.MusicController;
import de.melody.speechpackets.MessageFormatter;
import de.melody.utils.Emoji;
import de.melody.utils.Utils;
import de.nebalus.botbuilder.command.ServerCommand;
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
	public void performCommand(Member m, TextChannel channel, Message message, Guild guild) {
		GuildEntity ge = melody.getEntityManager().getGuildEntity(guild);
		if(ge.isMemberDJ(m)) {
			GuildVoiceState state;
			VoiceChannel vc;
			if((state = m.getVoiceState()) != null && (vc = state.getChannel()) != null) {
				guild.getAudioManager().openAudioConnection(vc);
				MusicController controller = melody.playerManager.getController(guild.getIdLong());
				AudioPlayer player = controller.getPlayer();
				message.addReaction(Emoji.OK_HAND).queue();
				if(player.getPlayingTrack() == null) {
					controller.setAfkTime(600);
				}
			}else 
				Utils.sendErrorEmbled(message, mf.format(guild, "feedback.music.user-not-in-vc"), m);
		}
	}

	@Override
	public List<String> getCommandPrefix() {
		return List.of("join","j");
	}
	@Override
	public CommandType getCommandType() {
		return CommandType.CHAT_COMMAND;
	}

	@Override
	public CommandInfo getCommandInfo() {
		return CommandInfo.INFO_COMMAND;
	}
	@Override
	public String getCommandDescription() {
		return null;
	}

	@Override
	public void performSlashCommand(Member member, MessageChannel channel, Guild guild, SlashCommandEvent event) {
		
	}

	@Override
	public List<OptionData> getCommandOptions() {
		// TODO Auto-generated method stub
		return null;
	}
}
