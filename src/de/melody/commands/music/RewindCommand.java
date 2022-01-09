package de.melody.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import de.melody.core.Melody;
import de.melody.entities.GuildEntity;
import de.melody.music.MusicController;
import de.melody.speechpackets.MessageFormatter;
import de.melody.tools.Utils.Emoji;
import de.melody.tools.commandbuilder.CommandPermission;
import de.melody.tools.commandbuilder.CommandType;
import de.melody.tools.commandbuilder.Command;
import de.melody.tools.helper.MathHelper;
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

public class RewindCommand implements Command{
	
	private Melody melody = Melody.INSTANCE;
	private MessageFormatter mf = melody._messageformatter;
	private final String usagemsg = "<amount sec|min|hour>";
	@Override
	public void performCommand(Member member, TextChannel channel, Message message, Guild guild, GuildEntity guildentity) {
		GuildVoiceState state;
		if((state = guild.getSelfMember().getVoiceState()) != null && state.getChannel() != null) {
			String[] args = message.getContentDisplay().split(" ");
			MusicController controller = melody._playerManager.getController(guild.getIdLong());
			if(controller.isPlayingTrack()) {
				if(args.length <= 1) {
					Messenger.sendErrorMessage(channel, new ErrorMessageBuilder().setMessageFormat(guild, "info.command-usage", getCommandPrefix()[0]+" "+usagemsg));	
				}else {
					Long rewindmillis;
					String subTime = "";
					for(int i = 1; i < args.length; i++) {
						subTime = subTime +" "+args[i];
					}
					if((rewindmillis = MathHelper.decodeTimeMillisFromString(subTime)) > 0) {
						AudioPlayer player = controller.getPlayer();
						AudioTrack track = player.getPlayingTrack();					
						Messenger.sendMessageEmbed(channel, Emoji.REWIND+" "+mf.format(guild, "command.rewind.set",MathHelper.decodeStringFromTimeMillis(MathHelper.countupUntil(rewindmillis, player.getPlayingTrack().getPosition())))).queue();
						track.setPosition(MathHelper.countdownUntil(player.getPlayingTrack().getPosition()-rewindmillis, 0l));	
					}else {
						Messenger.sendErrorMessage(channel, new ErrorMessageBuilder().setMessageFormat(guild, "info.command-usage", getCommandPrefix()[0]+" "+usagemsg));	
					}
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
		return new String[] {"rewind","rw","r"};
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
	
	@Override
	public CommandPermission getMainPermmision() {
		return CommandPermission.DJ;
	}
}
