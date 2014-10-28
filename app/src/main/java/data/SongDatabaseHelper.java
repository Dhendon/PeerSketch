package data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by davidhendon on 10/28/14.
 */

public class SongDatabaseHelper extends SQLiteOpenHelper {

    public static final String TABLE_SONG = "song";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_CREATED_DATE = "created_date";
    public static final String COLUMN_TRACKS_ID = "tracks_id";
    public static final String COLUMN_FOR_LOOPS_ID = "for_loops_id";
    public static final String COLUMN_EFFECTS_ID = "effects_id";
    public static final String COLUMN_TEMPO = "tempo";
    public static final String COLUMN_PHRASE_LENGTH = "phrase_length";
    public static final String COLUMN_DESCRIPTION = "description";
    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_SONG + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_CREATED_DATE + " integer, " // datetime
            + COLUMN_TRACKS_ID + " integer, "
            + COLUMN_FOR_LOOPS_ID + " integer, "
            + COLUMN_EFFECTS_ID + " integer, "
            + COLUMN_TEMPO + " integer, "
            + COLUMN_PHRASE_LENGTH + " integer, "
            + COLUMN_DESCRIPTION + " text )";
    private static final String DATABASE_NAME = "song.db";
    private static final int DATABASE_VERSION = 1;

    public SongDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(SongDatabaseHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data"
        );
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SONG);
        onCreate(db);
    }

}