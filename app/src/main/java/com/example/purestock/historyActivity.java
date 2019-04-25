package com.example.purestock;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class historyActivity extends AppCompatActivity{ // implements AdapterView.OnItemClickListener {  //
    DatabaseHelper dbHelper;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        //Toolbar toolbar = findViewById(R.id.top_toolbar);
        //setSupportActionBar(toolbar);
       /* comment this line 4-25-2019
        listView = findViewById( R.id.listview3 );

        List<Map<String, String>> data = getSource();

        SimpleAdapter adapter = new SimpleAdapter( historyActivity.this, data,
                R.layout.activity_history, new String[] {"history_SID", "history_price", "history_NUMS"},
                new int[] {R.id.history_SID, R.id.history_price, R.id.history_NUMS});
        listView.setAdapter( adapter );
        listView.setOnClickListener( (View.OnClickListener) this );
*/

    }
/*

    //Database Related Constants
    public static final String KEY_ROWID = "TID";
    public static final String KEY_NAME = "fooditem";
    public static final String KEY_TIMESTAMP = "timestamp";

    private static final String DATABASE_NAME = "AJFoodDB";
    private static final String DATABASE_TABLE = "AJ_Food";
    private static final int DATABASE_VERSION = 1;

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu); //here attach menu object
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item1:
                Toast.makeText(this, "Item1 selected", Toast.LENGTH_SHORT);
                return true;

            case R.id.item2:
                Toast.makeText(this, "Item2 selected", Toast.LENGTH_SHORT);
                return true;


        }
        return super.onOptionsItemSelected(item);
    }
*/

/*comment this line 4-25-2019
@Override
public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                        long arg3) {

}


    public static List<Map<String, String>> getSource() {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        Map<String, String> map = null;

        for (int i = 0; i < 20; i++) {
            map = new HashMap<String, String>();
            map.put("history_SID", );
            map.put("history_price", );
            map.put("history_NUMS", );
            list.add(map);
        }
        return list;
    }

    private String GetStockNameByID(String TransactionID)
    {
        String result = "";
        Cursor stockCursor = dbHelper.getData("select stockID, price, numberStocks from " + DatabaseHelper.TRANSACTIONS_TABLE + " WHERE ID=\"" +
                TransactionID + "\"");

        if (stockCursor != null) {
            if (stockCursor.moveToFirst()) {
                result = stockCursor.getString(stockCursor.getColumnIndex("NAME"));
            }
        }

        return result;
    }

*/
}
