package psl.com.aptitudetest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ajit_thakare on 7/12/2016.
 */
public class MySqlHelper extends SQLiteOpenHelper {
    public  static final String TABLE_NAME ="question_table";
    public static final String DELETE_DATABASE="DELETE FROM "+TABLE_NAME;
    public  static final String Column_ID="_id";
    public  static final String Column_question ="question";
    public  static final String Column_opt1 ="opt1";
    public  static final String Column_opt2 ="opt2";
    public  static final String Column_opt3 ="opt3";
    public  static final String Column_opt4 ="opt4";
    public  static final String Column_correctAns ="correctAns";
    private static final String CREATE_STATEMENT= "create table "+ TABLE_NAME + "(" +Column_ID+" integer primary key autoincrement,"+ Column_question +" text not null,"+Column_opt1+" text not null,"+Column_opt2+" text not null,"+Column_opt3+" text not null,"+Column_opt4+" text not null,"+Column_correctAns+" text not null);";
    private static final String DATABASE_NAME = "questions.db";
    private static final int DATABASE_VERSION = 1;

    public MySqlHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
sqLiteDatabase.execSQL(CREATE_STATEMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }
}
