package in.wptrafficanalyzer.navigationdrawerdemo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static in.wptrafficanalyzer.navigationdrawerdemo.LoginActivity.basicAuth;

/**
 * Created by user on 2016. 12. 13..
 */


public class Post extends Activity {


    TextView title;
    WebView content;
    ProgressDialog progressDialog;
    Gson gson;
    Map<String, Object> mapPost;
    Map<String, Object> mapTitle;
    Map<String, Object> mapContent;
    String URL;
    static String id1;
    Button bn;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post);


        bn = (Button) findViewById(R.id.requestBtn);
        bn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendShare();
            }
        });


        final String id1 = getIntent().getExtras().getString("id");

        title = (TextView) findViewById(R.id.title);
        content = (WebView) findViewById(R.id.content);
        /*
        ImageResponse detail = (new Gson()).fromJson(mapContent.toString(), ImageResponse.class);
        List<String> images = detail.lists;
        Context context = this;
        ImageView imageView = null;
        Picasso.with(this).load(String.valueOf(images)).into(imageView);
        */

        progressDialog = new ProgressDialog(Post.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        final String url = "http://btest.soyo.or.kr/wp-json/wp/v2/posts/" + id1 + "?fields=title,content,link";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {





            @Override
            public void onResponse(String s) {
                gson = new Gson();
                mapPost = (Map<String, Object>) gson.fromJson(s, Map.class);
                mapTitle = (Map<String, Object>) mapPost.get("title");
                mapContent = (Map<String, Object>) mapPost.get("content");
                String data = mapContent.get("rendered").toString();





                title.setText(mapTitle.get("rendered").toString());
                //content.loadData(mapContent.get("rendered").toString(),"text/html","UTF-8");
                content.loadDataWithBaseURL(url, getHtmlData(data), "text/html", "UTF-8", "ISO-8859-1");



                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
                Toast.makeText(Post.this, id1, Toast.LENGTH_LONG).show();
            }



        }) {

            @Override
            public Map getHeaders() throws AuthFailureError {
                Map params = new HashMap();
                params.put("Authorization", basicAuth);

                return params;
            }

        };








        RequestQueue rQueue = Volley.newRequestQueue(Post.this);
        rQueue.add(request);

    }


    private String getHtmlData(String bodyHTML) {
        String head = "<head><style>img{max-width: 100%; width:auto; height: auto;}</style></head>";
        return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
    }


    private void sendShare() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");

        URL = (String) mapPost.get("link");


        List<ResolveInfo> resInfo = getPackageManager().queryIntentActivities(intent, 0);
        if (resInfo.isEmpty()) {
            return;
        }

        List<Intent> shareIntentList = new ArrayList<Intent>();

        for (ResolveInfo info : resInfo) {
            Intent shareIntent = (Intent) intent.clone();

            if (info.activityInfo.packageName.toLowerCase().equals("com.facebook.katana")) {
                //facebook
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, URL);
//                shareIntent.setType("image/jpg");
//                shareIntent.putExtra(Intent.EXTRA_STREAM,  Uri.parse("file:///"+mImagePath));
            } else {
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, mapTitle.get("rendered").toString());
                shareIntent.putExtra(Intent.EXTRA_TEXT, URL);
                //shareIntent.putExtra(Intent.EXTRA_STREAM,  Uri.parse("file:///"+mImagePath));
            }
            shareIntent.setPackage(info.activityInfo.packageName);
            shareIntentList.add(shareIntent);
        }

        Intent chooserIntent = Intent.createChooser(shareIntentList.remove(0), "select");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, shareIntentList.toArray(new Parcelable[]{}));
        startActivity(chooserIntent);
    }
}







