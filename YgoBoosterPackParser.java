package ygoParsers;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author Alec Pierce
 *	<p>
 *	Created: 10/13/2018
 *	</p>
 */
public class YgoBoosterPackParser extends YgoParser {
	private String aElement = "";
	private String listElement = "";
	private String yearElement = "";
	private String divElement = "";
	private Map<String, List<String>> yearsAndBoosterPacks = new LinkedHashMap<String, List<String>>();
	
	public YgoBoosterPackParser() {
		super("https://www.yugiohcardguide.com/yugioh-booster-packs.html");
		aElement = "a[href^=/sets/]";
		listElement = "ul.flush mBottom";
		yearElement = "h2";
		divElement = "div.col-sm-6";
		// TODO Auto-generated constructor stub
	}
	
	private Elements selectElements(String elementToFind) {
		return super.getElements(elementToFind);
	}
	
	/**
	 * Creates set objects and stores the associated booster packs in the set. 
	 * Those boosterPacks will then be added to a map.
	 * <p>
	 * Map: Key = Set's year (String), Value = Set object (Set)
	 * </p>
	 */
	public Map<String, List<String>> createBoosterPackList() {
		// have each set's cards be in a separate object or something
		Elements divElements = new Elements();
		List<String> boosterPacks = new ArrayList<String>();
		String year = "";
		
		divElements = selectElements(divElement);
		for (Element element : divElements) {
			Elements children = element.children();
			for (Element child : children) {
				if (child.tagName() != null && child.tagName().equalsIgnoreCase("h2") && !StringUtils.containsIgnoreCase(year, child.text())) {
					if(!boosterPacks.isEmpty()) {
						yearsAndBoosterPacks.put(year, boosterPacks);
						year = "";
						boosterPacks = new ArrayList<String>();
					}
//					Elements yearsElement = child.children().select(yearElement);
//					year = yearsElement.get(0).text();
					year = child.text();
					String yearPattern = "\\d{4}";
					Matcher yearMatcher = Pattern.compile(yearPattern).matcher(year);
					if (yearMatcher.find()) {
						year = yearMatcher.group();
					}
				} else {
					Elements boosterPackElements = child.children().select(aElement);
					for (Element boosterPack : boosterPackElements) {
						boosterPacks.add(boosterPack.text());
					}
				}
			}
		}
		yearsAndBoosterPacks.put(year, boosterPacks);
		return yearsAndBoosterPacks;
	}
	
}
