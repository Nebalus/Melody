package de.nebalus.dcbots.melody.commands.info;

import de.nebalus.dcbots.melody.core.Melody;
import de.nebalus.dcbots.melody.core.constants.Build;
import de.nebalus.dcbots.melody.core.constants.Url;
import de.nebalus.dcbots.melody.tools.cmdbuilder.PermissionGroup;
import de.nebalus.dcbots.melody.tools.cmdbuilder.SlashCommand;
import de.nebalus.dcbots.melody.tools.cmdbuilder.SlashExecuter;
import de.nebalus.dcbots.melody.tools.entitymanager.entitys.GuildEntity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;

public class InviteCommand extends SlashCommand
{

	public InviteCommand()
	{
		super("invite");
		setPermissionGroup(PermissionGroup.EVERYONE);
		setDescription("Shows the invitation link to invite " + Build.NAME + " to your own Discord server.");
	
		setExecuter(new SlashExecuter()
		{
			@Override
			public void executeGuild(Member member, MessageChannel channel, Guild guild, GuildEntity guildentity, SlashCommandInteractionEvent event, InteractionHook hook) 
			{
				hook.sendMessage(Melody.formatMessage(guild, "command.info.invite", Url.INVITE.toString())).queue();
			}
		});
	}
	
}