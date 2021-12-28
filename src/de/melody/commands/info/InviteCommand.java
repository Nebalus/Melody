package de.melody.commands.info;

import de.melody.core.Constants;
import de.melody.core.Melody;
import de.melody.entities.GuildEntity;
import de.melody.speechpackets.MessageFormatter;
import de.melody.tools.commandbuilder.CommandPermission;
import de.melody.tools.commandbuilder.CommandType;
import de.melody.tools.commandbuilder.ServerCommand;
import de.melody.tools.messenger.Messenger;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;


public class InviteCommand implements ServerCommand{

	private Melody melody = Melody.INSTANCE;
	private MessageFormatter mf = melody._messageformatter;
	
	@Override
	public void performCommand(Member member, TextChannel channel, Message message, Guild guild, GuildEntity guildentity) {
		Messenger.sendMessage(channel,mf.format(channel.getGuild(), "feedback.info.invite", Constants.INVITE_URL)).queue();
	}

	@Override
	public void performSlashCommand(Member member, MessageChannel channel, Guild guild, GuildEntity guildentity, SlashCommandEvent event) {
		event.reply(mf.format(guild, "feedback.info.invite", Constants.INVITE_URL)).queue();
	}
	
	@Override
	public String[] getCommandPrefix() {
		return new String[] {"invite"};
	}
	
	@Override
	public CommandType getCommandType() {
		return CommandType.BOTH;
	}

	@Override
	public String getCommandDescription() {
		return "Invite "+Constants.BUILDNAME+" to your own Discord Server";
	}
	
	@Override
	public OptionData[] getCommandOptions() {
		return null;
	}
	@Override
	public CommandPermission getMainPermmision() {
		return CommandPermission.EVERYONE;
	}
}
