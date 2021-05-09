package de.pixelbeat.commands.music;

import de.pixelbeat.PixelBeat;
import de.pixelbeat.commands.types.ServerCommand;
import de.pixelbeat.music.MusicController;
import de.pixelbeat.music.Queue;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class ShuffelCommand implements ServerCommand{

	private PixelBeat pixelbeat = PixelBeat.INSTANCE;
	
	@Override
	public void performCommand(Member m, TextChannel channel, Message message, Guild guild) {
		MusicController controller = pixelbeat.playerManager.getController(channel.getGuild().getIdLong());
		Queue queue = controller.getQueue();
		if(queue.getQueuelist().size() > 1) {
			queue.shuffel(); 
			channel.sendMessage(queue.getQueuelist().size()+" tracks has been shuffeled!").queue();
		}
	}
}
