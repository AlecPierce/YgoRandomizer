package ygoParsers;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * @author Alec Pierce
 *	<p>
 *	Created: 10/13/2018
 *	</p>
 */
public class YgoParser {
	private Document doc;
	
	public YgoParser (String pathway) {
		try {
			if (StringUtils.isNotBlank(pathway)) {
				// if a connection is made successfully without errors
				// else try to parse a file
				setDocument(loadDocumentFromURL(pathway));
				if (noDocumentFound()) {
					setDocument(loadDocumentFromFile(pathway));
				}
			} else {
				IllegalArgumentException invalidArgumentException = new IllegalArgumentException("URL or file path provided was invalid.");
				throw invalidArgumentException;
			}
		}
		catch (IllegalArgumentException invalidArgumentException) {
			invalidArgumentException.printStackTrace();
		}
	}
	
	private Document loadDocumentFromURL(String pathway) {
		Document doc = null;
		try {
			doc = Jsoup.connect(pathway).get();
		}
		catch (IOException ioException) {
			ioException.printStackTrace();
		}
		return doc;
	}
	
	private Document loadDocumentFromFile(String pathway) {
		Document doc = null;
		try {
			FileReader file = new FileReader(pathway);
			doc = Jsoup.parse(file.toString());
			file.close();
		} catch (FileNotFoundException fileNotFoundException) {
			fileNotFoundException.printStackTrace();
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
		return doc;
	}
	
	public Elements getElements(String element) {
		Document docToParse = getDocument();
		System.out.println("Finding " + element);
		Elements elements = docToParse.select(element);
		return elements;
	}
	
	private void setDocument(Document doc) {
		this.doc = doc;
	}
	
	public Document getDocument() {
		return this.doc;
	}
	
	public boolean noDocumentFound() {
		if (getDocument() == null) {
			return true;
		} else {
			return false;
		}
	}
}
