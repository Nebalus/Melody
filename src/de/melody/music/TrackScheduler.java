package de.melody.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;

import de.melody.core.Constants;
import de.melody.core.Melody;
import de.melody.entities.GuildEntity;
import de.melody.music.audioloader.AudioLoadResult;
import de.melody.speechpackets.MessageFormatter;
import de.melody.utils.messenger.Messenger;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.VoiceChannel;

public class TrackScheduler extends AudioEventAdapter{
	
	private Melody melody = Melody.INSTANCE;
	private PlayerManager playerManager = melody.playerManager;
	private MessageFormatter mf = melody.getMessageFormatter();
	
	@Override
	public void onPlayerPause(AudioPlayer player) {}
	
	@Override
	public void onPlayerResume(AudioPlayer player) {}
	
	@Override
	public void onTrackStart(AudioPlayer player, AudioTrack track_UNUSED) {
		Guild guild = melody.shardMan.getGuildById(playerManager.getGuildByPlayerHash(player.hashCode()));
		MusicController controller = playerManager.getController(guild.getIdLong());
		Queue queue = controller.getQueue();
		GuildEntity ge = melody.getEntityManager().getGuildEntity(guild);
		if(controller.isLoop() == false && controller.isLoopQueue() == false && ge.canAnnounceSongs()) {
			EmbedBuilder builder = new EmbedBuilder();
			QueuedTrack queuedtrack = queue.currentlyPlaying();
			AudioTrackInfo info = queuedtrack.getTrack().getInfo();
			switch(queuedtrack.getService()) {
				case YOUTUBE:
					builder.setImage(queuedtrack.getImageURL());
					break;
					
				case SPOTIFY:
					builder.setThumbnail(queuedtrack.getImageURL());
					break;
					
				default:
					break;
			}
			builder.setDescription("  ["+mf.format(guild, "music.track.now-playing")+"]("+Constants.WEBSITE_URL+")");
			builder.addField("**"+info.author+"**","[" + info.title+"]("+info.uri+")", true);
			builder.addField(mf.format(guild, "music.track.length"), MusicUtil.getTime(info,0l),true);
			builder.setFooter(mf.format(guild, "music.user.who-requested")+ queue.currentlyPlaying().getWhoQueued().getUser().getAsTag());
			Messenger.sendMessageEmbed(ge.getMusicChannel(), builder).queue((trackmessage) ->{
				//TrackReaction te = new TrackReaction(info);
				//melody.getEntityManager().getGuildController(guild.getIdLong()).getReactionManager().addReactionMessage(trackmessage.getIdLong(), te);
				//trackmessage.addReaction(Emoji.SPARKLING_HEART).queue();
			});
		}
	}
	
	@Override
	public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {	
		long guildid = playerManager.getGuildByPlayerHash(player.hashCode());
		Guild guild = melody.shardMan.getGuildById(guildid);
		MusicController controller = melody.playerManager.getController(guildid);
		Queue queue = controller.getQueue();
		if(endReason.mayStartNext) {	
			GuildVoiceState state;
			VoiceChannel vc;
			if((state = guild.getSelfMember().getVoiceState()) != null && (vc = state.getChannel()) != null) {
				if(!controller.isLoop()) {
					if(!controller.isLoopQueue()) {
						if(queue.next(1) != 1) { 
							if(vc.getMembers().size() > 1) {
								controller.setAfkTime(600);
							}
						}else {
							return;
						}
					}else {
						final String uri = track.getInfo().uri;
						melody.audioPlayerManager.loadItem(uri, new AudioLoadResult(controller, uri, null));
						return;
					}
				}else {
					final String uri = track.getInfo().uri;
					melody.audioPlayerManager.loadItem(uri, new AudioLoadResult(controller, uri, null));
					return;
				}
			}
		}
		if(controller.isLoop()) {
			controller.setLoop(false);	
		}
		player.stopTrack();
	  }
	
	  @Override
	  public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {}
	  
	  @Override
	  public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {
		 Guild guild = melody.shardMan.getGuildById(playerManager.getGuildByPlayerHash(player.hashCode()));
		 EmbedBuilder builder = new EmbedBuilder();
		 builder.setDescription("An error occured.");
		 builder.addField("Errormessage",exception.getMessage(), false);
		 builder.addField("Errorcode",exception.getCause()+"", false);
		 MusicUtil.sendEmbled(guild, builder);
	 }
}
