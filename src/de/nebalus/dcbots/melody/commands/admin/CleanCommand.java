package de.nebalus.dcbots.melody.commands.admin;

import java.util.ArrayList;
import java.util.List;
import de.nebalus.dcbots.melody.tools.cmdbuilder.InternPermission;
import de.nebalus.dcbots.melody.tools.cmdbuilder.ServerCommand;
import de.nebalus.dcbots.melody.tools.entitymanager.entitys.GuildEntity;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class CleanCommand extends ServerCommand
{
	
	public CleanCommand() 
	{
		super("clean");
		setInternPermission(InternPermission.ADMIN);
		setDescription("Clears command and bot messages.");
		setSlashCommandData(
				Commands.slash(getPrefix(), getDescription())
					.addOptions(new OptionData(OptionType.INTEGER, "amount", "Enter an amount between 1-200")
							.setRequiredRange(1, 200)
							.setRequired(true))
					.setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MESSAGE_MANAGE))
		);
	}
	
	@Override
	public void performMainCmd(Member member, MessageChannel channel, Guild guild, GuildEntity guildentity, SlashCommandInteractionEvent event) 
	{
		final List<Message> purgemessages = new ArrayList<Message>();
		final int purgeamount = event.getOption("amount").getAsInt();
		int currentmessage = 0;
		
		for(Message cachemessage : channel.getIterableHistory().cache(false)) 
		{
			if(cachemessage.getAuthor() == channel.getJDA().getSelfUser()) 
			{
				purgemessages.add(cachemessage);
			}
			
			if(++currentmessage >= purgeamount) 
				break;
		}
		
		channel.purgeMessages(purgemessages);	
		event.reply(purgemessages.size() + " from " + currentmessage + " Messages have been deleted.").queue();
	}
	
}
