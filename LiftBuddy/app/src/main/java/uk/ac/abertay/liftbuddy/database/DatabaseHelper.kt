package uk.ac.abertay.liftbuddy.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    val HISTORY_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME_WORKOUT + " (" +
            ID_COL + " INTEGER PRIMARY KEY, " +
            DATE_COL + " TEXT," +
            TITLE_COL + " TEXT," +
            EXERCISE_INFO + " BLOB" + ")"

    val TEMPLATE_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME_TEMPLATE + " (" +
            ID_COL + " INTEGER PRIMARY KEY, " +
            DATE_COL + " TEXT," +
            TITLE_COL + " TEXT," +
            EXERCISE_INFO + " BLOB" + ")"

        //Method for creating database
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(HISTORY_CREATE_TABLE)
        db.execSQL(TEMPLATE_CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, old: Int, new: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_WORKOUT)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_TEMPLATE)
        onCreate(db)
    }

    // Adding data to workout table
    fun addWorkout(date : String, title : String, exerciseInfo: ByteArray?){
        val values = ContentValues()

        values.put(DATE_COL, date)
        values.put(TITLE_COL, title)
        values.put(EXERCISE_INFO, exerciseInfo)

        val db = this.writableDatabase

        db.insert(TABLE_NAME_WORKOUT, null, values)

        db.close()
    }


    // Adding data to template table
    fun addTemplate(date : String, title : String, exerciseInfo: ByteArray?){

        val values = ContentValues()

        values.put(DATE_COL, date)
        values.put(TITLE_COL, title)
        values.put(EXERCISE_INFO, exerciseInfo)

        val db = this.writableDatabase

        db.insert(TABLE_NAME_TEMPLATE, null, values)

        db.close()
    }

    // Getting data from template table
    fun getTemplate(): Cursor? {

        val db = this.readableDatabase

        return db.rawQuery("SELECT * FROM " + TABLE_NAME_TEMPLATE, null)
    }

    // Getting data from workout table
    fun getWorkout(): Cursor? {

        val db = this.readableDatabase

        return db.rawQuery("SELECT * FROM " + TABLE_NAME_WORKOUT, null)
    }


    // Used for getting number of rows in tables
    fun countTemplates(): Long {
        val db = this.readableDatabase
        val templatesCount = DatabaseUtils.queryNumEntries(db, TABLE_NAME_TEMPLATE)
        db.close()
        return templatesCount
    }

    fun countWorkouts(): Long {
        val db = this.readableDatabase
        val workoutsCount = DatabaseUtils.queryNumEntries(db, TABLE_NAME_WORKOUT)
        db.close()
        return workoutsCount
    }

    //Used to define variables for database
    companion object{
        private val DATABASE_NAME = "LiftBuddy.DB"
        private val DATABASE_VERSION = 1
        val TABLE_NAME_WORKOUT = "Workout"
        val TABLE_NAME_TEMPLATE = "Template"
        val ID_COL = "id"
        val DATE_COL = "date"
        val TITLE_COL = "title"
        val EXERCISE_INFO = "exerciseinfo"
    }
}
