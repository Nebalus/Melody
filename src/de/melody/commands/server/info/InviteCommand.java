package de.melody.commands.server.info;

import de.melody.Melody;
import de.melody.commands.types.SlashCommand;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.components.Button;

public class InviteCommand implements SlashCommand{

	@Override
	public CommandData getCommandData() {
		return new CommandData("invite", "Gets the invite link from "+Melody.name);
	}

	@Override
	public void performSlashCommand(SlashCommandEvent slash) {
		slash.replyFormat(Melody.INSTANCE.getMessageFormatter().format(slash.getGuild().getIdLong(), "feedback.info.invite", slash.getUser().getAsMention()))
		.addActionRow(Button.link("https://nebalus.ga/invite", "Invite")).queue();
	}
}
