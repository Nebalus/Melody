package de.melody.commands.dev;

import java.util.List;

import de.melody.core.Constants;
import de.melody.utils.commandbuilder.CommandInfo;
import de.melody.utils.commandbuilder.CommandType;
import de.melody.utils.commandbuilder.ServerCommand;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class GetHostIPCommand implements ServerCommand{

	@Override
	public void performCommand(Member member, TextChannel channel, Message message, Guild guild) {
		channel.sendMessage("Scanning for the IP Adress please wait...").queue();
		try (java.util.Scanner s = new java.util.Scanner(new java.net.URL("https://api.ipify.org").openStream(), "UTF-8").useDelimiter("\\A")) {
			channel.sendMessage("Curently the IP Adress from my host is `"+s.next()+"`").queue();
		} catch (java.io.IOException e) {
			channel.sendMessage("IP Request failed... Please try again later :(").queue();
		    e.printStackTrace();
		}
	}

	@Override
	public void performSlashCommand(Member member, MessageChannel channel, Guild guild, SlashCommandEvent event) {}

	@Override
	public List<String> getCommandPrefix() {
		return List.of("gethostip");
	}

	@Override
	public CommandType getCommandType() {
		return CommandType.CHAT_COMMAND;
	}

	@Override
	public CommandInfo getCommandInfo() {
		return CommandInfo.DEVELOPER_COMMAND;
	}

	@Override
	public List<OptionData> getCommandOptions() {
		return null;
	}

	@Override
	public String getCommandDescription() {
		return "Gets the public IP from the "+Constants.BUILDNAME;
	}

}
