package psl.com.aptitudetest;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import psl.com.util.NetworkHelper;


public class Test extends ActionBarActivity implements View.OnClickListener{
    private static String TAG = StartTest.class.getCanonicalName();
    DBManager dbm;
    Button opt1;
    Button opt2;
    Button opt3;
    Button opt4;
    ImageButton prev;
    ImageButton nxt;
    TextView quest;
    Toolbar toolbarBottom;

    int correctAns=0;
    List<QuestionPO> allQuestions;
    Map userAnswers;
    int totalQuestions;
    Chronometer simpleTimer;
    TextView pagination;
    String testMode="Test";
    Map resultAnalysis;
    NetworkHelper netObj;
    RadioGroup reportGroup;
    RadioButton reportRadioBtn;
    private int currentQuestion=0; // if this is static then test resumes with last attempted question
    private ActionMenuView amvMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Bundle extras = getIntent().getExtras();
        String topicName="All";
        AppController.getInstance(this);
       // toolbarBottom=(android.support.v7.widget.Toolbar)findViewById(R.id.toolbar_bottom);
        //setSupportActionBar(toolbarBottom);
        if (extras != null) {

            Set set=extras.keySet();
            if(set.contains("topicName"))
            {
                topicName = extras.getString("topicName");
                // Log.d(TAG,"Topic Name found");
            }
            if(set.contains("testmode"))
            {
                testMode=extras.getString("testmode");
                //Log.d(TAG,"Test mode found :"+testMode);
            }
        }
        Toolbar t = (Toolbar) findViewById(R.id.tToolbar);
        amvMenu = (ActionMenuView) t.findViewById(R.id.amvMenu);
        amvMenu.setOnMenuItemClickListener(new ActionMenuView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                return onOptionsItemSelected(menuItem);
            }
        });
        t.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

               // TODO
                return false;
            }
        });

        setSupportActionBar(t);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dbm= new DBManager(this);
        try {
            dbm.open();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        opt1= (Button) findViewById(R.id.button1);
        opt2= (Button) findViewById(R.id.button2);
        opt3= (Button) findViewById(R.id.button3);
        opt4= (Button) findViewById(R.id.button4);

        prev= (ImageButton) findViewById(R.id.btnPrev);
        nxt= (ImageButton) findViewById(R.id.btnNext);

        quest= (TextView) findViewById(R.id.question);
        pagination= (TextView)findViewById(R.id.currentQuestionCounter);
        simpleTimer= (Chronometer)findViewById(R.id.timer);
        simpleTimer.start();

        startTheTest(topicName);

    }

    public void startTheTest(String topicName)
    {
        if(topicName.equalsIgnoreCase("All")) {
            allQuestions = dbm.getAllQuestions(); // here instead of getting all Questions, get selective questions
        }
        else {
            allQuestions = dbm.getQuestionsByTopic(topicName);
        }
        totalQuestions=allQuestions.size();
        userAnswers= new HashMap(totalQuestions);
        if(totalQuestions!=0) {
            displayQuestion(allQuestions.get(currentQuestion));
            prev.setOnClickListener(this);
            nxt.setOnClickListener(this);
            opt1.setOnClickListener(this);
            opt2.setOnClickListener(this);
            opt3.setOnClickListener(this);
            opt4.setOnClickListener(this);
        }
        else
        {
            Log.w(TAG,"Database has 0 questions.");
        }
    }
    public void displayQuestion(QuestionPO question)
    {
        //  Log.d(TAG,""+question.toString()); //Current question being displayed is shown in Log
        quest.setText(question.getQuestion());
        opt1.setText(question.getOpt1());
        opt2.setText(question.getOpt2());
        opt3.setText(question.getOpt3());
        opt4.setText(question.getOpt4());
        correctAns=question.getCorrectAns();
        pagination.setText((currentQuestion+1)+"/"+totalQuestions);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_start_test, menu);

        getMenuInflater().inflate(R.menu.menu_start_test, amvMenu.getMenu());
        // getActionBar().setDisplayShowTitleEnabled(false);
        // toolbarBottom.setLogo(getDrawable(R.drawable.guess));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
       // getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId())
        {
            case R.id.prevMenu:
                if(currentQuestion>0){
                    currentQuestion--;
                    displayQuestion(allQuestions.get(currentQuestion));
                    resetOptionButtonColor();
                }
                if(currentQuestion==0)
                {

                }
                nxt.setEnabled(true);
                checkIfalreadyAnswered(userAnswers,currentQuestion);
                break;

            case R.id.nextMenu:
                // Log.d(TAG,"before next button["+currentQuestion+"]" + allQuestions.size());
                if(currentQuestion<allQuestions.size()-1){
                    currentQuestion++;
                    displayQuestion(allQuestions.get(currentQuestion));
                    resetOptionButtonColor();
                }
                if(currentQuestion==allQuestions.size()-1)
                {
                    nxt.setEnabled(false);

                }
                prev.setEnabled(true);
                checkIfalreadyAnswered(userAnswers,currentQuestion);
                //Log.d(TAG,"currentQuestion["+currentQuestion+"]" + allQuestions.size());
                break;

            case R.id.doneMenu:
                    endTest();
                break;
            case R.id.reportMenu:
                reportQuestion();
                break;


        }

        return super.onOptionsItemSelected(item);
    }
public void reportQuestion()
{       ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        netObj= NetworkHelper.getInstance(cm);
    LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
   final View popupView = layoutInflater.inflate(R.layout.report_popup,null );
    final PopupWindow popupWindow = new PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,true);

    ImageButton close = (ImageButton)popupView.findViewById(R.id.closePopup);
    close.setImageResource(R.drawable.close_button);
    close.setOnClickListener(new Button.OnClickListener(){

        @Override
        public void onClick(View v) {
            popupWindow.dismiss();
        }});

    popupWindow.showAtLocation(opt1, Gravity.CENTER,0,0);
    reportGroup = (RadioGroup)popupView.findViewById(R.id.errorGroup);
    Button submit= (Button) popupView.findViewById(R.id.submit);

    submit.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int selectedId= reportGroup.getCheckedRadioButtonId();
            reportRadioBtn=(RadioButton)popupView.findViewById(selectedId);
            TextView mistakeText = (TextView)popupView.findViewById(R.id.editText);
            String txt= String.valueOf(mistakeText.getText());
           // Toast.makeText(getApplicationContext(),reportRadioBtn.getText()+" is selected "+txt,Toast.LENGTH_SHORT).show(); // NOw send this txt BY http call
            if(netObj.isOnline())
            {        String tag_json_obj = "report_question";
                    String url = "http://aptitude.southeastasia.cloudapp.azure.com:8080/test/services/reports";
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("qid", String.valueOf(allQuestions.get(currentQuestion).getQuestionID()));
                    params.put("mistakeField", reportRadioBtn.getText().toString());
                    params.put("issue", txt);
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
                    Toast.makeText(getApplicationContext(), "Reported !", Toast.LENGTH_SHORT).show();
                    popupWindow.dismiss(); // close the popup
                }
            }
      //  }    if che brackets

    }); // End of listener
    if(netObj.isConnectedToInternet())  // After Submit click, POST it through this code
    {
        if(netObj.isOnline()) // Disable the button and download questions
        {
            //simply used else part for displaying internet related warning msgs
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnPrev:
                if(currentQuestion>0){
                    currentQuestion--;
                    displayQuestion(allQuestions.get(currentQuestion));
                    resetOptionButtonColor();
                }
                if(currentQuestion==0)
                {
                   prev.setEnabled(false);
                }
                nxt.setEnabled(true);
                checkIfalreadyAnswered(userAnswers,currentQuestion);
                break;

            case R.id.btnNext:
               // Log.d(TAG,"before next button["+currentQuestion+"]" + allQuestions.size());
                if(currentQuestion<allQuestions.size()-1){
                    currentQuestion++;
                    displayQuestion(allQuestions.get(currentQuestion));
                    resetOptionButtonColor();
                }
                if(currentQuestion==allQuestions.size()-1)
                {
                    nxt.setEnabled(false);
                   // nxt.setImageResource(R.drawable.ic_done_white_48dp);

                }
                prev.setEnabled(true);
                checkIfalreadyAnswered(userAnswers,currentQuestion);
                //Log.d(TAG,"currentQuestion["+currentQuestion+"]" + allQuestions.size());
                break;


           /* case R.id.button1:if (allQuestions.get(currentQuestion).getCorrectAns()==1)
            {
                Log.d(TAG,"Correct");
               // opt1.setBackgroundColor(Color.GREEN);
                opt1.setBackground(getResources().getDrawable(R.drawable.button_green_color, null));
            }
                else
                opt1.setBackground(getResources().getDrawable(R.drawable.button_red_color, null));

                break;
            case R.id.button2:if (allQuestions.get(currentQuestion).getCorrectAns()==2)
            {
                Log.d(TAG,"Correct");
               // opt2.setBackgroundColor(Color.GREEN);
                opt2.setBackground(getResources().getDrawable(R.drawable.button_green_color, null));
            }
                else
                opt2.setBackground(getResources().getDrawable(R.drawable.button_red_color, null));
                break;
            case R.id.button3:if (allQuestions.get(currentQuestion).getCorrectAns()==3)
            {
                Log.d(TAG,"Correct");
                // opt2.setBackgroundColor(Color.GREEN);
                opt3.setBackground(getResources().getDrawable(R.drawable.button_green_color, null));
            }
            else
                opt3.setBackground(getResources().getDrawable(R.drawable.button_red_color, null));
                break;
            case R.id.button4:if (allQuestions.get(currentQuestion).getCorrectAns()==4)
            {
                Log.d(TAG,"Correct");
                // opt2.setBackgroundColor(Color.GREEN);
                opt4.setBackground(getResources().getDrawable(R.drawable.button_green_color, null));
            }
            else
                opt4.setBackground(getResources().getDrawable(R.drawable.button_red_color, null));
                break;
*/
            default:
                evaluateAsPerMode(testMode,view); // Depending on test Mode we will change behavior of Answer keys

                break;

        }
    }
    public void endTest()
    {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("End TEST")
                .setMessage("Do you want to end this test?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        checkScore(userAnswers,allQuestions);
                        Intent intent= new Intent(getApplicationContext(),resultGraph.class);
                        Bundle extras= new Bundle();
                        extras.putString("Correct",resultAnalysis.get("Correct").toString());
                        extras.putString("Incorrect",resultAnalysis.get("Incorrect").toString());
                        extras.putString("NotAttempted",resultAnalysis.get("NotAttempted").toString());

                        intent.putExtras(extras);

                        startActivity(intent);
                        finish(); // Instead of finishing, show result first..
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }
    @Override
    public void onBackPressed() {
       endTest(); // show end test dialog.
    }

    private void checkScore(Map userAnswers, List<QuestionPO> allQuestions) {
        int score=0;
        int correct=0;
        int notAttempted=0;
        int attempted=0;
        int incorrect=0;

        for(int i=0;i<allQuestions.size();i++)
        {
            if(userAnswers.containsKey(allQuestions.get(i).getQuestionID()))
            {
                attempted++;
                if(userAnswers.get(allQuestions.get(i).getQuestionID())==(allQuestions.get(i).getCorrectAns()))
                {
                    correct++;
                    Log.d(TAG,"Correct ans found "+allQuestions.get(i).getQuestion());
                }
                else
                {
                    incorrect++;
                }
            }
        }
        notAttempted=allQuestions.size()-attempted;
        score=correct; // No negative marking right now
        resultAnalysis= new HashMap();
        resultAnalysis.put("Correct",correct);
        resultAnalysis.put("Incorrect",incorrect);
        resultAnalysis.put("NotAttempted",notAttempted);

    }

    private void checkIfalreadyAnswered(Map userAnswers, int currentQuestion) {
        int id= allQuestions.get(currentQuestion).getQuestionID();
        if(userAnswers.containsKey(id))
        {
            //    Toast.makeText(this, "Key found",Toast.LENGTH_SHORT ).show();
            //   Log.d(TAG,"key found");
            showOptionButtonSelected((Integer)userAnswers.get(id));
            //   Toast.makeText(this, userAnswers.get(id).toString(),Toast.LENGTH_SHORT ).show();
        }
        else
        {
            //   Toast.makeText(this, "NOT found",Toast.LENGTH_SHORT );
            //  Log.d(TAG,"NOT found");
        }


    }

    private void evaluateAsPerMode(String testMode, View view) {  // Add user marked answers in  MAP
        if(testMode.equalsIgnoreCase("Guess"))
        {
            switch (view.getId()) {

                case R.id.button1:
                    if (allQuestions.get(currentQuestion).getCorrectAns() == 1) {

                        // opt1.setBackgroundColor(Color.GREEN);
                        opt1.setBackground(getResources().getDrawable(R.drawable.button_green_color, null));
                    } else
                        opt1.setBackground(getResources().getDrawable(R.drawable.button_red_color, null));
                    userAnswers.put(allQuestions.get(currentQuestion).getQuestionID(),1);
                    break;
                case R.id.button2:
                    if (allQuestions.get(currentQuestion).getCorrectAns() == 2) {
                        Log.d(TAG, "Correct");
                        // opt2.setBackgroundColor(Color.GREEN);
                        opt2.setBackground(getResources().getDrawable(R.drawable.button_green_color, null));
                    } else
                        opt2.setBackground(getResources().getDrawable(R.drawable.button_red_color, null));
                    userAnswers.put(allQuestions.get(currentQuestion).getQuestionID(),2);
                    break;
                case R.id.button3:
                    if (allQuestions.get(currentQuestion).getCorrectAns() == 3) {
                        Log.d(TAG, "Correct");
                        // opt2.setBackgroundColor(Color.GREEN);
                        opt3.setBackground(getResources().getDrawable(R.drawable.button_green_color, null));
                    } else
                        opt3.setBackground(getResources().getDrawable(R.drawable.button_red_color, null));
                    userAnswers.put(allQuestions.get(currentQuestion).getQuestionID(),3);
                    break;
                case R.id.button4:
                    if (allQuestions.get(currentQuestion).getCorrectAns() == 4) {
                        Log.d(TAG, "Correct");
                        // opt2.setBackgroundColor(Color.GREEN);
                        opt4.setBackground(getResources().getDrawable(R.drawable.button_green_color, null));
                    } else
                        opt4.setBackground(getResources().getDrawable(R.drawable.button_red_color, null));
                    userAnswers.put(allQuestions.get(currentQuestion).getQuestionID(),4);
                    break;
            }
            showCorrectAnswer(currentQuestion);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    nxt.callOnClick();
                }
            }, 500);
        }

        else if(testMode.equalsIgnoreCase("Test")) // solve first, get Result afterwords
        {
            switch (view.getId()) {
                case R.id.button1:
                    if (allQuestions.get(currentQuestion).getCorrectAns() == 1) {

                    }
                    userAnswers.put(allQuestions.get(currentQuestion).getQuestionID(),1);

                    break;
                case R.id.button2:
                    if (allQuestions.get(currentQuestion).getCorrectAns() == 2) {

                    }
                    userAnswers.put(allQuestions.get(currentQuestion).getQuestionID(),2);
                    break;
                case R.id.button3:
                    if (allQuestions.get(currentQuestion).getCorrectAns() == 3) {

                    }
                    userAnswers.put(allQuestions.get(currentQuestion).getQuestionID(),3);
                    break;
                case R.id.button4:
                    if (allQuestions.get(currentQuestion).getCorrectAns() == 4) {
                    }
                    userAnswers.put(allQuestions.get(currentQuestion).getQuestionID(),4);
                    break;
            }
            checkIfalreadyAnswered(userAnswers,currentQuestion);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                     nxt.callOnClick();
                }
            }, 200);  // wait for 200 milliseconds before going to nxt question


        }
        else if(testMode.equalsIgnoreCase("RapidFire"))
        {
            /*switch (view.getId()) {
                case R.id.button1:
                    if (allQuestions.get(currentQuestion).getCorrectAns() == 1) {
                        Log.d(TAG, "Correct");
                        // opt1.setBackgroundColor(Color.GREEN);
                        opt1.setBackground(getResources().getDrawable(R.drawable.button_green_color, null));
                    } else
                        opt1.setBackground(getResources().getDrawable(R.drawable.button_red_color, null));

                    break;
                case R.id.button2:
                    if (allQuestions.get(currentQuestion).getCorrectAns() == 2) {
                        Log.d(TAG, "Correct");
                        // opt2.setBackgroundColor(Color.GREEN);
                        opt2.setBackground(getResources().getDrawable(R.drawable.button_green_color, null));
                    } else
                        opt2.setBackground(getResources().getDrawable(R.drawable.button_red_color, null));
                    break;
                case R.id.button3:
                    if (allQuestions.get(currentQuestion).getCorrectAns() == 3) {
                        Log.d(TAG, "Correct");
                        // opt2.setBackgroundColor(Color.GREEN);
                        opt3.setBackground(getResources().getDrawable(R.drawable.button_green_color, null));
                    } else
                        opt3.setBackground(getResources().getDrawable(R.drawable.button_red_color, null));
                    break;
                case R.id.button4:
                    if (allQuestions.get(currentQuestion).getCorrectAns() == 4) {
                        Log.d(TAG, "Correct");
                        // opt2.setBackgroundColor(Color.GREEN);
                        opt4.setBackground(getResources().getDrawable(R.drawable.button_green_color, null));
                    } else
                        opt4.setBackground(getResources().getDrawable(R.drawable.button_red_color, null));
                    break;
            }*/

        }

    }

    private void showCorrectAnswer(int currentQuestion) {
        int ans=allQuestions.get(currentQuestion).getCorrectAns();

        if(ans==1)
        {
            opt1.setBackground(getResources().getDrawable(R.drawable.button_green_color, null));
        }
        if(ans==2)
        {
            opt2.setBackground(getResources().getDrawable(R.drawable.button_green_color, null));
        }
        if(ans==3)
        {
            opt3.setBackground(getResources().getDrawable(R.drawable.button_green_color, null));
        }
        if(ans==4)
        {
            opt4.setBackground(getResources().getDrawable(R.drawable.button_green_color, null));
        }

    }

    public void showOptionButtonSelected(int buttonNo) {
        if (testMode.equalsIgnoreCase("test")) {
            opt1.setBackground(getResources().getDrawable(R.drawable.button_gray_color, null));
            opt2.setBackground(getResources().getDrawable(R.drawable.button_gray_color, null));
            opt3.setBackground(getResources().getDrawable(R.drawable.button_gray_color, null));
            opt4.setBackground(getResources().getDrawable(R.drawable.button_gray_color, null));
            if (buttonNo == 1) {
                opt1.setBackground(getResources().getDrawable(R.drawable.button_lightgray_color, null));
            } else if (buttonNo == 2) {
                opt2.setBackground(getResources().getDrawable(R.drawable.button_lightgray_color, null));
            } else if (buttonNo == 3) {
                opt3.setBackground(getResources().getDrawable(R.drawable.button_lightgray_color, null));
            } else if (buttonNo == 4) {
                opt4.setBackground(getResources().getDrawable(R.drawable.button_lightgray_color, null));
            }
        }
        else if(testMode.equalsIgnoreCase("guess"))
        {

            if (buttonNo == 1) {
                opt1.setBackground(getResources().getDrawable(R.drawable.button_red_color, null));
            } else if (buttonNo == 2) {
                opt2.setBackground(getResources().getDrawable(R.drawable.button_red_color, null));
            } else if (buttonNo == 3) {
                opt3.setBackground(getResources().getDrawable(R.drawable.button_red_color, null));
            } else if (buttonNo == 4) {
                opt4.setBackground(getResources().getDrawable(R.drawable.button_red_color, null));
            }
            showCorrectAnswer(currentQuestion);

        }
    }
    public void resetOptionButtonColor()
    {
        //  opt1.setBackgroundResource(android.R.drawable.btn_default);  //One way
        //   opt2.setBackground(getResources().getDrawable(R.drawable.button_selected_answer_color, null));

        opt1.setBackground(getResources().getDrawable(R.drawable.button_gray_color, null));
        opt2.setBackground(getResources().getDrawable(R.drawable.button_gray_color, null));
        opt3.setBackground(getResources().getDrawable(R.drawable.button_gray_color, null));
        opt4.setBackground(getResources().getDrawable(R.drawable.button_gray_color, null));
    }

}
