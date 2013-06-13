package com.informatica.kanban_gantt.data;

public class LaneData {
	
	private long id;
	private String name;
	
	public LaneData(long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	

}
