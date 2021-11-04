package de.melody.listeners;

import java.util.List;
import de.melody.core.Melody;
import de.melody.entities.GuildEntity;
import de.melody.speechpackets.MessageFormatter;
import de.melody.utils.messenger.Messenger;
import de.melody.utils.messenger.Messenger.ErrorMessageBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CommandListener extends ListenerAdapter{

	private Melody melody = Melody.INSTANCE;
	private MessageFormatter mf = melody.getMessageFormatter();
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		String message = event.getMessage().getContentDisplay();
		if(event.isFromType(ChannelType.TEXT)) {
			if(!event.getAuthor().isBot()) {
				List<User> MentionedUsers = event.getMessage().getMentionedUsers();
				TextChannel channel = event.getTextChannel();
				Guild guild = event.getGuild();
				GuildEntity ge = melody.getEntityManager().getGuildEntity(guild);
				if(message.startsWith(ge.getPrefix())) {
					int count = 0;
					for (int i = 0; i < ge.getPrefix().length(); i++) {
						count++;
					}
					String[] args = message.substring(count).split(" ");
					if(args.length > 0){
						if(!melody.getCmdMan().performServer(args[0], event.getMember(), channel, event.getMessage(), event.getGuild(), ge)) {	
							Messenger.sendErrorMessage(channel, new ErrorMessageBuilder().setMessageFormat(guild, "info.unknown-command"));
						}
					}
				}else if(MentionedUsers.contains(channel.getJDA().getSelfUser())) {
					channel.sendMessage(mf.format(guild, "feedback.info.prefix",ge.getPrefix())).queue();
				}
			}
		}
	}
	
	@Override
	public void onSlashCommand(SlashCommandEvent event){
		if (event.isFromGuild()) {
			GuildEntity ge = melody.getEntityManager().getGuildEntity(event.getGuild());
	    	if(!melody.getCmdMan().performServerSlash(event.getName(), event.getMember(), event.getChannel(), event.getGuild(), ge, event)) {	
	    		event.reply("I can't handle that command right now :(").setEphemeral(true).queue();
			}
	    }
	}
}
	


