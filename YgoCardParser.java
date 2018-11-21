package ygoParsers;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author Alec Pierce
 *	<p>
 *	Created: 10/13/2018
 *	</p>
 */
public class YgoCardParser extends YgoParser {
	private String cardElement = "";
	private String setElement = "";
	private String aElement = "";
	private String tdSetElement = "";
	private String tableBodyElement = "";
	private Map<String, List<String>> setsWithCards = new LinkedHashMap<String, List<String>>();
	
	public YgoCardParser() {
		super("https://www.yugiohcardguide.com/card_list.html");
		cardElement = "a[href^=/single/]";
		setElement = "tr.set";
		aElement = "td > a";
		tdSetElement = "tr.set > td";
		tableBodyElement = "table[id^=card_data] > tbody > tr";
	}
	
	private Elements selectElements(String elementToFind) {
		return super.getElements(elementToFind);
	}
	
	public List<String> getCards() {
		Elements elements = new Elements();
		List<String> cardList = new ArrayList<String>();
		elements = selectElements(this.tdSetElement);
		System.out.println("ELEMENT(S) FOUND:" + elements.toString());
		elements = selectElements(this.cardElement);
		System.out.println("Element(s) found: " + elements.toString());
		for (Element element : elements) {
			cardList.add(element.text());
			if (cardList.size() > 0) {
				System.out.println("Card added: " + cardList.remove(0));
			} else {
				System.out.println("Card Parsing Complete.");
			}
		}
		return cardList;
	}
	
	public List<String> getSetNames() {
		Elements elements = new Elements();
		List<String> setNames = new ArrayList<String>();
		
		elements = selectElements(this.setElement);
		System.out.println("Element(s) found: " + elements.toString());
		for (Element element : elements) {
			setNames.add(element.text());
			if (setNames.size() > 0) {
				System.out.println("Set added: " + setNames.remove(0));
			} else {
				System.out.println("Set Parsing Complete.");
			}
		}
		return setNames;
	}
	
	/**
	 * Creates set objects and stores the associated cards in the set. 
	 * Those setList will then be added to a map.
	 * <p>
	 * Map: Key = Set's name (String), Value = Set object (Set)
	 * </p>
	 */
	public Map<String, List<String>> createSets() {
		// have each set's cards be in a separate object or something
		Elements tableElements = new Elements();
		List<String> cardsFromSet = new ArrayList<String>();
		String setName = "";
		
		tableElements = selectElements(this.tableBodyElement);
		for (Element element : tableElements) {
			if (element.attributes() != null && element.attributes().hasKeyIgnoreCase("class")
					&& element.attr("class").equalsIgnoreCase("set")) {
				if(!cardsFromSet.isEmpty()) {
					setsWithCards.put(setName, cardsFromSet);
					setName = "";
					cardsFromSet = new ArrayList<String>();
				}
				Elements set = element.children().select(aElement);
				setName = set.get(0).text();
			} else {
				Elements cards = element.children().select(aElement);
				for (Element card : cards) {
					cardsFromSet.add(card.text());
				}
			}
		}
		setsWithCards.put(setName, cardsFromSet);
		return setsWithCards;
	}
}
