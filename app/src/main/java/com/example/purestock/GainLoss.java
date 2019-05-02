package com.example.purestock;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.example.purestock.Model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;



public class GainLoss extends AppCompatActivity{
    TextView startPeriodTV;
    TextView endPeriodTV;
    int currUID;
    private ListViewAdapter listviewDisplayAdapter;
    private ListView displayGainloss;
    private List<String[]> transactions;
    private DatePickerDialog.OnDateSetListener mStartDateListenner, mEndDateListenner;
    private DatabaseHelper dbHelper;
    private String startDate, endDate;
    private AlphaVantageHelper avHelper;
    private ArrayList<HashMap<String, SpannableString>> resultEndpointList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_gainloss );

        Toolbar toolbar1 = (Toolbar)findViewById(R.id.gainloss_toolbar);
        setSupportActionBar(toolbar1);
        getSupportActionBar().setTitle("Gain Loss");

        User us = new User();
        currUID = us.getUid();
        dbHelper = new  DatabaseHelper(this);
        avHelper = new AlphaVantageHelper("27KRMC96L132GI57", 2,1000);
        resultEndpointList = new ArrayList<HashMap<String,SpannableString>>();

        startPeriodTV = findViewById( R.id.start_date);
        endPeriodTV = findViewById( R.id.end_date);
        transactions = new ArrayList<String[]>();
        displayGainloss = (ListView) findViewById(R.id.gainloss_display_listview);
        int tmpIDs[] = new int[6];

        tmpIDs[0] = R.id.gainloss_listview_display_first_column;
        tmpIDs[1] = R.id.gainloss_listview_display_second_column;
        tmpIDs[2] = R.id.gainloss_listview_display_third_column;
        tmpIDs[3] = R.id.gainloss_listview_display_fourth_column;
        tmpIDs[4] = R.id.gainloss_listview_display_fifth_column;

        listviewDisplayAdapter = new ListViewAdapter(GainLoss.this, resultEndpointList, R.layout.gainloass_listview_display, 5, tmpIDs);
        listviewDisplayAdapter.setIdentifyCoumn(4);
        displayGainloss.setAdapter(listviewDisplayAdapter);


        startPeriodTV.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog pickDialog = new DatePickerDialog(GainLoss.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth, mStartDateListenner,
                        year, month, day);
                pickDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                pickDialog.show();
            }
        });

        mStartDateListenner = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                startPeriodTV.setText(month+1+"/"+dayOfMonth+"/"+year);
                startDate = year + "-" + Integer.toString(month+1) + "-" + dayOfMonth + " 00:00:00";
            }
        };

        endPeriodTV.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog pickDialog = new DatePickerDialog(GainLoss.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth, mEndDateListenner,
                        year, month, day);
                pickDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                pickDialog.show();
            }
        });

        mEndDateListenner = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                endPeriodTV.setText(month+1+"/"+dayOfMonth+"/"+year);
                endDate = year + "-" + Integer.toString(month+1) + "-" + dayOfMonth + " 00:00:00";
                LoadTransactions(Integer.toString(currUID), startDate, endDate);

                avHelper.setParameter(0, "function=GLOBAL_QUOTE");
                new ALphaVantageQuoteEndpointQuery().execute("");
            }
        };
    }

    private String GetStockNameByID(String stockID)
    {
        String result = "";
        Cursor stockCursor = dbHelper.getData("select NAME from " + DatabaseHelper.STOCKS_TABLE + " WHERE ID=\"" +
                stockID + "\"");

        if (stockCursor != null) {
            if (stockCursor.moveToFirst()) {
                result = stockCursor.getString(stockCursor.getColumnIndex("NAME"));
            }
        }

        return result;
    }

    public class ALphaVantageQuoteEndpointQuery extends AsyncTask<String, Void, List<String>> {

        @Override
        protected List<String> doInBackground(String... params)
        {
            List<String> tmp = new ArrayList<>();

            for (String[] values : transactions) {
                if(values[4].equals("0")) {
                    avHelper.setParameter(1, "symbol=" + values[1]);
                    tmp.add(avHelper.submitRequest());
                }
            }

            return tmp;
        }

        @Override
        protected void onPostExecute(List<String> searchResults)
        {
            JSONObject json;
            String stockID = "";
            String stockIDNAM = "";
            double gainloss;
            double oldPrice, newPrice;
            int index = 0;

            if(!resultEndpointList.isEmpty())
                resultEndpointList.clear();

            try {
                for(String value : searchResults) {

                    json = new JSONObject(value);

                    JSONObject matchedObject;

                    matchedObject = json.optJSONObject("Global Quote");
                    if (matchedObject != null) {
                        HashMap<String, SpannableString> hashmap = new HashMap<String, SpannableString>();

                        stockID = matchedObject.getString("01. symbol");
                        stockIDNAM = stockID + "\n" + GetStockNameByID(stockID);

                        SpannableString ss1=  new SpannableString(stockIDNAM);
                        ss1.setSpan(new RelativeSizeSpan(0.5f), stockID.length() + 1, stockIDNAM.length(), 0); // set size
                        hashmap.put("0", ss1);

                        gainloss = Double.parseDouble(matchedObject.getString("05. price"));

                        ss1 = new SpannableString(transactions.get(index)[3]);
                        hashmap.put("1", ss1);

                        ss1 = new SpannableString(transactions.get(index)[2]);
                        hashmap.put("2", ss1);

                        newPrice = Double.parseDouble(matchedObject.getString("05. price"));
                        ss1 = new SpannableString(String.format("%.2f", newPrice));
                        hashmap.put("3", ss1);

                        oldPrice = Double.parseDouble(transactions.get(index)[2]);
                        gainloss = ((gainloss - oldPrice)/oldPrice)*100;
                        ss1 = new SpannableString(String.format("%.2f", gainloss) + "%");
                        hashmap.put("4", ss1);

                        resultEndpointList.add(hashmap);
                    }
                    index++;
                }

                listviewDisplayAdapter.notifyDataSetChanged();
                displayGainloss.setAdapter(listviewDisplayAdapter);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }

    private int compareDateTime(String startdate, String enddate)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date d1 = null;
        try {
            d1 = sdf.parse(startdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Date d2 = null;
        try {
            d2 = sdf.parse(enddate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int compareResult = d1.compareTo(d2);

        return compareResult;
    }

    private int findInTransaction(String SID)
    {
        int index = -1;
        String tmp[] = new String[6];

        if(transactions.size() > 0)
        {
            for(int i=0; i<transactions.size(); i++)
            {
                tmp = transactions.get(i);
                if(tmp[1].equals(SID)) {
                    index = i;
                    break;
                }
            }
        }

        return index;
    }

    private void LoadTransactions(String UID, String startdate, String enddate) {
        Cursor transactionCursor = dbHelper.getData("SELECT * FROM " + DatabaseHelper.TRANSACTIONS_TABLE + " WHERE UID=\"" +
                UID + "\"");

        // if Cursor is contains results
        if (transactionCursor != null)
        {
            int index;
            transactions.clear();
            int compareResult;

            if (transactionCursor.moveToFirst()) {
                do {

                    compareResult = compareDateTime(transactionCursor.getString(transactionCursor.getColumnIndex("DATE")), startdate);
                    if(compareResult >= 0)              // Older than start date
                    {
                        compareResult = compareDateTime(transactionCursor.getString(transactionCursor.getColumnIndex("DATE")), enddate);
                        if(compareResult <= 0)          // Younger than end date
                        {
                            index = findInTransaction(transactionCursor.getString(transactionCursor.getColumnIndex("SID")));

                            if(index == -1)             // New transaction
                            {
                                String[] values = new String[6];

                                values[0] = transactionCursor.getString(transactionCursor.getColumnIndex("TID"));
                                values[1] = transactionCursor.getString(transactionCursor.getColumnIndex("SID"));
                                values[2] = transactionCursor.getString(transactionCursor.getColumnIndex("PRICE"));
                                values[3] = transactionCursor.getString(transactionCursor.getColumnIndex("NUMBER_STOCK"));
                                values[4] = transactionCursor.getString(transactionCursor.getColumnIndex("TYPE"));
                                values[5] = transactionCursor.getString(transactionCursor.getColumnIndex("DATE"));

                                if(transactions != null)
                                    transactions.add(values);
                            }
                            else                        // Transaction exited in list
                            {
                                String[] values = new String[6];
                                double avgPrice;
                                int shares;

                                values = transactions.get(index);

                                if(transactionCursor.getString(transactionCursor.getColumnIndex("TYPE")).equals("0")) {
                                    if (values[4].equals("0")) {
                                        shares = Integer.parseInt(values[3]) + Integer.parseInt(transactionCursor.getString(transactionCursor.getColumnIndex("NUMBER_STOCK")));
                                        avgPrice = ((Double.parseDouble(values[2]) + Double.parseDouble(transactionCursor.getString(transactionCursor.getColumnIndex("PRICE")))) / 2);
                                        values[2] = Double.toString(avgPrice);
                                    } else {
                                        shares = Integer.parseInt(values[3]) + Integer.parseInt(transactionCursor.getString(transactionCursor.getColumnIndex("NUMBER_STOCK")));
                                        if (shares > 0) {
                                            values[2] = transactionCursor.getString(transactionCursor.getColumnIndex("PRICE"));
                                            values[4] = "0";
                                        }
                                    }
                                }
                                else {
                                    if (values[4].equals("0")) {
                                        shares = Integer.parseInt(values[3]) - Integer.parseInt(transactionCursor.getString(transactionCursor.getColumnIndex("NUMBER_STOCK")));
                                        if (shares < 0) {
                                            values[4] = "1";
                                        }
                                    }
                                    else
                                        shares = -Integer.parseInt(values[3]) - Integer.parseInt(transactionCursor.getString(transactionCursor.getColumnIndex("NUMBER_STOCK")));
                                }
                                values[3] = Integer.toString(shares);
                                transactions.set(index, values);
                            }
                        }
                    }
                } while (transactionCursor.moveToNext());
            }
        }
    }

}