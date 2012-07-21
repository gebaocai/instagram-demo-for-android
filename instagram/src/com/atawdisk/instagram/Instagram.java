package com.atawdisk.instagram;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.gerstner.oauth2android.Client;
import org.gerstner.oauth2android.OAuth;
import org.gerstner.oauth2android.Server;
import org.gerstner.oauth2android.common.Connection;
import org.gerstner.oauth2android.exception.InvalidClientException;
import org.gerstner.oauth2android.exception.InvalidGrantException;
import org.gerstner.oauth2android.exception.InvalidRequestException;
import org.gerstner.oauth2android.exception.InvalidScopeException;
import org.gerstner.oauth2android.exception.InvalidTokenTypeException;
import org.gerstner.oauth2android.exception.OAuthException;
import org.gerstner.oauth2android.exception.UnauthorizedClientException;
import org.gerstner.oauth2android.exception.UnsupportedGrantTypeException;
import org.gerstner.oauth2android.response.Response;
import org.gerstner.oauth2android.token.BearerToken;
import org.gerstner.oauth2android.token.BearerTokenTypeDefinition;
import org.gerstner.oauth2android.token.Token;
import org.gerstner.oauth2android.token.TokenTypeDefinition;

public class Instagram {

	private String clientId;
	private String clientSecret;
	private String redirectUri = "oauth://mydomain";
	private String accessToken;
	private OAuth oAuth;
	
	private final static String authorizationServer = "https://instagram.com/oauth/authorize/";
	private final static String accessTokenServer = "https://instagram.com/oauth/authorize/";
	private final static String resourceServer = "https://api.instagram.com/v1";
	
	public Instagram(String clientId, String clientSecret, String accessToken) {
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.accessToken = accessToken;
	}

	public Instagram(String clientId, String clientSecret, String accessToken, String redirectUri) {
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.accessToken = accessToken;
		this.redirectUri = redirectUri;
	}
	
	private OAuth getOAuth() throws IGramException {
		if (oAuth == null) {
			Client  client = new Client(clientId, clientSecret, redirectUri);
	        Server server = new Server(authorizationServer, accessTokenServer, resourceServer);
	        server.useAuthorizationHeader(false);
	   	  	TokenTypeDefinition tokenTypeDefinition = new BearerTokenTypeDefinition();  // for BEARER Tokens
	        oAuth = new OAuth( server, client, tokenTypeDefinition );
	        if (accessToken == null || accessToken.equals("")) {
	        	throw new IGramException("AccessToken Require");
	        }
	        Token t = new BearerToken(accessToken, "", 0);
        	oAuth.getClient().setAccessToken(t);
	        
		}
		return oAuth;
	}
	
	/**
	 * http://instagram.com/developer/endpoints/users/#get_users
	 * Get basic information about a user.
	 */
	public String getUserInfo(String id) throws IGramException, InvalidRequestException, InvalidClientException,
				InvalidGrantException, UnauthorizedClientException, UnsupportedGrantTypeException, InvalidScopeException,
				InvalidTokenTypeException, OAuthException, IOException {
		OAuth oAuth = getOAuth();
		String requestUri = "/users/"+id;
        List<NameValuePair> parameter = new ArrayList<NameValuePair>();    
        
        Response response = oAuth.executeProtectedResourceRequest( requestUri, parameter, Connection.HTTP_METHOD_GET);
        return response.getResponseString();
	}
	
	/**
	 * http://instagram.com/developer/endpoints/users/#get_users_feed
	 * See the authenticated user's feed.
	 */
	public String getSelfFeed(int count, String minId, String maxId) throws IGramException, InvalidRequestException, InvalidClientException, InvalidGrantException, UnauthorizedClientException, UnsupportedGrantTypeException, InvalidScopeException, InvalidTokenTypeException, OAuthException, IOException {
		OAuth oAuth = getOAuth();
		String requestUri = "/users/self/feed";
        List<NameValuePair> parameter = new ArrayList<NameValuePair>();
        if (count > 0)
        	parameter.add(new BasicNameValuePair("count", String.valueOf(count))); 
        if (minId != null)
        	parameter.add(new BasicNameValuePair("MIN_ID", minId)); 
        if (maxId != null)
        	parameter.add(new BasicNameValuePair("MAX_ID", maxId)); 
        
        Response response = oAuth.executeProtectedResourceRequest( requestUri, parameter, Connection.HTTP_METHOD_GET);
        return response.getResponseString();
	}
	
	/**
	 * http://instagram.com/developer/endpoints/users/#get_users_media_recent
	 * Get the most recent media published by a user.
	 */
	public String getUsersMediaRecent(String id, int count, String maxTimestamp, String minTimestamp, String minId, String maxId) throws IGramException, InvalidRequestException, InvalidClientException, InvalidGrantException, UnauthorizedClientException, UnsupportedGrantTypeException, InvalidScopeException, InvalidTokenTypeException, OAuthException, IOException {
		OAuth oAuth = getOAuth();
		String requestUri = "/users/"+id+"/media/recent";
        List<NameValuePair> parameter = new ArrayList<NameValuePair>();
        if (count > 0)
        	parameter.add(new BasicNameValuePair("count", String.valueOf(count))); 
        if (maxTimestamp != null)
        	parameter.add(new BasicNameValuePair("MAX_TIMESTAMP", maxTimestamp)); 
        if (minTimestamp != null)
        	parameter.add(new BasicNameValuePair("MIN_TIMESTAMP", minTimestamp)); 
        if (minId != null)
        	parameter.add(new BasicNameValuePair("MIN_ID", minId)); 
        if (maxId != null)
        	parameter.add(new BasicNameValuePair("MAX_ID", maxId)); 
        
        Response response = oAuth.executeProtectedResourceRequest( requestUri, parameter, Connection.HTTP_METHOD_GET);
        return response.getResponseString();
	}
	
	/**
	 * http://instagram.com/developer/endpoints/users/#get_users_feed_liked
	 * See the authenticated user's list of media they've liked. Note that 
	 * this list is ordered by the order in which the user liked the media.
	 * Private media is returned as long as the authenticated user has permission
	 * to view that media. Liked media lists are only available for the currently authenticated user.
	 */
	public String getUsersFeedLiked(int count, String maxLikeId) throws IGramException, InvalidRequestException, InvalidClientException, InvalidGrantException, UnauthorizedClientException, UnsupportedGrantTypeException, InvalidScopeException, InvalidTokenTypeException, OAuthException, IOException {
		OAuth oAuth = getOAuth();
		String requestUri = "/users/self/media/liked";
        List<NameValuePair> parameter = new ArrayList<NameValuePair>();
        if (count > 0)
        	parameter.add(new BasicNameValuePair("count", String.valueOf(count))); 
        if (maxLikeId != null)
        	parameter.add(new BasicNameValuePair("MAX_LIKE_ID", maxLikeId)); 
        
        Response response = oAuth.executeProtectedResourceRequest( requestUri, parameter, Connection.HTTP_METHOD_GET);
        return response.getResponseString();
	}
	
	/**
	 * http://instagram.com/developer/endpoints/users/#get_users_search
	 * Search for a user by name.
	 */
	public String getUsersSearch(int count, String query) throws IGramException, InvalidRequestException, InvalidClientException, InvalidGrantException, UnauthorizedClientException, UnsupportedGrantTypeException, InvalidScopeException, InvalidTokenTypeException, OAuthException, IOException {
		OAuth oAuth = getOAuth();
		String requestUri = "/users/self/media/liked";
        List<NameValuePair> parameter = new ArrayList<NameValuePair>();
        if (count > 0)
        	parameter.add(new BasicNameValuePair("count", String.valueOf(count))); 
        if (query != null)
        	parameter.add(new BasicNameValuePair("Q", query)); 
        
        Response response = oAuth.executeProtectedResourceRequest( requestUri, parameter, Connection.HTTP_METHOD_GET);
        return response.getResponseString();
	}
	
	public String getUsersFollows(String id) throws IGramException, InvalidRequestException, InvalidClientException, InvalidGrantException, UnauthorizedClientException, UnsupportedGrantTypeException, InvalidScopeException, InvalidTokenTypeException, OAuthException, IOException {
		OAuth oAuth = getOAuth();
		String requestUri = "/users/"+id+"/follows";
        List<NameValuePair> parameter = new ArrayList<NameValuePair>();
        
        Response response = oAuth.executeProtectedResourceRequest( requestUri, parameter, Connection.HTTP_METHOD_GET);
        return response.getResponseString();
	}
	
	public String getUsersFollowsBy(String id) throws IGramException, InvalidRequestException, InvalidClientException, InvalidGrantException, UnauthorizedClientException, UnsupportedGrantTypeException, InvalidScopeException, InvalidTokenTypeException, OAuthException, IOException {
		OAuth oAuth = getOAuth();
		String requestUri = "/users/"+id+"/followed-by";
        List<NameValuePair> parameter = new ArrayList<NameValuePair>();
        Response response = oAuth.executeProtectedResourceRequest( requestUri, parameter, Connection.HTTP_METHOD_GET);
        return response.getResponseString();
	}
	
	public String getUsersRequestedBy(String id) throws IGramException, InvalidRequestException, InvalidClientException, InvalidGrantException, UnauthorizedClientException, UnsupportedGrantTypeException, InvalidScopeException, InvalidTokenTypeException, OAuthException, IOException {
		OAuth oAuth = getOAuth();
		String requestUri = "/users/"+id+"/requested-by";
        List<NameValuePair> parameter = new ArrayList<NameValuePair>();
        Response response = oAuth.executeProtectedResourceRequest( requestUri, parameter, Connection.HTTP_METHOD_GET);
        return response.getResponseString();
	}
	
	public String getUsersRelationship(String id) throws IGramException, InvalidRequestException, InvalidClientException, InvalidGrantException, UnauthorizedClientException, UnsupportedGrantTypeException, InvalidScopeException, InvalidTokenTypeException, OAuthException, IOException {
		OAuth oAuth = getOAuth();
		String requestUri = "/users/"+id+"/requested-by";
        List<NameValuePair> parameter = new ArrayList<NameValuePair>();
        Response response = oAuth.executeProtectedResourceRequest( requestUri, parameter, Connection.HTTP_METHOD_GET);
        return response.getResponseString();
	}
	
	public String setUsersRelationship(String id, String action) throws IGramException, InvalidRequestException, InvalidClientException, InvalidGrantException, UnauthorizedClientException, UnsupportedGrantTypeException, InvalidScopeException, InvalidTokenTypeException, OAuthException, IOException {
		OAuth oAuth = getOAuth();
		String requestUri = "/users/"+id+"/requested-by";
        List<NameValuePair> parameter = new ArrayList<NameValuePair>();
        parameter.add(new BasicNameValuePair("ACTION", action)); 
        Response response = oAuth.executeProtectedResourceRequest( requestUri, parameter, Connection.HTTP_METHOD_POST);
        return response.getResponseString();
	}
	
	public String getMedia(String id) throws IGramException, InvalidRequestException, InvalidClientException, InvalidGrantException, UnauthorizedClientException, UnsupportedGrantTypeException, InvalidScopeException, InvalidTokenTypeException, OAuthException, IOException {
		OAuth oAuth = getOAuth();
		String requestUri = "/media/"+id;
        List<NameValuePair> parameter = new ArrayList<NameValuePair>();
        Response response = oAuth.executeProtectedResourceRequest( requestUri, parameter, Connection.HTTP_METHOD_GET);
        return response.getResponseString();
	}
	
	/**
	 * 
	 * @param minTimestamp	A unix timestamp. All media returned will be taken later than this timestamp.
	 * @param maxTimestamp	A unix timestamp. All media returned will be taken earlier than this timestamp.
	 * @param lat			Latitude of the center search coordinate. If used, lng is required.
	 * @param lng			Longitude of the center search coordinate. If used, lat is required.
	 * @param distance		Default is 1km (distance=1000), max distance is 5km.
	 * @return
	 * @throws IGramException
	 * @throws InvalidRequestException
	 * @throws InvalidClientException
	 * @throws InvalidGrantException
	 * @throws UnauthorizedClientException
	 * @throws UnsupportedGrantTypeException
	 * @throws InvalidScopeException
	 * @throws InvalidTokenTypeException
	 * @throws OAuthException
	 * @throws IOException
	 */
	public String getMediaSearch(String minTimestamp, String maxTimestamp, String lat, String lng, int distance) throws IGramException, InvalidRequestException, InvalidClientException, InvalidGrantException, UnauthorizedClientException, UnsupportedGrantTypeException, InvalidScopeException, InvalidTokenTypeException, OAuthException, IOException {
		OAuth oAuth = getOAuth();
		String requestUri = "/media/search";
        List<NameValuePair> parameter = new ArrayList<NameValuePair>();
        if (minTimestamp != null)
        	parameter.add(new BasicNameValuePair("MIN_TIMESTAMP", minTimestamp));
        if (maxTimestamp != null)
        	parameter.add(new BasicNameValuePair("MAX_TIMESTAMP", maxTimestamp));
        if (lat != null)
        	parameter.add(new BasicNameValuePair("LAT", lat));
        if (lng != null)
        	parameter.add(new BasicNameValuePair("LNG", lng));
        if (distance > 0)
        	parameter.add(new BasicNameValuePair("DISTANCE", String.valueOf(distance)));
        
        Response response = oAuth.executeProtectedResourceRequest( requestUri, parameter, Connection.HTTP_METHOD_GET);
        return response.getResponseString();
	}
	
	public String getMediaPopular() throws IGramException, InvalidRequestException, InvalidClientException, InvalidGrantException, UnauthorizedClientException, UnsupportedGrantTypeException, InvalidScopeException, InvalidTokenTypeException, OAuthException, IOException {
		OAuth oAuth = getOAuth();
		String requestUri = "/media/popular";
        List<NameValuePair> parameter = new ArrayList<NameValuePair>();
        Response response = oAuth.executeProtectedResourceRequest( requestUri, parameter, Connection.HTTP_METHOD_GET);
        return response.getResponseString();
	}

}
