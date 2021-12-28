package de.melody.commands.music;

import java.util.ArrayList;

import de.melody.core.Melody;
import de.melody.entities.GuildEntity;
import de.melody.music.LoopMode;
import de.melody.music.MusicController;
import de.melody.speechpackets.MessageFormatter;
import de.melody.tools.Utils.Emoji;
import de.melody.tools.commandbuilder.CommandPermission;
import de.melody.tools.commandbuilder.CommandType;
import de.melody.tools.commandbuilder.ServerCommand;
import de.melody.tools.messenger.Messenger;
import de.melody.tools.messenger.Messenger.ErrorMessageBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.Command.Choice;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class LoopCommand implements ServerCommand{

	private Melody melody = Melody.INSTANCE;
	private MessageFormatter mf = melody.getMessageFormatter();
	
	@Override
	public void performCommand(Member member, TextChannel channel, Message message, Guild guild, GuildEntity guildentity) {
		String[] args = message.getContentDisplay().split(" ");
		GuildVoiceState state;
		if((state = guild.getSelfMember().getVoiceState()) != null && state.getChannel() != null) {
			MusicController controller = melody.playerManager.getController(guild.getIdLong());
			if(args.length > 1) {
				LoopMode mode = LoopMode.getFromString(args[1]);
				if(mode != null) {
					controller.setLoopMode(mode);
					switch(controller.getLoopMode()) {
					case QUEUE:
						Messenger.sendMessageEmbed(channel,Emoji.QUEUE_LOOP+mf.format(controller.getGuild(), "music.info.loopmode-queue")).queue();
						break;
					case SONG:
						Messenger.sendMessageEmbed(channel,Emoji.SINGLE_LOOP+mf.format(controller.getGuild(), "music.info.loopmode-song")).queue();
						break;
					case NONE:
						Messenger.sendMessageEmbed(channel,Emoji.EXIT+mf.format(controller.getGuild(), "music.info.loopmode-none")).queue();
						break;
					default: 
						break;
					}
					return;
				}
			}
			
			switch(controller.getLoopMode()) {
				case QUEUE:
					controller.setLoopMode(LoopMode.SONG);
					Messenger.sendMessageEmbed(channel,Emoji.SINGLE_LOOP+mf.format(controller.getGuild(), "music.info.loopmode-song")).queue();
					break;
				case SONG:
					controller.setLoopMode(LoopMode.NONE);
					Messenger.sendMessageEmbed(channel,Emoji.EXIT+mf.format(controller.getGuild(), "music.info.loopmode-none")).queue();
					break;
				case NONE:
					controller.setLoopMode(LoopMode.QUEUE);
					Messenger.sendMessageEmbed(channel,Emoji.QUEUE_LOOP+mf.format(controller.getGuild(), "music.info.loopmode-queue")).queue();
					break;
				default: 
					break;
			}
		}else {
			Messenger.sendErrorMessage(channel, new ErrorMessageBuilder().setMessageFormat(guild, "music.bot-not-in-vc"));
		}
	}
	@Override
	public void performSlashCommand(Member member, MessageChannel channel, Guild guild, GuildEntity guildentity, SlashCommandEvent event) {
		GuildVoiceState state;
		if((state = guild.getSelfMember().getVoiceState()) != null && state.getChannel() != null) {
			LoopMode loopmode = LoopMode.getFromString(event.getOption("loopmode").getAsString());
			
				MusicController controller = melody.playerManager.getController(guild.getIdLong());
				if(loopmode != null) {
					controller.setLoopMode(loopmode);
					switch(controller.getLoopMode()) {
					case QUEUE:
						event.replyEmbeds(Messenger.getMessageEmbed(Emoji.QUEUE_LOOP+mf.format(controller.getGuild(), "music.info.loopmode-queue"))).queue();
						break;
					case SONG:
						event.replyEmbeds(Messenger.getMessageEmbed(Emoji.SINGLE_LOOP+mf.format(controller.getGuild(), "music.info.loopmode-song"))).queue();
						break;
					case NONE:
						event.replyEmbeds(Messenger.getMessageEmbed(Emoji.EXIT+mf.format(controller.getGuild(), "music.info.loopmode-none"))).queue();
						break;
					default: 
						break;
					}
					return;
				}
					
				switch(controller.getLoopMode()) {
					case QUEUE:
						controller.setLoopMode(LoopMode.SONG);
						event.replyEmbeds(Messenger.getMessageEmbed(Emoji.SINGLE_LOOP+mf.format(controller.getGuild(), "music.info.loopmode-song"))).queue();
						break;
					case SONG:
						controller.setLoopMode(LoopMode.NONE);
						event.replyEmbeds(Messenger.getMessageEmbed(Emoji.EXIT+mf.format(controller.getGuild(), "music.info.loopmode-none"))).queue();
						break;
					case NONE:
						controller.setLoopMode(LoopMode.QUEUE);
						event.replyEmbeds(Messenger.getMessageEmbed(Emoji.QUEUE_LOOP+mf.format(controller.getGuild(), "music.info.loopmode-queue"))).queue();
						break;
					default: 
						break;
				}
			
		}else {
			Messenger.sendErrorSlashMessage(event, new ErrorMessageBuilder().setMessageFormat(guild, "music.bot-not-in-vc"));
		}
	}
	
	
	@Override
	public String[] getCommandPrefix() {
		return new String[] {"loop"};
	}
	@Override
	public CommandType getCommandType() {
		return CommandType.BOTH;
	}
	
	@Override
	public String getCommandDescription() {
		return "Cycles through all three loop modes (queue, song, off)";
	}

	@Override
	public OptionData[] getCommandOptions() {
		ArrayList<Choice> choices = new ArrayList<Choice>();
		for(LoopMode lm : LoopMode.values()) {
			choices.add(new Choice(lm.getTextFormat(), lm.getTextFormat()));
		}
		return new OptionData[] {new OptionData(OptionType.STRING, "loopmode", "Enter the loopmode").addChoices(choices)};
	}
	
	@Override
	public CommandPermission getMainPermmision() {
		return CommandPermission.DJ;
	}
}
