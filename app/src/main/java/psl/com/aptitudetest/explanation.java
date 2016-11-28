package psl.com.aptitudetest;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;

import java.io.File;


public class explanation extends ActionBarActivity {
    File htmlFile;
    WebView webView;
    ImageButton button;
    String filename;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explanation);
        webView=(WebView)findViewById(R.id.webview);
        button = (ImageButton)findViewById(R.id.closeButton);
        button.setImageResource(R.drawable.close_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Bundle extras=getIntent().getExtras();
        if(extras!=null)
        {
            filename=extras.get("qid").toString();
        }

        WebSettings settings= webView.getSettings();
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webView.setHorizontalScrollBarEnabled(false);
        htmlFile= new File(getExternalFilesDir(null), filename+".html");
        webView.loadUrl("file:///"+htmlFile); // Load according to Topic, for all topics
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_explanation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
