package de.melody.commands.music;

import java.util.List;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import de.melody.core.Melody;
import de.melody.entities.reacts.QueueReaction;
import de.melody.music.MusicController;
import de.melody.music.Queue;
import de.melody.music.Queue.QueuedTrack;
import de.melody.speechpackets.MessageFormatter;
import de.melody.utils.Utils;
import de.melody.utils.Utils.Emoji;
import de.melody.utils.commandbuilder.CommandInfo;
import de.melody.utils.commandbuilder.CommandType;
import de.melody.utils.commandbuilder.ServerCommand;
import de.melody.utils.messenger.Messenger;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;


public class QueueCommand implements ServerCommand{

	private static Melody melody = Melody.INSTANCE;
	private static MessageFormatter mf = melody.getMessageFormatter();
	
	@Override
	public void performCommand(Member member, TextChannel channel, Message message, Guild guild) {	
		Messenger.sendMessageEmbed(channel,"Loading...").queue((queuemessage) ->{
			queuemessage.addReaction(Emoji.BACK).queue();	
			queuemessage.addReaction(Emoji.RESUME).queue();	
			queuemessage.addReaction(Emoji.REFRESH).queue();

			QueueReaction qe = new QueueReaction(melody.playerManager.getController(guild.getIdLong()).getQueue());
			melody.getEntityManager().getGuildEntity(guild).getReactionManager().addReactionMessage(queuemessage.getIdLong(), qe);
			queuemessage.editMessageEmbeds(Messenger.getMessageEmbed(loadQueueEmbed(guild,qe))).queue();
		});		
	}
	
	public static EmbedBuilder loadQueueEmbed(Guild guild, QueueReaction qe) {
		MusicController controller = melody.playerManager.getController(guild.getIdLong());
		Queue queue = controller.getQueue();
		EmbedBuilder builder = new EmbedBuilder();
		builder.setTitle(mf.format(guild, "music.queue.from-guild",guild.getName()));
		builder.setThumbnail(guild.getIconUrl());
		String list = null;
		if(controller.getPlayer().getPlayingTrack() != null) {
			if(queue.currentlyPlaying() != null) {
				list = mf.format(guild, "music.track.currently-playing")+"\n"
					+ " ["+ controller.getPlayer().getPlayingTrack().getInfo().title+"]("+controller.getPlayer().getPlayingTrack().getInfo().uri+") | "+mf.format(guild, "music.user.who-requested")+queue.currentlyPlaying().getWhoQueued().getUser().getAsTag()+" \n"
					+ " \n";
			}
			int size = 1;
			int page = qe.getPage()*10;
			for(QueuedTrack qt : queue.getQueuelist()) {
				if(size <= page && size >= page-9) {
					AudioTrack at = qt.getTrack();
					if(size == page-9) {
						list = list + mf.format(guild, "music.queue.in-queue")+"\n";
					}
					list = list + "``"+size+".`` ``["+Utils.getTimeFormat(at.getInfo().length)+"]`` **"+ at.getInfo().title+"** - "+qt.getWhoQueued().getAsMention()+"\n";
				}
				size++;
			}
			int queuepage = queue.getQueueSize()/10;
			int maxpage = (float) queue.getQueueSize()/10 > queuepage ? queuepage+1 : queuepage;
			builder.setFooter(mf.format(guild, "music.queue.page", qe.getPage()+"/"+maxpage));
			
		}else {
			list = mf.format(guild, "error.music.currently-playing-null.body");
		}
		builder.setDescription(list);
		return builder;
	}

	@Override
	public List<String> getCommandPrefix() {
		return List.of("queue","q");
	}
	@Override
	public CommandType getCommandType() {
		return CommandType.CHAT_COMMAND;
	}

	@Override
	public CommandInfo getCommandInfo() {
		return CommandInfo.MUSIC_COMMAND;
	}
	@Override
	public String getCommandDescription() {
		return null;
	}

	@Override
	public void performSlashCommand(Member member, MessageChannel channel, Guild guild, SlashCommandEvent event) {
		
	}

	@Override
	public List<OptionData> getCommandOptions() {
		// TODO Auto-generated method stub
		return null;
	}
}
