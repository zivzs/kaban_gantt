package com.informatica.kanban_gantt.data;

import java.util.HashMap;
import java.util.List;

public class BoardExportData {
	
	private HashMap<Long,List<CardEventData>> eventsMap;

	public BoardExportData() {
		super();
		eventsMap=new HashMap<Long, List<CardEventData>>();
	}

	public HashMap<Long, List<CardEventData>> getEventsMap() {
		return eventsMap;
	}
	
	
	
	

}
