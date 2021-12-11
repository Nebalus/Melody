package de.melody.commands.music;

import de.melody.core.Constants;
import de.melody.core.Melody;
import de.melody.entities.GuildEntity;
import de.melody.speechpackets.MessageFormatter;
import de.melody.utils.commandbuilder.CommandPermissions;
import de.melody.utils.commandbuilder.CommandType;
import de.melody.utils.commandbuilder.ServerCommand;
import de.melody.utils.messenger.Messenger;
import de.melody.utils.messenger.Messenger.ErrorMessageBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;


public class VolumeCommand implements ServerCommand{

	private Melody melody = Melody.INSTANCE;
	private MessageFormatter mf = melody.getMessageFormatter();
	
	@Override
	public void performCommand(Member member, TextChannel channel, Message message, Guild guild, GuildEntity guildentity) {
		String[] args = message.getContentDisplay().split(" ");
		if(args.length == 1) {
			Messenger.sendMessage(channel, mf.format(guild, "command.volume.show",guildentity.getVolume())).queue();
		}else {
			try {
				int amount = Integer.parseInt(args[1]);			
				if(amount <= Constants.MAXVOLUME && amount >= 1) {
					melody.playerManager.getController(guild.getIdLong()).getPlayer().setVolume(amount);
					guildentity.setVolume(amount);
					Messenger.sendMessage(channel, mf.format(guild, "command.volume.set",amount)).queue();
				}else {
					Messenger.sendErrorMessage(channel, new ErrorMessageBuilder().setMessageFormat(guild, "info.command-usage", getCommandPrefix()[0]+" <1-"+Constants.MAXVOLUME+">"));		
				}
			}catch(NumberFormatException e) {
				Messenger.sendErrorMessage(channel, new ErrorMessageBuilder().setMessageFormat(guild, "info.command-usage", getCommandPrefix()[0]+" <1-"+Constants.MAXVOLUME+">"));		
			}
		}
	}

	@Override
	public void performSlashCommand(Member member, MessageChannel channel, Guild guild, GuildEntity guildentity, SlashCommandEvent event) {
		if(event.getOption("amount") == null) {
			event.reply(mf.format(guild, "command.volume.show",guildentity.getVolume())).queue();
		}else {
			melody.playerManager.getController(guild.getIdLong()).getPlayer().setVolume((int) event.getOption("amount").getAsLong());
			guildentity.setVolume((int) event.getOption("amount").getAsLong());
			event.reply(mf.format(guild, "command.volume.set",(int) event.getOption("amount").getAsLong())).queue();
		}
	}
	
	@Override
	public String[] getCommandPrefix() {
		return new String[] {"volume","vol","v"};
	}
	@Override
	public CommandType getCommandType() {
		return CommandType.BOTH;
	}

	@Override
	public String getCommandDescription() {
		return "Lets you change the bots default output volume";
	}

	@Override
	public OptionData[] getCommandOptions() {
		return new OptionData[] {new OptionData(OptionType.INTEGER, "amount", "Enter the new volume value").setMinValue(1).setMaxValue(Constants.MAXVOLUME)};
	}
	@Override
	public CommandPermissions getMainPermmision() {
		return CommandPermissions.DJ;
	}
}
