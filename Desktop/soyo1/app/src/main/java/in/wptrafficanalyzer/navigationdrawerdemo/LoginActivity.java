package in.wptrafficanalyzer.navigationdrawerdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

public class LoginActivity extends Activity {


	Intent intent = null;
	String code;
	static String basicAuth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		Button bn;

		bn = (Button) findViewById(R.id.bn);
		final EditText username = (EditText) findViewById(R.id.editText);
		final EditText password = (EditText) findViewById(R.id.editText2);








		bn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				new Thread() {
					public void run() {


						try {
							URL url = new URL("http://btest.soyo.or.kr/wp-json/wp/v2/users/me");
							HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
							try {
								urlConnection.setRequestMethod("GET");
							} catch (ProtocolException e) {
								e.printStackTrace();
							}
							urlConnection.setConnectTimeout(5000);
							urlConnection.setReadTimeout(2000000);

							String Username = username.getText().toString();
							String Password = password.getText().toString();

							String userPass = Username + ":" + Password;

							code = new String(Base64.encodeToString(userPass.getBytes(), Base64.NO_WRAP));

//fix from SO

							basicAuth = "Basic " + code;
							urlConnection.setRequestProperty("Authorization", basicAuth);
							urlConnection.connect();

							final int responseCode = urlConnection.getResponseCode();
							if (responseCode == HttpURLConnection.HTTP_OK) {
								Handler mHandler = new Handler(Looper.getMainLooper());
								mHandler.postDelayed(new Runnable() {
									@Override
									public void run() {
										Toast toast = Toast.makeText(getApplicationContext(),
												"로그인 성공"+code, Toast.LENGTH_LONG);
										toast.setGravity(Gravity.CENTER, 0, 0);
										toast.show();

									}
								}, 0);

								Intent intent = null;
								intent = new Intent(getApplicationContext(), MainActivity.class);
								startActivity(intent);

							} else {


									Handler mHandler = new Handler(Looper.getMainLooper());
									mHandler.postDelayed(new Runnable() {
										@Override
										public void run() {
											Toast toast = Toast.makeText(getApplicationContext(),
													"로그인 실패"+responseCode+"/"+code, Toast.LENGTH_LONG);
											toast.setGravity(Gravity.CENTER, 0, 0);
											toast.show();

										}
									}, 0);


									Intent intent = null;
								intent = new Intent(getApplicationContext(), LoginActivity.class);
								startActivity(intent);

							}

//throws Exception
							try {
								urlConnection.getResponseCode();
							} catch (IOException e) {
								e.printStackTrace();
							}

						} catch (IOException e) {
							throw new RuntimeException(e);
						}

					}
				}.start();


			}
		});


		/** Called whenever we call invalidateOptionsMenu() */

	}
}
