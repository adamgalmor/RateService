package il.ac.shenkar

import java.net.URL
import scala.xml.XML

class ScalaRateService extends RateService {
  def GetAllRates(): Array[Currency] = {
    var url = new URL("http://www.boi.org.il/currency.xml")
    var xml = XML.load(url.openConnection.getInputStream)
    var cnodes = xml \ "CURRENCY"
    var results: Array[Currency] = Array[Currency]()
    for (node <- cnodes) {
      var c: Currency = new Currency
      c.name = (node \ "NAME").text
      c.currencyCode = (node \ "CURRENCYCODE").text
      c.unit = (node \ "UNIT").text.toInt
      c.country = (node \ "COUNTRY").text
      c.rate = (node \ "RATE").text.toDouble
      c.change = (node \ "CHANGE").text.toDouble
      results = results :+ c
    }
    results
  }

  def GetRate(cc: String): Currency = {
    var code: String = RateService.currencyCodes.get(cc)
    var xml = XML.load(new URL("http://www.boi.org.il/currency.xml?curr=" + code))
    var node = (xml \\ "CURRENCY")(0)
    var c: Currency = new Currency
    c.name = (node \ "NAME").text
    c.currencyCode = (node \ "CURRENCYCODE").text
    c.unit = (node \ "UNIT").text.toInt
    c.country = (node \ "COUNTRY").text
    c.rate = (node \ "RATE").text.toDouble
    c.change = (node \ "CHANGE").text.toDouble
    c
  }
}