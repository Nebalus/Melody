package de.melody.commands.server.info;

import de.melody.Melody;
import de.melody.commands.types.SlashCommand;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class GithubCommand implements SlashCommand{

	@Override
	public void performCommand(Member m, TextChannel channel, Message message, Guild guild) {
		channel.sendMessage(Melody.INSTANCE.getMessageFormatter().format(channel.getGuild().getIdLong(), "feedback.info.github", m.getAsMention(), "https://github.com/Nebalus/Melody/")).queue();
	}

	@Override
	public CommandData getCommandData() {
		// TODO Auto-generated method stub
		return null;
	}
}
