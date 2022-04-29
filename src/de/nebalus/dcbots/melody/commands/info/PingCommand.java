package de.nebalus.dcbots.melody.commands.info;

import de.nebalus.dcbots.melody.core.Constants;
import de.nebalus.dcbots.melody.tools.ConsoleLogger;
import de.nebalus.dcbots.melody.tools.cmdbuilder.CommandPermission;
import de.nebalus.dcbots.melody.tools.cmdbuilder.ServerCommand;
import de.nebalus.dcbots.melody.tools.entitymanager.entitys.GuildEntity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Modal;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;

public class PingCommand extends ServerCommand{

	public PingCommand() {
		super();
		setMainPermission(CommandPermission.EVERYONE);
		setDescription("See the response time of "+Constants.BUILDNAME+" to the Discord Gateway.");
		setPrefix("ping");
	}
	
	@Override
	public void performMainCMD(Member member, MessageChannel channel, Guild guild, GuildEntity guildentity, SlashCommandInteractionEvent event) {
		ConsoleLogger.info("ztes");
		TextInput email = TextInput.create("email", "Email", TextInputStyle.SHORT)
	                .setPlaceholder("Enter your E-mail")
	                .setMinLength(10)
	                .setMaxLength(100) // or setRequiredRange(10, 100)
	                .build();

	        TextInput body = TextInput.create("body", "Body", TextInputStyle.PARAGRAPH)
	                .setPlaceholder("Your concerns go here")
	                .setMinLength(30)
	                .setMaxLength(1000)
	                .build();

	        Modal modal = Modal.create("support", "Support")
	                .addActionRows(ActionRow.of(email), ActionRow.of(body))
	                .build();

	        event.replyModal(modal).queue();
//		long gatewayping = channel.getJDA().getGatewayPing();
//		channel.getJDA().getRestPing().queue( (time) ->
//			channel.sendMessageFormat(Melody.formatMessage(guild, "feedback.info.ping"), time, gatewayping).queue()
//		);
	}
	
}