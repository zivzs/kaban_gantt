package com.informatica.kanban_gantt.data;

import java.util.HashMap;

public class BoardData {
	
	private HashMap<Long,LaneData> lanesMap;
	private HashMap<Long,StoryData> storiesMap;
	
	
	public BoardData() {
		super();
		lanesMap=new HashMap<Long, LaneData>();
		storiesMap=new HashMap<Long, StoryData>();
	}


	public HashMap<Long, LaneData> getLanesMap() {
		return lanesMap;
	}


	public HashMap<Long, StoryData> getStoriesMap() {
		return storiesMap;
	}
	
	
	
	
	

}
