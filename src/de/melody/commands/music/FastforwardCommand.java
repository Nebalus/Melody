package de.melody.commands.music;

import java.util.List;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import de.melody.core.Melody;
import de.melody.entities.GuildEntity;
import de.melody.music.MusicController;
import de.melody.speechpackets.MessageFormatter;
import de.melody.utils.Utils;
import de.melody.utils.Utils.Emoji;
import de.melody.utils.commandbuilder.CommandInfo;
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


public class FastforwardCommand implements ServerCommand{
	
	private Melody melody = Melody.INSTANCE;
	private MessageFormatter mf = melody.getMessageFormatter();
	
	@SuppressWarnings({ "unused" })
	@Override
	public void performCommand(Member member, TextChannel channel, Message message, Guild guild) {
		GuildVoiceState state;
		if((state = guild.getSelfMember().getVoiceState()) != null && state.getChannel() != null) {
			String[] args = message.getContentDisplay().split(" ");
			GuildEntity ge = melody.getEntityManager().getGuildEntity(guild);
			MusicController controller = melody.playerManager.getController(guild.getIdLong());
			if(controller.isPlayingTrack()) {
				AudioPlayer player = controller.getPlayer();
				Long fastforwardmillis;
				if(args.length <= 1) {
					fastforwardmillis = 10000l;
					AudioTrack track = player.getPlayingTrack();
					track.setPosition(player.getPlayingTrack().getPosition()+fastforwardmillis);
				}else {
					String subTime = "";
					for(int i = 1; i < args.length; i++) {
						subTime = subTime +" "+args[i];
					}
					AudioTrack track = player.getPlayingTrack();
					fastforwardmillis = Utils.decodeTimeMillisFromString(subTime);
					track.setPosition(player.getPlayingTrack().getPosition()+fastforwardmillis);
				}
				Messenger.sendMessageEmbed(channel, Emoji.FAST_FORWARD+" "+mf.format(guild, "command.fastforward.set",Utils.decodeStringFromTimeMillis(fastforwardmillis,false))).queue();
			}else {
				Messenger.sendErrorMessage(channel, new ErrorMessageBuilder().setMessageFormat(guild, "music.currently-playing-null"));
			}
		}else {
			Messenger.sendErrorMessage(channel, new ErrorMessageBuilder().setMessageFormat(guild, "music.bot-not-in-vc"));
		}
	}

	@Override
	public List<String> getCommandPrefix() {
		return List.of("fastforward","fw","f");
	}
	
	@Override
	public CommandType getCommandType() {
		return CommandType.CHAT_COMMAND;
	}

	@Override
	public CommandInfo getCommandInfo() {
		return CommandInfo.DJ_COMMAND;
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
