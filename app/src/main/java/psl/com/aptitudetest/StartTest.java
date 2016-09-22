package psl.com.aptitudetest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class StartTest extends Activity implements View.OnClickListener{
    private static String TAG = StartTest.class.getCanonicalName();
    DBManager dbm;
    Button opt1;
    Button opt2;
    Button opt3;
    Button opt4;
    Button prev;
    Button nxt;
    TextView quest;
    int correctAns=0;
    List<QuestionPO> allQuestions;
    Map userAnswers;
    int totalQuestions;
    Chronometer simpleTimer;
    TextView pagination;
    String testMode="Test";
    Map resultAnalysis;
    private int currentQuestion=0; // if this is static then test resumes with last attempted question

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_test);
        Bundle extras = getIntent().getExtras();
        String topicName="All";

        if (extras != null) {
            topicName = extras.getString("topicName");
          //  Toast.makeText(StartTest.this,"Found in Intent"+topicName,Toast.LENGTH_SHORT).show();
        }
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
         prev= (Button) findViewById(R.id.btnPrev);
         nxt= (Button) findViewById(R.id.btnNext);
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
        getMenuInflater().inflate(R.menu.menu_start_test, menu);
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
    @Override
    public void onBackPressed() {
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

    public void showOptionButtonSelected(int buttonNo)
    {
        opt1.setBackground(getResources().getDrawable(R.drawable.button_gray_color, null));
        opt2.setBackground(getResources().getDrawable(R.drawable.button_gray_color, null));
        opt3.setBackground(getResources().getDrawable(R.drawable.button_gray_color, null));
        opt4.setBackground(getResources().getDrawable(R.drawable.button_gray_color, null));
        if(buttonNo==1)
        {
            opt1.setBackground(getResources().getDrawable(R.drawable.button_lightgray_color, null));
        }
        else if(buttonNo==2)
        {
            opt2.setBackground(getResources().getDrawable(R.drawable.button_lightgray_color, null));
        }
        else if(buttonNo==3)
        {
            opt3.setBackground(getResources().getDrawable(R.drawable.button_lightgray_color, null));
        }
        else if(buttonNo==4)
        {
            opt4.setBackground(getResources().getDrawable(R.drawable.button_lightgray_color, null));
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
