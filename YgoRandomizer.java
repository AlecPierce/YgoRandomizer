package ygo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ygoParsers.YgoCardParser;

/**
 * @author Alec Pierce
 *	<p>
 *	Created: 10/13/2018
 *	</p>
 */
public class YgoRandomizer {

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		List<String> cards = new ArrayList<String>();
		List<String> sets = new ArrayList<String>();
		Map<String, List<String>> setsWithCards = new LinkedHashMap<String, List<String>>();
		YgoCardParser ygoCardParser = new YgoCardParser();
		setsWithCards = ygoCardParser.createSets();
		cards = ygoCardParser.getCards();
		sets = ygoCardParser.getSetNames();
		for (Entry<String, List<String>> entry : setsWithCards.entrySet()) {
			System.out.println("SET: " + entry.getKey());
			System.out.println("CARDS: " + entry.getValue() + "\n");
		}
		long endTime = System.currentTimeMillis();
		System.out.println("Runtime is: " + (endTime - startTime)/1000 + " seconds");
	}
}
