package psl.com.aptitudetest;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


public class FormulaeDisplay extends ActionBarActivity {
    private static String TAG = FormulaeDisplay.class.getCanonicalName();
    ImageButton button;
    TextView topicName;
    WebView webView;
    String content="default Text";
    File htmlFile;
    String urlSuffix;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_formulae_display);
        topicName=(TextView)findViewById(R.id.topicName);
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
            topicName.setText("" + extras.get("topic").toString());
            String goal= extras.get("goal").toString();
            urlSuffix= getFilenameToDisplay(extras.get("topic").toString(),goal);
        }
        File dir[]= getExternalFilesDirs(null);
        List<String> list= new ArrayList<String>();
        for(int i=0;i<dir.length;i++)
        {
            File listFile[] = dir[i].listFiles();
            if(listFile!=null){
                for(int j=0;j<listFile.length;j++)
                {
                    list.add(listFile[j].getName());
                    // Toast.makeText(getApplicationContext(),listFile[j].getName(),Toast.LENGTH_SHORT).show();
                }
            }
        }
        Boolean needToCopy= true;
        Boolean needToDownload= true;
        for(String filename :list)
        {
            if(filename.equalsIgnoreCase("ajit.css"))
            {   // Toast.makeText(getApplicationContext(),"Files already present in storage",Toast.LENGTH_SHORT).show();
                needToCopy= false;
            }
            if(filename.equalsIgnoreCase(urlSuffix+".html"))
            {
                File file= new File(getExternalFilesDir(null), urlSuffix+".html");
                if(file.length()!=0)
                    needToDownload=false;
                else
                    Toast.makeText(getApplicationContext(),"File needs update, Please Connect to internet ☺",Toast.LENGTH_SHORT).show();
               // Toast.makeText(getApplicationContext(),"HTML file found",Toast.LENGTH_SHORT).show();
            }
        }
        if(needToCopy)
        {//Toast.makeText(getApplicationContext(),"Copying Assets !",Toast.LENGTH_SHORT).show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    copyAssets();
                }
            }).start();
        }
        /////**********
        WebSettings settings= webView.getSettings();
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webView.setHorizontalScrollBarEnabled(false);

       // webView.loadUrl("file:///android_asset/train.html"); // Load according to Topic, for all topics
        AppController.getInstance(this);

        if(needToDownload)
        {
            String  tag_json_req = "json_req";
            String url = "http://aptitude.southeastasia.cloudapp.azure.com:8080/test/services/html/"+urlSuffix;

            if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
                Toast.makeText(getApplicationContext(),"Error in accessing external storage",Toast.LENGTH_SHORT).show();
            }
            else {
                //    Toast.makeText(getApplicationContext(),"Storage available!",Toast.LENGTH_SHORT).show();
            }
            htmlFile= new File(getExternalFilesDir(null), urlSuffix+".html");
            if(!htmlFile.exists())
            {
                // Toast.makeText(getApplicationContext(),"Created TEMP.html",Toast.LENGTH_SHORT).show();
                try {
                    htmlFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                // Toast.makeText(getApplicationContext(),"temp present ",Toast.LENGTH_SHORT).show();
            }

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                content=response.get("content").toString();

                       /*     FileOutputStream outputStream;  // works well for internal storage
                            try {
                                outputStream = openFileOutput(htmlFile.getName(), MODE_PRIVATE);
                                outputStream.write(content.getBytes());
                                outputStream.close();*/
                                try {
                                    FileOutputStream fos = new FileOutputStream(htmlFile);
                                    fos.write(content.getBytes());
                                    fos.close();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });
            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_req);
        }
        htmlFile= new File(getExternalFilesDir(null), urlSuffix+".html");
        webView.loadUrl("file:///"+htmlFile);
        Log.d(TAG,content);

    }

    private String getFilenameToDisplay(String topic, String goal) {

        topic = topic.toLowerCase();
        topic=topic.replace(" ","_");
        if(goal.equalsIgnoreCase("formulae")){
            return topic;
        }
        else
        {
            return topic+"_solved";
        }
    }

    private void copyAssets() {
        AssetManager assetManager = getAssets();
        String[] files = null;
        try {
            files = assetManager.list("");
        } catch (IOException e) {
            Log.e("tag", "Failed to get asset file list.", e);
        }
        FileOutputStream fos;
        for(String filename : files) {
        if(filename.contains(".js")||filename.contains(".css"))
        {
            InputStream in = null;
            try {
                in = assetManager.open(filename);
                String outpath= getExternalFilesDir(null).getAbsolutePath()  ;

                File outFile = new File(outpath, filename);
                if(!outFile.exists())
                {
                    outFile.createNewFile();
                }
                fos= new FileOutputStream(outFile);
                fos.write(readFromFile(in).getBytes());
                fos.close();
            } catch(IOException e) {
                Log.e("tag", "Failed to copy asset file: " + filename, e);
            }
        }
        }
    }
    private String readFromFile(InputStream in)
    {   String myData = "";
        try {
            BufferedReader br =new BufferedReader(new InputStreamReader(in));
            String strLine;
            while ((strLine = br.readLine()) != null) {
                myData = myData + strLine;
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return myData;
    }


    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }
    private static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    private static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_formulae_display, menu);
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
