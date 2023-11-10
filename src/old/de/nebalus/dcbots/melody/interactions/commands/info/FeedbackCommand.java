package old.de.nebalus.dcbots.melody.interactions.commands.info;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import old.de.nebalus.dcbots.melody.tools.cmdbuilder.PermissionGroup;
import old.de.nebalus.dcbots.melody.tools.cmdbuilder.SlashCommand;
import old.de.nebalus.dcbots.melody.tools.cmdbuilder.interactions.SlashInteractionExecuter;
import old.de.nebalus.dcbots.melody.tools.entitymanager.entitys.GuildEntity;

public class FeedbackCommand extends SlashCommand {

	public FeedbackCommand() {
		super("feedback");
		setPermissionGroup(PermissionGroup.EVERYONE);
		setDescription("You can send Feedback");

		setExecuter(new SlashInteractionExecuter() {
			@Override
			public void executeGuild(Member member, MessageChannel channel, Guild guild, GuildEntity guildentity,
					SlashCommandInteractionEvent event) {
				TextInput body = TextInput.create("feedbackcontent", "Body2", TextInputStyle.PARAGRAPH)
						.setPlaceholder("Body3").setMinLength(10).setMaxLength(200).build();

				Modal modal = Modal.create("feedback", "Feedback WIP").addActionRows(ActionRow.of(body)).build();

				event.replyModal(modal).queue();
			}
		});
	}

}