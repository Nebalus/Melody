package de.nebalus.dcbots.melody.listeners;

import de.nebalus.dcbots.melody.core.Melody;
import de.nebalus.dcbots.melody.core.constants.Url;
import de.nebalus.dcbots.melody.tools.entitymanager.entitys.GuildEntity;
import de.nebalus.dcbots.melody.tools.messenger.Messenger;
import de.nebalus.dcbots.melody.tools.messenger.Messenger.ErrorMessageBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public final class CommandListener extends ListenerAdapter {
	
	private Melody melody = Melody.INSTANCE;
	//private MessageFormatter mf = melody.messageformatter;
	
	@Override
	public void onSlashCommandInteraction(SlashCommandInteractionEvent event)
	{
		if (event.isFromGuild()) 
		{
			Guild guild = event.getGuild();
			GuildEntity ge = melody.entityMan.getGuildEntity(guild);
			if(!ge.isRateLimited()) 
			{
				ge.addRateRequest();
				try 
				{
			    	if(!melody.cmdMan.performServer(ge, event)) 
			    	{	
			    		event.reply("I can't handle that command right now :(").setEphemeral(true).queue();
					}
				}
				catch(InsufficientPermissionException e) 
				{
					Messenger.sendErrorMessage(event, new ErrorMessageBuilder().setMessageFormat(event.getGuild(), "bot-no-permmisions", e.getPermission()), true);
				}
			}
			else 
			{
				if(!ge.ratelimitmsgsend) 
				{
					ge.ratelimitmsgsend = true;
					Messenger.sendErrorMessage(event, new ErrorMessageBuilder().setMessageFormat(guild, "info.ratelimit", Melody.getConfig()._ratelimitmaxrequests, Melody.getConfig()._ratelimititerationduration), true);
				}
			}
	    }
		else 
	    {
	    	event.reply("My commands only work in a guild where im in :( \n" + Url.INVITE.toString()).setEphemeral(true).queue();
	    }
	}
	
//	@Override
//	public void onMessageReceived(MessageReceivedEvent event) {
//		String message = event.getMessage().getContentDisplay();
//		if(event.isFromType(ChannelType.TEXT)) {
//			if(!event.getAuthor().isBot()) {
//				List<User> MentionedUsers = event.getMessage().getMentionedUsers();
//				TextChannel channel = event.getTextChannel();
//				Guild guild = event.getGuild();
//				GuildEntity ge = melody.entityMan.getGuildEntity(guild);
//				if(!ge.isRateLimited()) {
//					if(message.startsWith(ge.getPrefix())) {
//						
//						ge.addRateRequest();
//						
//						int count = 0;
//						for (int i = 0; i < ge.getPrefix().length(); i++) {
//							count++;
//						}
//						String[] args = message.substring(count).split(" ");
//						if(args.length > 0){
//							try {
//								if(!melody.cmdMan.performServer(args, event.getMember(), channel, event.getMessage(), event.getGuild(), ge)) {	
//									Messenger.sendErrorMessage(channel, new ErrorMessageBuilder().setMessageFormat(guild, "info.unknown-command"));
//								}
//							}catch(InsufficientPermissionException e) {
//								Messenger.sendErrorMessage(channel, new ErrorMessageBuilder().setMessageFormat(guild, "bot-no-permmisions", e.getPermission()));
//							}
//						}
//					}else if(MentionedUsers.contains(channel.getJDA().getSelfUser())) {
//						ge.addRateRequest();
//						channel.sendMessage(mf.format(guild, "feedback.info.prefix", ge.getPrefix())).queue();
//					}
//				}else {
//					if(!ge.ratelimitmsgsend) {
//						ge.ratelimitmsgsend = true;
//						Messenger.sendErrorMessage(channel, new ErrorMessageBuilder().setMessageFormat(guild, "info.ratelimit", Melody.getConfig()._ratelimitmaxrequests, Melody.getConfig()._ratelimititerationduration));
//					}
//				}
//			}
//		}
//	}
}
