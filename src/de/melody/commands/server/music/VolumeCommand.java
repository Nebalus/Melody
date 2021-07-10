package de.melody.commands.server.music;

import de.melody.Melody;
import de.melody.commands.types.ServerCommand;
import de.melody.entities.GuildEntity;
import de.melody.music.MusicUtil;
import de.melody.speechpackets.MessageFormatter;
import de.melody.utils.Utils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class VolumeCommand implements ServerCommand{

	private Melody melody = Melody.INSTANCE;
	private MessageFormatter mf = melody.getMessageFormatter();
	
	private final int maxvolume = 100;
	
	@Override
	public void performCommand(Member m, TextChannel channel, Message message, Guild guild) {
		String[] args = message.getContentDisplay().split(" ");
		GuildEntity guildentity = melody.entityManager.getGuildEntity(guild.getIdLong());
		guildentity.setChannelId(channel.getIdLong());
		if(args.length == 1) {
			EmbedBuilder builder = new EmbedBuilder();
			builder.setDescription(mf.format(guild.getIdLong(), "config.volume.show",guildentity.getVolume()));
			MusicUtil.sendEmbled(guild.getIdLong(), builder);
		}else {
			try {
				int amount = Integer.parseInt(args[1]);			
				if(amount <= maxvolume) {
					if(amount >= 1) {
						melody.playerManager.getController(guild.getIdLong()).getPlayer().setVolume(amount);
						EmbedBuilder builder = new EmbedBuilder();
						guildentity.setVolume(amount);
						builder.setDescription(mf.format(guild.getIdLong(), "config.volume.set",amount));
						MusicUtil.sendEmbled(guild.getIdLong(), builder);
					}else
						Utils.sendErrorEmbled(channel, mf.format(guild.getIdLong(), "config.volume.min-int"), m);
				}else
					Utils.sendErrorEmbled(channel, mf.format(guild.getIdLong(), "config.volume.max-int",maxvolume), m);
			}catch(NumberFormatException e) {
				Utils.sendErrorEmbled(channel, mf.format(guild.getIdLong(), "config.volume.out-of-bounds",maxvolume), m);
			}
		}
	}
}
