package de.nebalus.dcbots.melody.commands;

import java.util.Random;

import de.nebalus.dcbots.melody.tools.cmdbuilder.CommandPermission;
import de.nebalus.dcbots.melody.tools.cmdbuilder.ServerCommand;
import de.nebalus.dcbots.melody.tools.cmdbuilder.SubCommand;
import de.nebalus.dcbots.melody.tools.entitymanager.entitys.GuildEntity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class TestCommand extends ServerCommand{

	public TestCommand() {
		super();
		setMainPermission(CommandPermission.EVERYONE);
		setPrefix("test");
		addSubCommand(new SubCommand("hallo hds sd wew weda", CommandPermission.EVERYONE) {
			@Override
			public void execute(Member member, TextChannel channel, Message message, Guild guild, GuildEntity guildentity) {
				channel.sendMessage("Hey...").queue();
			}
		});
		addSubCommand(new SubCommand("hallo sd wew weda", CommandPermission.EVERYONE) {
			@Override
			public void execute(Member member, TextChannel channel, Message message, Guild guild, GuildEntity guildentity) {
				channel.sendMessage("Hey.s..").queue();
			}
		});
	}
	
	@Override
	public void performMainCMD(Member member, MessageChannel channel, Guild guild, GuildEntity guildentity, SlashCommandEvent event) {
		Random ran = new Random();
		
		final int test = ran.nextInt(99)+1;
		
		guildentity.setVolume(test);
		channel.sendMessage("HEHEHE... "+test).queue();
	}
	
}
