package de.melody.commands.music;

import de.melody.core.Melody;
import de.melody.entities.GuildEntity;
import de.melody.music.LoopMode;
import de.melody.music.MusicController;
import de.melody.speechpackets.MessageFormatter;
import de.melody.utils.Utils.Emoji;
import de.melody.utils.commandbuilder.CommandPermissions;
import de.melody.utils.commandbuilder.CommandType;
import de.melody.utils.commandbuilder.ServerCommand;
import de.melody.utils.messenger.Messenger;
import de.melody.utils.messenger.Messenger.ErrorMessageBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;


public class LoopCommand implements ServerCommand{

	private Melody melody = Melody.INSTANCE;
	private MessageFormatter mf = melody.getMessageFormatter();
	
	@Override
	public void performCommand(Member member, TextChannel channel, Message message, Guild guild, GuildEntity guildentity) {
		GuildVoiceState state;
		if((state = guild.getSelfMember().getVoiceState()) != null && state.getChannel() != null) {
			MusicController controller = melody.playerManager.getController(guild.getIdLong());
			switch(controller.getLoopMode()) {
				case QUEUE:
					controller.setLoopMode(LoopMode.SONG);
					Messenger.sendMessageEmbed(channel,Emoji.SINGLE_LOOP+mf.format(guild, "music.info.loopmode-song")).queue();
					break;
				case SONG:
					controller.setLoopMode(LoopMode.NONE);
					Messenger.sendMessageEmbed(channel,Emoji.EXIT+mf.format(guild, "music.info.loopmode-none")).queue();
					break;
				case NONE:
					controller.setLoopMode(LoopMode.QUEUE);
					Messenger.sendMessageEmbed(channel,Emoji.QUEUE_LOOP+mf.format(guild, "music.info.loopmode-queue")).queue();
					break;
				default: 
					break;
			}
		}else {
			Messenger.sendErrorMessage(channel, new ErrorMessageBuilder().setMessageFormat(guild, "music.bot-not-in-vc"));
		}
	}

	@Override
	public String[] getCommandPrefix() {
		return new String[] {"loop"};
	}
	@Override
	public CommandType getCommandType() {
		return CommandType.CHAT;
	}
	
	@Override
	public String getCommandDescription() {
		return null;
	}

	@Override
	public void performSlashCommand(Member member, MessageChannel channel, Guild guild, GuildEntity guildentity, SlashCommandEvent event) {
		
	}

	@Override
	public OptionData[] getCommandOptions() {
		return null;
	}
	@Override
	public CommandPermissions getMainPermmision() {
		return CommandPermissions.DJ;
	}
}