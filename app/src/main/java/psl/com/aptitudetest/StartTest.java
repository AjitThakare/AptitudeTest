package psl.com.aptitudetest;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import java.util.List;


public class StartTest extends Activity implements View.OnClickListener{
    private static String TAG = StartTest.class.getCanonicalName();
    private static int currentQuestion=0;
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
    Chronometer simpleTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_test);
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
        simpleTimer= (Chronometer)findViewById(R.id.timer);
        simpleTimer.start();

    startTheTest();

    }

    public void startTheTest()
    {
         allQuestions= dbm.getAllQuestions();
        if(allQuestions.size()!=0) {
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
    Log.d(TAG,""+question.toString());
    quest.setText(question.getQuestion());
    opt1.setText(question.getOpt1());
    opt2.setText(question.getOpt2());
    opt3.setText(question.getOpt3());
    opt4.setText(question.getOpt4());
    correctAns=question.getCorrectAns();
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
                break;

            case R.id.btnNext:
               // Log.d(TAG,"before next button["+currentQuestion+"]" + allQuestions.size());
                if(currentQuestion<allQuestions.size()-1){
                    currentQuestion++;
                    displayQuestion(allQuestions.get(currentQuestion));
                    resetOptionButtonColor();
                }

                //Log.d(TAG,"currentQuestion["+currentQuestion+"]" + allQuestions.size());
                break;
            case R.id.button1:if (allQuestions.get(currentQuestion).getCorrectAns()==1)
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


        }
    }
    public void resetOptionButtonColor()
    {
        opt1.setBackgroundResource(android.R.drawable.btn_default);
        opt2.setBackgroundResource(android.R.drawable.btn_default);
        opt3.setBackgroundResource(android.R.drawable.btn_default);
        opt4.setBackgroundResource(android.R.drawable.btn_default);
    }
}
