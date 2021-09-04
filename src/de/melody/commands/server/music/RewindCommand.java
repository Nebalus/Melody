package de.melody.commands.server.music;

import java.util.List;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import de.melody.CommandManager.CommandType;
import de.melody.commands.types.ServerCommand;
import de.melody.core.Constants;
import de.melody.core.Melody;
import de.melody.music.MusicController;
import de.melody.speechpackets.MessageFormatter;
import de.melody.utils.Emoji;
import de.melody.utils.Utils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;


public class RewindCommand implements ServerCommand{
	
	private Melody melody = Melody.INSTANCE;
	private MessageFormatter mf = melody.getMessageFormatter();
	

	@SuppressWarnings("deprecation")
	@Override
	public void performCommand(Member m, TextChannel channel, Message message, Guild guild) {
		GuildVoiceState state;
		if((state = guild.getSelfMember().getVoiceState()) != null && state.getChannel() != null) {
			String[] args = message.getContentDisplay().split(" ");
			MusicController controller = melody.playerManager.getController(guild.getIdLong());
			if(controller.isPlayingTrack()) {
				AudioPlayer player = controller.getPlayer();
				Long rewindmillis;
				if(args.length <= 1) {
					rewindmillis = 10000l;
					AudioTrack track = player.getPlayingTrack();
					track.setPosition(player.getPlayingTrack().getPosition()-rewindmillis);
				}else {
					String subTime = "";
					for(int i = 1; i < args.length; i++) {
						subTime = subTime +" "+args[i];
					}
					AudioTrack track = player.getPlayingTrack();
					rewindmillis = Utils.decodeTimeMillisFromString(subTime);
					track.setPosition(player.getPlayingTrack().getPosition()-rewindmillis);
				}
				EmbedBuilder builder = new EmbedBuilder();
				builder.setColor(Constants.EMBEDCOLOR);
				builder.setDescription(Emoji.REWIND+" "+mf.format(guild, "command.rewind.set",Utils.decodeStringFromTimeMillis(rewindmillis,false)));
				channel.sendMessage(builder.build()).queue();
			}else 
				Utils.sendErrorEmbled(message, mf.format(guild, "feedback.music.currently-playing-null"),m);
		}else 
			Utils.sendErrorEmbled(message, mf.format(guild, "feedback.music.bot-not-in-vc"), m);
	}

	@Override
	public List<String> getCommandPrefix() {
		return List.of("rewind","rw","r");
	}
	@Override
	public CommandType getCommandType() {
		return CommandType.MUSIC_COMMAND;
	}
	@Override
	public boolean isSlashCommandCompatible() {
		return false;
	}
	@Override
	public String getCommandDescription() {
		return null;
	}

	@Override
	public void performSlashCommand(Member member, MessageChannel channel, Guild guild, SlashCommandEvent event) {
		
	}
}
