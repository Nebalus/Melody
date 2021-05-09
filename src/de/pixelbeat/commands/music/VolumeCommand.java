package de.pixelbeat.commands.music;

import de.pixelbeat.PixelBeat;
import de.pixelbeat.commands.types.ServerCommand;
import de.pixelbeat.entities.GuildEntity;
import de.pixelbeat.music.MusicUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class VolumeCommand implements ServerCommand{

	private PixelBeat pixelbeat = PixelBeat.INSTANCE;
	//private MessageFormatter mf = pixelbeat.getMessageFormatter();
	
	@Override
	public void performCommand(Member m, TextChannel channel, Message message, Guild guild) {
		String[] args = message.getContentDisplay().split(" ");
		GuildEntity guildentity = pixelbeat.entityManager.getGuildEntity(guild.getIdLong());
		guildentity.setChannelId(channel.getIdLong());
		if(args.length == 1) {
			EmbedBuilder builder = new EmbedBuilder();
			builder.setDescription("The volume from the bot: " + guildentity.getVolume());
			MusicUtil.sendEmbled(guild.getIdLong(), builder);
		}else {
			try {
				int amount = Integer.parseInt(args[1]);			
				if(amount <= 200) {
					if(amount >= 1) {
						pixelbeat.playerManager.getController(guild.getIdLong()).getPlayer().setVolume(amount);
						EmbedBuilder builder = new EmbedBuilder();
						guildentity.setVolume(amount);
						builder.setDescription("The volume from the bot has been set to " + amount);
						MusicUtil.sendEmbled(guild.getIdLong(), builder);
					}else {
						MusicUtil.sendEmbledError(guild.getIdLong(), m.getAsMention() + " the min volume you can use is 1!");
					}
				}else {
					MusicUtil.sendEmbledError(guild.getIdLong(), m.getAsMention() + " the max volume you can use is 200!");
				}		
			}catch(NumberFormatException e) {
				MusicUtil.sendEmbledError(guild.getIdLong(), m.getAsMention() + " Please choose a number between 1-200!");
			}
		}
	}
}
