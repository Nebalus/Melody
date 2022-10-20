package de.nebalus.dcbots.melody.interactions.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

import de.nebalus.dcbots.melody.core.Melody;
import de.nebalus.dcbots.melody.tools.audioplayer.AudioController;
import de.nebalus.dcbots.melody.tools.cmdbuilder.SlashCommand;
import de.nebalus.dcbots.melody.tools.cmdbuilder.interactions.SlashInteractionExecuter;
import de.nebalus.dcbots.melody.tools.entitymanager.entitys.GuildEntity;
import de.nebalus.dcbots.melody.tools.messenger.Messenger;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class PlayCommand extends SlashCommand
{

	public PlayCommand() 
	{
		super("play");
		setDescription("Plays a song.");
		addOption(new OptionData(OptionType.STRING, "query", "Please enter a (song name/url)")
			.setRequired(true));
		
		setExecuter(new SlashInteractionExecuter()
		{
			@Override
			public void executeGuild(Member member, MessageChannel channel, Guild guild, GuildEntity guildentity, SlashCommandInteractionEvent event)
			{		
				GuildVoiceState state;
				AudioChannel ac;
				if((state = member.getVoiceState()) == null && (ac = state.getChannel()) == null) 
				{
					Messenger.sendErrorMessage(event, Melody.formatMessage(guild, "music.user-not-in-vc"));
					return;
				}
				
				AudioController controller = Melody.getMusicManager().getController(guild.getIdLong());
				AudioPlayerManager apm = Melody.getAudioPlayerManager();
				AudioPlayer player = controller.getPlayer();
			}
		});
	}
	
//	@Override
//	public void performCommand(Member member, TextChannel channel, Message message, Guild guild, GuildEntity guildentity) {
//		String[] args = message.getContentDisplay().split(" ");
//		if(args.length > 1) {
//			GuildVoiceState state;
//			VoiceChannel vc;
//			if((state = member.getVoiceState()) != null && (vc = state.getChannel()) != null) {
//				MusicController controller = melody._playerManager.getController(guild.getIdLong());
//				melody._playerManager.setAnounceChannelID(guild.getIdLong(), channel.getIdLong());
//				AudioPlayerManager apm = melody._audioPlayerManager;
//				AudioManager manager = guild.getAudioManager();
//				StringBuilder strBuilder = new StringBuilder();
//				for (int i = 1; i < args.length; i++) strBuilder.append(args[i] + " ");
//				String url = strBuilder.toString().trim();
//				boolean isytsearch = false;
//				if(!url.startsWith("http")) {
//					url = "ytsearch: " + url;
//					isytsearch = true;
//				}
//				Service service;
//				if((service = MusicUtil.isDomainVerified(url)) != null || isytsearch) {
//					if(isytsearch) {
//						service = Service.YOUTUBE;
//					}
//					switch(service) {
//						case SPOTIFY:
//							SpotifyApi spotify = new SpotifyApi.Builder()
//							.setAccessToken(Melody.INSTANCE._spotifyutils.getToken())
//							.build();
//							if(url.toLowerCase().startsWith("https://open.spotify.com/track/")){
//								String[] urlid = url.split("/");
//								String id = urlid[4].substring(0, 22);
//								
//								final GetTrackRequest TrackRequest = spotify.getTrack(id).build();
//								try {
//									final CompletableFuture<Track> trackFuture = TrackRequest.executeAsync();
//									final Track track = trackFuture.join();
//									
//									manager.openAudioConnection(vc);
//									final String uri = "ytsearch: " + track.getName() + " "+track.getArtists()[0].getName();
//									final Image[] images = track.getAlbum().getImages();
//									apm.loadItem(uri, new AudioLoadResult(controller, uri, service, member, images[1].getUrl()));
//								}catch (CompletionException e) {
//									System.out.println("Error: " + e.getCause().getMessage());
//								}catch (CancellationException e) {
//									System.out.println("Async operation cancelled.");
//								}
//							}
//							return;
//						default:
//							break;
//					}
//					manager.openAudioConnection(vc);
//					final String uri = url;
//					apm.loadItem(uri, new AudioLoadResult(controller, uri, service, member));
//				}else {
//					Messenger.sendErrorMessage(channel, new ErrorMessageBuilder().setMessageFormat(guild, "music.non-whitelisted-domain", Utils.getDomain(url)));
//				}					
//			}else {
//				Messenger.sendErrorMessage(channel, new ErrorMessageBuilder().setMessageFormat(guild, "music.user-not-in-vc"));	
//			}	
//		}else {
//			Messenger.sendErrorMessage(channel, new ErrorMessageBuilder().setMessageFormat(guild, "info.command-usage", getCommandPrefix()[0]+" <url/search query>"));	
//		}
//	}

}
