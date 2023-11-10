package old.de.nebalus.dcbots.melody.tools.messenger;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import old.de.nebalus.dcbots.melody.core.constants.Melody;
import old.de.nebalus.dcbots.melody.core.constants.Settings;
import old.de.nebalus.dcbots.melody.core.constants.Url;
import old.de.nebalus.dcbots.melody.tools.messenger.embedbuilders.RemasteredEmbedBuilder;
import old.de.nebalus.dcbots.melody.tools.messenger.embedbuilders.RemasteredEmbedData;

public class Messenger {

//	public static MessageEmbed getMessageEmbed(String message)
//	{
//		return getNewEmbedBuilder().setDescription(message).build();
//	}
//
//	public static EmbedBuilder getNewEmbedBuilder()
//	{
//		EmbedBuilder builder = new EmbedBuilder();
//		builder.setColor(Settings.EMBED_COLOR);
//		return builder;
//	}
//
	public static RemasteredEmbedData generateErrorMessage(Language lang, String formatid, Object... args) {
		final RemasteredEmbedBuilder reb = new RemasteredEmbedBuilder();
		final String headerid = "error." + formatid + ".header";
		final String bodyid = "error." + formatid + ".body";

		reb.setHeader(Melody.formatMessage(lang, headerid, args));
		reb.setBody(Melody.formatMessage(lang, bodyid, args));
		reb.setFooter(formatid.replace(".", " > ").toUpperCase());
		reb.setColorScheme(Settings.ERROR_EMBED_COLOR);
		reb.enableColorLine();
		reb.setThumbnail(Url.ERROR_ICON.toString());
		// Color.decode("#C6B0FF")

		return reb.build();
	}

	public static void sendErrorMessage(SlashCommandInteractionEvent event, String formatid, Object... args) {
		if (event.isFromGuild()) {
			final Guild guild = event.getGuild();
			sendInteractionMessage(event,
					generateErrorMessage(Melody.getGuildEntity(guild).getLanguage(), formatid, args)
							.getAsMessageCreateData(),
					true);
		} else {
			sendInteractionMessage(event,
					generateErrorMessage(Settings.DEFAULT_LANGUAGE, formatid, args).getAsMessageCreateData(), true);
		}

	}

	public static void sendInteractionMessage(SlashCommandInteractionEvent event, MessageCreateData mcd,
			boolean isephemeral) {
		final InteractionHook hook = event.getHook();

		if (!event.isAcknowledged()) {
			event.deferReply(isephemeral).queue();
		}

		hook.sendMessage(mcd).queue();
	}

	public static void sendInteractionMessage(SlashCommandInteractionEvent event, RemasteredEmbedData red,
			boolean isephemeral) {
		sendInteractionMessage(event, red.getAsMessageCreateData(), isephemeral);
	}

	public static void sendInteractionMessage(SlashCommandInteractionEvent event, String text, boolean isephemeral) {
		final InteractionHook hook = event.getHook();

		if (!event.isAcknowledged()) {
			event.deferReply(isephemeral).queue();
		}

		hook.sendMessage(text).queue();
	}

	public static void sendInteractionMessage(SlashCommandInteractionEvent event, MessageEmbed me,
			boolean isephemeral) {
		final InteractionHook hook = event.getHook();

		if (!event.isAcknowledged()) {
			event.deferReply(isephemeral).queue();
		}

		hook.sendMessageEmbeds(me).queue();
	}

	public static void sendInteractionMessageFormat(SlashCommandInteractionEvent event, String text,
			boolean isephemeral, Object... args) {
		final InteractionHook hook = event.getHook();

		if (!event.isAcknowledged()) {
			event.deferReply(isephemeral).queue();
		}

		hook.sendMessageFormat(text, args).queue();
	}
//
//	public static void sendMessage(SlashCommandInteractionEvent event, MessageCreateBuilder mcb, boolean isephemeral)
//	{
//		final InteractionHook hook = event.getHook();
//
//		if(!event.isAcknowledged())
//		{
//			event.deferReply(isephemeral).queue();
//		}
//
//		hook.sendMessage(mcb.build()).queue();
//	}
//
//	public static void sendMessage(SlashCommandInteractionEvent event, RemasterdEmbedBuilder cleb, boolean isephemeral)
//	{
//		final InteractionHook hook = event.getHook();
//
//		if(!event.isAcknowledged())
//		{
//			event.deferReply(isephemeral).queue();
//		}
//
//		final MessageCreateBuilder builder = new MessageCreateBuilder();
//
//		builder.setEmbeds(cleb.build());
//
//		if(cleb.isColorLineEnabled())
//		{
//			builder.setFiles(FileUpload.fromData(cleb.getImageFile(), "colorline.png"));
//		}
//
//		hook.sendMessage(builder.build()).setEphemeral(isephemeral).queue();
//	}
//
//	public static void sendMessage(SlashCommandInteractionEvent event, RemasterdEmbedBuilder cleb)
//	{
//		sendMessage(event, cleb, false);
//	}
}
