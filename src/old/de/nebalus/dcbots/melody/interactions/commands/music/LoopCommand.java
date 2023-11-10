package old.de.nebalus.dcbots.melody.interactions.commands.music;

import java.util.ArrayList;

import net.dv8tion.jda.api.interactions.commands.Command.Choice;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import old.de.nebalus.dcbots.melody.tools.audioplayer.enums.LoopMode;
import old.de.nebalus.dcbots.melody.tools.cmdbuilder.SlashCommand;

public class LoopCommand extends SlashCommand {

	public LoopCommand() {
		super("loop");
		setDescription("Cycles through all three loop modes (queue, song, off)");

		ArrayList<Choice> loopchoices = new ArrayList<>();
		for (LoopMode loopmode : LoopMode.values()) {
			loopchoices.add(new Choice(loopmode.getTextFormat(), loopmode.getTextFormat()));
		}
		addOption(new OptionData(OptionType.STRING, "loopmode", "Enter a new loopmode").setRequired(false)
				.addChoices(loopchoices));
	}

}
