package de.nebalus.dcbots.melody.tools.messenger;

import de.nebalus.dcbots.melody.core.Melody;
import de.nebalus.dcbots.melody.core.constants.Settings;
import de.nebalus.dcbots.melody.core.constants.Url;
import de.nebalus.dcbots.melody.tools.messenger.embedbuilders.ColorLineEmbedBuilder;
import de.nebalus.dcbots.melody.tools.messenger.embedbuilders.DefaultEmbedBuilder;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;

public class Messenger 
{
	
	public static MessageEmbed getMessageEmbed(String message) 
	{
		return getEmbedBuilder(message).build();
	}
	
	public static EmbedBuilder getEmbedBuilder(String message) 
	{
		DefaultEmbedBuilder builder = new DefaultEmbedBuilder();
		builder.setDescription(message);
		return builder;
	}
	
	public static void sendErrorMessage(SlashCommandInteractionEvent event, String formatid, Object... args) 
	{
		ColorLineEmbedBuilder cleb = new ColorLineEmbedBuilder();
		
		final String headerid = "error." + formatid + ".header";
		final String bodyid = "error." + formatid + ".body";
		
		if(event.isFromGuild())
		{
			final Guild g = event.getGuild();
			
			cleb.setHeader(Melody.formatMessage(g, headerid, args));
			cleb.setBody(Melody.formatMessage(g, bodyid, args));
		}
		else
		{
			cleb.setHeader(Melody.formatMessage(Settings.DEFAULT_LANGUAGE, headerid, args));
			cleb.setBody(Melody.formatMessage(Settings.DEFAULT_LANGUAGE, bodyid, args));
		}
		
		cleb.setFooter(formatid.replace(".", " > ").toUpperCase());
		cleb.setColor(Settings.ERROR_EMBED_COLOR);
		cleb.setThumbnail(Url.ERROR_ICON.toString());
		//Color.decode("#C6B0FF")
		
		sendMessage(event, cleb, true);
	}
	
	public static void sendMessage(SlashCommandInteractionEvent event, ColorLineEmbedBuilder cleb, boolean ephemeral) 
	{
		final InteractionHook hook = event.getHook();
		
		if(cleb.isColorLineEnabled())
		{
			hook.sendMessageEmbeds(cleb.build()).setEphemeral(ephemeral).queue();
			hook.editOriginal(cleb.getImageFile(), "colorline.png").queue();
		}
		else
		{
			hook.sendMessageEmbeds(cleb.build()).setEphemeral(ephemeral).queue();
		}
	}
	
	public static void sendMessage(SlashCommandInteractionEvent event, ColorLineEmbedBuilder cleb) 
	{
		sendMessage(event, cleb, false);
	}
//	
//	public static void sendErrorMessage(InteractionHook hook, String formatid, Object... args) 
//	{
//		ColorLineEmbedBuilder cleb = new ColorLineEmbedBuilder();
//		
//		final String headerid = "error." + formatid + ".header";
//		final String bodyid = "error." + formatid + ".body";
//		
//		final Language defaultlang = Language.ENGLISH;
//		
//		final Interaction interaction = hook.getInteraction();
//		
//		if(interaction.isFromGuild())
//		{
//			final Guild g = interaction.getGuild();
//			
//			cleb.setHeader(Melody.formatMessage(g, headerid, args));
//			cleb.setBody(Melody.formatMessage(g, bodyid, args));
//		}
//		else
//		{
//			cleb.setHeader(Melody.formatMessage(defaultlang, headerid, args));
//			cleb.setBody(Melody.formatMessage(defaultlang, bodyid, args));
//		}
//		
//		cleb.setFooter(formatid.replace(".", " > ").toUpperCase());
//		cleb.setColor(Settings.ERROR_EMBED_COLOR);
//		cleb.setThumbnail(Url.ERROR_ICON.toString());
//		//Color.decode("#C6B0FF")
//		
//		sendColorlineMessage(event, cleb, true);
//	}
//	
//	public static void sendColorlineMessage(InteractionHook hook, ColorLineEmbedBuilder cleb, boolean ephemeral) 
//	{
//		if(cleb.isColorLineEnabled())
//		{
//			hook.sendMessageEmbeds(cleb.build()).setEphemeral(ephemeral).queue((picture) -> 
//			{
//				picture.(cleb.getImageFile(), "colorline.png").queue();
//			});
//		}
//		else
//		{
//			event.replyEmbeds(cleb.build()).setEphemeral(ephemeral).queue();
//		}
//	}
//	
//	public static void sendColorlineMessage(InteractionHook hook, ColorLineEmbedBuilder cleb) 
//	{
//		sendColorlineMessage(hook, cleb, false);
//	}
}
