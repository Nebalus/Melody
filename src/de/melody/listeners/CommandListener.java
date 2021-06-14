package de.melody.listeners;

import java.util.List;

import de.melody.ConsoleLogger;
import de.melody.Melody;
import de.melody.speechpackets.MessageFormatter;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
public class CommandListener extends ListenerAdapter{

	private Melody melody = Melody.INSTANCE;
	private MessageFormatter mf = melody.getMessageFormatter();
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		String message = event.getMessage().getContentDisplay();
		if(event.isFromType(ChannelType.TEXT) || event.isFromType(ChannelType.PRIVATE) ) {
			List<User> MentionedUsers = event.getMessage().getMentionedUsers();
			TextChannel channel = event.getTextChannel();
			Guild guild = event.getGuild();
			String prefix = "/";
			
			if(!event.getAuthor().isBot()) {
				if(MentionedUsers.contains(channel.getJDA().getSelfUser())) {
					event.getChannel().sendMessage(mf.format(guild.getIdLong(), "feedback.info.prefix",prefix)).queue();
				}
			}
		}else if(event.isFromType(ChannelType.PRIVATE)) {
			ConsoleLogger.info("Test", message);
		}
	}
	/*
	  @Override
	    public void onSlashCommand(SlashCommandEvent event)
	    {
	        // Only accept commands from guilds
	        if (event.getGuild() == null)
	            return;
	        switch (event.getName()){
	        case "test":
	        	event.reply("test").queue();
	            break;
	        default:
	            event.reply("I can't handle that command right now :(").setEphemeral(true).queue();
	        }
	    }
	    */
	  @Override
	   public void onSlashCommand(SlashCommandEvent event) {
		  if(event.isFromGuild()) {
			  if(melody.getCmdMan().getCommandData().containsKey(event.getName())) {
				  melody.getCmdMan().performSlash(event); 
			  }
		  }
	   }

	   @Override
	   public void onButtonClick(ButtonClickEvent event) {
	      
	   }
}
	


