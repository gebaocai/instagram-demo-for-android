package test;

import java.io.IOException;

import org.gerstner.oauth2android.exception.InvalidClientException;
import org.gerstner.oauth2android.exception.InvalidGrantException;
import org.gerstner.oauth2android.exception.InvalidRequestException;
import org.gerstner.oauth2android.exception.InvalidScopeException;
import org.gerstner.oauth2android.exception.InvalidTokenTypeException;
import org.gerstner.oauth2android.exception.OAuthException;
import org.gerstner.oauth2android.exception.UnauthorizedClientException;
import org.gerstner.oauth2android.exception.UnsupportedGrantTypeException;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.atawdisk.instagram.IGramException;
import com.atawdisk.instagram.Instagram;
import com.atawdisk.instagram.OAuthActivity;
import com.atawdisk.instagram.R;

public class Main extends Activity {
	
	private final static String TAG = Main.class.getSimpleName();  
	
	private final static String clientId = null;			//your clientId get from instagram
	private final static String clientSecret = null;		//your clientSecret get from instagram
	private final static String redirectUri = "oauth://mydomain";
	private String accessToken;
	private TextView status;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main);
		
		Button login = (Button)findViewById(R.id.login);
		login.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Main.this, OAuthActivity.class);
				intent.putExtra("clientId", clientId);
				intent.putExtra("clientSecret", clientSecret);
				intent.putExtra("redirectUri", redirectUri);
				startActivity(intent);
				
			}
		});
		
		Button aboutMe = (Button) findViewById(R.id.aboutme);
		aboutMe.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				getAboutMe();
			}
			
		});
		
		status = (TextView) findViewById(R.id.status);
		
	}
	
	private void getAboutMe() {
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		accessToken = pref.getString(OAuthActivity.TOKEN, null);
		
		if (accessToken == null) {
			Toast.makeText(this, "Please Login First", Toast.LENGTH_SHORT).show();
			Log.d(TAG, "Please Login First");
			return;
		}
		
		Instagram igram = new Instagram(clientId, clientSecret, accessToken, redirectUri);
		try {
			String result = igram.getUsersFeedLiked(2, null);
			status.setText(result);
		} catch (InvalidRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidGrantException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnauthorizedClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedGrantTypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidScopeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidTokenTypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IGramException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OAuthException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
