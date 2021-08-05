package de.melody.commands.server.music;

import java.util.List;

import de.melody.Melody;
import de.melody.commands.types.ServerCommand;
import de.melody.entities.GuildEntity;
import de.melody.speechpackets.MessageFormatter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class StayCommand implements ServerCommand{

	private Melody melody = Melody.INSTANCE;
	private MessageFormatter mf = melody.getMessageFormatter();
	
	@Override
	public void performCommand(Member m, TextChannel channel, Message message, Guild guild) {
		GuildEntity guildentity = melody.entityManager.getGuildEntity(guild.getIdLong());
		if(guildentity.is24_7()) {
			guildentity.set24_7(false);
			channel.sendMessage(mf.format(guild.getIdLong(), "command.staymode.disabled")).queue();
		}else {
			guildentity.set24_7(true);
			channel.sendMessage(mf.format(guild.getIdLong(), "command.staymode.enabled")).queue();
		}
	}

	@Override
	public List<String> getCommandPrefix() {
		return List.of("24/7","247");
	}
}
