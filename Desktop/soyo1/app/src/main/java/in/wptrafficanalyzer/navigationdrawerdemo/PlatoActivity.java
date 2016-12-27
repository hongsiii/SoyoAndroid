package in.wptrafficanalyzer.navigationdrawerdemo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;

import java.util.List;
import java.util.Map;

public class PlatoActivity extends Activity {
    String url = "http://btest.soyo.or.kr/wp-json/wp/v2/posts?categories=14?filter[posts_per_page]=10&fields=id,title";
    List<Object> list;
    Gson gson;
    ProgressDialog progressDialog;
    ListView postList;
    Map<String,Object> mapPost;
    Map<String,Object> mapTitle;
    int postID;
    String postTitle[];
    private FirebaseAnalytics mFirebaseAnalytics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        postList = new ListView(PlatoActivity.this);

        postList = (ListView)findViewById(R.id.postList);
        progressDialog = new ProgressDialog(PlatoActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                gson = new Gson();
                list = (List) gson.fromJson(s, List.class);
                postTitle = new String[list.size()];

                for(int i=0;i<list.size();++i){
                    mapPost = (Map<String,Object>)list.get(i);
                    mapTitle = (Map<String, Object>) mapPost.get("title");
                    postTitle[i] = (String) mapTitle.get("rendered");
                }

                postList.setAdapter(new ArrayAdapter(PlatoActivity.this, android.R.layout.simple_list_item_1, postTitle));
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(PlatoActivity.this, "Some error occurred", Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(PlatoActivity.this);
        rQueue.add(request);

        postList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mapPost = (Map<String,Object>)list.get(position);
                postID = ((Double)mapPost.get("id")).intValue();

                Intent intent = new Intent(getApplicationContext(),Post.class);
                intent.putExtra("id", ""+postID);

                Bundle bundle = new Bundle();
                bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, postID);

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
                mFirebaseAnalytics.setUserId(String.valueOf(postID));
                startActivity(intent);
            }
        });
    }



}



