package com.informatica.kanban_gantt.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CardEventData {

	private CardEventType type;
	private Calendar date;
	private String cardName;
	private Long cardId;
	private LaneData fromLane;
	private LaneData toLane;
	
	public CardEventData(CardEventType type, String dateString, String cardName,
			Long cardId, LaneData fromLane, LaneData toLane) {
		super();
		this.type = type;
		this.date = parseStringDate(dateString);
		this.cardName = cardName;
		this.cardId = cardId;
		this.fromLane = fromLane;
		this.toLane = toLane;
	}

	private static Calendar parseStringDate(String dateString) {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"dd/MM/yyyy' at 'hh:mm:ss aa");

		Date date = null;
		try {
			date = dateFormat.parse(dateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Calendar calendar = Calendar.getInstance();

		calendar.setTime(date);
		
		return calendar;
		
			
	}

	public CardEventType getType() {
		return type;
	}

	public Calendar getDate() {
		return date;
	}

	public String getCardName() {
		return cardName;
	}

	public Long getCardId() {
		return cardId;
	}

	public LaneData getFromLane() {
		return fromLane;
	}

	public LaneData getToLane() {
		return toLane;
	}
	
	
	
	
	
	
	
	
}
