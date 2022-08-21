package de.nebalus.dcbots.melody.commands.admin;

import java.util.ArrayList;
import java.util.List;

import de.nebalus.dcbots.melody.core.Melody;
import de.nebalus.dcbots.melody.core.constants.Build;
import de.nebalus.dcbots.melody.core.constants.Settings;
import de.nebalus.dcbots.melody.tools.cmdbuilder.PermissionGroup;
import de.nebalus.dcbots.melody.tools.cmdbuilder.SlashCommand;
import de.nebalus.dcbots.melody.tools.cmdbuilder.SlashExecuter;
import de.nebalus.dcbots.melody.tools.entitymanager.entitys.GuildEntity;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class CleanCommand extends SlashCommand
{
	
	public CleanCommand() 
	{
		super("clean");
		setPermissionGroup(PermissionGroup.ADMIN);
		setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MESSAGE_MANAGE));
		setDescription("Clears command and bot messages sent from " + Build.NAME + ".");
		
		setExecuter(new SlashExecuter() 
		{
			@Override
			public void executeGuild(Member member, MessageChannel channel, Guild guild, GuildEntity guildentity, SlashCommandInteractionEvent event, InteractionHook hook) 
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
				hook.sendMessage(Melody.formatMessage(guild, "command.admin.clean.messagesdeleted", purgemessages.size(), currentmessage)).queue();
			}
		});
		
		addOption(new OptionData(OptionType.INTEGER, "amount", "Enter an amount between 1-200")
			.setRequiredRange(1, 200)
			.setRequired(true));
	}
}
