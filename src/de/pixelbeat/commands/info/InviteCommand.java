package de.pixelbeat.commands.info;

import de.pixelbeat.PixelBeat;
import de.pixelbeat.commands.types.ServerCommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class InviteCommand implements ServerCommand{

	@Override
	public void performCommand(Member m, TextChannel channel, Message message) {
		channel.sendMessage(PixelBeat.INSTANCE.getMessageFormatter().format(channel.getGuild().getIdLong(), "feedback.info.invite", m.getAsMention(), "https://discord.com/oauth2/authorize?client_id=801856678063898644&scope=bot&permissions=8")).queue();
	}
}
