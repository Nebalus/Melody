package de.nebalus.dcbots.melody.commands.info;

import de.nebalus.dcbots.melody.core.Melody;
import de.nebalus.dcbots.melody.core.constants.Build;
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
		setDescription("See the response time of " + Build.NAME + " to the Discord Gateway.");
		setPrefix("ping");
	}
	
	@Override
	public void performMainCMD(Member member, MessageChannel channel, Guild guild, GuildEntity guildentity, SlashCommandInteractionEvent event) {
		ConsoleLogger.info("test");
		
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
	        
		long gatewayping = channel.getJDA().getGatewayPing();
		channel.getJDA().getRestPing().queue( (time) ->
			channel.sendMessageFormat(Melody.formatMessage(guild, "feedback.info.ping"), time, gatewayping).queue()
		);
	}
	
}