package de.pixelbeat.commands.info;

import de.pixelbeat.PixelBeat;
import de.pixelbeat.commands.types.ServerCommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class GithubCommand implements ServerCommand{

	
	
	public void performCommand(Member m, TextChannel channel, Message message) {
		channel.sendMessage(PixelBeat.INSTANCE.getMessageFormatter().format(channel.getGuild().getIdLong(), "feedback.info.github", m.getAsMention(), "https://github.com/Nebalus/PixelBeat/")).queue();
	}
}