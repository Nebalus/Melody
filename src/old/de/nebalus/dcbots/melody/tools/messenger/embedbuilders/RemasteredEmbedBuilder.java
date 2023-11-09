package old.de.nebalus.dcbots.melody.tools.messenger.embedbuilders;

import java.awt.Color;

import javax.annotation.Nullable;

public class RemasteredEmbedBuilder {
	private final Color DEFAULT_COLOR_SCHEME = new Color(47, 49, 54);

	private String HEADER_TEXT = null;
	private String BODY_TEXT = null;
	private String FOOTER_TEXT = null;
	private String THUMBNAIL_URL = null;
	private Color COLOR_SCHEME = DEFAULT_COLOR_SCHEME;
	private boolean ISCOLORLINEENABLED = false;

	public RemasteredEmbedBuilder() {
	}

	public RemasteredEmbedBuilder setHeader(@Nullable String header) {
		HEADER_TEXT = header;
		return this;
	}

	public RemasteredEmbedBuilder setBody(@Nullable String body) {
		BODY_TEXT = body;
		return this;
	}

	public RemasteredEmbedBuilder setFooter(@Nullable String footer) {
		FOOTER_TEXT = footer;
		return this;
	}

	public RemasteredEmbedBuilder setThumbnail(@Nullable String url) {
		THUMBNAIL_URL = url;
		return this;
	}

	public RemasteredEmbedBuilder setColorScheme(@Nullable Color color) {
		COLOR_SCHEME = color;
		return this;
	}

	public RemasteredEmbedBuilder enableColorLine() {
		ISCOLORLINEENABLED = true;
		return this;
	}

	public RemasteredEmbedBuilder disableColorLine() {
		ISCOLORLINEENABLED = false;
		return this;
	}

	public RemasteredEmbedBuilder clear() {
		HEADER_TEXT = null;
		BODY_TEXT = null;
		FOOTER_TEXT = null;
		THUMBNAIL_URL = null;
		COLOR_SCHEME = DEFAULT_COLOR_SCHEME;
		ISCOLORLINEENABLED = false;

		return this;
	}

	public RemasteredEmbedData build() {
		return new RemasteredEmbedData(HEADER_TEXT, BODY_TEXT, FOOTER_TEXT, THUMBNAIL_URL, COLOR_SCHEME, ISCOLORLINEENABLED);
	}
}
