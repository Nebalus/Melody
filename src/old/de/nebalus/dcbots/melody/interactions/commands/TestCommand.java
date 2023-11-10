package old.de.nebalus.dcbots.melody.interactions.commands;

import java.util.Random;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import old.de.nebalus.dcbots.melody.core.constants.Melody;
import old.de.nebalus.dcbots.melody.tools.cmdbuilder.PermissionGroup;
import old.de.nebalus.dcbots.melody.tools.cmdbuilder.SlashCommand;
import old.de.nebalus.dcbots.melody.tools.cmdbuilder.SlashSubCommand;
import old.de.nebalus.dcbots.melody.tools.cmdbuilder.SlashSubCommandGroup;
import old.de.nebalus.dcbots.melody.tools.cmdbuilder.interactions.SlashInteractionExecuter;
import old.de.nebalus.dcbots.melody.tools.entitymanager.entitys.GuildEntity;

public class TestCommand extends SlashCommand {

	public TestCommand() {
		super("test");
		setPermissionGroup(PermissionGroup.EVERYONE);

		setExecuter(new SlashInteractionExecuter() {
			@Override
			public void executeGuild(Member member, MessageChannel channel, Guild guild, GuildEntity guildentity,
					SlashCommandInteractionEvent event) {
				Random ran = new Random();

				final int test = ran.nextInt(99) + 1;

				guildentity.setVolume(test);
				Melody.getEntityManager().getUserEntity(member.getUser());
				event.reply(test + "").queue();
			}
		});

		addSubCommand(new SlashSubCommand("hwweflla", "Test").setExecuter(new SlashInteractionExecuter() {
			@Override
			public void executeGuild(Member member, MessageChannel channel, Guild guild, GuildEntity guildentity,
					SlashCommandInteractionEvent event) {
				TextInput email = TextInput.create("header1", "Header1", TextInputStyle.SHORT).setPlaceholder("Header1")
						.setMinLength(10).setMaxLength(100) // or setRequiredRange(10, 100)
						.build();

				TextInput body = TextInput.create("body1", "Body1", TextInputStyle.PARAGRAPH).setPlaceholder("Body1")
						.setMinLength(30).setMaxLength(1000).build();

				Modal modal = Modal.create("support", "Test Modal")
						.addActionRows(ActionRow.of(email), ActionRow.of(body)).build();

				event.replyModal(modal).queue();
			}
		}).addOption(new OptionData(OptionType.INTEGER, "test", "Test").setRequired(true)));

		addSubCommandGroup(new SlashSubCommandGroup("testgroup", "Krasser Test")
				.addSubCommand(new SlashSubCommand("iluminatitest", "test").setExecuter(new SlashInteractionExecuter() {
					@Override
					public void executeGuild(Member member, MessageChannel channel, Guild guild,
							GuildEntity guildentity, SlashCommandInteractionEvent event) {
						channel.sendMessage("Hey.s..").queue();
					}
				})));
	}
}
