package cs1302.api;

import com.google.gson.annotations.SerializedName;

/**
 * Represents a response from the Abstract API. This is used by Gson to
 * create an object from the JSON response body.
 */
public class IPData {
//    String ipAddress;
    String city;
    String region;
    @SerializedName("region_iso_code") String regionIsoCode;
    String country;
    @SerializedName("country_code") String countryCode;
    Currency currency;
} // UserIPAddress
