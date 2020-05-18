package com.sma.ui.core.jobdetails.connectors.agentlessbolt;

import org.eclipse.swt.widgets.Composite;

import com.sma.ui.core.jobdetails.connectors.agentlessbolt.modules.AgentlessBoltData;
import com.sma.ui.core.widgets.listeners.IDirtyListener;

public interface IJobDetailsAction {

	void createActionComposite(Composite taskActionComposite);

	Composite getComposite();
	
	void addListeners(IDirtyListener listener);

	void initializeContents(AgentlessBoltData agentlessBoltData);

	AgentlessBoltData getContents();

	void setDefaults();

}
