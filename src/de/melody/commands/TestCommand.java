package de.melody.commands;

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
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class TestCommand extends ServerCommand{

	public TestCommand() {
		super();
		setType(CommandType.CHAT);
		setMainPermission(CommandPermission.EVERYONE);
		setPrefixes("Test");
		addSubCommand(new SubCommand("test", CommandPermission.EVERYONE) {
			@Override
			public void execute(Member member, TextChannel channel, Message message, Guild guild, GuildEntity guildentity) {
				channel.sendMessage("HEHEHE...").queue();
			}
		});
	}
	
	@Override
	public void performMainCMD(Member member, TextChannel chennel, Message message, Guild guild, GuildEntity guildentity) {
		ConsoleLogger.info(message);
	}
	
}
