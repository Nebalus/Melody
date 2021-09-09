package de.melody.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;

import de.melody.core.Config;
import de.melody.core.Melody;
import de.melody.entities.GuildEntity;
import de.melody.entities.reacts.TrackReaction;
import de.melody.speechpackets.MessageFormatter;
import de.melody.utils.Emoji;
import de.nebalus.botbuilder.utils.Messenger;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.VoiceChannel;

public class TrackScheduler extends AudioEventAdapter{
	
	private Melody melody = Melody.INSTANCE;
	private PlayerManager playerManager = melody.playerManager;
	private MessageFormatter mf = melody.getMessageFormatter();
	
	@Override
	public void onPlayerPause(AudioPlayer player) {
		Guild guild = melody.shardMan.getGuildById(playerManager.getGuildByPlayerHash(player.hashCode()));
		
		EmbedBuilder builder = new EmbedBuilder();
		builder.setDescription(Emoji.PAUSE+" "+mf.format(guild, "music.track.pause"));
		MusicUtil.sendEmbled(guild, builder);		
	}
	
	@Override
	public void onPlayerResume(AudioPlayer player) {
		Guild guild = melody.shardMan.getGuildById(playerManager.getGuildByPlayerHash(player.hashCode()));
		
		EmbedBuilder builder = new EmbedBuilder();
		builder.setDescription(Emoji.RESUME+" "+mf.format(guild, "music.track.resume"));
		MusicUtil.sendEmbled(guild, builder);		
	}
	
	
	
	@Override
	public void onTrackStart(AudioPlayer player, AudioTrack track) {
		Guild guild = melody.shardMan.getGuildById(playerManager.getGuildByPlayerHash(player.hashCode()));
		MusicController controller = playerManager.getController(guild.getIdLong());
		Queue queue = controller.getQueue();
		GuildEntity ge = melody.entityManager.getGuildEntity(guild);
		if(controller.isLoop() == false && controller.isLoopQueue() == false && ge.canAnnounceSongs()) {
			EmbedBuilder builder = new EmbedBuilder();
			AudioTrackInfo info = track.getInfo();
			String url = info.uri;
			builder.setDescription("["+guild.getJDA().getEmoteById(Emoji.ANIMATED_PLAYING).getAsMention()+" "+mf.format(guild, "music.track.now-playing")+"]("+Config.WEBSITE_URL+")");
			builder.addField("**"+info.author+"**","[" + info.title+"]("+url+")", true);
			builder.addField(mf.format(guild, "music.track.length"), MusicUtil.getTime(info,0l),true);
			builder.setFooter(mf.format(guild, "music.user.who-requested")+ queue.currentlyPlaying().getWhoQueued().getUser().getAsTag());
			if(url.startsWith("https://www.youtube.com/watch?v=")) {
				String videoID = url.replace("https://www.youtube.com/watch?v=", "");
				//builder.setImage("https://i.ytimg.com/vi_webp/"+videoID+"/maxresdefault.webp");
				builder.setThumbnail("https://i.ytimg.com/vi_webp/"+videoID+"/maxresdefault.webp");		
			}
			Messenger.sendMessageEmbed(ge.getMusicChannel(), builder).queue((trackmessage) ->{
				TrackReaction te = new TrackReaction(info);
				melody.entityManager.getGuildController(guild.getIdLong()).getReactionManager().addReactionMessage(trackmessage.getIdLong(), te);
				trackmessage.addReaction(Emoji.SPARKLING_HEART).queue();
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
						melody.audioPlayerManager.loadItem(uri, new AudioLoadResult(controller, uri, null, false));
						return;
					}
				}else {
					final String uri = track.getInfo().uri;
					melody.audioPlayerManager.loadItem(uri, new AudioLoadResult(controller, uri, null, false));
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
