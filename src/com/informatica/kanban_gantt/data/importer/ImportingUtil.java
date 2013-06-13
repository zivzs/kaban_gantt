package com.informatica.kanban_gantt.data.importer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.informatica.kanban_gantt.data.BoardData;
import com.informatica.kanban_gantt.data.BoardExportData;
import com.informatica.kanban_gantt.data.CardEventData;
import com.informatica.kanban_gantt.data.CardEventType;
import com.informatica.kanban_gantt.data.CardType;
import com.informatica.kanban_gantt.data.LaneData;
import com.informatica.kanban_gantt.data.LaneLog;
import com.informatica.kanban_gantt.data.StoryData;

public class ImportingUtil {

	public static BoardData importBoardExportFile(String filename) {

		BoardData boardData = new BoardData();
		BoardExportData boardExportData = new BoardExportData();
		File file = new File(filename);

		try {
			parseFileToEventsAndLanes(boardData, boardExportData, file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		convetsEventsIntoStoriesData(boardData, boardExportData);

		return boardData;
	}

	private static void convetsEventsIntoStoriesData(BoardData boardData,
			BoardExportData boardExportData) {

		for (Long cardId : boardExportData.getEventsMap().keySet()) {

			List<CardEventData> events = boardExportData.getEventsMap().get(
					cardId);

			Collections.sort(events, new Comparator<CardEventData>() {

				@Override
				public int compare(CardEventData o1, CardEventData o2) {
					return o1.getDate().compareTo(o2.getDate());

				}
			});

			List<LaneLog> lanesLog = new LinkedList<LaneLog>();

			for (int i = 0; i < events.size() - 1; i++) {
				lanesLog.add(new LaneLog(events.get(i).getToLane(), events.get(
						i).getDate(), events.get(i + 1).getDate()));
			}
			if (lanesLog.size() > 0) {
				CardEventData lastEvent=events.get(events.size()-1);
				lanesLog.add(new LaneLog(lastEvent.getToLane(), lastEvent.getDate(), Calendar.getInstance()));
				boardData.getStoriesMap().put(
						cardId,
						new StoryData(events.get(0).getCardName(), cardId,
								CardType.ITERATION_STORY, lanesLog));
			}
		}

	}

	private static void parseFileToEventsAndLanes(BoardData boardData,
			BoardExportData boardExportData, File file)
			throws FileNotFoundException, IOException {
		BufferedReader bufRdr = new BufferedReader(new FileReader(file));
		String line = null;
		int i = 0;
		// read each line of text file
		while ((line = bufRdr.readLine()) != null) {
			System.out.println(i);
			if (line.contains("Card Move Event")
					|| line.contains("Card Creation Event")) {

				// if (line.contains("Card Creation Event")){
				line = removeConfusingcommas(line);
				String[] parts = line.split(",");
				CardEventType eventType;

				if (line.contains("Card Move Event")) {
					eventType = CardEventType.MOVE;
				} else {
					eventType = CardEventType.CREATE;
				}

				String date = parts[0];
				String cardName = parts[3];
				Long cardId = Long.parseLong(parts[5]);
				String fromLane = parts[6];
				Long fromLaneId = Long.parseLong(parts[7]);
				String toLane = parts[8];
				Long toLaneId = Long.parseLong(parts[9]);

				updateLanesData(boardData.getLanesMap(), fromLane, fromLaneId,
						toLane, toLaneId);

				if (!boardExportData.getEventsMap().containsKey(cardId)) {
					boardExportData.getEventsMap().put(cardId,
							new LinkedList<CardEventData>());
				}

				boardExportData
						.getEventsMap()
						.get(cardId)
						.add(new CardEventData(eventType, date, cardName,
								cardId,
								boardData.getLanesMap().get(fromLaneId),
								boardData.getLanesMap().get(toLaneId)));

			}

			i++;
		}

		// close the file
		bufRdr.close();
	}

	private static String removeConfusingcommas(String line) {
		line.replaceAll("\"\"", "NA");
		String[] parts = line.split("\"");
		StringBuilder sb = new StringBuilder();
		if (parts.length > 1) {
			for (int i = 1; i < parts.length; i = i + 2) {
				parts[i] = parts[i].replace(",", " ");

			}
			for (int i = 0; i < parts.length; i++) {
				sb.append(parts[i]);
			}
			return sb.toString();
		}

		return line;

	}

	private static void updateLanesData(HashMap<Long, LaneData> lanesMap,
			String fromLane, Long fromLaneId, String toLane, Long toLaneId) {

		if (!lanesMap.containsKey(toLaneId)) {
			LaneData laneData = new LaneData(toLaneId, toLane);
			lanesMap.put(toLaneId, laneData);
		}

		if (!lanesMap.containsKey(fromLaneId)) {
			LaneData laneData = new LaneData(fromLaneId, fromLane);
			lanesMap.put(fromLaneId, laneData);
		}

	}

}
