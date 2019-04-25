package com.example.purestock;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "StockDiaryDatabase";    // Database Name
    public static final String USERS_TABLE = "USER";
    private static final String USERS_COL_1 = "USERNAME";
    private static final String USERS_COL_2 = "PASSWORD";
    private static final String USERS_COL_3 = "FULLNAME";
    private static final String USERS_COL_4 = "EMAIL";
    public static final String STOCKS_TABLE = "STOCKS";
    private static final String STOCKS_COL_1 = "ID";
    private static final String STOCKS_COL_2 = "NAME";
    public static final String TRANSACTIONS_TABLE = "TRANSACTIONS";
    private static final String TRANSACTIONS_COL_1 = "TID";
    private static final String TRANSACTIONS_COL_2 = "UID";
    private static final String TRANSACTIONS_COL_3 = "SID";
    private static final String TRANSACTIONS_COL_4 = "PRICE";
    // Add stock number column (04/17/2019)
    private static final String TRANSACTIONS_COL_5 = "NUMBER_STOCK";

    private static final String TRANSACTIONS_COL_6 = "TYPE";
    private static final String TRANSACTIONS_COL_7 = "DATE";
    public static final String WATCHLISTS_TABLE = "WATCHLISTS";
    private static final String WATCHLISTS_COL_1 = "ID";
    private static final String WATCHLISTS_COL_2 = "NAME";
    private static final String WATCHLISTS_COL_3 = "DATE";
    private static final String WATCHLISTS_COL_4 = "UID";
    public static final String WATCHLISTS_STOCKS_TABLE = "WATCHLISTS_STOCKS";
    private static final String WATCHLISTS_STOCKS_COL_1 = "WID";
    private static final String WATCHLISTS_STOCKS_COL_2 = "SID";
    public static final String NOTES_TABLE = "NOTES";
    private static final String NOTES_COL_1 = "NID";
    private static final String NOTES_COL_2 = "NOTE";
    private static final String NOTES_COL_3 = "DATE";
    private static final String NOTES_COL_4 = "UID";

    private static final String CREATE_TABLE_USERS = "CREATE TABLE IF NOT EXISTS " + USERS_TABLE + " (" + USERS_COL_1 + " CHAR(20) NOT NULL PRIMARY KEY, " +
            USERS_COL_2 + " CHAR(40) NOT NULL, " + USERS_COL_3 + " NVARCHAR(200) NOT NULL, " + USERS_COL_4 + " CHAR(200))";
    private static final String CREATE_TABLE_STOCKS = "CREATE TABLE IF NOT EXISTS " + STOCKS_TABLE + " (" + STOCKS_COL_1 + " CHAR(10) NOT NULL PRIMARY KEY, " +
            STOCKS_COL_2 + " VARCHAR(100) NOT NULL)";
    private static final String CREATE_TABLE_TRANSACTIONS = "CREATE TABLE IF NOT EXISTS " + TRANSACTIONS_TABLE + " (" + TRANSACTIONS_COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            TRANSACTIONS_COL_2 + " CHAR(20) NOT NULL, " + TRANSACTIONS_COL_3 + " CHAR(10) NOT NULL, " +
            TRANSACTIONS_COL_4 + " DOUBLE NOT NULL, " + TRANSACTIONS_COL_5 + " INT NOT NULL, " +
            TRANSACTIONS_COL_6 + " BOOLEAN NOT NULL, " + TRANSACTIONS_COL_7 + " DATETIME NOT NULL, " +
            "FOREIGN KEY (" + TRANSACTIONS_COL_2 + ") REFERENCES " + USERS_TABLE + " (" + USERS_COL_1 + "), " +
            "FOREIGN KEY (" + TRANSACTIONS_COL_3 + ") REFERENCES " + STOCKS_TABLE + " (" + STOCKS_COL_1 + "))";
    //"PRIMARY KEY (" + TRANSACTIONS_COL_1 + ", " + TRANSACTIONS_COL_2 + "))";
    private static final String CREATE_TABLE_WATCHLISTS = "CREATE TABLE IF NOT EXISTS " + WATCHLISTS_TABLE + " (" + WATCHLISTS_COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            WATCHLISTS_COL_2 + " NVARCHAR(200) NOT NULL, " + WATCHLISTS_COL_3 + " DATETIME NOT NULL, " +
            WATCHLISTS_COL_4 + " CHAR(20) NOT NULL, " +
            "FOREIGN KEY (" + WATCHLISTS_COL_4 + ") REFERENCES " + USERS_TABLE + " (" + USERS_COL_1 + "))";
    //"FOREIGN KEY (" + WATCHLISTS_COL_2 + ") REFERENCES " + STOCKS_TABLE + " (" + STOCKS_COL_1 + "), " +
    //"PRIMARY KEY (" + WATCHLISTS_COL_1 + ", " + WATCHLISTS_COL_2 + "))";
    private static final String CREATE_TABLE_WATCHLISTS_STOCKS = "CREATE TABLE IF NOT EXISTS " + WATCHLISTS_STOCKS_TABLE + " (" + WATCHLISTS_STOCKS_COL_1 + " INTEGER NOT NULL, " +
            WATCHLISTS_STOCKS_COL_2 + " CHAR(10) NOT NULL, " +
            "FOREIGN KEY (" + WATCHLISTS_STOCKS_COL_1 + ") REFERENCES " + WATCHLISTS_TABLE + " (" + WATCHLISTS_COL_1 + "), " +
            "FOREIGN KEY (" + WATCHLISTS_STOCKS_COL_2 + ") REFERENCES " + STOCKS_TABLE + " (" + STOCKS_COL_1 + "), " +
            "PRIMARY KEY (" + WATCHLISTS_STOCKS_COL_1 + ", " + WATCHLISTS_STOCKS_COL_2 + "))";
    private static final String CREATE_TABLE_NOTES = "CREATE TABLE IF NOT EXISTS " + NOTES_TABLE + " (" + NOTES_COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            NOTES_COL_2 + " TEXT NOT NULL, " + NOTES_COL_3 + " DATETIME NOT NULL, " + NOTES_COL_4 + " CHAR(20) NOT NULL, " +
            "FOREIGN KEY (" + NOTES_COL_4 + ") REFERENCES " + USERS_TABLE + " (" + USERS_COL_1 + "))";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        //SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_STOCKS);
        db.execSQL(CREATE_TABLE_TRANSACTIONS);
        db.execSQL(CREATE_TABLE_WATCHLISTS);
        db.execSQL(CREATE_TABLE_WATCHLISTS_STOCKS);
        db.execSQL(CREATE_TABLE_NOTES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS USERS");
        db.execSQL("DROP TABLE IF EXISTS STOCK");
        db.execSQL("DROP TABLE IF EXISTS TRANSACTIONS");
        db.execSQL("DROP TABLE IF EXISTS WATCHLISTS");
        db.execSQL("DROP TABLE IF EXISTS WATCHLISTS_STOCKS");
        db.execSQL("DROP TABLE IF EXISTS NOTES");
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        db.disableWriteAheadLogging();  // Here the solution
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys=ON;");
    }

    public boolean insertUser(String username, String password, String fullname, String email)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USERS_COL_1, username);
        contentValues.put(USERS_COL_2, password);
        contentValues.put(USERS_COL_3, fullname);
        contentValues.put(USERS_COL_4, email);
        long result = db.insert(USERS_TABLE, null, contentValues);

        if(result == -1)
            return false;
        else
            return true;
    }


    public boolean insertStock(String id, String name)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(STOCKS_COL_1, id);
        contentValues.put(STOCKS_COL_2, name);

        long result = db.insert(STOCKS_TABLE, null, contentValues);

        if(result == -1)
            return false;
        else
            return true;
    }

    public boolean insertTransaction(String username, String stockID, double price, int numberStocks, boolean type, String date)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TRANSACTIONS_COL_2, username);
        contentValues.put(TRANSACTIONS_COL_3, stockID);
        contentValues.put(TRANSACTIONS_COL_4, price);
        contentValues.put(TRANSACTIONS_COL_5, numberStocks);
        contentValues.put(TRANSACTIONS_COL_6, type);
        contentValues.put(TRANSACTIONS_COL_7, date);
        long result = db.insert(TRANSACTIONS_TABLE, null, contentValues);

        if(result == -1)
            return false;
        else
            return true;
    }

    public boolean insertWatchlist(String username, String name, String date)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(WATCHLISTS_COL_2, name);
        contentValues.put(WATCHLISTS_COL_3, date);
        contentValues.put(WATCHLISTS_COL_4, username);
        long result = db.insert(WATCHLISTS_TABLE, null, contentValues);

        if(result == -1)
            return false;
        else
            return true;
    }

    public boolean insertWatchlist_stock(int watchlistID, String stockID, String date)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(WATCHLISTS_STOCKS_COL_1, watchlistID);
        contentValues.put(WATCHLISTS_STOCKS_COL_2, stockID);
        long result = db.insert(WATCHLISTS_STOCKS_TABLE, null, contentValues);

        if(result == -1)
            return false;
        else
            return true;
    }

    public boolean insertNote(String note, String date, String username)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTES_COL_2, note);
        contentValues.put(NOTES_COL_3, date);
        contentValues.put(NOTES_COL_4, username);
        long result = db.insert(NOTES_TABLE, null, contentValues);

        if(result == -1)
            return false;
        else
            return true;
    }

    public boolean updateUser(String username, String password, String fullname, String email)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USERS_COL_1, username);
        contentValues.put(USERS_COL_2, password);
        contentValues.put(USERS_COL_3, fullname);
        contentValues.put(USERS_COL_4, email);
        long result = db.update(USERS_TABLE, contentValues, USERS_COL_1 + " = ?", new String[] {username});

        if(result == 0)
            return false;
        else
            return true;
    }

    public boolean updateStock(String id, String name)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(STOCKS_COL_1, id);
        contentValues.put(STOCKS_COL_2, name);

        long result = db.update(STOCKS_TABLE, contentValues, STOCKS_COL_1 + " = ?", new String[] {id});

        if(result == 0)
            return false;
        else
            return true;
    }

    public boolean updateTransaction(int transactionID, String username, String stockID, float price, int numberStocks, boolean type, String date)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TRANSACTIONS_COL_1, transactionID);
        contentValues.put(TRANSACTIONS_COL_2, username);
        contentValues.put(TRANSACTIONS_COL_3, stockID);
        contentValues.put(TRANSACTIONS_COL_4, price);
        contentValues.put(TRANSACTIONS_COL_5, numberStocks);
        contentValues.put(TRANSACTIONS_COL_6, type);
        contentValues.put(TRANSACTIONS_COL_7, date);

        long result = db.update(TRANSACTIONS_TABLE, contentValues, TRANSACTIONS_COL_1 + " = ?",
                new String[] {Integer.toString(transactionID)});

        if(result == 0)
            return false;
        else
            return true;
    }

    public boolean updateWatchlist(int watchlistID, String username, String name, String date)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(WATCHLISTS_COL_1, watchlistID);
        contentValues.put(WATCHLISTS_COL_2, username);
        contentValues.put(WATCHLISTS_COL_3, name);
        contentValues.put(WATCHLISTS_COL_4, date);

        long result = db.update(WATCHLISTS_TABLE, contentValues, WATCHLISTS_COL_1 + " = ?",
                new String[] {Integer.toString(watchlistID)});

        if(result == 0)
            return false;
        else
            return true;
    }

    public boolean updateWatchlist_Stock(int watchlistID, String stockID)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(WATCHLISTS_STOCKS_COL_1, watchlistID);
        contentValues.put(WATCHLISTS_STOCKS_COL_2, stockID);

        long result = db.update(WATCHLISTS_STOCKS_TABLE, contentValues, WATCHLISTS_STOCKS_COL_1 + " = ? AND " +
                WATCHLISTS_STOCKS_COL_2 + " = ?", new String[] {Integer.toString(watchlistID), stockID});

        if(result == 0)
            return false;
        else
            return true;
    }

    public boolean updateNote(int noteID, String note, String date, String username)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTES_COL_1, noteID);
        contentValues.put(NOTES_COL_2, note);
        contentValues.put(NOTES_COL_3, date);
        contentValues.put(NOTES_COL_4, username);

        long result = db.update(STOCKS_TABLE, contentValues, NOTES_COL_1 + " = ?", new String[] {Integer.toString(noteID)});

        if(result == 0)
            return false;
        else
            return true;
    }

    public boolean deleteUser(String username)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        long result = db.delete(USERS_TABLE,USERS_COL_1 + " = ?", new String[] {username});

        if(result == 0)
            return false;
        else
            return true;
    }

    public boolean deleteStock(String id, String name)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        long result = db.delete(STOCKS_TABLE,STOCKS_COL_1 + " = ?", new String[] {id});

        if(result == 0)
            return false;
        else
            return true;
    }

    public boolean deleteTransaction(int transactionID)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        long result = db.delete(TRANSACTIONS_TABLE,TRANSACTIONS_COL_1 + " = ?", new String[] {Integer.toString(transactionID)});

        if(result == 0)
            return false;
        else
            return true;
    }

    public boolean deleteWatchlist(int watchlistID)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        long result = db.delete(WATCHLISTS_TABLE,WATCHLISTS_COL_1 + " = ?", new String[] {Integer.toString(watchlistID)});

        if(result == 0)
            return false;
        else
            return true;
    }

    public boolean deleteWatchlist_Stock(int watchlistID, String stockID)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        long result = db.delete(WATCHLISTS_STOCKS_TABLE,WATCHLISTS_STOCKS_COL_1 + "=? AND " +
                WATCHLISTS_STOCKS_COL_2 + "=?", new String[] {Integer.toString(watchlistID), stockID});

        if(result == 0)
            return false;
        else
            return true;
    }

    public boolean deleteNote(int noteID)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        long result = db.delete(NOTES_TABLE,NOTES_COL_1 + " = ?", new String[] {Integer.toString(noteID)});

        if(result == 0)
            return false;
        else
            return true;
    }

    public Cursor getAllData(String tableName)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor dataset = db.rawQuery("Select * from " + tableName, null);

        return dataset;
    }

    public Cursor getData(String queryStr)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor dataset = db.rawQuery(queryStr, null);


        return dataset;
    }
    


}