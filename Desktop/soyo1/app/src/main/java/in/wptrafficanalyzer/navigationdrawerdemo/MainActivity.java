package in.wptrafficanalyzer.navigationdrawerdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;



public class MainActivity extends Activity {


	// Within which the entire activity is enclosed
	DrawerLayout mDrawerLayout;

	// ListView represents Navigation Drawer
	ListView mDrawerList;

	// ActionBarDrawerToggle indicates the presence of Navigation Drawer in the action bar
	ActionBarDrawerToggle mDrawerToggle;

	// Title of the action bar
	String mTitle = "";

	private FirebaseAnalytics mFirebaseAnalytics;


	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//Firebase Analytics
		mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);





		//Network state

		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo Info = cm.getActiveNetworkInfo();
		Context context = this;

		if (Info == null || !Info.isConnectedOrConnecting()) {
			Toast.makeText(getApplicationContext(), "인터넷 연결이 끊어졌습니다", Toast.LENGTH_LONG).show();
		} else {
			int netType = Info.getType();
			int netSubtype = Info.getSubtype();

			if (netType == ConnectivityManager.TYPE_WIFI) {
				Toast.makeText(getApplicationContext(), "WiFi에 연결되었습니다", Toast.LENGTH_LONG).show();
				WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
				int linkSpeed = wifiManager.getConnectionInfo().getLinkSpeed();
				//Need to get wifi strength
			} else if (netType == ConnectivityManager.TYPE_MOBILE) {
				Toast.makeText(getApplicationContext(), "모바일 네트워크에 연결되었습니다.", Toast.LENGTH_LONG).show();
				//Need to get differentiate between 3G/GPRS
			}
		}
















		//<---------Drawer--------->


		mTitle = (String) getTitle();


		// Getting reference to the DrawerLayout
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);


		mDrawerList = (ListView) findViewById(R.id.drawer_list);

		// Getting reference to the ActionBarDrawerToggle
		mDrawerToggle = new ActionBarDrawerToggle(this,
				mDrawerLayout,
				R.drawable.ic_drawer,
				R.string.drawer_open,
				R.string.drawer_close) {

			/** Called when drawer is closed */
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu();

			}

			/** Called when a drawer is opened */
			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle("메뉴");
				invalidateOptionsMenu();
			}

		};

		// Setting DrawerToggle on DrawerLayout
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		// Creating an ArrayAdapter to add items to the listview mDrawerList
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				getBaseContext(),
				R.layout.drawer_list_item,
				getResources().getStringArray(R.array.menu)
		);

		// Setting the adapter on mDrawerList
		mDrawerList.setAdapter(adapter);
		// Enabling Home button
		getActionBar().setHomeButtonEnabled(true);

		// Enabling Up navigation
		getActionBar().setDisplayHomeAsUpEnabled(true);

		// Setting item click listener for the listview mDrawerList
		mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {


			@Override
			public void onItemClick(AdapterView<?> parent,
									View view,
									int position,
									long id) {

				// Getting an array of rivers
				String[] rivers = getResources().getStringArray(R.array.menu);

				//Currently selected river
				mTitle = rivers[position];



				if (mTitle == rivers[0]) {
					Intent intent1 = null;
					intent1 = new Intent(getApplicationContext(), SesangActivity.class);
					startActivity(intent1);
				} else if (mTitle == rivers[1]) {
					Intent intent1 = null;
					intent1 = new Intent(getApplicationContext(), CitizenActivity.class);
					startActivity(intent1);
				} else if (mTitle == rivers[2]) {
					Intent intent1 = null;
					intent1 = new Intent(getApplicationContext(), DangerActivity.class);
					startActivity(intent1);
				} else if (mTitle == rivers[3]) {
					Intent intent1 = null;
					intent1 = new Intent(getApplicationContext(), DetoxActivity.class);
					startActivity(intent1);
				} else if (mTitle == rivers[4]) {
					Intent intent1 = null;
					intent1 = new Intent(getApplicationContext(), PlatoActivity.class);
					startActivity(intent1);
				} else if (mTitle == rivers[5]) {
					Intent intent1 = null;
					intent1 = new Intent(getApplicationContext(), FamilyActivity.class);
					startActivity(intent1);
				} else if (mTitle == rivers[6]) {
					Intent intent1 = null;
					intent1 = new Intent(getApplicationContext(), LoginActivity.class);
					startActivity(intent1);
				} else {
					mDrawerLayout.closeDrawer(mDrawerList);
				}


				Bundle bundle = new Bundle();
				bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, mTitle);

				//Logs an app event.
				mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

				//Sets whether analytics collection is enabled for this app on this device.
				mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);

				//Sets the minimum engagement time required before starting a session. The default value is 10000 (10 seconds). Let's make it 20 seconds just for the fun
				mFirebaseAnalytics.setMinimumSessionDuration(20000);

				//Sets the duration of inactivity that terminates the current session. The default value is 1800000 (30 minutes).
				mFirebaseAnalytics.setSessionTimeoutDuration(500);

				//Sets the user ID property.

				//Sets a user property to a given value.
				mFirebaseAnalytics.setUserProperty("메뉴 클릭", mTitle);




				// Creating a Bundle object
				Bundle data = new Bundle();

				// Setting the index of the currently selected item of mDrawerList
				data.putInt("position", position);

				// Closing the drawer
				mDrawerLayout.closeDrawer(mDrawerList);



				//menu별 click수 analytics


				// choose random food name from the list

















			}
		});
	}


	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	/**
	 * Handling the touch event of app icon
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the drawer is open, hide action items related to the content view
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);

		menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


}

