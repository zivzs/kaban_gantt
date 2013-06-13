package com.informatica.kanban_gantt;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.nebula.widgets.ganttchart.GanttChart;
import org.eclipse.nebula.widgets.ganttchart.GanttEvent;
import org.eclipse.nebula.widgets.ganttchart.GanttSection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.informatica.kanban_gantt.data.BoardData;
import com.informatica.kanban_gantt.data.LaneLog;
import com.informatica.kanban_gantt.data.StoryData;
import com.informatica.kanban_gantt.data.importer.ImportingUtil;

public class View extends ViewPart {
	public static final String ID = "kanban_gantt.informatica.com.view";

	private TableViewer viewer;

	private GanttChart ganttChart;

	private static int currentLayer = 1;
	private static int minLayer = 1;
	private static int maxLayer = 3;

	private static Runnable runnable;

	/**
	 * The content provider class is responsible for providing objects to the
	 * view. It can wrap existing objects in adapters or simply return objects
	 * as-is. These objects may be sensitive to the current input of the view,
	 * or ignore it and always show the same content (like Task List, for
	 * example).
	 */
	class ViewContentProvider implements IStructuredContentProvider {
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}

		public void dispose() {
		}

		public Object[] getElements(Object parent) {
			if (parent instanceof Object[]) {
				return (Object[]) parent;
			}
			return new Object[0];
		}
	}

	class ViewLabelProvider extends LabelProvider implements
			ITableLabelProvider {
		public String getColumnText(Object obj, int index) {
			return getText(obj);
		}

		public Image getColumnImage(Object obj, int index) {
			return getImage(obj);
		}

		public Image getImage(Object obj) {
			return PlatformUI.getWorkbench().getSharedImages()
					.getImage(ISharedImages.IMG_OBJ_ELEMENT);
		}
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(final Composite parent) {

		parent.setLayout(new GridLayout(1, true));

		Button loadFileButton = new Button(parent, SWT.NONE);
		loadFileButton.setText("Load export file");

		ganttChart = new GanttChart(parent, SWT.NONE);
		ganttChart.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		// demo();

		loadFileButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(parent.getShell(), SWT.OPEN);
				String[] filterNames = new String[] { "Board export file",
						"All Files (*.*)" };
				String[] filterExtensions = new String[] { "*.csv", "*.*" };
				String filterPath = "c:\\";

				dialog.setFilterNames(filterNames);
				dialog.setFilterExtensions(filterExtensions);
				dialog.setFilterPath(filterPath);
				dialog.setFileName("myfile");
				String filename = dialog.open();

				BoardData boardData = ImportingUtil
						.importBoardExportFile(filename);
				DatesRangeDialog dateRangesDialog = new DatesRangeDialog(parent.getShell(), boardData);
				dateRangesDialog.create();
				dateRangesDialog.open();
				Calendar start = dateRangesDialog.getFromDate();
				Calendar end = dateRangesDialog.getToDate();
				Set<Long> selectedLanes=dateRangesDialog.getSelectedLanes();
				
				LinkedList<StoryData> storiesInDatesRange = new LinkedList<StoryData>();
				for (StoryData data : boardData.getStoriesMap().values()) {
					for (LaneLog laneLog : data.getLanesLog()) {
						if (laneLog.getStart().after(start)
								&& laneLog.getStart().before(end) && selectedLanes.contains(laneLog.getLane().getId())) {
							storiesInDatesRange.add(data);
							break;
						}

					}

				}

				Collections.sort(storiesInDatesRange,
						new Comparator<StoryData>() {

							@Override
							public int compare(StoryData o1, StoryData o2) {
								return o1.getName().compareToIgnoreCase(
										o2.getName());
							}
						});

				StoriesSelectionDialog storiesSelectionDialog = new StoriesSelectionDialog(
						parent.getShell(), storiesInDatesRange);
				storiesSelectionDialog.create();
				storiesSelectionDialog.open();
				List<StoryData> selectedStories = storiesSelectionDialog
						.getSelectedStories();

				for (StoryData data : selectedStories) {
					createStorySection(data);

				}

				parent.redraw();

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});

	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		// viewer.getControl().setFocus();
		ganttChart.setFocus();
	}

	private void createStorySection(StoryData storyData) {

		String name = storyData.getName();
		if (name.length() >= 30) {
			name = name.substring(0, 30);
		}
		GanttSection ganttSection = new GanttSection(ganttChart, name);
		ganttSection.setTextOrientation(SWT.HORIZONTAL);
		createLanesActivity(ganttSection, storyData.getLanesLog());

	}

	private void createLanesActivity(GanttSection ganttSection,
			List<LaneLog> lanesLog) {

		for (LaneLog laneLog : lanesLog) {
			GanttEvent event = new GanttEvent(ganttChart, laneLog.getLane()
					.getName(), laneLog.getStart(), laneLog.getEnd(), 0);
			event.setMoveable(false);
			event.setHorizontalTextLocation(SWT.CENTER);
			event.setVerticalTextLocation(SWT.TOP);
			ganttSection.addGanttEvent(event);
		}
		

	}
}