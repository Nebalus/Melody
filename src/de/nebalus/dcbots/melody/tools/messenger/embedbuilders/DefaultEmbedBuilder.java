package de.nebalus.dcbots.melody.tools.messenger.embedbuilders;

import de.nebalus.dcbots.melody.core.constants.Settings;
import net.dv8tion.jda.api.EmbedBuilder;

public class DefaultEmbedBuilder extends EmbedBuilder
{
	
	public DefaultEmbedBuilder()
	{
		setColor(Settings.EMBED_COLOR);
	}
	
}
