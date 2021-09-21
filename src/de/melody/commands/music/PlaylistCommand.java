package de.melody.commands.music;

import java.util.List;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

import de.nebalus.botbuilder.command.CommandInfo;
import de.nebalus.botbuilder.command.CommandType;
import de.melody.core.Melody;
import de.melody.entities.GuildEntity;
import de.melody.music.AudioLoadResult;
import de.melody.music.MusicController;
import de.melody.music.MusicUtil;
import de.melody.speechpackets.MessageFormatter;
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
import net.dv8tion.jda.api.managers.AudioManager;

public class PlaylistCommand implements ServerCommand{
	
	private Melody melody = Melody.INSTANCE;
	private MessageFormatter mf = melody.getMessageFormatter();
	
	@Override
	public void performCommand(Member m, TextChannel channel, Message message, Guild guild) {
		String[] args = message.getContentDisplay().split(" ");
		GuildEntity ge = melody.getEntityManager().getGuildEntity(guild);
		ge.setMusicChannelId(channel.getIdLong());
		if(args.length > 1) {
			GuildVoiceState state;
			VoiceChannel vc;
			if((state = m.getVoiceState()) != null && (vc = state.getChannel()) != null) {
				MusicController controller = melody.playerManager.getController(guild.getIdLong());
				AudioPlayerManager apm = melody.audioPlayerManager;
				AudioManager manager = guild.getAudioManager();
				StringBuilder strBuilder = new StringBuilder();
				for(int i = 1; i < args.length; i++) strBuilder.append(args[i] + " ");
				String url = strBuilder.toString().trim();		
				if(MusicUtil.isUrlVerified(url)) {
					manager.openAudioConnection(vc);
					final String uri = url;
					apm.loadItem(uri, new AudioLoadResult(controller, uri, m, true));
				}else 
					MusicUtil.sendEmbledError(guild, mf.format(guild, "feedback.music.non-whitelisted-domain",MusicUtil.getDomain(url)));				
			}else
				MusicUtil.sendEmbledError(guild, mf.format(guild, "feedback.music.user-not-in-vc"));
		}else 
			MusicUtil.sendEmbledError(guild, mf.format(guild, "feedback.info.command-usage",ge.getPrefix()+"playlist <url>"));
	}

	@Override
	public List<String> getCommandPrefix() {
		return List.of("playlist","pl");
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
