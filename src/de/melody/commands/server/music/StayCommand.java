package de.melody.commands.server.music;

import de.melody.Melody;
import de.melody.commands.types.ServerCommand;
import de.melody.entities.GuildEntity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class StayCommand implements ServerCommand{

	private Melody melody = Melody.INSTANCE;
	
	@Override
	public void performCommand(Member m, TextChannel channel, Message message, Guild guild) {
		GuildEntity guildentity = melody.entityManager.getGuildEntity(guild.getIdLong());
		if(guildentity.is24_7()) {
			guildentity.set24_7(false);
			channel.sendMessage("24/7 mode has been deaktivated!").queue();
		}else {
			guildentity.set24_7(true);
			channel.sendMessage("24/7 mode has been aktivated!").queue();
		}
	}
}
