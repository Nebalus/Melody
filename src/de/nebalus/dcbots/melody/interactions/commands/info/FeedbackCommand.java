package de.nebalus.dcbots.melody.interactions.commands.info;

import de.nebalus.dcbots.melody.tools.cmdbuilder.PermissionGroup;
import de.nebalus.dcbots.melody.tools.cmdbuilder.SlashCommand;
import de.nebalus.dcbots.melody.tools.cmdbuilder.interactions.SlashInteractionExecuter;
import de.nebalus.dcbots.melody.tools.entitymanager.entitys.GuildEntity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Modal;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;

public class FeedbackCommand extends SlashCommand {

	public FeedbackCommand() {
		super("feedback");
		setPermissionGroup(PermissionGroup.EVERYONE);
		setDescription("You can send Feedback");

		setExecuter(new SlashInteractionExecuter() {
			@Override
			public void executeGuild(Member member, MessageChannel channel, Guild guild, GuildEntity guildentity, SlashCommandInteractionEvent event) {
				TextInput body = TextInput.create("feedbackcontent", "Body2", TextInputStyle.PARAGRAPH)
						.setPlaceholder("Body3")
						.setMinLength(10)
						.setMaxLength(200)
						.build();

				Modal modal = Modal.create("feedback", "Feedback WIP")
						.addActionRows(ActionRow.of(body))
						.build();

				event.replyModal(modal).queue();
			}
		});
	}

}