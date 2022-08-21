package de.nebalus.dcbots.melody.tools.cmdbuilder;

import de.nebalus.dcbots.melody.tools.entitymanager.entitys.GuildEntity;
import de.nebalus.dcbots.melody.tools.entitymanager.entitys.UserEntity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;

public class SlashExecuter 
{
	
	public void executeGuild(Member member, MessageChannel channel, Guild guild, GuildEntity guildentity, SlashCommandInteractionEvent event, InteractionHook hook)
	{
		
	}
	
	public void executePrivate(User user, PrivateChannel pchannel, UserEntity userentity, SlashCommandInteractionEvent event, InteractionHook hook)
	{
		
	}
	
}
