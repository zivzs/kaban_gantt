package com.informatica.kanban_gantt.data;

import java.util.List;

public class StoryData {
	
	private String name;
	private long id;
	private CardType type;
	private List<LaneLog> lanesLog;
	
	public StoryData(String name, long id, CardType type, List<LaneLog> lanesLog) {
		super();
		this.name = name;
		this.id = id;
		this.type = type;
		this.lanesLog = lanesLog;
	}

	public String getName() {
		return name;
	}

	public long getId() {
		return id;
	}

	public CardType getType() {
		return type;
	}

	public List<LaneLog> getLanesLog() {
		return lanesLog;
	}
	
	
	
	
	

}
