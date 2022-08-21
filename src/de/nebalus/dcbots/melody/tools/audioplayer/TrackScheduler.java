package de.nebalus.dcbots.melody.tools.audioplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import de.nebalus.dcbots.melody.core.Melody;
import de.nebalus.dcbots.melody.tools.audioplayer.enums.LoopMode;
import de.nebalus.dcbots.melody.tools.entitymanager.entitys.GuildEntity;
import net.dv8tion.jda.api.entities.Guild;

public final class TrackScheduler extends AudioEventAdapter 
{
	
	@Override
	public final void onPlayerPause(AudioPlayer player) 
	{
		
	}

	@Override
	public final void onPlayerResume(AudioPlayer player) 
	{
		
	}

	@Override
	public final void onTrackStart(AudioPlayer player, AudioTrack track) 
	{
		final long guildid = Melody.getMusicManager().getGuildIdByPlayerHash(player.hashCode());
		final Guild guild = Melody.getGuildById(guildid);
		final AudioController controller = Melody.getMusicManager().getController(guildid);
		final GuildEntity guildentity = Melody.getEntityManager().getGuildEntity(guild);
		final Queue queue = controller.getQueue();
		
		if(controller.getLoopMode().equals(LoopMode.NONE) && guildentity.canAnnounceSongs()) 
		{
			
		}
	}

	@Override
	public final void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endreason) 
	{
		final long guildid = Melody.getMusicManager().getGuildIdByPlayerHash(player.hashCode());
		final AudioController acontroller = Melody.getMusicManager().getController(guildid);
		final Guild guild = Melody.getGuildById(guildid);
		final Queue queue = acontroller.getQueue();
		
		if(endreason.mayStartNext)
		{
			
		}
	}

	@Override
	public final void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {}

	@Override
	public final void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception)
	{

	}
}