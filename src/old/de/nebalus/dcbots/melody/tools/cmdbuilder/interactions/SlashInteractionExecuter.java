package old.de.nebalus.dcbots.melody.tools.cmdbuilder.interactions;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.PrivateChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import old.de.nebalus.dcbots.melody.tools.entitymanager.entitys.GuildEntity;
import old.de.nebalus.dcbots.melody.tools.entitymanager.entitys.UserEntity;

public class SlashInteractionExecuter {

	public void executeGuild(Member member, MessageChannel channel, Guild guild, GuildEntity guildentity,
			SlashCommandInteractionEvent event) {

	}

	public void executePrivate(User user, PrivateChannel pchannel, UserEntity userentity,
			SlashCommandInteractionEvent event) {

	}

}
