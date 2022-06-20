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
		
		final Language defaultlang = Language.ENGLISH;
		
		if(event.isFromGuild())
		{
			final Guild g = event.getGuild();
			
			cleb.setHeader(Melody.formatMessage(g, headerid, args));
			cleb.setBody(Melody.formatMessage(g, bodyid, args));
		}
		else
		{
			cleb.setHeader(Melody.formatMessage(defaultlang, headerid, args));
			cleb.setBody(Melody.formatMessage(defaultlang, bodyid, args));
		}
		
		cleb.setFooter(formatid.replace(".", " > ").toUpperCase());
		cleb.setColor(Settings.ERROR_EMBED_COLOR);
		cleb.setThumbnail(Url.ERROR_ICON.toString());
		//Color.decode("#C6B0FF")
		
		sendReworkMessage(event, cleb, true);
	}
	
	public static void sendReworkMessage(SlashCommandInteractionEvent event, ColorLineEmbedBuilder cleb, boolean ephemeral) 
	{
		if(cleb.isColorLineEnabled())
		{
			event.replyEmbeds(cleb.build()).setEphemeral(ephemeral).queue((picture) -> 
			{
				picture.editOriginal(cleb.getImageFile(), "colorline.png").queue();
			});
		}
		else
		{
			event.replyEmbeds(cleb.build()).setEphemeral(ephemeral).queue();
		}
	}
	
	public static void sendReworkMessage(SlashCommandInteractionEvent event, ColorLineEmbedBuilder cleb) 
	{
		sendReworkMessage(event, cleb, false);
	}
}
