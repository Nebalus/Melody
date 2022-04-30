package de.nebalus.dcbots.melody.tools.messenger;

import de.nebalus.dcbots.melody.core.Melody;
import de.nebalus.dcbots.melody.core.constants.Build;
import de.nebalus.dcbots.melody.core.constants.Settings;
import de.nebalus.dcbots.melody.tools.datamanager.FileResource;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class Messenger {
	
	public static MessageEmbed getMessageEmbed(String message) {
		EmbedBuilder builder = new EmbedBuilder();
		builder.setColor(Settings.EMBED_COLOR);
		builder.setDescription(message);
		return builder.build();
	}
	
	public static MessageEmbed getMessageEmbed(EmbedBuilder builder) {
		builder.setColor(Settings.EMBED_COLOR);
		return builder.build();
	}
	
	public static void sendErrorMessage(SlashCommandInteractionEvent event, ErrorMessageBuilder message, boolean ephemeral) {
		event.replyEmbeds(message.build()).setEphemeral(ephemeral).queue((picture)->{
			picture.editOriginal(FileResource.IMG_ERRORLINE.getFile(), "error-line.png").queue();
		});
	}
	
	public static class ErrorMessageBuilder{
		private String HEADER_TEXT;
		private String BODY_TEXT;
		private String FOOTER_TEXT;
		
		private final String PREFIX_FOOTER_TEXT = "· "+Build.VERSION+" · ";
		
		public ErrorMessageBuilder() {}
		
		public ErrorMessageBuilder setMessageFormat(Guild g, String formatid, Object... args) {
			MessageFormatter mf = Melody.INSTANCE.messageformatter;
			HEADER_TEXT = mf.format(g, "error."+formatid+".header",args);
			BODY_TEXT = mf.format(g, "error."+formatid+".body",args);
			FOOTER_TEXT = formatid.replace(".", " > ").toUpperCase();
			return this;
		}
		
		public ErrorMessageBuilder setHeader(String header) {
			HEADER_TEXT = header;
			return this;
		}
		
		public ErrorMessageBuilder setBody(String header) {
			BODY_TEXT = header;
			return this;
		}
		
		public ErrorMessageBuilder setFooter(String header) {
			FOOTER_TEXT = header;
			return this;
		}
		
		public MessageEmbed build() {
			EmbedBuilder builder = new EmbedBuilder();
			if(FOOTER_TEXT != null) {
				FOOTER_TEXT = PREFIX_FOOTER_TEXT + FOOTER_TEXT;
			}else {
				FOOTER_TEXT = PREFIX_FOOTER_TEXT;
			}
			builder.setFooter(FOOTER_TEXT);
			builder.setDescription(BODY_TEXT);
			builder.setImage("attachment://error-line.png");
			if(HEADER_TEXT != null) {
				HEADER_TEXT = "> **"+HEADER_TEXT+"**";
				builder.setTitle(HEADER_TEXT);
			}
			builder.setColor(Settings.ERROR_EMBED_COLOR);
			return builder.build();
		}
	}
}
