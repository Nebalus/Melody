package de.melody.commands.music;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.model_objects.specification.Playlist;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.requests.data.playlists.GetPlaylistRequest;
import com.wrapper.spotify.requests.data.tracks.GetTrackRequest;

import de.nebalus.botbuilder.command.CommandInfo;
import de.nebalus.botbuilder.command.CommandType;
import de.melody.core.Melody;
import de.melody.entities.GuildEntity;
import de.melody.music.AudioLoadResult;
import de.melody.music.MusicController;
import de.melody.music.MusicUtil;
import de.melody.speechpackets.MessageFormatter;
import de.nebalus.botbuilder.command.ServerCommand;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.managers.AudioManager;

public class PlayCommand implements ServerCommand{

	private Melody melody = Melody.INSTANCE;
	private MessageFormatter mf = melody.getMessageFormatter();
	
	@Override
	public void performCommand(Member m, TextChannel channel, Message message, Guild guild) {
		String[] args = message.getContentDisplay().split(" ");
		GuildEntity ge = melody.getEntityManager().getGuildEntity(guild);
		ge.setMusicChannelId(channel.getIdLong());
		if(args.length > 1) {
			GuildVoiceState state;
			VoiceChannel vc;
			if((state = m.getVoiceState()) != null && (vc = state.getChannel()) != null) {
				MusicController controller = melody.playerManager.getController(guild.getIdLong());
				AudioPlayerManager apm = melody.audioPlayerManager;
				AudioManager manager = guild.getAudioManager();
				StringBuilder strBuilder = new StringBuilder();
				for (int i = 1; i < args.length; i++) strBuilder.append(args[i] + " ");
				String url = strBuilder.toString().trim();
				boolean isytsearch = false;
				if(!url.startsWith("http")) {
					url = "ytsearch: " + url;
					isytsearch = true;
				}
				if(url.startsWith("https://open.spotify.com/")) {
					SpotifyApi spotify = new SpotifyApi.Builder()
							.setAccessToken(Melody.INSTANCE.spotifyutils.getToken())
							.build();
					if(url.toLowerCase().startsWith("https://open.spotify.com/track/")){
						String[] urlid = url.split("/");
						String id = urlid[4].substring(0, 22);
						
						final GetTrackRequest TrackRequest = spotify.getTrack(id).build();
						manager.openAudioConnection(vc);
						try {
							final CompletableFuture<Track> trackFuture = TrackRequest.executeAsync();
							final Track track = trackFuture.join();
							
							final String uri = "ytsearch: " + track.getName() + " "+track.getArtists()[0].getName();
							
							apm.loadItem(uri, new AudioLoadResult(controller, uri, m));
							} catch (CompletionException e) {
						      System.out.println("Error: " + e.getCause().getMessage());
							} catch (CancellationException e) {
						      System.out.println("Async operation cancelled.");
							}
					}else
						if(url.toLowerCase().startsWith("https://open.spotify.com/playlist/")){
							String[] urlid = url.split("/");
							String id = urlid[4].substring(0, 22);
							
							
							final GetPlaylistRequest playlistRequest = spotify.getPlaylist(id).build();
							manager.openAudioConnection(vc);
							try {
								final CompletableFuture<Playlist> playlistFuture = playlistRequest.executeAsync();
								final Playlist playlist = playlistFuture.join();
				
								AudioPlaylist aPlaylist = new AudioPlaylist() {
									
									@Override
									public boolean isSearchResult() {
										return false;
									}
									
									@Override
									public List<AudioTrack> getTracks() {
										List<AudioTrack> list = new ArrayList<AudioTrack>();
										
										return null;
									}
									
									@Override
									public AudioTrack getSelectedTrack() {
										return getTracks().get(0);
									}
									
									@Override
									public String getName() {
										return playlist.getName();
									}
								};
								
								final String uri = "ytsearch: " + playlist.getName();
								apm.loadItem(uri, new AudioLoadResult(controller, uri, m));
								} catch (CompletionException e) {
							      System.out.println("Error: " + e.getCause().getMessage());
								} catch (CancellationException e) {
							      System.out.println("Async operation cancelled.");
								}
						}
				}else if(MusicUtil.isUrlVerified(url) || isytsearch == true) {
					manager.openAudioConnection(vc);
					final String uri = url;
					apm.loadItem(uri, new AudioLoadResult(controller, uri, m));
				}else {
					MusicUtil.sendEmbledError(guild, mf.format(guild, "feedback.music.non-whitelisted-domain",MusicUtil.getDomain(url)));
				}					
			}else {
				MusicUtil.sendEmbledError(guild, mf.format(guild, "feedback.music.user-not-in-vc"));
			}
		}else {
			MusicUtil.sendEmbledError(guild, mf.format(guild, "feedback.info.command-usage",ge.getPrefix()+"play <url/search query>"));
		}
	}

	@Override
	public List<String> getCommandPrefix() {
		return List.of("play","p");
	}
	@Override
	public CommandType getCommandType() {
		return CommandType.CHAT_COMMAND;
	}

	@Override
	public CommandInfo getCommandInfo() {
		return CommandInfo.INFO_COMMAND;
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
