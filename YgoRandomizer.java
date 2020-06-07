package ygo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Date;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import org.apache.commons.lang3.StringUtils;

import ygoParsers.YgoBoosterPackParser;
import ygoParsers.YgoCardParser;
import ygoParsers.YgoYearParser;

/**
 * @author Alec Pierce
 *	<p>
 *	Created: 10/13/2018
 *	Select a date range after running the application so two random decks may be generated.
 *	</p>
 */
@SuppressWarnings("serial")
public class YgoRandomizer extends JFrame implements ItemListener {

	// frame 
	static JFrame f; 

	// label 
	static JLabel l, l1, L2, L3;

	// combo box 
	@SuppressWarnings("rawtypes")
	static JComboBox c1, c2; 
	
	Map<String, String> yearRange;
	
	String min = "";
	String max = "";
	List<String> cardList = new ArrayList<String>();
	
	public static void main(String[] args) {
		// used the code example from the following link as a reference when creating my JFrame:
		// https://www.geeksforgeeks.org/java-swing-jcombobox-examples/
		// create a new frame 
		f = new JFrame("frame"); 

		// create a object 
		YgoRandomizer s = new YgoRandomizer(); 

		// set layout of frame 
		f.setLayout(new FlowLayout()); 
        
        // check boxes
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel checkBoxPanel = new JPanel();

        mainPanel.add(checkBoxPanel);

		// array of string containing years
        int startYear = 2002;
        String year = "";
        ArrayList<String> yearOptions = new ArrayList<String>();
        yearOptions.add("Default");
        Date date = new Date();
        int currentYear = date.getYear() + 1900;
        for (int i = startYear; i <= currentYear; i++) {
			year = String.valueOf(i);
			yearOptions.add(year);
		}
        String s1[] = new String[yearOptions.size()];              
		for(int j = 0; j < yearOptions.size(); j++){
		  s1[j] = yearOptions.get(j);
		}

		// create checkbox 
		c1 = new JComboBox(s1); 
		c2 = new JComboBox(s1);

		// add ItemListener 
		c1.addItemListener(s); 
		c2.addItemListener(s);

		// create labels 
		l = new JLabel("Select the minimum year for the range "); 
		l1 = new JLabel("Default minimum year selected"); 
		L2 = new JLabel("Select the maximum year for the range ");
		L3 = new JLabel("Default maximum year selected");

		// set color of text 
		l.setForeground(Color.red); 
		l1.setForeground(Color.blue); 
		L2.setForeground(Color.red);
		L3.setForeground(Color.blue);

		// create a new panel 
		JPanel p = new JPanel(); 
		JPanel p2 = new JPanel();

		p.add(l); 
		p2.add(L2);

		// add combobox to panel 
		p.add(c1); 
		p2.add(c2);

		p.add(l1);
		p2.add(L3);

		// add panel to frame 
		f.add(mainPanel);
		f.add(p); 
		f.add(p2);

		// set the size of frame 
		f.setSize(600, 600); 

		f.show(); 
		
		f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
	
	public void itemStateChanged (ItemEvent e) 
	{ 
		yearRange = new HashMap<String, String>();
		// if the state combobox is changed 
		if (e.getSource() == c1) { 
			l1.setText(c1.getSelectedItem() + " selected"); 
			min = c1.getSelectedItem().toString();
		} 
		if (e.getSource() == c2) {
			L3.setText(c2.getSelectedItem() + " selected");
			max = c2.getSelectedItem().toString();
		}
		if (StringUtils.isNotBlank(min) && StringUtils.isNotBlank(max)) {
			yearRange.put("min", min);
			yearRange.put("max", max);
			cardList = new ArrayList<String>();
			Map<String, List<String>> setsWithCards = new LinkedHashMap<String, List<String>>();
			Map<String, List<String>> setsToUse = new LinkedHashMap<String, List<String>>();
			Map<String, List<String>> boostersToUse = new LinkedHashMap<String, List<String>>();
			YgoCardParser ygoCardParser = new YgoCardParser();
			setsWithCards = ygoCardParser.createSets();
			YgoBoosterPackParser ygoBoosterPackParser = new YgoBoosterPackParser();
			Map<String, List<String>> yearsAndBoosterPacks = ygoBoosterPackParser.createBoosterPackList();
			YgoYearParser ygoYearParser = new YgoYearParser();
			setsToUse = ygoYearParser.filterByYear(yearRange, setsWithCards);
			boostersToUse = ygoYearParser.filterByYear(yearRange, yearsAndBoosterPacks);
			addBoosterSets(boostersToUse, setsWithCards, setsToUse);
			addToCardList(setsToUse);
			createDecks(cardList);
		}
	} 
	
	private void addBoosterSets(Map<String, List<String>> boostersToUse, Map<String, List<String>> setsWithCards,
			Map<String, List<String>> setsToUse) {
		for (Entry<String, List<String>> boosterEntry : boostersToUse.entrySet()) {
			List<String> boosters = boosterEntry.getValue();
			for (String boosterName : boosters) {
				for (Entry<String, List<String>> setsEntry : setsWithCards.entrySet()) {
					if (StringUtils.equalsIgnoreCase(boosterName, setsEntry.getKey())) {
						setsToUse.put(setsEntry.getKey(), setsEntry.getValue());
					}
				}
			}
		}
	}

	private void addToCardList (Map<String, List<String>> setsWithCards) {
		for (Entry<String, List<String>> entry : setsWithCards.entrySet()) {
			cardList.addAll(entry.getValue());
		}
	}
	
	public void createDecks(List<String> cardList) {
		try {
			PrintWriter deck = new PrintWriter("C:\\Decks\\randomDeck.txt", "UTF-8");
			PrintWriter deck2 = new PrintWriter("C:\\Decks\\randomDeck2.txt", "UTF-8");
			
			deck.flush(); // new deck
			deck2.flush(); // new deck2
			
			// For deck 1
			Random rand = new Random();
			long seed = rand.nextLong();
			if (seed < 0) {
				seed = seed * -1;
			}
			while (seed > cardList.size()) {
				seed = (seed/2);
			}
			rand.setSeed(seed);
			
			// For deck 2
			Random rand2 = new Random();
			long seed2 = rand2.nextLong();
			if (seed2 < 0) {
				seed2 = seed2 * -1;
			}
			while (seed2 > cardList.size()) {
				seed2 = (seed2/2);
			}
			rand2.setSeed(seed2);
			
			createRandomDecks(cardList, deck, deck2, rand, rand2, seed, seed2);
			deck.close();
			deck2.close();
		} catch (FileNotFoundException fileNotFoundException) {
			fileNotFoundException.printStackTrace();
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	/**
	 * Creates both of the random decks by grabbing cards using indexes, and writing the retrieved cards to their respective files.
	 * @param cardList
	 * @param deck
	 * @param deck2
	 * @param rand
	 * @param seed
	 * @param seed2
	 */
	private static void createRandomDecks(List<String> cardList, PrintWriter deck, PrintWriter deck2, Random rand, Random rand2,
			long seed, long seed2) {
		// Creates random deck 1 and 2
		int deckCounter = 0;
		int deckSize = 55;
		while (deckCounter < deckSize) {
			int index = rand.nextInt((int) seed); // creates a pseudo-random index
			String card = cardList.get(index);
			deck.write(card);
			deck.println(); // after adding the card to the deck, terminate the current line
			index = rand2.nextInt((int) seed2);
			card = cardList.get(index);
			deck2.write(card);
			deck2.println();
			deckCounter++;
		}
	}
}
