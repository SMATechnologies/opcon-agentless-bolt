package com.sma.ui.core.jobdetails.connectors.agentlessbolt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

import com.sma.core.api.constants.SystemConstants;
import com.sma.core.api.interfaces.IPersistentJob;
import com.sma.core.session.ContextID;
import com.sma.ui.core.jobdetails.JobDetailsHelper;
import com.sma.ui.core.jobdetails.JobUtil;
import com.sma.ui.core.jobdetails.connectors.agentlessbolt.enums.AgentlessBoltEnums;
import com.sma.ui.core.jobdetails.connectors.agentlessbolt.modules.AgentlessBoltData;
import com.sma.ui.core.messages.IMessageDisplayer;
import com.sma.ui.core.widgets.listeners.ControlTokenSelectorListener;
import com.sma.ui.core.widgets.listeners.DirtyModifyAdapter;
import com.sma.ui.core.widgets.listeners.DirtySelectionAdapter;
import com.sma.ui.core.widgets.listeners.IDirtyListener;

public class RunFileUploadJobDetailsAction implements IJobDetailsAction {

	private static final String USER_NAME = "User";
	private static final String USER_TOOLTIP = "The name of the user to use for the remote execution";
	private static final String PASSWORD_NAME = "Password";
	private static final String PASSWORD_TOOLTIP = "The password associated with the user";
	private static final String RUN_AS_NAME = "Run As User";
	private static final String RUN_AS_TOOLTIP = "The name of the user to run the task under";
	private static final String SUDO_PASSWORD_NAME = "Sudo Password";
	private static final String SUDO_PASSWORD_TOOLTIP = "The password that will allow a user to change to another user";
	private static final String TARGET_NAME = "Target(s)";
	private static final String TARGET_TOOLTIP = "The target system(s), multiple systems comma seperated";
	private static final String SOURCE_NAME = "Source";
	private static final String SOURCE_TOOLTIP = "Full name of file or directory to upload";
	private static final String DESTINATION_NAME = "Destination";
	private static final String DESTINATION_TOOLTIP = "Full name of file or directory in target system";

	@SuppressWarnings("unused")
	private final IPersistentJob _job;
	@SuppressWarnings("unused")
	private final IMessageDisplayer _messageDisplayer;
	private final ContextID _contextID;

	private Composite _composite;

	private Text _userText;
	private Text _passwordText;
	private Text _runAsText;
	private Text _sudoPasswordText;
	private Text _targetsText;
	private Button _windowsCheckBox;
	private Button _sslCheckBox;
	private Button _noHostKeyCheckCheckBox;
	private Text _sourceText;
	private Text _destinationText;
	
	public RunFileUploadJobDetailsAction(IPersistentJob job, ContextID contextID, IMessageDisplayer messageDisplayer) {
		this._job = job;
		this._contextID = contextID;
		this._messageDisplayer = messageDisplayer;
	}
	
	public void createActionComposite(Composite jobActionComposite) {
		_composite = JobDetailsHelper.createComposite(jobActionComposite, 1, false);
		_composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		Group userGroup = JobDetailsHelper.createGroup(_composite, SystemConstants.EMPTY_STRING, 8, false);
		userGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, true));

		// User
		JobDetailsHelper.createTextLabel(userGroup, USER_NAME, new GridData(SWT.BEGINNING, SWT.CENTER, false, true), JobUtil.COLOR_BLUE);
		_userText = JobDetailsHelper.createTextBox(userGroup, new GridData(SWT.FILL, SWT.CENTER, true, false), SWT.BORDER, 50);
		final GridData usrGd = new GridData(SWT.FILL, SWT.FILL, false, true);
		usrGd.widthHint = 300;
		_userText.setLayoutData(usrGd);
		_userText.setToolTipText(USER_TOOLTIP);

		// Password
		JobDetailsHelper.createTextLabel(userGroup, PASSWORD_NAME, new GridData(SWT.BEGINNING, SWT.CENTER, false, true), JobUtil.COLOR_BLUE);
		_passwordText = JobDetailsHelper.createTextBox(userGroup, new GridData(SWT.FILL, SWT.CENTER, true, false), SWT.BORDER, 50);
		final GridData pwdGd = new GridData(SWT.FILL, SWT.FILL, false, true);
		pwdGd.widthHint = 300;
		_passwordText.setLayoutData(pwdGd);
		_passwordText.setToolTipText(PASSWORD_TOOLTIP);

		// RunAs
		JobDetailsHelper.createTextLabel(userGroup, RUN_AS_NAME, new GridData(SWT.BEGINNING, SWT.CENTER, false, true), JobUtil.COLOR_BLUE);
		_runAsText = JobDetailsHelper.createTextBox(userGroup, new GridData(SWT.FILL, SWT.CENTER, true, false), SWT.BORDER, 50);
		final GridData runAsGd = new GridData(SWT.FILL, SWT.FILL, false, true);
		runAsGd.widthHint = 300;
		_runAsText.setLayoutData(runAsGd);
		_runAsText.setToolTipText(RUN_AS_TOOLTIP);

		// SudoPassword
		JobDetailsHelper.createTextLabel(userGroup, SUDO_PASSWORD_NAME, new GridData(SWT.BEGINNING, SWT.CENTER, false, true), JobUtil.COLOR_BLUE);
		_sudoPasswordText = JobDetailsHelper.createTextBox(userGroup, new GridData(SWT.FILL, SWT.CENTER, true, false), SWT.BORDER, 50);
		final GridData suPwdGd = new GridData(SWT.FILL, SWT.FILL, false, true);
		suPwdGd.widthHint = 300;
		_sudoPasswordText.setLayoutData(suPwdGd);
		_sudoPasswordText.setToolTipText(SUDO_PASSWORD_TOOLTIP);

		Group targetGroup = JobDetailsHelper.createGroup(_composite, SystemConstants.EMPTY_STRING, 8, false);
		targetGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		// target
		_targetsText = JobUtil.createLabeledText(targetGroup, TARGET_NAME, 0, JobUtil.COLOR_BLUE, JobUtil.COLOR_LIGHT_GREEN, SWT.BORDER, 1);
		_targetsText.setToolTipText(TARGET_TOOLTIP);

		GridData gd = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		gd.horizontalSpan = 2;
		_windowsCheckBox = this.createButton(targetGroup, SWT.CHECK, "Windows", gd);

		gd = new GridData(SWT.RIGHT, SWT.CENTER, false, false);
		gd.horizontalSpan = 2;
		_sslCheckBox = this.createButton(targetGroup, SWT.CHECK, "SSL", gd);

		gd = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		gd.horizontalSpan = 2;
		_noHostKeyCheckCheckBox = this.createButton(targetGroup, SWT.CHECK, "No Host Key Check", gd);

		// upload
		Group commandGroup = JobDetailsHelper.createGroup(_composite, SystemConstants.EMPTY_STRING, 2, false);
		commandGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		_sourceText = JobUtil.createLabeledText(commandGroup, SOURCE_NAME, 0, JobUtil.COLOR_BLUE, JobUtil.COLOR_LIGHT_GREEN, SWT.BORDER, 1);
		_sourceText.setToolTipText(SOURCE_TOOLTIP);

		_destinationText = JobUtil.createLabeledText(commandGroup, DESTINATION_NAME, 0, JobUtil.COLOR_BLUE, JobUtil.COLOR_LIGHT_GREEN, SWT.BORDER, 1);
		_destinationText.setToolTipText(DESTINATION_TOOLTIP);
	}

	public Composite getComposite() {
		return _composite;
	}

	public void addListeners(IDirtyListener listener) {
		_userText.addModifyListener(new DirtyModifyAdapter(listener));
		_passwordText.addModifyListener(new DirtyModifyAdapter(listener));
		_runAsText.addModifyListener(new DirtyModifyAdapter(listener));
		_sudoPasswordText.addModifyListener(new DirtyModifyAdapter(listener));
		_targetsText.addModifyListener(new DirtyModifyAdapter(listener));
		_windowsCheckBox.addSelectionListener(new DirtySelectionAdapter(listener));
		_sslCheckBox.addSelectionListener(new DirtySelectionAdapter(listener));
		_noHostKeyCheckCheckBox.addSelectionListener(new DirtySelectionAdapter(listener));
		_sourceText.addModifyListener(new DirtyModifyAdapter(listener));
		_destinationText.addModifyListener(new DirtyModifyAdapter(listener));

		// add CTRL+T shortcut for global properties
		new ControlTokenSelectorListener(_userText, _contextID);
		new ControlTokenSelectorListener(_passwordText, _contextID);
		new ControlTokenSelectorListener(_runAsText, _contextID);
		new ControlTokenSelectorListener(_sudoPasswordText, _contextID);
		new ControlTokenSelectorListener(_targetsText, _contextID);
		new ControlTokenSelectorListener(_sourceText, _contextID);
		new ControlTokenSelectorListener(_destinationText, _contextID);
	}

	public void setDefaults() {
		_userText.setText(SystemConstants.EMPTY_STRING);
		_passwordText.setText(SystemConstants.EMPTY_STRING);
		_runAsText.setText(SystemConstants.EMPTY_STRING);
		_sudoPasswordText.setText(SystemConstants.EMPTY_STRING);
		_targetsText.setText(SystemConstants.EMPTY_STRING);
		_windowsCheckBox.setSelection(false);
		_sslCheckBox.setSelection(false);
		_noHostKeyCheckCheckBox.setSelection(false);
		_sourceText.setText(SystemConstants.EMPTY_STRING);
		_destinationText.setText(SystemConstants.EMPTY_STRING);
	}
	
	public void initializeContents(AgentlessBoltData agentlessBoltData) {
		_userText.setText(agentlessBoltData.getUser());
		_passwordText.setText(agentlessBoltData.getPassword());
		_targetsText.setText(agentlessBoltData.getTargets());
		if(agentlessBoltData.getRunAs() != null) {
			_runAsText.setText(agentlessBoltData.getRunAs());
		} else {
			_runAsText.setText(SystemConstants.EMPTY_STRING);
		}
		if(agentlessBoltData.getSudoPassword() != null) {
			_sudoPasswordText.setText(agentlessBoltData.getSudoPassword());
		} else {
			_sudoPasswordText.setText(SystemConstants.EMPTY_STRING);
		}
		if(agentlessBoltData.isWindows()) {
			_windowsCheckBox.setSelection(true);
		} else {
			_windowsCheckBox.setSelection(false);
		}
		if(agentlessBoltData.isSsl()) {
			_sslCheckBox.setSelection(true);
		} else {
			_sslCheckBox.setSelection(false);
		}
		if(agentlessBoltData.isNoHostKeyCheck()) {
			_noHostKeyCheckCheckBox.setSelection(true);
		} else {
			_noHostKeyCheckCheckBox.setSelection(false);
		}
		_sourceText.setText(agentlessBoltData.getUploadFileName());
		_destinationText.setText(agentlessBoltData.getDestinationFileName());
	}

	public AgentlessBoltData getContents() {
		AgentlessBoltData data = new AgentlessBoltData();

		data.setBoltTask(AgentlessBoltEnums.Task.upload);
		data.setUser(_userText.getText());
		data.setPassword(_passwordText.getText());
		data.setTargets(_targetsText.getText());
		if(_runAsText.getText().length() > 0) {
			data.setRunAs(_runAsText.getText());
		}
		if(_sudoPasswordText.getText().length() > 0) {
			data.setSudoPassword(_sudoPasswordText.getText());
		}
		if(_windowsCheckBox.getSelection()) {
			data.setWindows(true);
		} else {
			data.setWindows(false);
		}
		if(_sslCheckBox.getSelection()) {
			data.setSsl(true);
		} else {
			data.setSsl(false);
		}
		if(_noHostKeyCheckCheckBox.getSelection()) {
			data.setNoHostKeyCheck(true);
		} else {
			data.setNoHostKeyCheck(false);
		}
		data.setUploadFileName(_sourceText.getText());
		data.setDestinationFileName(_destinationText.getText());
		return data;
	}

	private Button createButton(Composite parent, int style, String text, Object dataLayout) {
		Button button = new Button(parent, style);
		button.setText(text);
		button.setLayoutData(dataLayout);
	
		if (((style & SWT.CHECK) == SWT.CHECK) || ((style & SWT.RADIO) == SWT.RADIO)) {
			button.setBackground(parent.getBackground());
		}
	
		return button;
	}

}
