package de.nebalus.dcbots.melody.commands.admin;

import de.nebalus.dcbots.melody.core.Melody;
import de.nebalus.dcbots.melody.core.constants.Build;
import de.nebalus.dcbots.melody.tools.cmdbuilder.SlashCommand;
import de.nebalus.dcbots.melody.tools.cmdbuilder.SlashExecuter;
import de.nebalus.dcbots.melody.tools.entitymanager.entitys.GuildEntity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;

public class StayCommand extends SlashCommand
{

	public StayCommand() 
	{
		super("24-7");
		setDescription("Toggles " + Build.NAME + " to stay 24/7 in the Audio Channel.");
		
		setExecuter(new SlashExecuter()
		{
			@Override
			public void executeGuild(Member member, MessageChannel channel, Guild guild, GuildEntity guildentity, SlashCommandInteractionEvent event, InteractionHook hook) 
			{
				if(guildentity.is24_7()) 
				{
					guildentity.set24_7(false);
					hook.sendMessage(Melody.formatMessage(guild, "command.staymode.disabled")).queue();
				}
				else 
				{
					guildentity.set24_7(true);
					hook.sendMessage(Melody.formatMessage(guild, "command.staymode.enabled")).queue();
				}		
			}
		});
	}
}
