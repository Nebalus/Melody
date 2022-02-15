package de.melody.tools.cmdbuilder;

public class SubCommand {
	
	private final String description;
	private final String prefix;
	private final CommandPermission permission;
	
	protected SubCommand(String prefix, CommandPermission permission) {
		this.description = "What will be here is written in the stars :P";
		this.permission = permission;
		this.prefix = prefix;
	}
	
	public void execute() {
		
	}
}
