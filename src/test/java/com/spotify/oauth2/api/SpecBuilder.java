package com.spotify.oauth2.api;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
//import io.qameta.allure.restassured;
import static com.spotify.oauth2.api.Route.BASE_PATH;

public class SpecBuilder{

	public static RequestSpecification getRequestSpec(){
		return  new RequestSpecBuilder().
				setBaseUri("https://api.spotify.com").
				//setBaseUri(System.getProperty("BASE_URI")).
				setBasePath(BASE_PATH).
				//("Authorization", "Bearer " + access_token).
				setContentType(ContentType.JSON).
				//addFilter(new AllureAssured()).
				log(LogDetail.ALL).
				build();
		
	}
	public static RequestSpecification getAccountRequestSpec(){
		return  new RequestSpecBuilder().
				setBaseUri("https://accounts.spotify.com").
				//setBaseUri(System.getProperty("ACCOUNT_BASE_URI")).
				//setBasePath("/v1").
				//("Authorization", "Bearer " + access_token).
				setContentType(ContentType.URLENC).
				log(LogDetail.ALL).
				build();
		
	}
	public static ResponseSpecification getResponseSpec() {
		return new ResponseSpecBuilder().
				//expectContentType(ContentType.JSON).
				log(LogDetail.ALL).
				build();
		
		
	}

}
