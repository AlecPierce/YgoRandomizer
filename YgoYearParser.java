package ygoParsers;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YgoYearParser {
	public Map<String, List<String>> filterByYear (Map<String, String> yearRange, Map<String, List<String>> sets) {
		Map<String, List<String>> setsToUse = new LinkedHashMap<String, List<String>>();
		String yearPattern = "\\d{4}";
		String min = yearRange.get("min");
		String max = yearRange.get("max");
		for (Entry<String, List<String>> entry : sets.entrySet()) {
			Matcher yearMatcher = Pattern.compile(yearPattern).matcher(entry.getKey());
			if (yearMatcher.find()) {
				String year = yearMatcher.group();
				int givenYear = Integer.parseInt(year);
				int minYear = Integer.parseInt(min);
				int maxYear = Integer.parseInt(max);
				if (givenYear <= maxYear && givenYear >= minYear) {
					setsToUse.put(entry.getKey(), entry.getValue());
				} 
			} 
		}
		return setsToUse;
	}
}
