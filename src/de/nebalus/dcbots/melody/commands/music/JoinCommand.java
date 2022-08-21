package de.nebalus.dcbots.melody.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import de.nebalus.dcbots.melody.core.Melody;
import de.nebalus.dcbots.melody.core.constants.Build;
import de.nebalus.dcbots.melody.tools.audioplayer.AudioController;
import de.nebalus.dcbots.melody.tools.cmdbuilder.PermissionGroup;
import de.nebalus.dcbots.melody.tools.cmdbuilder.SlashCommand;
import de.nebalus.dcbots.melody.tools.cmdbuilder.SlashExecuter;
import de.nebalus.dcbots.melody.tools.entitymanager.entitys.GuildEntity;
import de.nebalus.dcbots.melody.tools.messenger.Messenger;
import net.dv8tion.jda.api.entities.AudioChannel;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;

public class JoinCommand extends SlashCommand
{

	public JoinCommand()
	{
		super("join");
		setDescription("Let " + Build.NAME + " join the audio channel your in.");
		setPermissionGroup(PermissionGroup.DEVELOPER);
		
		setExecuter(new SlashExecuter() 
		{
			@Override
			public void executeGuild(Member member, MessageChannel channel, Guild guild, GuildEntity guildentity, SlashCommandInteractionEvent event, InteractionHook hook)
			{
				GuildVoiceState state;
				AudioChannel ac;
				if((state = member.getVoiceState()) != null && (ac = state.getChannel()) != null) 
				{
					AudioController controller = Melody.getMusicManager().getController(guild.getIdLong());
					AudioPlayer player = controller.getPlayer();
					
					guild.getAudioManager().openAudioConnection(ac);
					hook.sendMessage(Melody.formatMessage(guild, "music.info.bot-join-vc", ac.getName())).setEphemeral(false).queue();
					
					if(player.getPlayingTrack() == null) 
					{
						//controller.setAfkTime(600);
					}
				}
				else
				{
					Messenger.sendErrorMessage(event, "music.user-not-in-vc");	
				}	
			}
		});
	}

}
