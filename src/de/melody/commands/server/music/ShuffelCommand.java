package de.melody.commands.server.music;

import de.melody.Melody;
import de.melody.commands.types.SlashCommand;
import de.melody.music.MusicController;
import de.melody.music.Queue;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class ShuffelCommand implements SlashCommand{

	private Melody melody = Melody.INSTANCE;
	
	@Override
	public void performCommand(Member m, TextChannel channel, Message message, Guild guild) {
		MusicController controller = melody.playerManager.getController(channel.getGuild().getIdLong());
		Queue queue = controller.getQueue();
		if(queue.getQueuelist().size() > 1) {
			queue.shuffel(); 
			channel.sendMessage(queue.getQueuelist().size()+" tracks has been shuffeled!").queue();
		}
	}
}
