package com.spotify.oauth2.api.applicationApi;

//import static com.spotify.oauth2.api.SpecBuilder.getRequestSpec;
//import static com.spotify.oauth2.api.SpecBuilder.getResponseSpec;
import static com.spotify.oauth2.api.TokenManager.getToken;
//import static io.restassured.RestAssured.given;
import static  com.spotify.oauth2.api.Route.USERS;
import static com.spotify.oauth2.api.Route.PLAYLISTS;
import static com.spotify.oauth2.api.Route.BASE_PATH;
import static com.spotify.oauth2.api.Route.TOKEN;
import static com.spotify.oauth2.api.Route.API;

import com.spotify.oauth2.api.Restresource;
import com.spotify.oauth2.pojo.Playlist;
import com.spotify.oauth2.utils.ConfigLoader;

import io.qameta.allure.Step;
import io.restassured.response.Response;

public class PlaylistApi {
	
	@Step()
	public static Response post(Playlist requestPlaylist) {
		//return Restresource.post("/users/31whbf4p4ftvebp6cnzrpyyll44i/playlists", getToken(), requestPlaylist);
		//return Restresource.post(USERS + "/31whbf4p4ftvebp6cnzrpyyll44i" + PLAYLISTS , getToken(), requestPlaylist);
		return Restresource.post(USERS + "/" + ConfigLoader.getInstance().getUserId() 
				+ PLAYLISTS , getToken(), requestPlaylist);
		
	}
	public static Response post(String token, Playlist requestPlaylist) {
		//return Restresource.post("/users/31whbf4p4ftvebp6cnzrpyyll44i/playlists", token, requestPlaylist);
		//return Restresource.post(USERS + "/31whbf4p4ftvebp6cnzrpyyll44i" + PLAYLISTS , token, requestPlaylist);
		return Restresource.post(USERS + "/" + ConfigLoader.getInstance().getUserId() 
		+ PLAYLISTS , token, requestPlaylist);
		
	}
	public static Response get(String playlistId) {
		//return Restresource.get("/playlists/" + playlistId, getToken());
		return Restresource.get( PLAYLISTS + "/" + playlistId, getToken());
		
	}
	public static Response update(String playlistId, Playlist requestPlaylist) {
		return Restresource.update(PLAYLISTS + "/" + playlistId,getToken(), requestPlaylist);
		
	}
}
