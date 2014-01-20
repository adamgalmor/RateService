package il.ac.shenkar;

import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class JavaRateService implements RateService {

    /**
     * Utility method to get text node contents
     * 
     * @param el
     *            XML Element to extract from
     * @param name
     *            Node to extract text from
     * @return String The contents of the element's inner text node
     */
    private String FromNodeValue(Element el, String name) {
	return el.getElementsByTagName(name).item(0).getFirstChild()
		.getNodeValue();
    }

    @Override
    public Currency[] GetAllRates() throws Exception {
	DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance()
		.newDocumentBuilder();
	Document doc;
	doc = dBuilder
		.parse("http://www.boi.org.il/currency.xml");
	NodeList currencies = doc.getElementsByTagName("CURRENCY");
	Currency result[] = new Currency[currencies.getLength()];

	for (int i = 0; i < currencies.getLength(); i++) {
	    Element el = (Element) currencies.item(i);
	    result[i] = new Currency();

	    result[i].name = FromNodeValue(el, "NAME");
	    result[i].country = FromNodeValue(el, "COUNTRY");
	    result[i].currencyCode = FromNodeValue(el, "CURRENCYCODE");
	    result[i].unit = Integer.parseInt(FromNodeValue(el, ("UNIT")));
	    result[i].rate = Double.parseDouble(FromNodeValue(el, ("RATE")));
	    result[i].change = Double
		    .parseDouble(FromNodeValue(el, ("CHANGE")));
	}
	return result;
    }

    @Override
    public Currency GetRate(String currencyCode) throws Exception {
	String code = RateService.currencyCodes.get(currencyCode);
	URL url = new URL(
		"http://www.boi.org.il/currency.xml?curr="
			+ code);

	DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance()
		.newDocumentBuilder();

	Document doc = dBuilder.parse(url.openStream());
	Element root = doc.getDocumentElement();
	Element el = (Element) root.getElementsByTagName("CURRENCY").item(0);

	Currency c = new Currency();

	c.name = FromNodeValue(el, "NAME");
	c.country = FromNodeValue(el, "COUNTRY");
	c.currencyCode = FromNodeValue(el, "CURRENCYCODE");
	c.unit = Integer.parseInt(FromNodeValue(el, ("UNIT")));
	c.rate = Double.parseDouble(FromNodeValue(el, ("RATE")));
	c.change = Double.parseDouble(FromNodeValue(el, ("CHANGE")));

	return c;
    }
}
