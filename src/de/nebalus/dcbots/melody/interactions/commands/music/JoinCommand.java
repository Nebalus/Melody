package de.nebalus.dcbots.melody.interactions.commands.music;

import de.nebalus.dcbots.melody.core.Melody;
import de.nebalus.dcbots.melody.core.constants.Build;
import de.nebalus.dcbots.melody.tools.audioplayer.AudioController;
import de.nebalus.dcbots.melody.tools.cmdbuilder.PermissionGroup;
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

public class JoinCommand extends SlashCommand
{

	public JoinCommand()
	{
		super("join");
		setDescription("Let " + Build.NAME + " join the voicechat your in.");
		setPermissionGroup(PermissionGroup.DEVELOPER);
		
		setExecuter(new SlashInteractionExecuter() 
		{
			@Override
			public void executeGuild(Member member, MessageChannel channel, Guild guild, GuildEntity guildentity, SlashCommandInteractionEvent event)
			{
				final GuildVoiceState mstate;
				final AudioChannel mac;
				
				if((mstate = member.getVoiceState()) == null || (mac = mstate.getChannel()) == null) 
				{
					Messenger.sendErrorMessage(event, "music.user-not-in-vc");	
					return;
				}
				
				AudioController controller = Melody.getMusicManager().getController(guild.getIdLong());
				
				controller.join(mac);
				Messenger.sendInteractionMessage(event, Melody.formatMessage(guild, "music.bot-join-vc", mac.getName()), false);				
			}
		});
	}
}
