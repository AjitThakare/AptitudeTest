package psl.com.aptitudetest;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import psl.com.util.NetworkHelper;


public class UpdateQuestions extends ActionBarActivity {
    private static String TAG = UpdateQuestions.class.getCanonicalName();
    Button updateButton;
    TextView message;
    ProgressBar progressbar;
    DBManager dbm;
    JSONArray array;
    NetworkHelper netObj;
    private int progressStatus=0;
    private Handler mHandler;
    private long totalSize = 1;
    private long inserted =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_questions);
        updateButton= (Button)findViewById(R.id.updateButton);
        message= (TextView)findViewById(R.id.download);
        progressbar= (ProgressBar)findViewById(R.id.progressBar);
        dbm= new DBManager(this);
        try {
            dbm.open();
     /*       String[]input={"4"};
            dbm.deleteByQID(input);
            input[0]="2";
            dbm.deleteByQID(input);
            input[0]="3";
            dbm.deleteByQID(input);*/
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        AppController.getInstance(this);
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Toast.makeText(getApplicationContext(),(int) msg.obj+" Questions added",Toast.LENGTH_SHORT).show();
                message.setText("Update Complete!");
            }
        };
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                netObj= NetworkHelper.getInstance(cm); //  used singleton for this
                if(netObj.isConnectedToInternet())
                {
                    if(netObj.isOnline()) // Disable the button and download questions
                    {
                        //Toast.makeText(getApplicationContext(),"Internet access ON",Toast.LENGTH_SHORT).show();
                        updateButton.setClickable(false);
                        /* Code handling UPDATE*/
                        progressbar.setProgress(0);
                        progressbar.setMax(100);
                        //reset progress bar and filesize status
                        progressStatus = 0; //
                        // totalSize = 0; //

                        String tag_json_arry = "json_array_req";
                        String url= "http://aptitude.southeastasia.cloudapp.azure.com:8080/test/services/questions";
                        JsonArrayRequest req = new JsonArrayRequest(url,
                                new Response.Listener<JSONArray>() {
                                    @Override
                                    public void onResponse(JSONArray response) {
                                        Log.d(TAG, response.toString());
                                        array= response;
                                        totalSize= array.length();
                                        final List <Integer>presentQIDs = dbm.getAllQuestionIDs();
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                try{
                                                    for(int i=0;i<totalSize;i++)
                                                    {
                                                        JSONObject obj = (JSONObject) array.get(i);
                                                        // before adding chk if this id is already present in database or not

                                                        try {
                                                            Thread.sleep(1000);
                                                        } catch (InterruptedException e) {
                                                            e.printStackTrace();
                                                        }
                                                        if(presentQIDs.contains(obj.getInt("id")))
                                                        {
                                                            Log.d(TAG,"QID already present "+obj.getInt("id"));
                                                        }
                                                        else{
                                                            dbm.addQuestion(String .valueOf(obj.getInt("id")),obj.getString("question"),obj.getString("option1"),
                                                                    obj.getString("option2"),obj.getString("option3"),obj.getString("option4"),String.valueOf(obj.getInt("correctAnswer")),
                                                                    obj.getString("topic"));
                                                            Log.d(TAG,"New Question added "+obj.getInt("id"));
                                                        }
                                                        inserted++;
                                                        doOperation();
                                                        progressbar.setProgress(progressStatus);
                                                        if(i==totalSize) // progressStatus>100 old value
                                                        {
                                                            if(mHandler!=null)
                                                            {
                                                                Message msg= mHandler.obtainMessage();
                                                                msg.obj=new Integer(String.valueOf(inserted));
                                                                mHandler.sendMessage(msg);
                                                            }
                                                        }
                                                    }
                                                }
                                                catch (JSONException e) {
                                                    e.printStackTrace();
                                                }}
                                        }).start();

                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                VolleyLog.d(TAG, "Error: " + error.getMessage());
                                Log.d(TAG,"Network Response"+error.toString());
                                Log.d(TAG,"Trace- "+error.getStackTrace().toString());

                            }

                        }){
                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                HashMap<String, String> headers = new HashMap<String, String>();
                                headers.put("Content-Type", "application/json");
                                headers.put("Accept", "application/json");
                                // headers.put("apiKey", "xxxxxxxxxxxxxxx");  // add api key in future
                                return headers;
                            }
                        };
                        AppController.getInstance().addToRequestQueue(req, tag_json_arry);  // Online REquest made
                        // Adding request to request queue
                        /*Update code END*/
                    }
                    else
                       Toast.makeText(getApplicationContext(),"Please check your internet connection",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"No Internet connection",Toast.LENGTH_SHORT).show();
                }
              }//end of onClick method
        });


    }

    private int doOperation() {

        if(totalSize>0)
        progressStatus+=(inserted * 100)/totalSize; //
        return progressStatus;
          }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_update_questions, menu);
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
