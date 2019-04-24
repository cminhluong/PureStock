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

public class Watch_ListFragment extends Fragment {
    MaterialSearchView subSearchView;
    ListViewAdapter listviewSearchAdapter;
    ListViewAdapter listviewDisplayAdapter;
    SwipeMenuListView searchResult;
    SwipeMenuListView displayWatchlist;
    AlphaVantageHelper avHelper;
    View view;
    ArrayList<HashMap<String, String>> resultList;
    ArrayList<HashMap<String, String>> resultEndpointList;
    private String username;
    DatabaseHelper dbHelper;
    CommonUtilities cUtil;
    List<String> stockIDs;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        username = "test";
    }

    private void LoadWatchlistStock()
    {
        Cursor cursor = dbHelper.getAllData(DatabaseHelper.WATCHLISTS_STOCKS_TABLE);

        if((stockIDs != null) && (stockIDs.size() > 0))
            stockIDs.clear();

        // if Cursor is contains results
        if (cursor != null) {

            stockIDs = new ArrayList<String>();
            if (cursor.moveToFirst()) {
                do {
                    // Get version from Cursor
                    String stockID = cursor.getString(cursor.getColumnIndex("SID"));

                    if(stockID != null)
                        // add the bookName into the bookTitles ArrayList
                        stockIDs.add(stockID);

                } while (cursor.moveToNext());
            }
        }
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

            try {
                for(String value : searchResults) {

                    json = new JSONObject(value);

                    JSONObject matchedObject;

                    matchedObject = json.optJSONObject("Global Quote");
                    if (matchedObject != null) {
                        HashMap<String, String> hashmap = new HashMap<String, String>();

                        hashmap.put("0", matchedObject.getString("01. symbol"));
                        hashmap.put("1", matchedObject.getString("05. price"));
                        hashmap.put("2", matchedObject.getString("10. change percent"));
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

            resultList=new ArrayList<HashMap<String,String>>();
            if (searchResults != null)
            {
                for(int i=0; i<searchResults.length/2; i++)
                {
                    HashMap<String,String> hashmap=new HashMap<String, String>();
                    //hashmap.put("First", searchResults[i*2]);
                    //hashmap.put("Second", searchResults[i*2+1]);
                    hashmap.put("0", searchResults[i*2]);
                    hashmap.put("1", searchResults[i*2+1]);
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
        resultEndpointList = new ArrayList<HashMap<String,String>>();
        searchResult.setVisibility(View.GONE);

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
                        HashMap<String, String> tmp = (HashMap<String, String>) listviewDisplayAdapter.getItem(position);

                        if(dbHelper.deleteWatchlist_Stock(1, tmp.get("0"))) {
                            listviewDisplayAdapter.remove(position);
                            listviewDisplayAdapter.notifyDataSetChanged();


                            listviewDisplayAdapter.setIdentifyCoumn(2);
                            displayWatchlist.setAdapter(listviewDisplayAdapter);
                            displayWatchlist.invalidateViews();

                            Toast.makeText(view.getContext(), "Failed to add new watch list stock", Toast.LENGTH_LONG).show();
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
                // create "open" item
                //SwipeMenuItem openItem = new SwipeMenuItem(getActivity().getApplicationContext());

                // set item background
                // openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                //       0xCE)));
                // set item width
                //openItem.setWidth(170);
                // set item title
                //openItem.setTitle("Add");
                // set item title fontsize
                //openItem.setTitleSize(18);
                // set item title font color
                //openItem.setTitleColor(Color.WHITE);
                // add to menu
                //menu.addMenuItem(openItem);

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
                        HashMap<String, String>  tmp = (HashMap<String, String> )listviewSearchAdapter.getItem(position);

                        dbHelper.insertUser(username, "aaaaaa", "Test Test", "test@test.com");
                        if(dbHelper.insertStock(tmp.get("0"), tmp.get("1")))
                            Toast.makeText(view.getContext(), "Added to Stock", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(view.getContext(), "Existed in Stock", Toast.LENGTH_LONG).show();

                        if(dbHelper.insertWatchlist(username, "TestWatchlist", cUtil.getCurrentDatetime()))
                            Toast.makeText(view.getContext(), "Create new watch list", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(view.getContext(), "Failed to create new watch list", Toast.LENGTH_LONG).show();

                        if(dbHelper.insertWatchlist_stock(1, tmp.get("0"),cUtil.getCurrentDatetime())) {

                            Toast.makeText(view.getContext(), "Added new watch list stock", Toast.LENGTH_LONG).show();

                            avHelper.setParameter(0, "function=GLOBAL_QUOTE");


                            avHelper.setParameter(1, "symbol=" + tmp.get("0"));
                            stockIDs.clear();
                            stockIDs.add(tmp.get("0"));
                            //try {
                            //String str_result = new ALphaVantageQuoteEndpointQuery().execute("");//.get(3000, TimeUnit.SECONDS );
                            new ALphaVantageQuoteEndpointQuery().execute("");
                            //displayWatchlist.invalidateViews();
                            //} catch (TimeoutException e) {
                            //     e.printStackTrace();
                            // } catch (InterruptedException e) {
                            //     e.printStackTrace();
                            // } catch (ExecutionException e) {
                            //     e.printStackTrace();
                            // }
                        }
                        else
                            Toast.makeText(view.getContext(), "Failed to add new watch list stock", Toast.LENGTH_LONG).show();




                        break;

                    case 1:
                        break;
                }
                // false : close the menu; true : not close the menu
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