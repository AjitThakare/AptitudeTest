package psl.com.aptitudetest;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import psl.com.util.NetworkHelper;
import psl.com.util.URLHelper;

public class Login extends ActionBarActivity {
    private static String TAG = Login.class.getCanonicalName();
EditText username;
    EditText password;
    Button loginButton;
    ImageView logo;
    NetworkHelper netObj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username= (EditText) findViewById(R.id.username);
        password= (EditText) findViewById(R.id.password);
        loginButton= (Button) findViewById(R.id.login);
        logo= (ImageView)findViewById(R.id.logo);
        logo.setImageResource(R.drawable.ic_launcher);
        AppController.getInstance(this); // App controller instantiated !
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               login();
               // registerUser();
              //  getAllUsers();
            }
        });
    }

    private void login() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        netObj= NetworkHelper.getInstance(cm);
        if(netObj.isConnectedToInternet())  // After Submit click, POST it through this code
        {
            if(netObj.isOnline()) // Disable the button and download questions
            {
        String  tag_string_req = "string_req";
        String url = "http://aptitude.southeastasia.cloudapp.azure.com:8080/test/services/users/login";

        Map<String,String> param= new HashMap<>();
        param.put("username",username.getText().toString());
        param.put("password",password.getText().toString());

        URLHelper helper= new URLHelper(url,param);
        url= helper.addQueryParam();
        Log.d(TAG,"Ans : "+url);

        final StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {   @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());

            try {
                JSONObject object= new JSONObject(response);
                Log.d(TAG, object.get("id").toString());   // use this id to Request all required info of user TODO add username to page after login
                Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_SHORT).show(); // Store username and write Welcome user
                   Intent intent = new Intent(getApplicationContext(),HomeScreen.class);
                startActivity(intent);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),"Enter correct credentials !",Toast.LENGTH_SHORT).show();
                username.setText("");
                password.setText("");
                           }
        });

// Adding request to request queue
             AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
            } // net is available
            else
                Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"No Internet connection",Toast.LENGTH_SHORT).show();
        }
    }

    private void getAllUsers(){
        // Tag used to cancel the request
        String tag_json_arry = "json_array_req";

        String url = "http://aptitude.southeastasia.cloudapp.azure.com:8080/test/services/users";

        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(req, tag_json_arry);
    }

public void openRegister(View view)
{
    Intent intent= new Intent(this,register.class)  ;
    startActivity(intent);
}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
