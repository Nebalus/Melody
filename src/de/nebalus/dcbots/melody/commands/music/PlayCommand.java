package de.nebalus.dcbots.melody.commands.music;

import de.nebalus.dcbots.melody.tools.cmdbuilder.ServerCommand;
import de.nebalus.dcbots.melody.tools.entitymanager.entitys.GuildEntity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class PlayCommand extends ServerCommand
{

	public PlayCommand() 
	{
		super("play");
		setDescription("Plays a song.");
		setSlashCommandData(
				Commands.slash(getPrefix(), getDescription())
					.addOption(OptionType.STRING, "query", "Please enter a (song name/url)", true)
		);
	}

	@Override
	public void performMainCmd(Member member, MessageChannel channel, Guild guild, GuildEntity guildentity, SlashCommandInteractionEvent event) 
	{
		
	}
}
