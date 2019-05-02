package com.example.purestock.Fragement;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.purestock.DatabaseHelper;

import com.example.purestock.ListViewAdapter;
import com.example.purestock.MainActivity;
import com.example.purestock.CommonUtilities;
import com.example.purestock.Model.User;
import com.example.purestock.R;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.example.purestock.AlphaVantageHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

//04-25-2019
/*
* Fix bug
* reason: foreign key can not connect to the user because the UID is not unique and esixt
* How to solve: By change the user table add one more col for UID type to be integer and auto increasing
 */




public class Watch_ListFragment extends Fragment {
    MaterialSearchView subSearchView;
    ListViewAdapter listviewSearchAdapter;
    ListViewAdapter listviewDisplayAdapter;
    SwipeMenuListView searchResult;
    SwipeMenuListView displayWatchlist;
    AlphaVantageHelper avHelper;
    View view;
    //ArrayList<HashMap<String, String>> resultList;
    ArrayList<HashMap<String, SpannableString>> resultList;
    ArrayList<HashMap<String, SpannableString>> resultEndpointList ;
    int currUID;
    DatabaseHelper dbHelper;
    CommonUtilities cUtil;
    List<String> stockIDs;
    //HashMap<String,String> stockIDs;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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

    private void LoadWatchlistStock()
    {
        Cursor watchlistCursor = dbHelper.getAllData(DatabaseHelper.WATCHLISTS_STOCKS_TABLE);
        String stockID;

        if((stockIDs != null) && (stockIDs.size() > 0))
            stockIDs.clear();

        // if Cursor is contains results
        if (watchlistCursor != null) {

            stockIDs = new ArrayList<String>();
            int index = 0;

            if (watchlistCursor.moveToFirst()) {
                do {
                    stockID = watchlistCursor.getString(watchlistCursor.getColumnIndex("SID"));

                    if(stockID != null) {
                        stockIDs.add(stockID);
                    }
                } while (watchlistCursor.moveToNext());
            }
        }
    }

    private boolean isWatchlistNameExited(String watchlistName, String UID)
    {
        boolean result = true;

        Cursor stockCursor = dbHelper.getData("select ID from " + DatabaseHelper.WATCHLISTS_TABLE + " WHERE NAME=\"" +
                watchlistName + "\"" + " AND UID=\"" + UID + "\"");

        if (stockCursor != null) {
            if(stockCursor.getCount() > 0)
                result = false;
        }

        return result;
    }

    private void GetWatchlistInfo() {
        if(!stockIDs.isEmpty())
        {
            if((resultEndpointList != null) && (resultEndpointList.size() > 0))
                resultEndpointList.clear();

            new ALphaVantageQuoteEndpointQuery().execute("");
        }
    }

    public class ALphaVantageQuoteEndpointQuery extends AsyncTask<String, Void, List<String>> {

        @Override
        protected List<String> doInBackground(String... params)
        {
            List<String> tmp = new ArrayList<>();

            avHelper.setParameter(0, "function=GLOBAL_QUOTE");

            for (String value : stockIDs) {
                avHelper.setParameter(1, "symbol=" + value);
                tmp.add(avHelper.submitRequest());
            }

            return tmp;
        }

        @Override
        protected void onPostExecute(List<String> searchResults)
        {
            //add
            JSONObject json;
            String stockID = "";
            String stockIDNAM = "";

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
                        ss1 = new SpannableString(matchedObject.getString("05. price"));
                        hashmap.put("1", ss1);
                        ss1 = new SpannableString(matchedObject.getString("10. change percent"));
                        hashmap.put("2", ss1);
                        // hashmap.put("2", matchedObject.getString("10. change percent"));
                        //hashmap.put("1", SpannableString(matchedObject.getString("05. price")));
                        // hashmap.put("2", matchedObject.getString("10. change percent"));
                        resultEndpointList.add(hashmap);
                    }
                }

                listviewDisplayAdapter.notifyDataSetChanged();
                displayWatchlist.setAdapter(listviewDisplayAdapter);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }

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
                    bestMatched = new String[matchedArray.length() * 2];

                    for (int i = 0; i < matchedArray.length(); i++) {
                        JSONObject stockEntry = matchedArray.getJSONObject(i);
                        bestMatched[i * 2] = stockEntry.getString("1. symbol");
                        bestMatched[i * 2 + 1] = stockEntry.getString("2. name");
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
            //List<String> searchContents = new ArrayList<String>();

            //resultList=new ArrayList<HashMap<String,String>>();
            resultList=new ArrayList<HashMap<String,SpannableString>>();
            if (searchResults != null)
            {
                SpannableString ss1 ;
                for(int i=0; i<searchResults.length/2; i++)
                {
                    //HashMap<String,String> hashmap=new HashMap<String, String>();
                    HashMap<String,SpannableString> hashmap=new HashMap<String, SpannableString>();
                    //hashmap.put("First", searchResults[i*2]);
                    //hashmap.put("Second", searchResults[i*2+1]);
                    ss1 =  new SpannableString(searchResults[i*2]);
                    hashmap.put("0", ss1);
                    ss1 =  new SpannableString(searchResults[i*2+1]);
                    hashmap.put("1", ss1);
                    //hashmap.put("0", searchResults[i*2]);
                    //hashmap.put("1", searchResults[i*2+1]);
                    resultList.add(hashmap);
                }
            }
            int tmpIDs[] = new int[2];

            tmpIDs[0] = R.id.watchlist_listview_search_first_column;
            tmpIDs[1] = R.id.watchlist_listview_search_second_column;
            listviewSearchAdapter = new ListViewAdapter(((MainActivity)getActivity()), resultList, R.layout.watchlist_listview_search_stock, 2, tmpIDs);
            searchResult.setAdapter(listviewSearchAdapter);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate( R.layout.fragment_watch__list, container, false );
        subSearchView = (MaterialSearchView)view.findViewById(R.id.search_view1);
        searchResult = (SwipeMenuListView)view.findViewById(R.id.watchlist_search_listview);
        displayWatchlist = (SwipeMenuListView) view.findViewById(R.id.watchlist_display_listview);
        avHelper = new AlphaVantageHelper("27KRMC96L132GI57", 2,1000);
        dbHelper = new DatabaseHelper(view.getContext());
        cUtil = new CommonUtilities();
        resultEndpointList = new ArrayList<HashMap<String,SpannableString>>();
        searchResult.setVisibility(View.GONE);

        User us = new User();
        currUID = us.getUid();

        int tmpIDs[] = new int[4];

        tmpIDs[0] = R.id.watchlist_listview_display_first_column;
        tmpIDs[1] = R.id.watchlist_listview_display_second_column;
        tmpIDs[2] = R.id.watchlist_listview_display_third_column;

        listviewDisplayAdapter = new ListViewAdapter(((MainActivity)getActivity()), resultEndpointList, R.layout.watchlist_listview_display, 3, tmpIDs);
        listviewDisplayAdapter.setIdentifyCoumn(2);
        displayWatchlist.setAdapter(listviewDisplayAdapter);

        LoadWatchlistStock();
        GetWatchlistInfo();

        SwipeMenuCreator creator1 = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {

                // create "delete" item
                SwipeMenuItem addItem = new SwipeMenuItem(getActivity().getApplicationContext());

                // set item background
                addItem.setBackground(new ColorDrawable(Color.rgb(0xD0,
                        0xD3, 0xD4)));
                // set item width
                addItem.setWidth(170);
                // set a icon
                addItem.setIcon(R.drawable.ic_delete_watchlist_item);

                // add to menu
                menu.addMenuItem(addItem);
            }
        };

        // set creator
        displayWatchlist.setMenuCreator(creator1);
        displayWatchlist.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index)
                {
                    case 0:
                    {
                        String tmp1;
                        int index1;
                        //HashMap<String, String> tmp = (HashMap<String, String>) listviewDisplayAdapter.getItem(position);
                        HashMap<String, SpannableString> tmp = (HashMap<String, SpannableString>) listviewDisplayAdapter.getItem(position);
                        tmp1 = tmp.get("0").toString();
                        index1 = tmp1.indexOf("\n");
                        if(dbHelper.deleteWatchlist_Stock(1,tmp1.substring(0, index1))) {
                            listviewDisplayAdapter.remove(position);
                            listviewDisplayAdapter.notifyDataSetChanged();

                            listviewDisplayAdapter.setIdentifyCoumn(2);
                            displayWatchlist.setAdapter(listviewDisplayAdapter);
                            displayWatchlist.invalidateViews();

                            Toast.makeText(view.getContext(), "Item deleted", Toast.LENGTH_LONG).show();
                        }
                        break;
                    }
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });

        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                // create "delete" item
                SwipeMenuItem addItem = new SwipeMenuItem(getActivity().getApplicationContext());

                // set item background
                addItem.setBackground(new ColorDrawable(Color.rgb(0x36,
                        0xFF, 0x33)));
                // set item width
                addItem.setWidth(170);
                // set a icon
                addItem.setIcon(R.drawable.ic_add_to_watchlist);

                // add to menu
                menu.addMenuItem(addItem);
            }
        };

        // set creator
        searchResult.setMenuCreator(creator);
        searchResult.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        HashMap<String, SpannableString>  tmp = (HashMap<String, SpannableString> )listviewSearchAdapter.getItem(position);
                        //HashMap<String, String>  tmp = (HashMap<String, String> )listviewSearchAdapter.getItem(position);

                        dbHelper.insertStock(tmp.get("0").toString(), tmp.get("1").toString());

                        if(isWatchlistNameExited("Watchlist", Integer.toString(currUID))) {
                            if (dbHelper.insertWatchlist(currUID, "Watchlist", cUtil.getCurrentDatetime()))
                                Toast.makeText(view.getContext(), "Create new watch list", Toast.LENGTH_LONG).show();
                            else
                                Toast.makeText(view.getContext(), "Failed to create new watch list", Toast.LENGTH_LONG).show();
                        }

                        if(dbHelper.insertWatchlist_stock(1, tmp.get("0").toString(),cUtil.getCurrentDatetime())) {

                            Toast.makeText(view.getContext(), "Added new watch list stock", Toast.LENGTH_LONG).show();

                            avHelper.setParameter(0, "function=GLOBAL_QUOTE");


                            avHelper.setParameter(1, "symbol=" + tmp.get("0"));
                            stockIDs.clear();                       // Only query the last added stock
                            stockIDs.add(tmp.get("0").toString());

                            new ALphaVantageQuoteEndpointQuery().execute("");

                        }
                        else
                            Toast.makeText(view.getContext(), "Failed to add new watch list stock", Toast.LENGTH_LONG).show();

                        break;

                    case 1:
                        break;
                }

                return false;
            }
        });

        subSearchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                searchResult.setVisibility(View.VISIBLE);
                displayWatchlist.setVisibility(View.GONE);
            }

            @Override
            public void onSearchViewClosed() {
                searchResult.setVisibility(View.GONE);
                displayWatchlist.setVisibility(View.VISIBLE);
            }
        });

        subSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query.length() == 0)
                    avHelper.setParameter(1,"");
                else
                    avHelper.setParameter(1,"keywords=" + query);

                avHelper.setParameter(0,"function=SYMBOL_SEARCH");
                new ALphaVantageStockSymbolQuery().execute("");

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                avHelper.setParameter(0,"function=SYMBOL_SEARCH");
                if((newText.length() <= 0) || (newText == null))
                    avHelper.setParameter(1,"");
                else
                    avHelper.setParameter(1,"keywords=" + newText);
                new ALphaVantageStockSymbolQuery().execute("");

                return false;
            }
        });
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_iteams, menu);
        super.onCreateOptionsMenu(menu, inflater);
        //subSearchView = (MaterialSearchView)getActivity().findViewById(R.id.search_view1);
        final MenuItem item = menu.findItem(R.id.action_search);
        subSearchView.setMenuItem(item);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toolbar toolbar1 = (Toolbar) view.findViewById(R.id.toolbar1);
        ((MainActivity)getActivity()).setSupportActionBar(toolbar1);
        toolbar1.setTitle("Watch List");

        setHasOptionsMenu(true);
    }
}