package de.melody.listeners;

import java.util.List;
import java.util.concurrent.TimeUnit;

import de.melody.core.Melody;
import de.melody.entities.GuildEntity;
import de.melody.speechpackets.MessageFormatter;
import de.melody.utils.Emoji;
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
				GuildEntity ge = melody.entityManager.getGuildEntity(guild);
				if(message.startsWith(ge.getPrefix())) {
					int count = 0;
					for (int i = 0; i < ge.getPrefix().length(); i++) {
						count++;
					}
					String[] args = message.substring(count).split(" ");
					if(ge.canRevokeCommand()) {
						event.getMessage().delete().queueAfter(5, TimeUnit.SECONDS);
					}
					if(args.length > 0){
						if(!melody.getCmdMan().performServer(args[0], event.getMember(), channel, event.getMessage(), event.getGuild())) {	
							channel.sendMessage(event.getJDA().getEmoteById(Emoji.ANIMATED_THINKING_EMOJI).getAsMention()+" "+mf.format(guild, "feedback.info.unknown-command",ge.getPrefix())).queue();
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
	    	if(!melody.getCmdMan().performServerSlash(event.getName(), event.getMember(), event.getChannel(), event.getGuild(), event)) {	
	    		event.reply("I can't handle that command right now :(").setEphemeral(true).queue();
			}
	    }
	}
}
	


