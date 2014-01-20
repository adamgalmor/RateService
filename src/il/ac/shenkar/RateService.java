package il.ac.shenkar;

import java.util.Hashtable;

/**
 * RESTful Client Interface
 * 
 * Gets rates from Bank of Israel, via its RESTful web service.
 * 
 */
public interface RateService {
    /**
     * Currency code(Alphabetic) to RESTful code(Numeric) dictionary.
     */
    static Hashtable<String, String> currencyCodes = new Hashtable<String, String>() {
	private static final long serialVersionUID = 1L;
	{
	    put("USD", "01");
	    put("GBP", "02");
	    put("SEK", "03");
	    put("CHF", "05");
	    put("CAD", "06");
	    put("DKK", "12");
	    put("ZAR", "17");
	    put("AUD", "18");
	    put("EUR", "27");
	    put("NOK", "28");
	    put("JPY", "31");
	    put("JOD", "69");
	    put("LBP", "70");
	    put("EGP", "79");
	}
    };

    /**
     * @param code Country code to get rate for
     * @return Currency
     * @throws Exception 
     */
    Currency GetRate(String code) throws Exception;

    /**
     * @return Array of all currencies
     * @throws Exception 
     */
    Currency[] GetAllRates() throws Exception;
}
