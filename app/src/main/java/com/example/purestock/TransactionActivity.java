package com.example.purestock;
import com.example.purestock.Model.User;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TransactionActivity extends AppCompatActivity{

    Button submit;
    EditText stock_id, price, numbs, type;
    Spinner tranType;
    int uid;
    DatabaseHelper database;
    AlphaVantageHelper avHelper;
    String transStr;

    public class ALphaVantageStockSymbolQuery extends AsyncTask<String, Void, String[]> {

        @Override
        protected String[] doInBackground(String... params)
        {
            String bestMatched[] = null;
            JSONObject json;

            try {
                json = new JSONObject(avHelper.submitRequest());

                JSONArray matchedArray = json.optJSONArray("bestMatches");
                if(matchedArray != null) {

                    for (int i = 0; i < matchedArray.length(); i++) {
                        JSONObject stockEntry = matchedArray.getJSONObject(i);
                        if(params[0].equals(stockEntry.getString("1. symbol"))) {
                            bestMatched = new String[2];
                            bestMatched[0] = stockEntry.getString("1. symbol");
                            bestMatched[1] = stockEntry.getString("2. name");
                        }
                    }
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            return bestMatched;
        }

        @Override
        protected void onPostExecute(String[] searchResults) {
            if (searchResults != null)
            {
                database.insertStock(searchResults[0], searchResults[1]);
                double double_price = Double.parseDouble(price.getText().toString());
                int numShares = Integer.parseInt(numbs.getText().toString());
                String date = new SimpleDateFormat("YYYY-MM-DD HH:MM:SS", Locale.getDefault()).format(new Date());
                String tmpType = "";

                if(transStr.equals("Buy"))
                    tmpType = "0";
                else if(transStr.equals("Sell"))
                    tmpType = "1";

                database.insertStock(searchResults[0], searchResults[1]);
                Boolean insertTrans = database.insertTransaction( uid, searchResults[0], double_price, numShares, tmpType, date);

                if (insertTrans){
                    Toast.makeText(TransactionActivity.this, "New transaction inserted!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(TransactionActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    // Toast.makeText( TransactionActivity.this,"uid = " + uid , Toast.LENGTH_SHORT ).show();
                    //Toast.makeText(TransactionActivity.this, "Failed to insert transaction data", Toast.LENGTH_LONG).show();
                }
            }
            else
            {
                Toast.makeText(TransactionActivity.this, "Stock symbol incorrect!", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_transaction );

        avHelper = new AlphaVantageHelper("27KRMC96L132GI57", 2,1000);
        database = new  DatabaseHelper(this);

        submit = findViewById( R.id.submit_transcation );
        stock_id = findViewById( R.id.Trans_stockID );
        price = findViewById( R.id.Trans_price );
        numbs = findViewById( R.id.Trans_num );
        tranType = findViewById( R.id.transaction_type);

        String[] items = new String[]{"Buy", "Sell"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);

        tranType.setAdapter(adapter);

        tranType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                transStr = (String)parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        submit.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_stock_id = stock_id.getText().toString().toUpperCase();
                User us = new User();
                uid = us.getUid();
//
                //Toast.makeText(TransactionActivity.this, "uid" + uid, Toast.LENGTH_SHORT).show();

                if( TextUtils.isEmpty( str_stock_id ) || TextUtils.isEmpty(price.getText().toString())){
                    Toast.makeText( TransactionActivity.this, "All fileds are required!", Toast.LENGTH_SHORT ).show();
                } else{

                    avHelper.setParameter(1,"keywords=" + str_stock_id);
                    avHelper.setParameter(0,"function=SYMBOL_SEARCH");
                    new ALphaVantageStockSymbolQuery().execute(str_stock_id);
                }
            }
        } );
    }
}