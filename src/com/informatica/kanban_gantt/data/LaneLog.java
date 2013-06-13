package com.informatica.kanban_gantt.data;

import java.util.Calendar;

public class LaneLog {

	private LaneData lane;
	private Calendar start;
	private Calendar end;

	public LaneLog(LaneData lane, Calendar start, Calendar end) {
		super();
		this.lane = lane;
		this.start = start;
		this.end = end;
	}

	public LaneData getLane() {
		return lane;
	}

	public Calendar getStart() {
		return start;
	}

	public Calendar getEnd() {
		return end;
	}

}
