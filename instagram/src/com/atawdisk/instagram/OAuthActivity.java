package com.atawdisk.instagram;

import org.gerstner.oauth2android.Client;
import org.gerstner.oauth2android.OAuth;
import org.gerstner.oauth2android.Server;
import org.gerstner.oauth2android.common.Connection;
import org.gerstner.oauth2android.token.BearerTokenTypeDefinition;
import org.gerstner.oauth2android.token.TokenTypeDefinition;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class OAuthActivity extends Activity {
	
	public final static String TOKEN = "token";
	public final static int REQUESTOAUTH = 88;
	private String clientId;
	private String clientSecret;
	private String redirectUri;
    private OAuth oAuth;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Intent intent = this.getIntent();
        clientId = intent.getExtras().getString("clientId");
        clientSecret = intent.getExtras().getString("clientSecret");
        redirectUri = intent.getExtras().getString("redirectUri");
        
        
        Client  client = new Client(clientId, clientSecret, redirectUri);
        
        Server server = new Server("https://instagram.com/oauth/authorize/", "https://instagram.com/oauth/authorize/", "https://api.instagram.com/v1");
        
   	  	TokenTypeDefinition tokenTypeDefinition = new BearerTokenTypeDefinition();  // for BEARER Tokens
        
        oAuth = new OAuth( server, client, tokenTypeDefinition );
        
        server.setPreferredHttpMethod( Connection.HTTP_METHOD_GET );
        server.useAuthorizationHeader(false);
   	
   	 	String authorizationUri =	oAuth.returnAuthorizationRequestUri();
        
        authorizationUri = authorizationUri.replace("code&", "token&");
        
        Uri uri = Uri.parse( authorizationUri );
        Intent web = new Intent( Intent.ACTION_VIEW, uri );
        startActivity( web );
    }
   
    @Override
    protected void onNewIntent(Intent intent) {
            	
        Uri uri = intent.getData();
        if (uri != null) {
        	String path = uri.toString();
        	String accessToken = path.substring(path.indexOf("=") + 1);
        	SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        	SharedPreferences.Editor editor = pref.edit();
    		editor.putString(TOKEN, accessToken);
    		editor.commit();
        }
        finish();
    }
}