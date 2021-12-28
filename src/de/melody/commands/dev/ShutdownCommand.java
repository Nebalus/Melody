package de.melody.commands.dev;

import java.util.concurrent.TimeUnit;

import de.melody.core.Constants;
import de.melody.core.Melody;
import de.melody.entities.GuildEntity;
import de.melody.tools.commandbuilder.CommandPermission;
import de.melody.tools.commandbuilder.CommandType;
import de.melody.tools.commandbuilder.ServerCommand;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class ShutdownCommand implements ServerCommand{
	
	@Override
	public void performCommand(Member member, TextChannel channel, Message message, Guild guild, GuildEntity guildentity) {
		try {
			channel.sendMessage("Preparing Shutdown.sh").queue();
			TimeUnit.SECONDS.sleep(2);
			channel.sendMessage("Succes "+Constants.BUILDNAME+" will shutdown in 10 Seconds").queue();
			TimeUnit.SECONDS.sleep(10);
			Melody.INSTANCE.shutdown();
		} catch (Exception e) {
			channel.sendMessage("Error Something failed: "+e.getMessage()).queue();
			e.printStackTrace();
			
		}
	}

	@Override
	public void performSlashCommand(Member member, MessageChannel channel, Guild guild, GuildEntity guildentity, SlashCommandEvent event) {}

	@Override
	public String[] getCommandPrefix() {
		return new String[] {"shutdownbot"};
	}

	@Override
	public CommandType getCommandType() {
		return CommandType.CHAT;
	}

	@Override
	public OptionData[] getCommandOptions() {
		return null;
	}

	@Override
	public String getCommandDescription() {
		return "Shutdowns the bot";
	}
	@Override
	public CommandPermission getMainPermmision() {
		return CommandPermission.DEVELOPER;
	}
}