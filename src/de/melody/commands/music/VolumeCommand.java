package de.melody.commands.music;

import java.util.List;

import de.nebalus.botbuilder.command.CommandInfo;
import de.nebalus.botbuilder.command.CommandType;
import de.melody.core.Melody;
import de.melody.entities.GuildEntity;
import de.melody.speechpackets.MessageFormatter;
import de.melody.utils.Utils;
import de.nebalus.botbuilder.command.ServerCommand;
import de.nebalus.botbuilder.utils.Messenger;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;


public class VolumeCommand implements ServerCommand{

	private Melody melody = Melody.INSTANCE;
	private MessageFormatter mf = melody.getMessageFormatter();
	
	private final int maxvolume = 100;
	
	@Override
	public void performCommand(Member m, TextChannel channel, Message message, Guild guild) {
		String[] args = message.getContentDisplay().split(" ");
		GuildEntity ge = melody.getEntityManager().getGuildEntity(guild);
		if(args.length == 1) {
			Messenger.sendMessageEmbed(channel, mf.format(guild, "command.volume.show",ge.getVolume())).queue();
		}else {
			try {
				int amount = Integer.parseInt(args[1]);			
				if(amount <= maxvolume && amount >= 1) {
					melody.playerManager.getController(guild.getIdLong()).getPlayer().setVolume(amount);
					ge.setVolume(amount);
					Messenger.sendMessageEmbed(channel, mf.format(guild, "command.volume.set",amount)).queue();
				}else
					Utils.sendErrorEmbled(message, mf.format(guild, "command.volume.out-of-bounds",maxvolume), m);
			}catch(NumberFormatException e) {
				Utils.sendErrorEmbled(message, mf.format(guild, "command.volume.out-of-bounds",maxvolume), m);
			}
		}
	}

	@Override
	public List<String> getCommandPrefix() {
		return List.of("volume","vol","v");
	}
	@Override
	public CommandType getCommandType() {
		return CommandType.CHAT_COMMAND;
	}

	@Override
	public CommandInfo getCommandInfo() {
		return CommandInfo.INFO_COMMAND;
	}
	@Override
	public String getCommandDescription() {
		return null;
	}

	@Override
	public void performSlashCommand(Member member, MessageChannel channel, Guild guild, SlashCommandEvent event) {
		
	}

	@Override
	public List<OptionData> getCommandOptions() {
		// TODO Auto-generated method stub
		return null;
	}
}
