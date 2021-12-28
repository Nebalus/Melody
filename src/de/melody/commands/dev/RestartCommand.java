package de.melody.commands.dev;

import java.io.File;
import java.util.ArrayList;
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

public class RestartCommand implements ServerCommand{
	
	@Override
	public void performCommand(Member member, TextChannel channel, Message message, Guild guild, GuildEntity guildentity) {
		try {
			channel.sendMessage("Preparing Restart.sh").queue();
			TimeUnit.SECONDS.sleep(2);
			final String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
			File currentJar = new File(Melody.class.getProtectionDomain().getCodeSource().getLocation().toURI());
			/* is it a jar file? */
			if(!currentJar.getName().endsWith(".jar"))
			   return;
			channel.sendMessage("Boot path found: "+currentJar.toString()).queue();
			TimeUnit.SECONDS.sleep(1);
			/* Build command: java -jar application.jar */
			final ArrayList<String> command = new ArrayList<String>();
			command.add(javaBin);
			command.add("-jar");
			command.add(currentJar.getPath());

			final ProcessBuilder builder = new ProcessBuilder(command);
			channel.sendMessage("Start Command: "+builder.command().toString()).queue();
			TimeUnit.SECONDS.sleep(1);
			channel.sendMessage("Succes "+Constants.BUILDNAME+" will restart in 10 Seconds").queue();
			TimeUnit.SECONDS.sleep(10);
			builder.start();
			System.exit(0);
		} catch (Exception e) {
			channel.sendMessage("Error Something failed: "+e.getMessage()).queue();
			e.printStackTrace();
			
		}
	}

	@Override
	public void performSlashCommand(Member member, MessageChannel channel, Guild guild, GuildEntity guildentity, SlashCommandEvent event) {}

	@Override
	public String[] getCommandPrefix() {
		return new String[] {"restartbot"};
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
		return "Restarts the bot";
	}
	@Override
	public CommandPermission getMainPermmision() {
		return CommandPermission.DEVELOPER;
	}
}
