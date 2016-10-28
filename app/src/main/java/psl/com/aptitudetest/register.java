package psl.com.aptitudetest;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


public class register extends ActionBarActivity {
    private static String TAG = register.class.getCanonicalName();
    EditText username ;
    EditText password ;
    EditText mobile ;
    EditText email ;
    Button register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

         username =(EditText) findViewById(R.id.username);
         password =(EditText) findViewById(R.id.password);
         mobile =(EditText) findViewById(R.id.mobile);
         email =(EditText) findViewById(R.id.email);
register = (Button)findViewById(R.id.register);

    }

    public void registerMe(View view)
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