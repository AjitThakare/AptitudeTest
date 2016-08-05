package psl.com.aptitudetest;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.SQLException;


public class HomeScreen extends Activity {
DBManager dbm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        dbm= new DBManager(this);
        try {
            dbm.open();
            final int REQUEST_CODE_ASK_PERMISSIONS = 123;
            int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE_ASK_PERMISSIONS);
                return;
            }
            dbm.insertDB();                   //insets DB from DBManager class
          //  dbm.deleteAllQuestions();        //to Reset the DB
        } catch (SQLException e) {
            e.printStackTrace();
        }
        addListner();
    }
public void addListner(){

    Button add= (Button)findViewById(R.id.btnAdd);
    final EditText question= (EditText)findViewById(R.id.question);
    final EditText opt1= (EditText)findViewById(R.id.optionOne);
   final EditText opt2= (EditText)findViewById(R.id.optionTwo);
    final EditText opt3= (EditText)findViewById(R.id.optionThree);
    final EditText opt4= (EditText)findViewById(R.id.optionFour);
    final EditText correctAnswer= (EditText) findViewById(R.id.correctAnswer);
    add.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
           // Toast.makeText(HomeScreen.this, question.getText(), Toast.LENGTH_SHORT).show();
            if(question.getText().toString().equals(""))
            {
                Toast.makeText(HomeScreen.this,"Please enter question first",Toast.LENGTH_SHORT).show();
            }
                else {
                Toast.makeText(HomeScreen.this,"Toast is :"+question.getText(),Toast.LENGTH_SHORT).show();
                dbm.addQuestion(question.getText().toString(), opt1.getText().toString(), opt2.getText().toString(), opt3.getText().toString(), opt4.getText().toString(), correctAnswer.getText().toString());
                question.setText("");
                opt1.setText("");
                opt2.setText("");
                opt3.setText("");
                opt4.setText("");
                correctAnswer.setText("");
            }
                    }
    });
}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_screen, menu);
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
        else if(id==R.id.view_questions){
            Intent intent= new Intent(this,StartTest.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }



}
