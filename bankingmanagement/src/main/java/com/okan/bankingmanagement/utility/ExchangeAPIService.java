package com.okan.bankingmanagement.utility;

import java.io.IOException;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ExchangeAPIService {

	 @Value("${collect.api.token}")
	    private String accessToken;

	    
	    public double getUsdTryExchangeRate() throws JSONException, IOException {
	        String url = "https://api.collectapi.com/economy/singleCurrency?int=1&tag=USD";
	        return HttpUtil.sendGetRequest(url, accessToken)
	                .getJSONArray("result")
	                .getJSONObject(0)
	                .getDouble("selling");
	    }

	   
	    public double getXauTryExchangeRate() throws JSONException, IOException {
	        String url = "https://api.collectapi.com/economy/goldPrice";
	        return HttpUtil.sendGetRequest(url, accessToken)
	                .getJSONArray("result")
	                .getJSONObject(0)
	                .getDouble("selling");
	    }
	
}
