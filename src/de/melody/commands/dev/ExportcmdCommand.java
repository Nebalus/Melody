package de.melody.commands.dev;

import de.melody.core.Melody;
import de.melody.datamanagment.GenerateFile;
import de.melody.entities.GuildEntity;
import de.melody.tools.commandbuilder.CommandManager;
import de.melody.tools.commandbuilder.CommandPermission;
import de.melody.tools.commandbuilder.CommandType;
import de.melody.tools.commandbuilder.Command;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class ExportcmdCommand implements Command {
	
	private final String htmlfile = "<!doctype html>\r\n"
			+ "<html lang=\"en\">\r\n"
			+ "  <head>\r\n"
			+ ""
			+ "    <title>Melody's Commands - A detailed documentation</title>\r\n"
			+ "    \r\n"
			+ "    <meta property=\"og:title\" content=\"Melody's Commands - A detailed documentation\">\r\n"
			+ "    <meta charset=\"utf-8\">\r\n"
			+ "    <meta name=\"application-name\" content=\"Melody Bot\">\r\n"
			+ "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"/>\r\n"
			+ "    <meta name=\"author\" content=\"Nebalus\">\r\n"
			+ "    <meta property=\"og:description\" content=\"Explore Melody's extensive capabilities and learn more about specific commands.\">  \r\n"
			+ "    <meta name=\"theme-color\" content=\"#2EB8BF\">\r\n"
			+ "    <meta property=\"og:image\" content=\"https://melodybot.ga/assets/images/icon.png\">\r\n"
			+ "    <meta name=\"og:image:height\" property=\"og:image:height\" content=\"300\">\r\n"
			+ "    <meta name=\"og:image:width\" property=\"og:image:width\" content=\"300\">\r\n"
			+ "    <meta name=\"og:image:type\" property=\"og:image:type\" content=\"image/png\">\r\n"
			+ "    <meta property=\"og:site_name\" content=\"Melody's Commands\">\r\n"
			+ "    <link rel=\"shortcut icon\" type=\"image/x-icon\" href=\"assets/images/icon.ico\">"
			+ ""
			+ "    <link rel=\"stylesheet\" href=\"assets/css/font-icon.css\"/>\r\n"
			+ "    <link rel=\"stylesheet\" href=\"assets/css/navbar.css\"/>\r\n"
			+ "    <link rel=\"stylesheet\" href=\"assets/css/index.css\"/>\r\n"
			+ "    <link rel=\"stylesheet\" type=\"text/css\" href=\"\">\r\n"
			+ "    <link href=\"https://fonts.googleapis.com/css?family=Open+Sans:300,400,700&display=swap\" rel=\"stylesheet\"/>\r\n"
			+ ""
			+ "    <script src=\"https://kit.fontawesome.com/90a8233232.js\" crossorigin=\"anonymous\"></script>\r\n"
			+ "    <!-- Cloudflare Web Analytics -->\r\n"
			+ "    <script defer src='https://static.cloudflareinsights.com/beacon.min.js' data-cf-beacon='{\"token\": \"c6a4cef6e7f14f58bb4f7b30c09261bf\"}'></script>\r\n"
			+ "    <!-- End Cloudflare Web Analytics -->\r\n"
			+ ""
			+ "    <title>Melody - Commands</title>\r\n"
			+ "   \r\n"
			+ "    <style>\r\n"
			+ "        .cmdwrapper{\r\n"
			+ "            flex-direction: column;\r\n"
			+ "            max-width: 1280px;\r\n"
			+ "            margin-top: 111px;\r\n"
			+ "            padding-left: 1rem;\r\n"
			+ "            padding-right: 3rem;\r\n"
			+ "        }\r\n"
			+ "       \r\n"
			+ "        .cmd{\r\n"
			+ "            margin-top: 1rem;\r\n"
			+ "            padding-left: 1rem;\r\n"
			+ "            padding-right: 1rem;\r\n"
			+ "            padding-top: 1rem;\r\n"
			+ "            padding-bottom: 1rem;\r\n"
			+ "        }\r\n"
			+ "        \r\n"
			+ "        .cmddescription{\r\n"
			+ "            padding-left: 1rem;\r\n"
			+ "            padding-right: 1rem;\r\n"
			+ "            padding-top: 0.1rem;\r\n"
			+ "            padding-bottom: 0.1rem;\r\n"
			+ "        }\r\n"
			+ "        \r\n"
			+ "        main h2{\r\n"
			+ "            margin-top: 0rem;\r\n"
			+ "            margin-bottom: 1rem;\r\n"
			+ "        }\r\n"
			+ "    </style>\r\n"
			+ "  </head>\r\n"
			+ "    \r\n"
			+ "  <body>\r\n"
			+ "   <nav class=\"navbar\">\r\n"
			+ "      <ul class=\"navbar-nav\">\r\n"
			+ "      <li class=\"logo\">\r\n"
			+ "        <a href=\"index.html\" class=\"nav-link\">        \r\n"
			+ "          <span class=\"link-text logo-text\">Melody</span>\r\n"
			+ "          <i class=\"fas fa-angle-double-right fa-primary font-icon\"></i>        \r\n"
			+ "        </a>\r\n"
			+ "      </li>\r\n"
			+ "\r\n"
			+ "      <li class=\"nav-item\">\r\n"
			+ "        <a target=\"popup\" onclick=\"window.open('', 'popup', 'width=430,height=700,scrollbars=no, toolbar=no,status=no,resizable=yes,menubar=no,location=no,directories=no,top=150,left=250')\" href=\"invite.html\" class=\"nav-link\">\r\n"
			+ "             <i class=\"fas fa-plus-circle fa-primary font-icon\"></i>\r\n"
			+ "          <span class=\"link-text\">Add to Discord</span>\r\n"
			+ "        </a>\r\n"
			+ "      </li>\r\n"
			+ "      <li class=\"nav-item\">\r\n"
			+ "        <a href=\"commands.html\" class=\"nav-link\">\r\n"
			+ "          <i class=\"fas fa-terminal fa-primary font-icon\"></i>\r\n"
			+ "          <span class=\"link-text\" href=\"commands.html\">Commands</span>\r\n"
			+ "        </a>\r\n"
			+ "      </li>\r\n"
			+ "      <li class=\"nav-item\">\r\n"
			+ "        <a target=\"popup\" onclick=\"window.open('', 'popup', 'width=430,height=700,scrollbars=no, toolbar=no,status=no,resizable=yes,menubar=no,location=no,directories=no,top=150,left=250')\" href=\"support.html\" class=\"nav-link\">\r\n"
			+ "          <i class=\"fab fa-discord fa-primary font-icon\"></i>\r\n"
			+ "          <span class=\"link-text\">Support</span>\r\n"
			+ "        </a>\r\n"
			+ "        </li>\r\n"
			+ "	  <li class=\"nav-item\">\r\n"
			+ "        <a href=\"https://github.com/Nebalus/Melody\" class=\"nav-link\">\r\n"
			+ "		 <i class=\"fab fa-github fa-primary font-icon\"></i>         \r\n"
			+ "          <span class=\"link-text\">Github</span>\r\n"
			+ "        </a>\r\n"
			+ "        </li>\r\n"
			+ "    </ul>\r\n"
			+ "  </nav>\r\n"
			+ "      <main>\r\n"
			+ "        <div class=\"cmdwrapper margin-center flex-col\">\n"
			+ "            {CONTENT}\n"
			+ "        </div>\r\n"
			+ "    </main>\r\n"
			+ "  </body>\r\n"
			+ "</html>";
	
	@Override
	public void performCommand(Member member, TextChannel channel, Message message, Guild guild, GuildEntity guildentity) {
		channel.sendMessage("Generating commands.html please wait").queue();
		String content = "";
		CommandManager cmdman = Melody.INSTANCE._cmdManager;
		for(Command scmd : cmdman.getRawCommands()) {
			if(!scmd.getMainPermmision().equals(CommandPermission.DEVELOPER)) {
				content = content + addCommand(scmd.getCommandPrefix()[0],scmd.getCommandDescription(),scmd.getCommandPrefix());
			}
		}
		GenerateFile gfile = new GenerateFile("html");
		gfile.writeStringToFile(htmlfile.replace("{CONTENT}", content));
		channel.sendFile(gfile.getFile(), "commands.html").queue();
		
	}
	
	private String addCommand(String cmd, String cmddiscript, String[] prefix) {
		String aliases = null;
		if(prefix != null) {
			try {
				int time = 1;
				for(String cmdp : prefix) {
					if(time != 1) {
						if(time == 2) {
							aliases = cmdp;
						}else {
							aliases = aliases + ", " + cmdp;
						}
					}
					time++;
				}
			}catch (Exception e) {}
		}
		return "<div class=\"cmd w-full bg-darker-gray rounded-lg\"> \n"
				+ "<h2>"+Melody.INSTANCE._config._defaultprefix+cmd+"</h2>  \n"
				+ "<div class=\"cmddescription bg-dark-gray rounded-lg\">  \n"
				+ (cmddiscript != null ? "<p>"+cmddiscript+"</p>" : "")+ "  \n"
				+ (aliases != null ? "<p class=\"opacity-75\">Aliases: "+aliases.toString().replace("[", " ").replace("]", " ")+"</p>" : "")+ " \n"
				+ "</div>\n"
				+ "</div>\n";
	}

	@Override
	public void performSlashCommand(Member member, MessageChannel channel, Guild guild, GuildEntity guildentity, SlashCommandEvent event) {}

	@Override
	public String[] getCommandPrefix() {
		return new String[] {"exportcommands"};
	}

	@Override
	public CommandType getCommandType() {
		return CommandType.CHAT;
	}

	@Override
	public OptionData[] getCommandOptions() {
		return null;
	}

	@Override
	public String getCommandDescription() {
		return "Exports all commands in a HTML format";
	}
	@Override
	public CommandPermission getMainPermmision() {
		return CommandPermission.DEVELOPER;
	}
}
