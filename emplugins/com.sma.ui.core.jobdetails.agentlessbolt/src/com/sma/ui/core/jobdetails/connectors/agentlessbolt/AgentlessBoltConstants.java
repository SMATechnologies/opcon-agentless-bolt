package com.sma.ui.core.jobdetails.connectors.agentlessbolt;

import com.sma.ui.core.jobdetails.connectors.agentlessbolt.enums.AgentlessBoltEnums;
import com.sma.ui.core.widgets.base.ComboItem;

public interface AgentlessBoltConstants {

	int COMMAND_LINE_LIMIT = 4000;

	String INVALID_COMMAND_LINE = "Invalid command line, please go back to the WINDOWS details to fix the command line.";
	String TOO_LONG_COMMAND_LINE = "Invalid command line, total length exceeds " + COMMAND_LINE_LIMIT + " characters.";
	String PARSE_JOB_COMMAND_ERROR = "Cannot parse the command line, this does not look like a valid {0}.";
	String TEXTBOX_CANNOT_BE_EMPTY = "{0} cannot be empty.";
	String OTHER_OPTIONS = "Other Options";

	interface AgentlessBoltComboItems {
		
		public ComboItem [] TASKS = new ComboItem[] { new ComboItem ("Remote Command", AgentlessBoltEnums.Task.command), 
				new ComboItem ("Exec. Script on Remote", AgentlessBoltEnums.Task.script),
				new ComboItem ("Exec. Task on Remote", AgentlessBoltEnums.Task.task),
				new ComboItem ("Upload File to Remote", AgentlessBoltEnums.Task.upload), 
				new ComboItem ("unknown", AgentlessBoltEnums.Task.unknown)};

	}
	
	interface CommandLineArguments {
		
		public static final String CommandRun = "command run";
		public static final String ScriptRun = "script run";
		public static final String FileUpload = "file upload";
		public static final String TaskRun = "task run";
		
		public static final String TargetsArgument = "--targets";
		public static final String UserArgument = "--user";
		public static final String PasswordArgument = "--password";
		public static final String RunAsArgument = "--run-as";
		public static final String SudoPasswordArgument = "--sudo-password";
		
		public static final String TargetWindows = "winrm://";
		public static final String TargetNoSSL = "--no-ssl";
		public static final String TargetNoHostKeyCheck = "--no-host-key-check";

	}
	
}
