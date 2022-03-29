package de.melody.commands;

import java.util.Random;

import de.melody.tools.ConsoleLogger;
import de.melody.tools.cmdbuilder.CommandPermission;
import de.melody.tools.cmdbuilder.CommandType;
import de.melody.tools.cmdbuilder.ServerCommand;
import de.melody.tools.cmdbuilder.SubCommand;
import de.melody.tools.entitymanager.entitys.GuildEntity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class TestCommand extends ServerCommand{

	public TestCommand() {
		super();
		setType(CommandType.CHAT);
		setMainPermission(CommandPermission.EVERYONE);
		setPrefixes("test");
		addSubCommand(new SubCommand("hallo", CommandPermission.EVERYONE, CommandType.BOTH) {
			@Override
			public void execute(Member member, TextChannel channel, Message message, Guild guild, GuildEntity guildentity) {
				channel.sendMessage("Hey...").queue();
			}
		});
	}
	
	@Override
	public void performMainCMD(Member member, TextChannel channel, Message message, Guild guild, GuildEntity guildentity) {
		ConsoleLogger.info(message);
		Random ran = new Random();
		
		final int test = ran.nextInt(99)+1;
		
		guildentity.setVolume(test);
		channel.sendMessage("HEHEHE... "+test).queue();
	}
	
}
