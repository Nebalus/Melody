package de.melody.utils;

import java.awt.Color;

import de.melody.core.Constants;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

public class Messenger {

	public static MessageAction sendMessageEmbed(TextChannel channel, String message) {
		EmbedBuilder builder = new EmbedBuilder();
		builder.setColor(Constants.EMBEDCOLOR);
		builder.setDescription(message);
		return channel.sendMessageEmbeds(builder.build());
	}
	
	public static MessageAction sendMessageEmbed(TextChannel channel, String message, Color color) {
		EmbedBuilder builder = new EmbedBuilder();
		builder.setColor(color);
		builder.setDescription(message);
		return channel.sendMessageEmbeds(builder.build());
	}
	
	public static MessageAction sendMessageEmbed(TextChannel channel, EmbedBuilder builder) {
		builder.setColor(Constants.EMBEDCOLOR);
		return channel.sendMessageEmbeds(builder.build());
	}
	
	public static MessageAction sendMessageEmbed(TextChannel channel, EmbedBuilder builder, Color color) {
		builder.setColor(color);
		return channel.sendMessageEmbeds(builder.build());
	}
	
	public static MessageEmbed getMessageEmbed(Guild guild, String message) {
		EmbedBuilder builder = new EmbedBuilder();
		builder.setColor(Constants.EMBEDCOLOR);
		builder.setDescription(message);
		return builder.build();
	}
	
	public static MessageEmbed getMessageEmbed(Guild guild, EmbedBuilder builder) {
		builder.setColor(Constants.EMBEDCOLOR);
		return builder.build();
	}
	
	public static MessageAction sendMessage(TextChannel channel, String message) {
		return channel.sendMessage(message);
	}
	
	public static MessageAction sendMessage(TextChannel channel, EmbedBuilder builder) {
		return channel.sendMessage(builder.build().getDescription());
	}
	
	public class ErrorMessageBuilder{
		
		
		public ErrorMessageBuilder() {
			
		}
		
		
	}
}
