package de.melody.tools.commandbuilder;

import de.melody.entities.GuildEntity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;


public interface ServerCommand {

	public void performCommand(Member member, TextChannel channel, Message message, Guild guild, GuildEntity guildentity);
	
	public void performSlashCommand(Member member, MessageChannel channel, Guild guild, GuildEntity guildentity, SlashCommandEvent event);
	
	public String[] getCommandPrefix();
	
	public CommandType getCommandType();
	
	public OptionData[] getCommandOptions();
	
	public String getCommandDescription();
	
	public CommandPermission getMainPermmision();
}

