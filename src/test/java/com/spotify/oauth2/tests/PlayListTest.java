package com.spotify.oauth2.tests;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.spotify.oauth2.pojo.Playlist;
import com.spotify.oauth2.utils.DataLoader;
import com.spotify.oauth2.api.StatusCode;
import com.spotify.oauth2.api.applicationApi.PlaylistApi;
import com.spotify.oauth2.pojo.Error;
import com.spotify.oauth2.pojo.InnerError;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Issue;
import io.qameta.allure.Link;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import io.qameta.allure.TmsLink;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static com.spotify.oauth2.api.SpecBuilder.getRequestSpec;
import static com.spotify.oauth2.api.SpecBuilder.getResponseSpec;
import static com.spotify.oauth2.utils.FakerUtils.generateName;
import static com.spotify.oauth2.utils.FakerUtils.generateDescription;
@Epic("Spotify Oauth 2.0")
@Feature("Playlist api")
public class PlayListTest  extends BaseTest{
	
	@Story("Create a playlist story")
	@Link("https://example.org")
	@Link(name = "allure", type = "mylink")
	@TmsLink("12345")
	@Issue("123456789")
	@Description("This is the description")	
	@Test(description= "Should be create a playlist")
	public void ShouldBeableTocreatePlaylist() {
		Playlist requestPlaylist = playlistBuilder(generateName(),generateDescription(),false);
		Response response = PlaylistApi.post(requestPlaylist);
		assertStatusCode(response.statusCode(),201);
		assertPlaylistEqual(response.as(Playlist.class),requestPlaylist);
		
		}

	@Test
	public void ShouldBeableToGetAPlaylist() {
		Playlist requestPlaylist = playlistBuilder("Updated Playlist Name","Updated playlist description",false);
		Response response = PlaylistApi.get(DataLoader.getInstance().getPlaylistId());
		assertStatusCode(response.statusCode(),200);
		assertPlaylistEqual(response.as(Playlist.class),requestPlaylist);
		}
	@Test
	public void ShouldBeableToUpdateAPlaylist() {
		Playlist requestPlaylist = playlistBuilder(generateName(),generateDescription(),false);
		Response response = PlaylistApi.update(DataLoader.getInstance().getupdatePlaylistId(), requestPlaylist);
		assertStatusCode(response.statusCode(),200);
		}
	@Story("Create a playlist story")
	@Test
	public void ShouldnotBeableTocreatePlaylistWithoutname() {
		Playlist requestPlaylist = playlistBuilder("",generateDescription(),false);
		Response response = PlaylistApi.post(requestPlaylist);
		assertStatusCode(response.statusCode(),400);
		assertError(response.as(Error.class),400,"Missing required field: name");
			}
	@Story("Create a playlist story")
	@Test
	public void ShouldnotBeableTocreatePlaylistWithExpiredToken() {
		String invalid_token = "12345";
		Playlist requestPlaylist = playlistBuilder(generateName(),generateDescription(),false);
		Response response =PlaylistApi.post(invalid_token,requestPlaylist);
		assertStatusCode(response.statusCode(),401);
		assertError(response.as(Error.class),401,"Invalid access token");
		}
	
	@Step()
	public Playlist playlistBuilder(String name,String description,boolean _public) {
		Playlist playlist = new Playlist();
		playlist.setName(name);
		playlist.setDescription(description);
		playlist.set_public(_public);
		return playlist;
		}
	@Step()
	public void assertPlaylistEqual(Playlist responsePlaylist,Playlist requestPlaylist) {
		assertThat(responsePlaylist.getName(),equalTo(requestPlaylist.getName()));
		assertThat(responsePlaylist.getDescription(),equalTo(requestPlaylist.getDescription()));
		assertThat(responsePlaylist.get_public(),equalTo(requestPlaylist.get_public()));
		}
	@Step()
	public void assertStatusCode(int actualStatusCode, int expectedStatusCode) {
		assertThat(actualStatusCode,equalTo(expectedStatusCode));
		
		}
	
	public void assertError(Error responseErr,  int expectedStatusCode, String expectedMsg) {
		assertThat(responseErr.getError().getStatus(),equalTo(expectedStatusCode));
		assertThat(responseErr.getError().getMessage(),equalTo(expectedMsg));
		}
	
}
