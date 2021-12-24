package de.melody.commands.music;

import de.melody.entities.GuildEntity;
import de.melody.utils.commandbuilder.CommandPermission;
import de.melody.utils.commandbuilder.CommandType;
import de.melody.utils.commandbuilder.ServerCommand;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class PlaylistCommand implements ServerCommand{

	@Override
	public void performCommand(Member member, TextChannel channel, Message message, Guild guild, GuildEntity guildentity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void performSlashCommand(Member member, MessageChannel channel, Guild guild, GuildEntity guildentity, SlashCommandEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String[] getCommandPrefix() {
		return new String[] {"playlist", "pl"};
	}

	@Override
	public CommandType getCommandType() {
		return CommandType.CHAT;
	}

	@Override
	public OptionData[] getCommandOptions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCommandDescription() {
		return "List your saved playlists";
	}

	@Override
	public CommandPermission getMainPermmision() {
		return CommandPermission.EVERYONE;
	}

}
