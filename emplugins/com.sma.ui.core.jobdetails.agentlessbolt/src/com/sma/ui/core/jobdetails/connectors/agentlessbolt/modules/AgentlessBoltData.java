package com.sma.ui.core.jobdetails.connectors.agentlessbolt.modules;

import java.util.ArrayList;
import java.util.List;

import com.sma.ui.core.jobdetails.connectors.agentlessbolt.enums.AgentlessBoltEnums;

public class AgentlessBoltData {

	private AgentlessBoltEnums.Task boltTask = null;
	private String user = null;
	private String password = null;
	private String runAs = null;
	private String sudoPassword = null;
	private String targets = null;
	private boolean windows = false;
	private boolean ssl = false;
	private boolean noHostKeyCheck = false;
	
	private String command = null;
	private String uploadFileName = null;
	private String destinationFileName = null;

	private String script = null;
	
	private String task = null;

	private String argument = null;
	private List<String> taskArguments = new ArrayList<String>();

	public AgentlessBoltEnums.Task getBoltTask() {
		return boltTask;
	}

	public void setBoltTask(AgentlessBoltEnums.Task boltTask) {
		this.boltTask = boltTask;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRunAs() {
		return runAs;
	}

	public void setRunAs(String runAs) {
		this.runAs = runAs;
	}

	public String getSudoPassword() {
		return sudoPassword;
	}

	public void setSudoPassword(String sudoPassword) {
		this.sudoPassword = sudoPassword;
	}

	public String getTargets() {
		return targets;
	}

	public void setTargets(String targets) {
		this.targets = targets;
	}

	public boolean isWindows() {
		return windows;
	}

	public void setWindows(boolean windows) {
		this.windows = windows;
	}

	public boolean isSsl() {
		return ssl;
	}

	public void setSsl(boolean ssl) {
		this.ssl = ssl;
	}

	public boolean isNoHostKeyCheck() {
		return noHostKeyCheck;
	}

	public void setNoHostKeyCheck(boolean noHostKeyCheck) {
		this.noHostKeyCheck = noHostKeyCheck;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getUploadFileName() {
		return uploadFileName;
	}

	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}

	public String getDestinationFileName() {
		return destinationFileName;
	}

	public void setDestinationFileName(String destinationFileName) {
		this.destinationFileName = destinationFileName;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public String getArgument() {
		return argument;
	}

	public String getTask() {
		return task;
	}

	public void setTask(String task) {
		this.task = task;
	}

	public void setArgument(String argument) {
		this.argument = argument;
	}

	public List<String> getTaskArguments() {
		return taskArguments;
	}

	public void setTaskArguments(List<String> taskArguments) {
		this.taskArguments = taskArguments;
	}

}
