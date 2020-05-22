package com.sma.ui.core.jobdetails.connectors.agentlessbolt;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli.ParseException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.sma.core.OpconException;
import com.sma.core.api.constants.ExitCodeAdvancedConstants;
import com.sma.core.api.constants.SystemConstants;
import com.sma.core.api.interfaces.IPersistentJob;
import com.sma.core.api.interfaces.ISpecificJobProperties;
import com.sma.core.api.job.specific.UnixJobProperties;
import com.sma.core.session.ContextID;
import com.sma.core.util.Util;
import com.sma.ui.core.jobdetails.JobUtil;
import com.sma.ui.core.jobdetails.connectors.agentlessbolt.AgentlessBoltConstants.CommandLineArguments;
import com.sma.ui.core.jobdetails.connectors.agentlessbolt.enums.AgentlessBoltEnums;
import com.sma.ui.core.jobdetails.connectors.agentlessbolt.modules.AgentlessBoltData;
import com.sma.ui.core.messages.IMessageDisplayer;
import com.sma.ui.core.widgets.base.CTabFolder2;
import com.sma.ui.core.widgets.base.ComboItem;
import com.sma.ui.core.widgets.base.ItemCombo;
import com.sma.ui.core.widgets.interfaces.IJobDetailEditorContext;
import com.sma.ui.core.widgets.job.ExitCodeAdvancedWidget;
import com.sma.ui.core.widgets.listeners.ControlTokenSelectorListener;
import com.sma.ui.core.widgets.listeners.DirtyModifyAdapter;
import com.sma.ui.core.widgets.listeners.DirtySelectionAdapter;
import com.sma.ui.core.widgets.validation.ValidationMessage;

public class AgentlessBoltUnixJobDetailsWidget extends AbstractAgentlessBoltUnixSubJobDetailsWidget {

	private static final String COMMAND_SUFFIX = SystemConstants.SLASH
			+ "bolt"; //$NON-NLS-1$

	private static final String LOCATION_PROPERTY_NAME = "[[BOLT_UPATH]]";
	private static final String LOCPATH_NAME = "Location";
	private static final String LOCPATH_TOOLTIP = "The name of a global property that contains the location of the Bolt software";
	private static final String TASK_NAME = "Bolt Task";
	private static final String TASK_TOOLTIP = "The task type associated with this Bolt request";

	private Composite _mainComposite;
	private Composite _mainInfoComposite;

	private CTabFolder2 _tabFolder;
	private CTabItem _taskActionTab;
	private Text _locPathText;
	private Label _taskLabel;
	private ItemCombo _taskItemCombo;
	private Composite _taskActionComposite;
	
	private CTabItem _failureCriteriaTab;
	private ExitCodeAdvancedWidget _advancedExitCodeWidget;
	
	private IJobDetailsAction _currentTaskAction;
	private AgentlessBoltData _agentlessBoltData = null;
	
	private IJobDetailEditorContext _iJobDetailEditorContext;
	
	public AgentlessBoltUnixJobDetailsWidget(Composite parent, IMessageDisplayer messageManager, ContextID context, IJobDetailEditorContext iJobDetailEditorContext) {
		super(parent, messageManager, context);

		this._iJobDetailEditorContext = iJobDetailEditorContext;
		
		GridLayout layout = new GridLayout(1, false);
		layout.marginHeight = layout.marginWidth = 0;
		layout.horizontalSpacing = layout.verticalSpacing = 0;
		this.setLayout(layout);
		
		this.createPart(this);
		addListeners();
	}

	private void createPart(Composite parent) {

		_mainComposite = new Composite(parent, SWT.NONE);
		_mainComposite.setLayout(new GridLayout(1, false));
		_mainComposite.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

		_mainInfoComposite = new Composite(_mainComposite, SWT.NONE);
		_mainInfoComposite.setLayout(new GridLayout(2, false));
		_mainInfoComposite.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

		_locPathText = JobUtil.createLabeledText(_mainInfoComposite, LOCPATH_NAME, 0, JobUtil.COLOR_BLUE, JobUtil.COLOR_LIGHT_GREEN, SWT.BORDER, 1);
		_locPathText.setToolTipText(LOCPATH_TOOLTIP);
		
		_taskLabel = new Label(_mainInfoComposite, SWT.TRAIL);
		_taskLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false, 1, 1));
		_taskLabel.setText(TASK_NAME);

		_taskItemCombo = new ItemCombo(_mainInfoComposite, SWT.READ_ONLY | SWT.DROP_DOWN);
		_taskItemCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		_taskItemCombo.setItems(Arrays.asList(AgentlessBoltConstants.AgentlessBoltComboItems.TASKS));
		_taskItemCombo.setToolTipText(TASK_TOOLTIP);

		
		_tabFolder = createTabFolder(_mainComposite);

	}
	
	private CTabFolder2 createTabFolder(Composite parent) {

		_tabFolder = new CTabFolder2(parent, SWT.NONE);
		_tabFolder.setLayout(new GridLayout(1, false));
		_tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		_tabFolder.applyFormStyle();

		// create tabs
		_taskActionTab = createTaskActionTab(_tabFolder);
		_failureCriteriaTab = createFailureCriteriaTab(_tabFolder);

		// show the job details tab
		_tabFolder.setSelection(_taskActionTab);

		return _tabFolder;
	}
	
	private CTabItem createTaskActionTab(CTabFolder tabFolder) {

		_taskActionComposite = new Composite(tabFolder, SWT.NONE);
		_taskActionComposite.setLayout(new StackLayout());
		_taskActionComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		_taskActionTab = JobUtil.createTabItem(tabFolder, _taskActionComposite, "Task Action");
		
		return _taskActionTab;

	}
	
	private CTabItem createFailureCriteriaTab(CTabFolder tabFolder) {

		Composite failureCriteriaComposite = new Composite(tabFolder,	SWT.INHERIT_DEFAULT);
		failureCriteriaComposite.setLayout(new GridLayout(1, false));

		Group failureCriteriaAdvanced = new Group(failureCriteriaComposite, SWT.NONE);
		failureCriteriaAdvanced.setLayout(new GridLayout());
		failureCriteriaAdvanced.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true));
		
		_advancedExitCodeWidget = new ExitCodeAdvancedWidget(failureCriteriaAdvanced, this.getMessageDisplayer(), ExitCodeAdvancedConstants.MINIMUM_ROWS_TO_DISPLAY,
				ExitCodeAdvancedConstants.MAXIMUM_ROWS_TO_DISPLAY);
		_advancedExitCodeWidget.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));


		_failureCriteriaTab = JobUtil.createTabItem(tabFolder, failureCriteriaComposite, "Failure Criteria");
		
		return _failureCriteriaTab;

	}

	private void addListeners() {
		_locPathText.addModifyListener(new DirtyModifyAdapter(this));
		_taskItemCombo.addSelectionListener(new DirtySelectionAdapter(this));

		_advancedExitCodeWidget.addDirtyListener(this);

		_taskItemCombo.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("incomplete-switch")
			public void widgetSelected(final SelectionEvent e) {
				if(_taskItemCombo.getSelectedItem() == null) {
					loadTaskActionWidget(new RunCommandJobDetailsAction(getInput(), getContextID(), getMessageDisplayer()));
					return;
				}
				switch (getSelectedTask()) {

				case command:
					loadTaskActionWidget(new RunCommandJobDetailsAction(getInput(), getContextID(), getMessageDisplayer()));
					break;
					
				case script:
					loadTaskActionWidget(new RunScriptJobDetailsAction(getInput(), getContextID(), getMessageDisplayer()));
					break;

				case task:
					loadTaskActionWidget(new RunTaskJobDetailsAction(getInput(), getContextID(), getMessageDisplayer()));
					break;

				case upload:
					loadTaskActionWidget(new RunFileUploadJobDetailsAction(getInput(), getContextID(), getMessageDisplayer()));
					break;

				}
			}
		});
		
		// add CTRL+T shortcut for global properties
		new ControlTokenSelectorListener(_locPathText, getContextID());
	}	
	
	public void setDefaults() {
		setSendDirtyEvents(false);
		_locPathText.setText(LOCATION_PROPERTY_NAME);
		_taskItemCombo.setEnabled(true);
		_taskItemCombo.setSelection(AgentlessBoltEnums.Task.command, true);
		_taskItemCombo.removeItem(new ComboItem ("unknown", AgentlessBoltEnums.Task.unknown));
		_advancedExitCodeWidget.setDefaults();
		setSendDirtyEvents(true);
	}

	public ValidationMessage doSave(IProgressMonitor monitor, IPersistentJob toSave) throws OpconException {
		ValidationMessage msg = super.doSave(monitor, toSave);
		if (msg != null) {
			return msg;
		}

		final UnixJobProperties unixJob = (UnixJobProperties) toSave.getSpecificJobProperties();
		monitor.beginTask("AgentlessBolt Advanced Failure Criteria Properties", 5);

		unixJob.setExitCodeAdvancedOperators(_advancedExitCodeWidget.getExitCodeAdvancedOperators());
		monitor.worked(1);

		unixJob.setExitCodeAdvancedValues(_advancedExitCodeWidget.getExitCodeAdvancedValues());
		monitor.worked(1);

		unixJob.setExitCodeAdvancedEndValues(_advancedExitCodeWidget.getExitCodeAdvancedEndValues());
		monitor.worked(1);

		unixJob.setExitCodeAdvancedResults(_advancedExitCodeWidget.getExitCodeAdvancedResults());
		monitor.worked(1);

		unixJob.setExitCodeAdvancedComparators(_advancedExitCodeWidget.getExitCodeAdvancedComparators());
		monitor.worked(1);

		monitor.done();

		return null;
	}
	
	private void loadTaskActionWidget(IJobDetailsAction toLoad) {
		if (_currentTaskAction != null && _currentTaskAction.getClass().equals(toLoad.getClass())) {
			//already loaded, avoid reload it and loose data
			_taskActionComposite.layout(true);
			_iJobDetailEditorContext.reLayout();
			return;
		} 

		final StackLayout stackActionLayout = (StackLayout) _taskActionComposite.getLayout();
		_currentTaskAction = toLoad;
		_currentTaskAction.createActionComposite(_taskActionComposite);
		
		if (stackActionLayout.topControl != null) {
			stackActionLayout.topControl.dispose();
		}
		
		stackActionLayout.topControl = _currentTaskAction.getComposite();
		_currentTaskAction.addListeners(AgentlessBoltUnixJobDetailsWidget.this);
		_currentTaskAction.setDefaults();
		if (_agentlessBoltData != null) {
			_currentTaskAction.initializeContents(_agentlessBoltData);
		}
		_taskActionComposite.layout(true);
		_iJobDetailEditorContext.reLayout();
	}
	
	@Override
	protected String getStartImage() {
		AgentlessBoltData data = _currentTaskAction.getContents();
		StringBuilder builder = new StringBuilder();
		builder.append(JobUtil.autoQuote(_locPathText.getText().trim() + COMMAND_SUFFIX, true));
		// add command specific data
		AgentlessBoltEnums.Task task = data.getBoltTask();
		
		switch (task) {
		
		case command:
			// add command
			builder.append(SystemConstants.SPACE);
			builder.append(AgentlessBoltConstants.CommandLineArguments.CommandRun);
			builder.append(SystemConstants.SPACE);
			if(data.getCommand().contains(SystemConstants.QUOTE_SINGLE)) {
				builder.append(SystemConstants.QUOTE);
				builder.append(data.getCommand());
				builder.append(SystemConstants.QUOTE);
			} else {
				builder.append(SystemConstants.QUOTE_SINGLE);
				builder.append(data.getCommand());
				builder.append(SystemConstants.QUOTE_SINGLE);
			}
			break;
			
		case script:
			// add file information
			builder.append(SystemConstants.SPACE);
			builder.append(AgentlessBoltConstants.CommandLineArguments.ScriptRun);
			builder.append(SystemConstants.SPACE);
			builder.append(SystemConstants.QUOTE_SINGLE);
			builder.append(data.getScript());
			builder.append(SystemConstants.QUOTE_SINGLE);
			builder.append(SystemConstants.SPACE);
			if(data.getArgument() != null) {
				builder.append(SystemConstants.QUOTE_SINGLE);
				builder.append(data.getArgument());
				builder.append(SystemConstants.QUOTE_SINGLE);
			}
			break;
		
		case task:
			// add task information
			builder.append(SystemConstants.SPACE);
			builder.append(AgentlessBoltConstants.CommandLineArguments.TaskRun);
			builder.append(SystemConstants.SPACE);
			builder.append(SystemConstants.QUOTE_SINGLE);
			builder.append(data.getTask());
			builder.append(SystemConstants.QUOTE_SINGLE);
			if(!data.getTaskArguments().isEmpty()) {
				for(String argument : data.getTaskArguments()) {
					builder.append(SystemConstants.SPACE);
					builder.append(SystemConstants.QUOTE_SINGLE);
					builder.append(argument);
					builder.append(SystemConstants.QUOTE_SINGLE);
				}
			} else {
				builder.append(SystemConstants.SPACE);
			}
			break;

		case upload:
			// add file information
			builder.append(SystemConstants.SPACE);
			builder.append(AgentlessBoltConstants.CommandLineArguments.FileUpload);
			builder.append(SystemConstants.SPACE);
			builder.append(SystemConstants.QUOTE_SINGLE);
			builder.append(data.getUploadFileName());
			builder.append(SystemConstants.QUOTE_SINGLE);
			builder.append(SystemConstants.SPACE);
			builder.append(SystemConstants.QUOTE_SINGLE);
			builder.append(data.getDestinationFileName());
			builder.append(SystemConstants.QUOTE_SINGLE);
			break;

		case unknown:
			break;

		}

		// add targets
		builder.append(SystemConstants.SPACE);
		builder.append(AgentlessBoltConstants.CommandLineArguments.TargetsArgument);
		builder.append(SystemConstants.SPACE);
		if(data.isWindows()) {
			String wSystems[] = tokenizeParameters(data.getTargets(), false, SystemConstants.COMMA);
			for(String wSystem : wSystems) {
				builder.append(AgentlessBoltConstants.CommandLineArguments.TargetWindows);
				builder.append(wSystem);
				builder.append(SystemConstants.COMMA);
			}
			//remove extra comma
			builder.deleteCharAt(builder.length() - 1);
		} else {
			builder.append(data.getTargets());
			builder.append(SystemConstants.SPACE);
			builder.append(AgentlessBoltConstants.CommandLineArguments.TargetNoHostKeyCheck);
			builder.append(SystemConstants.SPACE);
		}
		if(!data.isSsl()) {
			builder.append(SystemConstants.SPACE);
			builder.append(AgentlessBoltConstants.CommandLineArguments.TargetNoSSL);
		}			
		// add user
		builder.append(SystemConstants.SPACE);
		builder.append(AgentlessBoltConstants.CommandLineArguments.UserArgument);
		builder.append(SystemConstants.SPACE);
		builder.append(data.getUser());
		// add password
		builder.append(SystemConstants.SPACE);
		builder.append(AgentlessBoltConstants.CommandLineArguments.PasswordArgument);
		builder.append(SystemConstants.SPACE);
		builder.append(data.getPassword());
		if(data.getRunAs() != null) {
			// add runas
			builder.append(SystemConstants.SPACE);
			builder.append(AgentlessBoltConstants.CommandLineArguments.RunAsArgument);
			builder.append(SystemConstants.SPACE);
			builder.append(data.getRunAs());
		}
		if(data.getSudoPassword() != null) {
			// add sudopassword
			builder.append(SystemConstants.SPACE);
			builder.append(AgentlessBoltConstants.CommandLineArguments.SudoPasswordArgument);
			builder.append(SystemConstants.SPACE);
			builder.append(data.getSudoPassword());
		}
		return builder.toString();
	}

	@Override
	protected void initializeContents(ISpecificJobProperties jobProperties)
			throws OpconException {
		UnixJobProperties unixDetails = (UnixJobProperties) jobProperties;

		try {
			if (unixDetails != null) {
				// basic check first
				if (!unixDetails.getStartImage().contains(COMMAND_SUFFIX)) {
					MessageFormat.format(AgentlessBoltConstants.PARSE_JOB_COMMAND_ERROR,	"Agentless (Bolt)");
				}
				_agentlessBoltData = parseStartImage(unixDetails.getStartImage());
				if(_agentlessBoltData.getBoltTask() == AgentlessBoltEnums.Task.command) {
					loadTaskActionWidget(new RunCommandJobDetailsAction(getInput(), getContextID(), getMessageDisplayer()));
				} else if(_agentlessBoltData.getBoltTask() == AgentlessBoltEnums.Task.upload) {
					loadTaskActionWidget(new RunFileUploadJobDetailsAction(getInput(), getContextID(), getMessageDisplayer()));
				} else if(_agentlessBoltData.getBoltTask() == AgentlessBoltEnums.Task.script) {
					loadTaskActionWidget(new RunScriptJobDetailsAction(getInput(), getContextID(), getMessageDisplayer()));
				}
				_advancedExitCodeWidget.initializeContents();
				if (getInput() != null && getInput().getSpecificJobProperties() != null) {
					try {
						UnixJobProperties _jobProperties = (UnixJobProperties) getInput().getSpecificJobProperties();
						_advancedExitCodeWidget.setInput(_jobProperties.getExitCodeAdvancedRows());
					} catch (OpconException e) {
						setErrorMessage("Error initializing user selector " + Util.getCauseError(e));
					}
				}
			} else {
				setDefaults();
			}
		} catch (ParseException e) {
			throw new OpconException(e);
		}
		
	}
	
	private AgentlessBoltData parseStartImage(String startImage) throws ParseException {

		_agentlessBoltData = new AgentlessBoltData();
		int location = -1;

		System.out.println("startimagea (" + startImage + ")");
		// extract the location property from the commandline and display this is the 
		int endProperty = startImage.indexOf(SystemConstants.SLASH);
		if(endProperty > -1) {
			_locPathText.setText(startImage.substring(1, endProperty));
		}
		// remove 
		if(startImage.contains(AgentlessBoltConstants.CommandLineArguments.TargetNoHostKeyCheck)) {
			startImage = startImage.replace(AgentlessBoltConstants.CommandLineArguments.TargetNoHostKeyCheck, SystemConstants.EMPTY_STRING);
		}
		// remove executable stuff
		location = startImage.indexOf(COMMAND_SUFFIX);
		startImage = startImage.substring(location + COMMAND_SUFFIX.length() + 2, startImage.length()).trim();
		// parse from the end
		if(startImage.contains(CommandLineArguments.SudoPasswordArgument)) {
			location = startImage.indexOf(AgentlessBoltConstants.CommandLineArguments.SudoPasswordArgument);
			String sudoPassword = startImage.substring(location, startImage.length()).trim();
			_agentlessBoltData.setSudoPassword(sudoPassword.replace(AgentlessBoltConstants.CommandLineArguments.SudoPasswordArgument, SystemConstants.EMPTY_STRING).trim());
			startImage = startImage.substring(0, location).trim();
		}
		if(startImage.contains(CommandLineArguments.RunAsArgument)) {
			location = startImage.indexOf(AgentlessBoltConstants.CommandLineArguments.RunAsArgument);
			String runAs = startImage.substring(location, startImage.length()).trim();
			_agentlessBoltData.setRunAs(runAs.replace(AgentlessBoltConstants.CommandLineArguments.RunAsArgument, SystemConstants.EMPTY_STRING).trim());
			startImage = startImage.substring(0, location).trim();
		}
		// parse from the end
		location = startImage.indexOf(AgentlessBoltConstants.CommandLineArguments.PasswordArgument);
		String password = startImage.substring(location, startImage.length()).trim();
		_agentlessBoltData.setPassword(password.replace(AgentlessBoltConstants.CommandLineArguments.PasswordArgument, SystemConstants.EMPTY_STRING).trim());
		startImage = startImage.substring(0, location).trim();
		location = startImage.indexOf(AgentlessBoltConstants.CommandLineArguments.UserArgument);
		String user = startImage.substring(location, startImage.length()).trim();
		_agentlessBoltData.setUser(user.replace(AgentlessBoltConstants.CommandLineArguments.UserArgument, SystemConstants.EMPTY_STRING).trim());
		startImage = startImage.substring(0, location).trim();
		if(startImage.contains(AgentlessBoltConstants.CommandLineArguments.TargetNoSSL)) {
			startImage = startImage.replace(AgentlessBoltConstants.CommandLineArguments.TargetNoSSL, SystemConstants.EMPTY_STRING).trim();
			_agentlessBoltData.setSsl(false);
		} else {
			_agentlessBoltData.setSsl(true);
		}
		location = startImage.indexOf(AgentlessBoltConstants.CommandLineArguments.TargetsArgument);
		String targets = startImage.substring(location, startImage.length()).trim();
		targets = targets.replace(AgentlessBoltConstants.CommandLineArguments.TargetsArgument, SystemConstants.EMPTY_STRING).trim();
		if(targets.contains(AgentlessBoltConstants.CommandLineArguments.TargetWindows)) {
			_agentlessBoltData.setTargets(targets.replace(AgentlessBoltConstants.CommandLineArguments.TargetWindows, SystemConstants.EMPTY_STRING).trim());
			_agentlessBoltData.setWindows(true);
		} else {
			_agentlessBoltData.setTargets(targets);
			_agentlessBoltData.setWindows(false);
		}
		startImage = startImage.substring(0, location).trim();
		if(startImage.contains(AgentlessBoltConstants.CommandLineArguments.FileUpload)) {
			// extract file upload data
			startImage = startImage.replace(AgentlessBoltConstants.CommandLineArguments.FileUpload, SystemConstants.EMPTY_STRING).trim();
			String[] filenames = tokenizeParameters(startImage, false, "' '");
			_agentlessBoltData.setUploadFileName(filenames[0].replace(SystemConstants.QUOTE_SINGLE, SystemConstants.EMPTY_STRING));
			_agentlessBoltData.setDestinationFileName(filenames[1].replace(SystemConstants.QUOTE_SINGLE, SystemConstants.EMPTY_STRING));
			// set task
			_agentlessBoltData.setBoltTask(AgentlessBoltEnums.Task.upload);
			_taskItemCombo.setSelection(AgentlessBoltEnums.Task.upload, true);
			_taskItemCombo.setEnabled(false);
		} else if(startImage.contains(AgentlessBoltConstants.CommandLineArguments.CommandRun)) {
			// extract command run data
			startImage = startImage.replace(AgentlessBoltConstants.CommandLineArguments.CommandRun, SystemConstants.EMPTY_STRING).trim();
			if(startImage.startsWith(SystemConstants.QUOTE_SINGLE)) {
				startImage = removeLeadingTrailingSingleQuotes(startImage);
				_agentlessBoltData.setCommand(startImage);
			} else {
				startImage = removeLeadingTrailingDoubleQuotes(startImage); 
				_agentlessBoltData.setCommand(startImage);
			}
			// set task
			_agentlessBoltData.setBoltTask(AgentlessBoltEnums.Task.command);
			_taskItemCombo.setSelection(AgentlessBoltEnums.Task.command, true);
			_taskItemCombo.setEnabled(false);
		} else if(startImage.contains(AgentlessBoltConstants.CommandLineArguments.ScriptRun)) {
			// extract script run data
			startImage = startImage.replace(AgentlessBoltConstants.CommandLineArguments.ScriptRun, SystemConstants.EMPTY_STRING).trim();
			String[] arguments = tokenizeParameters(startImage, false, "' '");
			_agentlessBoltData.setScript(arguments[0].replace(SystemConstants.QUOTE_SINGLE, SystemConstants.EMPTY_STRING));
			if(arguments.length > 1) {
				_agentlessBoltData.setArgument(arguments[1].replace(SystemConstants.QUOTE_SINGLE, SystemConstants.EMPTY_STRING));
			}
			_agentlessBoltData.setBoltTask(AgentlessBoltEnums.Task.script);
			_taskItemCombo.setSelection(AgentlessBoltEnums.Task.script, true);
			_taskItemCombo.setEnabled(false);
		} else if(startImage.contains(AgentlessBoltConstants.CommandLineArguments.TaskRun)) {
			// extract task run data
			startImage = startImage.replace(AgentlessBoltConstants.CommandLineArguments.TaskRun, SystemConstants.EMPTY_STRING).trim();
			String[] arguments = tokenizeParameters(startImage, false, "' '");
			_agentlessBoltData.setTask(arguments[0].replace(SystemConstants.QUOTE_SINGLE, SystemConstants.EMPTY_STRING));
			if(arguments.length > 1) {
				List<String> taskArguments = new ArrayList<String>();
				for(String argument : arguments) {
					taskArguments.add(argument.replace(SystemConstants.QUOTE_SINGLE, SystemConstants.EMPTY_STRING));
				}
				_agentlessBoltData.setTaskArguments(taskArguments);
			}
			_agentlessBoltData.setBoltTask(AgentlessBoltEnums.Task.task);
			_taskItemCombo.setSelection(AgentlessBoltEnums.Task.task, true);
			_taskItemCombo.setEnabled(false);
		}
		return _agentlessBoltData;
	}

	private AgentlessBoltEnums.Task getSelectedTask() {
		final ComboItem comboItem = _taskItemCombo.getSelectedItem();
		if(comboItem == null) {
			return AgentlessBoltEnums.Task.unknown;
		}
		return (AgentlessBoltEnums.Task) comboItem.data;
	}

	private String removeLeadingTrailingDoubleQuotes(String input) {
		String removed = input.trim();
		if(removed.substring(0,1).equals(SystemConstants.QUOTE)) {
			removed = removed.substring(1,removed.length());
		}
		if(removed.substring(removed.length() - 1, removed.length()).equals(SystemConstants.QUOTE)) {
			removed = removed.substring(0,removed.length() - 1);
		}
		return removed;
	}

	private String removeLeadingTrailingSingleQuotes(String input) {
		String removed = input.trim();
		if(removed.substring(0,1).equals(SystemConstants.QUOTE_SINGLE)) {
			removed = removed.substring(1,removed.length());
		}
		if(removed.substring(removed.length() - 1, removed.length()).equals(SystemConstants.QUOTE_SINGLE)) {
			removed = removed.substring(0,removed.length() - 1);
		}
		return removed;
	}

	private static String[] tokenizeParameters(String parameters, boolean keepQuote, String delimiter) {
		final char QUOTE = SystemConstants.QUOTE.toCharArray()[0];
		final char BACK_SLASH = SystemConstants.BACK_SLASH.toCharArray()[0];
		char prevChar = 0;
		char currChar = 0;
		StringBuffer sb = new StringBuffer(parameters.length());

		if (!keepQuote) {
			for (int i = 0; i < parameters.length(); i++) {
				if (i > 0) {
					prevChar = parameters.charAt(i - 1);
				}
				currChar = parameters.charAt(i);

				if (currChar != QUOTE || (currChar == QUOTE && prevChar == BACK_SLASH)) {
					sb.append(parameters.charAt(i));
				}
			}

			if (sb.length() > 0) {
				parameters = sb.toString();
			}
		}
		return parameters.split(delimiter);
	}

}
