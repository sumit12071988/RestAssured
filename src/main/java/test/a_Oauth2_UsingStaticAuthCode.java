package test;

import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;

/**
 * 
 * @author Sumit Saha
 * Access Token: a62dfaf24c024d09448c71146b2d93fc10a97247
 */
public class a_Oauth2_UsingStaticAuthCode {
	
	@Test
	public void test1() {
		
		Response resp = RestAssured
							.given()
							.auth()
							.oauth2("a62dfaf24c024d09448c71146b2d93fc10a97247")
							.post("http://coop.apps.symfonycasts.com/api/1536/chickens-feed");
		
		System.out.println("Code "+resp.getStatusCode());
		//System.out.println("Code "+resp.jsonPath().prettify());
		
		System.out.println("Code "+resp.getBody().asString());
	}

}
