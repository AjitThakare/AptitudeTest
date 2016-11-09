package psl.com.aptitudetest;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import psl.com.util.NetworkHelper;


public class register extends ActionBarActivity {
    private static String TAG = register.class.getCanonicalName();
    EditText username ;
    EditText password ;
    EditText mobile ;
    EditText email ;
    Button register;
    NetworkHelper netObj;
    SharedPreferences sharedpref;
    SharedPreferences.Editor editor;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        toolbar= (Toolbar) findViewById(R.id.tToolbar);
        toolbar.setTitle("Register");
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
         username =(EditText) findViewById(R.id.username);
         password =(EditText) findViewById(R.id.password);
         mobile =(EditText) findViewById(R.id.mobile);
         email =(EditText) findViewById(R.id.email);
         register = (Button)findViewById(R.id.register);
         sharedpref= getSharedPreferences(getString(R.string.spf_file_key), Context.MODE_PRIVATE);
         editor = sharedpref.edit();
    }

    public void registerMe(View view)
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        netObj= NetworkHelper.getInstance(cm);
        if(netObj.isConnectedToInternet())  // After Submit click, POST it through this code
        {
            if(netObj.isOnline()) // Disable the button and download questions
            {
                String tag_json_obj = "json_obj_req";

                String url = "http://aptitude.southeastasia.cloudapp.azure.com:8080/test/services/users";


                Map<String, String> params = new HashMap<String, String>();
                params.put("usrName", username.getText().toString());
                params.put("password", password.getText().toString());
                params.put("mobileNo", mobile.getText().toString());
                params.put("emailID", email.getText().toString());

                JsonObjectRequest jsonObjReq = new JsonObjectRequest(url, new JSONObject(params),
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d(TAG, response.toString());
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        NetworkResponse errorRes = error.networkResponse;
                        String stringData = "";
                        if (errorRes != null && errorRes.data != null) {
                            try {
                                stringData = new String(errorRes.data, "UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }
                        Log.e("Error", stringData);
                    }

                });

// Adding request to request queue
                AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

                //TODO , save username in shared preference, close this activity and start Homepage activity
                editor.putString(getString(R.string.saved_username),username.getText().toString()); // saved username in sharedPref
                editor.putString(getString(R.string.logged_in_check), "true");
                editor.commit();
            }
            else
                Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"No Internet connection",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
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
