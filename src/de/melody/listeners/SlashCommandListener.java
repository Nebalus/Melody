package de.melody.listeners;

import de.melody.Melody;
import de.melody.speechpackets.MessageFormatter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class SlashCommandListener extends ListenerAdapter{

	private Melody melody = Melody.INSTANCE;
	private MessageFormatter mf = melody.getMessageFormatter();
	
	@Override
	public void onSlashCommand(SlashCommandEvent event){
		if (event.isFromGuild()) {
			Guild guild = event.getGuild();
	    	switch (event.getName()){
	    	case "prefix":
	        		event.reply(mf.format(guild.getIdLong(), "feedback.info.prefix",melody.entityManager.getGuildEntity(guild.getIdLong()).getPrefix())).queue();
	        	break;        	
	        default:
	            event.reply("I can't handle that command right now :(").setEphemeral(true).queue();
	        }
	    }
	}
}
