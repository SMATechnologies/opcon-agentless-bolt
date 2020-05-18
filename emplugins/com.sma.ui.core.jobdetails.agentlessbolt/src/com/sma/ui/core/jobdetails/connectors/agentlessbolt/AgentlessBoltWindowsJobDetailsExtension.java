package com.sma.ui.core.jobdetails.connectors.agentlessbolt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import com.sma.ui.core.jobdetails.AbstractSubJobsDetailsWidget;
import com.sma.ui.core.jobdetails.windows.AbstractWindowsSubJobDetailsWidgetExtension;

public class AgentlessBoltWindowsJobDetailsExtension extends AbstractWindowsSubJobDetailsWidgetExtension {

	@Override
	public AbstractSubJobsDetailsWidget createJobDetailsWidget(Composite parent) {
		AbstractSubJobsDetailsWidget widget = new AgentlessBoltWindowsJobDetailsWidget(parent, getMessageDisplayer(), getContextID(), getJobDetailEditorContext());
		widget.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

		return widget;
	}

}
