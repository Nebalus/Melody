package de.melody.commands.server.music;

import de.melody.Melody;
import de.melody.commands.types.ServerCommand;
import de.melody.music.MusicUtil;
import de.melody.speechpackets.MessageFormatter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;

public class ResumeCommand implements ServerCommand{

	private Melody melody = Melody.INSTANCE;
	private MessageFormatter mf = melody.getMessageFormatter();
	
	@SuppressWarnings("unused")
	@Override
	public void performCommand(Member m, TextChannel channel, Message message, Guild guild) {
		melody.entityManager.getGuildEntity(guild.getIdLong()).setChannelId(channel.getIdLong());
		GuildVoiceState state;
		VoiceChannel vc;
		if((state = guild.getSelfMember().getVoiceState()) != null && (vc = state.getChannel()) != null) {
			melody.playerManager.getController(guild.getIdLong()).getPlayer().setPaused(false);
		}else
			MusicUtil.sendEmbledError(guild.getIdLong(), mf.format(guild.getIdLong(), "feedback.music.bot-not-in-vc"));
	}
}
