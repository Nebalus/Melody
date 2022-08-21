package de.nebalus.dcbots.melody.commands;

import java.util.Random;

import de.nebalus.dcbots.melody.core.Melody;
import de.nebalus.dcbots.melody.tools.cmdbuilder.PermissionGroup;
import de.nebalus.dcbots.melody.tools.cmdbuilder.SlashCommand;
import de.nebalus.dcbots.melody.tools.cmdbuilder.SlashExecuter;
import de.nebalus.dcbots.melody.tools.cmdbuilder.SlashSubCommand;
import de.nebalus.dcbots.melody.tools.cmdbuilder.SlashSubCommandGroup;
import de.nebalus.dcbots.melody.tools.entitymanager.entitys.GuildEntity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Modal;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;

public class TestCommand extends SlashCommand
{

	public TestCommand() 
	{
		super("test");
		setPermissionGroup(PermissionGroup.EVERYONE);
		
		setExecuter(new SlashExecuter()
		{
			@Override
			public void executeGuild(Member member, MessageChannel channel, Guild guild, GuildEntity guildentity, SlashCommandInteractionEvent event, InteractionHook hook)
			{
				Random ran = new Random();
				
				final int test = ran.nextInt(99) + 1;
				
				guildentity.setVolume(test);
				Melody.getEntityManager().getUserEntity(member.getUser());
				event.reply(test + "").queue();
			}
		});
		
		addSubCommand(new SlashSubCommand("hwweflla", "Test")
			.setExecuter(new SlashExecuter()
			{
				@Override
				public void executeGuild(Member member, MessageChannel channel, Guild guild, GuildEntity guildentity, SlashCommandInteractionEvent event, InteractionHook hook)
				{
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
	
			        event.replyModal(modal).queue();       
				}
			})
			.addOption(new OptionData(OptionType.INTEGER, "test", "Test")
					.setRequired(true))
		);

		addSubCommandGroup(new SlashSubCommandGroup("testgroup", "Krasser Test")
			.addSubCommand(new SlashSubCommand("iluminatitest", "test")
				.setExecuter(new SlashExecuter() 
				{
					@Override
					public void executeGuild(Member member, MessageChannel channel, Guild guild, GuildEntity guildentity, SlashCommandInteractionEvent event, InteractionHook hook)
					{
						channel.sendMessage("Hey.s..").queue();
					}
				})
			)
		);
	}
}
