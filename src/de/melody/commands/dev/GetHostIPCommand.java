package de.melody.commands.dev;

import de.melody.core.Constants;
import de.melody.entities.GuildEntity;
import de.melody.tools.commandbuilder.CommandPermission;
import de.melody.tools.commandbuilder.CommandType;
import de.melody.tools.commandbuilder.Command;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class GetHostIPCommand implements Command{

	@Override
	public void performCommand(Member member, TextChannel channel, Message message, Guild guild, GuildEntity guildentity) {
		channel.sendMessage("Scanning for the IP Address please wait...").queue();
		try (java.util.Scanner s = new java.util.Scanner(new java.net.URL("https://api.ipify.org").openStream(), "UTF-8").useDelimiter("\\A")) {
			channel.sendMessage("Currently the IP Address from my host is `"+s.next()+"`").queue();
		} catch (java.io.IOException e) {
			channel.sendMessage("IP Request failed... Please try again later :(").queue();
		    e.printStackTrace();
		}
	}

	@Override
	public void performSlashCommand(Member member, MessageChannel channel, Guild guild, GuildEntity guildentity, SlashCommandEvent event) {}

	@Override
	public String[] getCommandPrefix() {
		return new String[] {"gethostip"};
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
		return "Gets the public IP from the "+Constants.BUILDNAME;
	}
	@Override
	public CommandPermission getMainPermmision() {
		return CommandPermission.DEVELOPER;
	}
}
