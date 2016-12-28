package org.androidtown.bibrowser;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;


public class TestWebViewActivity extends Activity {


    private WebView mWebView;
    private Button requestBtn;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setLayout();

        // 웹뷰에서 자바스크립트실행가능
        mWebView.getSettings().setJavaScriptEnabled(true);
        // 구글홈페이지 지정
        mWebView.loadUrl("http://soyo.or.kr");
        // WebViewClient 지정
        mWebView.setWebViewClient(new WebViewClientClass());

        final EditText urlInput = (EditText) findViewById(R.id.urlInput);

        requestBtn = (Button) findViewById(R.id.requestBtn);
        requestBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mWebView.loadUrl(urlInput.getText().toString());
            }
        });







    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }



    private class WebViewClientClass extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }


    /*
     * Layout
     */
    private void setLayout() {
        mWebView = (WebView) findViewById(R.id.webview);
    }









}
