package de.pixelbeat.commands.music;

import java.util.Arrays;
import com.github.natanbc.lavadsp.timescale.TimescalePcmAudioFilter;

import de.pixelbeat.PixelBeat;
import de.pixelbeat.commands.types.ServerCommand;
import de.pixelbeat.entities.GuildEntity;
import de.pixelbeat.music.MusicUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class SpeedCommand implements ServerCommand {
	
	private PixelBeat pixelbeat = PixelBeat.INSTANCE;
	
	// Funktioniert nicht auf einen raspberry pi
	
	@Override
	public void performCommand(Member m, TextChannel channel, Message message, Guild guild) {
		String[] args = message.getContentDisplay().split(" ");
		GuildEntity guildentity = pixelbeat.entityManager.getGuildEntity(guild.getIdLong());
		guildentity.setChannelId(channel.getIdLong());
		if(args.length == 1) {
			EmbedBuilder builder = new EmbedBuilder();
			builder.setDescription("The music speed from the bot: " + guildentity.getSpeed());
			MusicUtil.sendEmbled(guild.getIdLong(), builder);
		}else {
			try {
				Double amount = Double.parseDouble(args[1]);			
				if(amount <= 5) {
					if(amount >= 0.25) {
						pixelbeat.playerManager.getController(guild.getIdLong()).getPlayer().setFilterFactory((track, format, output)->{
							TimescalePcmAudioFilter timescale = new TimescalePcmAudioFilter(output, format.channelCount, format.sampleRate);
							timescale.setSpeed(amount);
							return Arrays.asList(timescale);
						});
						guildentity.setSpeed(amount);
						EmbedBuilder builder = new EmbedBuilder();
						builder.setDescription("The music speed from the bot has been set to " + amount);
						MusicUtil.sendEmbled(guild.getIdLong(), builder);
					}else {
						MusicUtil.sendEmbledError(guild.getIdLong(), m.getAsMention() + " the min speed you can use is 0.25!");
					}
				}else {
					MusicUtil.sendEmbledError(guild.getIdLong(), m.getAsMention() + " the max speed you can use is 5.0!");
				}		
			}catch(NumberFormatException e) {
				MusicUtil.sendEmbledError(guild.getIdLong(), m.getAsMention() + " Please choose a number between 0.25-5.0!");
			}
		}
	}
}
