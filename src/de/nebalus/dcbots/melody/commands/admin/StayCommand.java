package de.nebalus.dcbots.melody.commands.admin;

import de.nebalus.dcbots.melody.core.Melody;
import de.nebalus.dcbots.melody.core.constants.Build;
import de.nebalus.dcbots.melody.tools.cmdbuilder.ServerCommand;
import de.nebalus.dcbots.melody.tools.entitymanager.entitys.GuildEntity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class StayCommand extends ServerCommand{

	public StayCommand() {
		super("24-7");
		setDescription("Toggles " + Build.NAME + " to stay 24/7 in the Audio Channel.");
	}
	
	@Override
	public void performMainCmd(Member member, MessageChannel channel, Guild guild, GuildEntity guildentity, SlashCommandInteractionEvent event) 
	{
		if(guildentity.is24_7()) 
		{
			guildentity.set24_7(false);
			event.reply(Melody.formatMessage(guild, "command.staymode.disabled"));
		}
		else 
		{
			guildentity.set24_7(true);
			event.reply(Melody.formatMessage(guild, "command.staymode.enabled"));
		}		
	}
	
}
