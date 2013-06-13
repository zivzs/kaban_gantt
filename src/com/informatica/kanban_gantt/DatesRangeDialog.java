package com.informatica.kanban_gantt;

import java.util.Calendar;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import com.informatica.kanban_gantt.data.BoardData;

public class DatesRangeDialog extends TitleAreaDialog {

	private Calendar fromDate = Calendar.getInstance();
	private Calendar toDate = Calendar.getInstance();
	private BoardData boardData;
	private Table table;
	private Set<Long> selectedLanes;

	protected DatesRangeDialog(Shell parentShell, BoardData boardData) {
		super(parentShell);
		this.boardData = boardData;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		// layout.horizontalAlignment = GridData.FILL;
		parent.setLayout(layout);

		// The text fields will grow with the size of the dialog
		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;

		Label label1 = new Label(parent, SWT.NONE);
		label1.setText("From");

		final DateTime dateTimeFrom = new DateTime(parent, SWT.CALENDAR);
		dateTimeFrom.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				fromDate.set(dateTimeFrom.getYear(), dateTimeFrom.getMonth(),
						dateTimeFrom.getDay());

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});

		Label toDateLabel = new Label(parent, SWT.NONE);
		toDateLabel.setText("To");

		final DateTime dateTimeTo = new DateTime(parent, SWT.CALENDAR);
		dateTimeTo.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				toDate.set(dateTimeTo.getYear(), dateTimeTo.getMonth(),
						dateTimeTo.getDay());

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});

		Label lanesLabel = new Label(parent, SWT.NONE);
		lanesLabel.setText("Select lanes to apply dates");

		final TableViewer tableViewer = new TableViewer(parent, SWT.MULTI
				| SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER
				| SWT.CHECK);
		table=tableViewer.getTable();
		tableViewer.getTable().setLayoutData(
				new GridData(SWT.FILL, SWT.FILL, true, true));
		tableViewer.setContentProvider(new ArrayContentProvider());

		TableViewerColumn nameColumn = new TableViewerColumn(tableViewer,
				SWT.NONE);
		nameColumn.getColumn().setWidth(500);
		nameColumn.getColumn().setText("Lane");
		nameColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof Long) {
					Long laneId = (Long) element;
					return boardData.getLanesMap().get(laneId).getName();
				}
				return null;
			}
		});

		tableViewer.setInput(boardData.getLanesMap().keySet().toArray());
		tableViewer.setComparator(new ViewerComparator(new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				String name1=boardData.getLanesMap().get(Long.parseLong(o1)).getName();
				String name2=boardData.getLanesMap().get(Long.parseLong(o2)).getName();
				return name1.compareTo(name2);
			}
		}));

		return parent;
	}

	@Override
	public void create() {
		super.create();
		// Set the title
		setTitle("This is my first own dialog");
		// Set the message
		setMessage("This is a TitleAreaDialog", IMessageProvider.INFORMATION);

	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalSpan = 3;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = SWT.CENTER;

		parent.setLayoutData(gridData);
		// Create Add button
		// Own method as we need to overview the SelectionAdapter
		createOkButton(parent, OK, "OK", true);
		// Add a SelectionListener

		// Create Cancel button
		Button cancelButton = createButton(parent, CANCEL, "Cancel", false);
		// Add a SelectionListener
		cancelButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				setReturnCode(CANCEL);
				close();
			}
		});
	}

	protected Button createOkButton(Composite parent, int id, String label,
			boolean defaultButton) {
		// increment the number of columns in the button bar
		((GridLayout) parent.getLayout()).numColumns++;
		Button button = new Button(parent, SWT.PUSH);
		button.setText(label);
		button.setFont(JFaceResources.getDialogFont());
		button.setData(new Integer(id));
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				if (isValidInput()) {
					okPressed();
				}
			}
		});
		if (defaultButton) {
			Shell shell = parent.getShell();
			if (shell != null) {
				shell.setDefaultButton(button);
			}
		}
		setButtonLayoutData(button);
		return button;
	}

	private boolean isValidInput() {
		boolean valid = true;

		return valid;
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	// Coyy textFields because the UI gets disposed
	// and the Text Fields are not accessible any more.
	private void saveInput() {
		selectedLanes=new HashSet<Long>();
		for(TableItem item:table.getItems())
		{
			if(	item.getChecked())
			{
				selectedLanes.add((Long)item.getData());
			}
				
		}
		
	}

	@Override
	protected void okPressed() {
		saveInput();
		super.okPressed();
	}

	public Calendar getFromDate() {
		return fromDate;
	}

	public Calendar getToDate() {
		return toDate;
	}
	
	public Set<Long> getSelectedLanes()
	{
		return selectedLanes;
	}
}
