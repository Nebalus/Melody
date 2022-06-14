package de.nebalus.dcbots.melody.tools.messenger;

import java.awt.Color;

import de.nebalus.dcbots.melody.core.Melody;
import de.nebalus.dcbots.melody.core.constants.Settings;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class Messenger 
{
	
	public static MessageEmbed getMessageEmbed(String message) 
	{
		EmbedBuilder builder = new EmbedBuilder();
		builder.setColor(Settings.EMBED_COLOR);
		builder.setDescription(message);
		return builder.build();
	}
	
	public static MessageEmbed getMessageEmbed(EmbedBuilder builder) 
	{
		builder.setColor(Settings.EMBED_COLOR);
		return builder.build();
	}
	
	
	public static void sendErrorMessage(SlashCommandInteractionEvent event, String formatid, Object... args) 
	{
		MessageFormatter mf = Melody.INSTANCE.messageformatter;
		ReworkMessageBuilder rmb = new ReworkMessageBuilder();
		
		final String headerid = "error." + formatid + ".header";
		final String bodyid = "error." + formatid + ".body";
		
		final Language defaultlang = Language.ENGLISH;
		
		if(event.isFromGuild())
		{
			final Guild g = event.getGuild();
			
			rmb.setHeader(mf.format(g, headerid, args));
			rmb.setBody(mf.format(g, bodyid, args));
		}
		else
		{
			rmb.setHeader(mf.format(defaultlang, headerid, args));
			rmb.setBody(mf.format(defaultlang, bodyid, args));
		}
		
		rmb.setFooter(formatid.replace(".", " > ").toUpperCase());
		rmb.setColor(Settings.ERROR_EMBED_COLOR);
		//Color.decode("#C6B0FF")
		
		sendReworkMessage(event, rmb, true);
	}
	
	public static void sendReworkMessage(SlashCommandInteractionEvent event, ReworkMessageBuilder rmb, boolean ephemeral) 
	{
		if(rmb.isColorLineEnabled())
		{
			event.replyEmbeds(rmb.build()).setEphemeral(ephemeral).queue((picture) -> 
			{
				picture.editOriginal(rmb.getImageFile(), "colorline.png").queue();
			});
		}
		else
		{
			event.replyEmbeds(rmb.build()).setEphemeral(ephemeral).queue();
		}
	}
}
