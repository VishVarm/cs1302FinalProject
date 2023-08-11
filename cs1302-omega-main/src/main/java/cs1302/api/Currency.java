package cs1302.api;

import com.google.gson.annotations.SerializedName;

/**
 * Represents a response from the Abstract API. This is used by Gson to
 * create an object from the JSON response body.
 */
public class Currency {
    @SerializedName("currency_code") String currencyCode;

} // Currency
