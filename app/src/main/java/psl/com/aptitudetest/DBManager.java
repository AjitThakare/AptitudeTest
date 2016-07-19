package psl.com.aptitudetest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ajit_thakare on 7/12/2016.
 */
public class DBManager {
    private MySqlHelper dbHelper;
    private SQLiteDatabase database;
    private String[] allColumns={MySqlHelper.Column_ID,MySqlHelper.Column_question,MySqlHelper.Column_opt1,MySqlHelper.Column_opt2,MySqlHelper.Column_opt3,MySqlHelper.Column_opt4,MySqlHelper.Column_correctAns};
    private static String TAG = DBManager.class.getCanonicalName();
    public DBManager(Context context) {
        dbHelper= new MySqlHelper(context); // Why did we do this
    }
    public void open() throws SQLException
    {
        database=dbHelper.getWritableDatabase();
    }
    public void close() throws SQLException
    {
      dbHelper.close();
    }
    public void addQuestion(String ques,String opt1, String opt2,String opt3, String opt4, String correctAns)
    {
        ContentValues values= new ContentValues();
        values.put(MySqlHelper.Column_question,ques);
        values.put(MySqlHelper.Column_opt1,opt1);
        values.put(MySqlHelper.Column_opt2,opt2);
        values.put(MySqlHelper.Column_opt3,opt3);
        values.put(MySqlHelper.Column_opt4,opt4);
        values.put(MySqlHelper.Column_correctAns,correctAns);

        long insertID = database.insert(MySqlHelper.TABLE_NAME,null,values);
        Log.d(TAG,"question Added to DB -"+ques);
    }

    public List<QuestionPO> getAllQuestions()
    {
        List<QuestionPO> listOfQuestions= new ArrayList<QuestionPO>();
        Cursor cursor= database.query(MySqlHelper.TABLE_NAME,allColumns,null,null,null,null,null);
        cursor.moveToFirst();
        int i=0;
        while(!cursor.isAfterLast())
        {
            QuestionPO question= new QuestionPO();
            question.setQuestion(cursor.getString(cursor.getColumnIndex(MySqlHelper.Column_question)));
            question.setOpt1(cursor.getString(cursor.getColumnIndex(MySqlHelper.Column_opt1)));
            question.setOpt2(cursor.getString(cursor.getColumnIndex(MySqlHelper.Column_opt2)));
            question.setOpt3(cursor.getString(cursor.getColumnIndex(MySqlHelper.Column_opt3)));
            question.setOpt4(cursor.getString(cursor.getColumnIndex(MySqlHelper.Column_opt4)));
            question.setCorrectAns(cursor.getInt(cursor.getColumnIndex(MySqlHelper.Column_correctAns)));
            listOfQuestions.add(question);
            Log.d(TAG,"Cursor["+i+"]="+question.toString());
            cursor.moveToNext();
        }

        return listOfQuestions;

    }
    public QuestionPO cursorToQuestion(Cursor cursor)
    {
        QuestionPO q= new QuestionPO();
        q.setQuestion(cursor.getString(0));
        q.setOpt1(cursor.getString(1));
        q.setOpt2(cursor.getString(2));
        q.setOpt3(cursor.getString(3));
        q.setOpt4(cursor.getString(4));
        q.setCorrectAns(cursor.getInt(5));

        return  q;
    }
}