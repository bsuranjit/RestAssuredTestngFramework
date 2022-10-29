package com.spotify.oauth2.api;
import java.time.Instant;
import java.util.HashMap;

import com.spotify.oauth2.utils.ConfigLoader;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static com.spotify.oauth2.api.SpecBuilder.getResponseSpec;
import static io.restassured.RestAssured.given;

public class TokenManager {
	private static String access_token;
	private static Instant expiry_time;
	
	public synchronized static String getToken() {
		try {
			if(access_token == null || Instant.now().isAfter(expiry_time)) {
				System.out.println("Renewing token ........");
				Response response = renewToken();
				access_token= response.path("access_token");
				int expiryDurationinsecond = response.path("expires_in");
				expiry_time = Instant.now().plusSeconds(expiryDurationinsecond-300);
			}else {
				System.out.println("Token is good to use");
			}
		}
		catch (Exception e) {
			throw new RuntimeException("Abbort failed to get token");
		}
		return access_token;
}
	private static Response renewToken() {
		HashMap<String, String> formParams = new HashMap<String,String>();
		formParams.put("client_id", ConfigLoader.getInstance().getClientId());
		formParams.put("client_secret",ConfigLoader.getInstance().getClientSecret());
		formParams.put("refresh_token",ConfigLoader.getInstance().getRefreshToken());
		formParams.put("grant_type",ConfigLoader.getInstance().getGrantType());
		Response response = Restresource.postAccount(formParams);
		
	if (response.statusCode()!= 200) {
		throw new RuntimeException("ABORT!!! Renew token failed");
	}
		return response;
		
	}

}
