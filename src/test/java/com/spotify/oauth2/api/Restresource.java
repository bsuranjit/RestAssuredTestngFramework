package com.spotify.oauth2.api;

import static com.spotify.oauth2.api.SpecBuilder.getRequestSpec;
import static com.spotify.oauth2.api.SpecBuilder.getResponseSpec;
import static com.spotify.oauth2.api.SpecBuilder.getAccountRequestSpec;
import static com.spotify.oauth2.api.Route.API;
import static com.spotify.oauth2.api.Route.TOKEN;
import static io.restassured.RestAssured.given;

import java.util.HashMap;

import com.spotify.oauth2.pojo.Playlist;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class Restresource {
	
	
	public static Response post(String path, String token, Object requestPlaylist) {
		return  given(getRequestSpec()).
				body(requestPlaylist).
				auth().oauth2(token).
				//header("Authorization","Bearer " + token).
		when().post(path).
		then().spec(getResponseSpec()).
			extract().
			response();
	}
	
	public static Response postAccount(HashMap<String,String> formParams) {
		return  given(getAccountRequestSpec()).
				//baseUri("https://accounts.spotify.com").
				//contentType(ContentType.URLENC).
				formParams(formParams).
				when().post(API + TOKEN).
				then().spec(getResponseSpec()).
				extract().
				response();
	}
	
	public static Response get(String path,String token) {
		return given(getRequestSpec()).
				auth().oauth2(token).
				//header("Authorization","Bearer " + token).
		when().get(path).
		then().spec(getResponseSpec()).
			extract().
			response();
	}
	public static Response update(String path, String token, Object requestPlaylist) {
		return given(getRequestSpec()).
				auth().oauth2(token).
				//header("Authorization","Bearer " + token).
				body(requestPlaylist).
				when().put(path).
		then().spec(getResponseSpec()).
				extract().
				response();
	}
}
