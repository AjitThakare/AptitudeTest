package psl.com.aptitudetest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ajit_thakare on 7/12/2016.
 */
public class DBManager {
    private static String TAG = DBManager.class.getCanonicalName();
    private MySqlHelper dbHelper;
    private SQLiteDatabase database;
    private String[] allColumns={MySqlHelper.Column_ID,MySqlHelper.Column_question,MySqlHelper.Column_opt1,MySqlHelper.Column_opt2,MySqlHelper.Column_opt3,MySqlHelper.Column_opt4,MySqlHelper.Column_correctAns,MySqlHelper.Column_topic};
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
    public void addQuestion(String id,String ques,String opt1, String opt2,String opt3, String opt4, String correctAns,String topic)
    {
        ContentValues values= new ContentValues();
        values.put(MySqlHelper.Column_ID,id);
        values.put(MySqlHelper.Column_question,ques);
        values.put(MySqlHelper.Column_opt1,opt1);
        values.put(MySqlHelper.Column_opt2,opt2);
        values.put(MySqlHelper.Column_opt3,opt3);
        values.put(MySqlHelper.Column_opt4,opt4);
        values.put(MySqlHelper.Column_correctAns,correctAns);
        values.put(MySqlHelper.Column_topic,topic);

        long insertID = database.insert(MySqlHelper.TABLE_NAME,null,values);
   //     Log.d(TAG,"question Added to DB -"+ques);
    }
public void deleteAllQuestions()
{

    database.execSQL(MySqlHelper.DELETE_DATABASE);
}
    public void insertDB()
    {String id=null;
        String ques=null;
        String opt1=null;
        String opt2=null;
        String opt3=null;
        String opt4=null;
        String correctAns=null;
           String topic=null;
       String [][]questionsArray={{"1+1=?","1","2","3","4","2"},
               {"Pune is a _ ?","City","Village","State","Country","1","GK"}
       ,{"Maharashtra is a _ ?","City","Village","State","Country","3","GK"}
       };


    DataProviderClass dp= new DataProviderClass();

        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS);
        File file = new File(path,"ajit.txt");
        File input = new File(path,"input.xls");
        try {
            // Make sure the Pictures directory exists.

            path.mkdirs();

            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
     //   Log.d(TAG,file.getAbsolutePath().toString());
        questionsArray= dp.getTableArray(input.getAbsolutePath(),"Sheet1","POINTER");

     //   Log.d(TAG,"input file path is "+input.getAbsolutePath());
      //  Log.d(TAG,"Questions array "+questionsArray.toString());

        for(int i=0;i<questionsArray.length;i++)
        {   id=questionsArray[i][0];
            ques=questionsArray[i][1];
            opt1=questionsArray[i][2];
            opt2=questionsArray[i][3];
            opt3=questionsArray[i][4];
            opt4=questionsArray[i][5];
            correctAns=questionsArray[i][6];
            topic=questionsArray[i][7];

            addQuestion(id,ques,opt1,opt2,opt3,opt4,correctAns,topic);
        }
Log.i(TAG,"Question from file are successFully saved in DB");
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
            question.setQuestionID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(MySqlHelper.Column_ID)))); // Ques ID is same as ID, primary key
            question.setQuestion(cursor.getString(cursor.getColumnIndex(MySqlHelper.Column_question)));
            question.setOpt1(cursor.getString(cursor.getColumnIndex(MySqlHelper.Column_opt1)));
            question.setOpt2(cursor.getString(cursor.getColumnIndex(MySqlHelper.Column_opt2)));
            question.setOpt3(cursor.getString(cursor.getColumnIndex(MySqlHelper.Column_opt3)));
            question.setOpt4(cursor.getString(cursor.getColumnIndex(MySqlHelper.Column_opt4)));
            question.setCorrectAns(cursor.getInt(cursor.getColumnIndex(MySqlHelper.Column_correctAns)));
            listOfQuestions.add(question);
         //   Log.d(TAG,"Cursor["+i+"]="+question.toString());
            cursor.moveToNext();
        }

        return listOfQuestions;

    }
    public List<QuestionPO>getQuestionsByTopic(String topicName)
    {
        List<QuestionPO> listOfQuestions= new ArrayList<QuestionPO>();

        String []topics={"All"};
        topics[0]=topicName;

        Cursor cursor= database.query(MySqlHelper.TABLE_NAME,allColumns, "topic=?",topics,null,null,null);
        cursor.moveToFirst();
        int i=0;
        while(!cursor.isAfterLast())
        {
            QuestionPO question= new QuestionPO();
            question.setQuestionID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(MySqlHelper.Column_ID)))); // Ques ID = _id from table
            question.setQuestion(cursor.getString(cursor.getColumnIndex(MySqlHelper.Column_question)));
            question.setOpt1(cursor.getString(cursor.getColumnIndex(MySqlHelper.Column_opt1)));
            question.setOpt2(cursor.getString(cursor.getColumnIndex(MySqlHelper.Column_opt2)));
            question.setOpt3(cursor.getString(cursor.getColumnIndex(MySqlHelper.Column_opt3)));
            question.setOpt4(cursor.getString(cursor.getColumnIndex(MySqlHelper.Column_opt4)));
            question.setCorrectAns(cursor.getInt(cursor.getColumnIndex(MySqlHelper.Column_correctAns)));
            listOfQuestions.add(question);
          //  Log.d(TAG,"Cursor["+i+"]="+question.toString());   // Shows list of total questions in DB, in start of application
            cursor.moveToNext();
        }

        return listOfQuestions;
    }
    public QuestionPO cursorToQuestion(Cursor cursor)
    {
        QuestionPO q= new QuestionPO();
        q.setQuestionID(cursor.getInt(0));
        q.setQuestion(cursor.getString(1));
        q.setOpt1(cursor.getString(2));
        q.setOpt2(cursor.getString(3));
        q.setOpt3(cursor.getString(4));
        q.setOpt4(cursor.getString(5));
        q.setCorrectAns(cursor.getInt(6));

        return  q;
    }
}
