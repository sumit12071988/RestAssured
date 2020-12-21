package test;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;

/**
 * http://coop.apps.symfonycasts.com/api
 * @author Sumit Saha
 */
public class b_OAuth2_AutomatedUsingGrantType_ClientCredentials {
	
	public String accessToken;
	
	
	
	/**
	 * Here we're generating ACCESS TOKEN
	 * This ACCESS TOKEN is only applicable for scope: chickens-feed, for which the app is made
	 */
	@Test(priority = 1)
	public void accessTokenGenerator() {
		Response resp = RestAssured
							.given()
								.formParam("client_id", "SumitApp")
								.formParam("client_secret","28cb0c03f92d6db4fc9139cfec0aa6e8")
								.formParam("grant_type","client_credentials")
							.when().log().all()
								.post("http://coop.apps.symfonycasts.com/token");
		
		// To fetch the entire JSON output
		String responseJSON = resp.jsonPath().prettify();
		System.out.println("JSON O/P Status Code of Initial Request: "+responseJSON);
		
		// To fetch only ACCESS TOKEN value using from JSON Response
		accessToken = resp.jsonPath().get("access_token");	// access_token is the KEY in JSON O/P
		
		System.out.println("Access Token is: "+accessToken);	// access token can now be used for any subsequent API calls.
	}
	
	
	
				
	/**
	 * VALID SCENARIO | Using ACCESS TOKEN for correct Scope: chicken-feed	
	 */
	@Test(priority = 2)
	public void validScenarioUsingAccessToken() {	
		// ****************** Use ACCESS TOKEN in subsequent requests **************************
		Response resp = RestAssured
				.given()
					.auth()
					.oauth2(accessToken)	// accessToken we've received from previous test
				.when().log().all()
					.post("http://coop.apps.symfonycasts.com/api/1536/chickens-feed");
		
		System.out.println("JSON O/P BODY of SubSequent Request with VALID SCOPE: "+resp.getBody().asString());

		Assert.assertEquals(resp.getStatusCode(),200);
	}
	
	
	
	/**
	 * INVALID SCENARIO | Using ACCESS TOKEN for incorrect Scope: eggs-collect
	 */
	@Test(priority = 3)
	public void invalidScenarioUsingAccessToken() {	
		// ****************** Use ACCESS TOKEN in subsequent requests **************************
		Response resp = RestAssured
				.given()
					.auth()
					.oauth2(accessToken)	// accessToken we've received from previous test
				.when().log().all()
					.post("http://coop.apps.symfonycasts.com/api/1536/eggs-collect");
		
		System.out.println("JSON O/P BODY of SubSequent Request with INVALID SCOPE: "+resp.getBody().asString());
		
		Assert.assertEquals(resp.getStatusCode(),401); // Status code for incorrect response
	}
	
}
