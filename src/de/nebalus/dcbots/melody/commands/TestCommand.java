package de.nebalus.dcbots.melody.commands;

import java.util.Random;

import de.nebalus.dcbots.melody.core.Melody;
import de.nebalus.dcbots.melody.tools.cmdbuilder.CommandPermission;
import de.nebalus.dcbots.melody.tools.cmdbuilder.ServerCommand;
import de.nebalus.dcbots.melody.tools.cmdbuilder.SubCommand;
import de.nebalus.dcbots.melody.tools.entitymanager.entitys.GuildEntity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Modal;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;

public class TestCommand extends ServerCommand{

	public TestCommand() {
		super("test");
		setMainPermission(CommandPermission.EVERYONE);
		addSubCommand(new SubCommand("hallo hds sd wew weda", CommandPermission.EVERYONE) {
			@Override
			public void execute(Member member, TextChannel channel, Message message, Guild guild, GuildEntity guildentity) {
				TextInput email = TextInput.create("header1", "Header1", TextInputStyle.SHORT)
		                .setPlaceholder("Header1")
		                .setMinLength(10)
		                .setMaxLength(100) // or setRequiredRange(10, 100)
		                .build();

		        TextInput body = TextInput.create("body1", "Body1", TextInputStyle.PARAGRAPH)
		                .setPlaceholder("Body1")
		                .setMinLength(30)
		                .setMaxLength(1000)
		                .build();

		        Modal modal = Modal.create("support", "Test Modal")
		                .addActionRows(ActionRow.of(email), ActionRow.of(body))
		                .build();

		      //  event.replyModal(modal).queue();
		        
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
	public void performMainCMD(Member member, MessageChannel channel, Guild guild, GuildEntity guildentity, SlashCommandInteractionEvent event) {
		Random ran = new Random();
		
		final int test = ran.nextInt(99)+1;
		
		guildentity.setVolume(test);
		Melody.getEntityManager().getUserEntity(member.getUser());
		event.reply(test+"").queue();
	}
	
}
