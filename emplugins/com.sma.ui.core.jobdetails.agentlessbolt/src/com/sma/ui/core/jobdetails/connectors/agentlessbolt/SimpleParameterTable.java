package com.sma.ui.core.jobdetails.connectors.agentlessbolt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

import com.sma.core.api.constants.SystemConstants;
import com.sma.ui.core.EnterpriseManagerUtil;
import com.sma.ui.core.constants.SharedImages;
import com.sma.ui.core.widgets.base.DirtyComposite;
import com.sma.ui.core.widgets.contentproviders.CollectionContentProvider;
import com.sma.ui.core.widgets.interfaces.IReadOnly;
import com.sma.ui.core.widgets.listeners.ReplaceCharVerifyListener;
import com.sma.ui.core.widgets.viewers.ParameterTable;
import com.sma.ui.core.widgets.viewers.ParameterTableListener;
import com.sma.ui.core.widgets.viewers.SortingTableViewer;


/**
 * Default implementation for a {@link ParameterTable}
 */
public class SimpleParameterTable extends DirtyComposite implements
		ParameterTableListener,
		IReadOnly {

	private Text _nameText;
	private ParameterTable _parameterTable;
	private List<String> _propValueList = new ArrayList<String>();
	private final String _paramTitle;

	private MenuManager _rowMenuManager;
	protected IAction _moveUpAction;
	protected IAction _moveDownAction;

	public static final int PROPERTY_LENGTH_LIMIT = 4000;

	/**
	 * Create the widget defining the instance properties.
	 * 
	 * @param parent
	 *            the parent composite.
	 * @param style
	 *            SWT style to use for the composite
	 * @param isMaster
	 *            the flag to indicate if the widget called from master job/schedule
	 */
	public SimpleParameterTable(Composite parent, int style, String paramTitle) {
		super(parent, SWT.NONE);
		_paramTitle = paramTitle;
		setLayout(new GridLayout());
		createContents(this);
	}

	private Text createText(Composite parent) {
		Text txt = new Text(parent, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd.heightHint = EnterpriseManagerUtil.getDefaultFontSize().x * 4;
		txt.setLayoutData(gd);

		return txt;
	}

	private void addListeners() {
		_parameterTable.addParameterTableListener(this);
		_parameterTable.getTable().addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				updateMoveMenuState(_parameterTable.getTable().getTable());
			}
		});
		// do not accept tabs or \n or \r
		_nameText.addVerifyListener(new ReplaceCharVerifyListener(Pattern.compile("[\\t\\n\\r]"), SystemConstants.EMPTY_STRING)); //$NON-NLS-1$
		_nameText.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				updateButtons();
			}
		});
	}

	private void createContents(Composite parent) {
		_parameterTable = new ParameterTable(parent, new String[] { _paramTitle });
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, false);
		gd.widthHint = 200; // avoid table to grow indefinitely
		_parameterTable.setLayoutData(gd);

		_parameterTable.getTable().setContentProvider(new CollectionContentProvider());
		_parameterTable.getTable().setLabelProvider(new LabelProvider());
		_parameterTable.getTable().setInput(_propValueList);
		_parameterTable.getAddButton().setEnabled(false); // disable add button by default
		_parameterTable.setPackColumn(true);

		Group editGroup = new Group(_parameterTable.getClientComposite(), SWT.NONE);
		editGroup.setLayout(new GridLayout());
		editGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		editGroup.setText(_paramTitle);

		_nameText = createText(editGroup);
		_nameText.setTextLimit(PROPERTY_LENGTH_LIMIT);

		hookPopupMenu(_parameterTable.getTable());
		addListeners();
	}

	/**
	 * Create Move Up and Down popup menu
	 */
	private void hookPopupMenu(SortingTableViewer tableViewer) {
		_rowMenuManager = new MenuManager();
		_moveUpAction = new Action("Move Up", SharedImages.getImageDescriptor(SharedImages.ICO_UP)) {

			@Override
			public void run() {
				moveUp();
			}
		};
		_rowMenuManager.add(_moveUpAction);
		_moveDownAction = new Action("Move Down", SharedImages.getImageDescriptor(SharedImages.ICO_DOWN)) {

			@Override
			public void run() {
				moveDown();
			}
		};
		_rowMenuManager.add(_moveDownAction);
		Menu menu = _rowMenuManager.createContextMenu(tableViewer.getControl());
		tableViewer.getControl().setMenu(menu);
	}

	/**
	 * Get the list of current parameters in the list
	 */
	public List<String> getParamList() {
		return _propValueList;
	}

	/**
	 * clear the text in the textbox of Define Property Values
	 */
	private void clearNameText() {
		_nameText.setText(SystemConstants.EMPTY_STRING);
	}

	/**
	 * Set the list of parameters to display in the list
	 */
	public void setParamList(List<String> params) {
		_propValueList = params;
		_parameterTable.getTable().setInput(_propValueList);
		_parameterTable.getTable().packColumns();
		clearNameText();
		updateButtons();
	}

	public boolean parameterAdded() {
		final String value = validateValue(_nameText.getText());
		if (value == null) {
			return false;
		}
		_propValueList.add(value);
		clearNameText();
		setDirty(true);
		updateButtons();
		return true;
	}

	public boolean parameterRemoved(Object parameter) {
		_propValueList.remove(_parameterTable.getTable().getTable().getSelectionIndex());
		updateButtons();
		setDirty(true);
		return true;
	}

	public void parameterSelected(Object parameter) {
		if (parameter == null) {
			_nameText.setText(SystemConstants.EMPTY_STRING);
		} else {
			String property = (String) parameter;
			_nameText.setText(property);
			_nameText.setEnabled(true);
		}
	}

	public boolean parameterUpdated(Object parameter) {
		final String value = validateValue(_nameText.getText());
		if (value == null) {
			return false;
		}
		int index = _parameterTable.getTable().getTable().getSelectionIndex();
		_propValueList.set(index, value);
		_nameText.setText(SystemConstants.EMPTY_STRING);
		setDirty(true);
		return true;
	}

	private void updateButtons() {
		boolean valid = validateValue(_nameText.getText()) != null;
		_parameterTable.getAddButton().setEnabled(!isReadOnly() && valid);
		if (_parameterTable.getSelection() != null) {
			_parameterTable.getUpdateButton().setEnabled(!isReadOnly() && valid);
			_parameterTable.getRemoveButton().setEnabled(!isReadOnly());
		}
	}

	public boolean isReadOnly() {
		return _parameterTable.isReadOnly();
	}

	public void setReadOnly(boolean isReadOnly) {
		_parameterTable.setReadOnly(isReadOnly);
	}

	/**
	 * Set the title of the parameter's list column.
	 * 
	 * @param title
	 *            the title to set
	 */
	public void setParamColumnTitle(String title) {
		_parameterTable.getTable().getTable().getColumn(0).setText(title);
	}

	/**
	 * Validates the text's value and return the value to add/update
	 * 
	 * @param text
	 *            the text the user has entered
	 * @return <code>null</code> to indicate value is not valid, or the value to add.
	 */
	protected String validateValue(String text) {
		final String value = text.trim();
		if (value.length() == 0) {
			return null;
		}
		return value;
	}

	/**
	 * Move the selection up
	 */
	protected void moveUp() {
		final Table table = _parameterTable.getTable().getTable();
		int index = table.getSelectionIndex();
		if (index != 0) {
			Collections.swap(_propValueList, index, index - 1);
			_parameterTable.getTable().refresh();
		}
		updateMoveMenuState(table);
	}

	/**
	 * Move the selection down
	 */
	protected void moveDown() {
		final Table table = _parameterTable.getTable().getTable();
		int index = table.getSelectionIndex();
		int lastRowCount = table.getItemCount() - 1;
		if (index != lastRowCount) {
			Collections.swap(_propValueList, index, index + 1);
			_parameterTable.getTable().refresh();
		}
		updateMoveMenuState(table);
	}

	protected void updateMoveMenuState(Table table) {
		int selIndex = table.getSelectionIndex();
		int size = table.getItemCount();

		boolean upOK = (selIndex > 0);
		boolean downOK = (selIndex < (size - 1));

		_moveUpAction.setEnabled(upOK);
		_moveDownAction.setEnabled(downOK);
	}

	/**
	 * @return the Text used for the user to enter parameters
	 */
	public Text getParamControl() {
		return _nameText;
	}
}
