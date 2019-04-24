package com.example.purestock;
import android.app.Activity;
import android.graphics.Color;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class ListViewAdapter extends BaseAdapter {
    public ArrayList<HashMap<String, SpannableString>> list;
    //public ArrayList<HashMap<String, String>> list;

    //public static final String FIRST_COLUMN="First";
    //public static final String SECOND_COLUMN="Second";
    //public static final String THIRD_COLUMN="Third";
    //public static final String FOURTH_COLUMN="Fourth";
    Activity activity;
    int layoutID;
    int numberLayouts;
    int itemIDs[];
    int identifyColumn;

    //public ListViewAdapter(Activity activity,ArrayList<HashMap<String, String>> list, int layout_id, int number_layouts, int textviewIDs[])
    public ListViewAdapter(Activity activity,ArrayList<HashMap<String, SpannableString>> list, int layout_id, int number_layouts, int textviewIDs[])
    {
        super();
        this.activity=activity;
        this.list=list;
        layoutID = layout_id;
        numberLayouts = number_layouts;
        itemIDs = new int[textviewIDs.length];
        itemIDs = textviewIDs.clone();
        identifyColumn = -1;
    }

    public void setIdentifyCoumn(int cloumnNumber)
    {
        identifyColumn = cloumnNumber;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void remove(int position) {
        list.remove(position);
    }


    private class ViewHolder{
        TextView txtLayout[];
        int numberItems;
        //TextView txtFirst;
        //TextView txtSecond;
        //TextView txtThird;
        //TextView txtFourth;

        public ViewHolder(int numberHolders)
        {
            numberItems = numberHolders;
            txtLayout = new TextView[numberItems];
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder;

        LayoutInflater inflater=activity.getLayoutInflater();

        if(convertView == null){

            convertView=inflater.inflate(layoutID, null);
            //convertView=inflater.inflate(R.layout.watchlist_listview_search_stock, null);
            holder = new ViewHolder(numberLayouts);

            for(int i=0; i<numberLayouts; i++)
            {
                holder.txtLayout[i] = (TextView) convertView.findViewById(itemIDs[i]);
            }
            //holder.txtFirst=(TextView) convertView.findViewById(R.id.firstColumn);
            //holder.txtSecond=(TextView) convertView.findViewById(R.id.secondColumn);
            //holder.txtThird=(TextView) convertView.findViewById(R.id.thirdColumn);
            //holder.txtFourth=(TextView) convertView.findViewById(R.id.fourthColumn);

            convertView.setTag(holder);
        }else{

            holder=(ViewHolder) convertView.getTag();
        }

        HashMap<String, SpannableString> map=list.get(position);
        for(int i=0; i<numberLayouts; i++)
        {
            holder.txtLayout[i].setText(map.get(Integer.toString(i)));
            if(i == identifyColumn)
            {
                if(map.get(Integer.toString(i)).charAt(0) == '-')
                    holder.txtLayout[i].setBackgroundColor(Color.parseColor("#FF0000"));
                else
                    holder.txtLayout[i].setBackgroundColor(Color.parseColor("#74FF33"));
            }
            //holder.txtLayout[i] = (TextView) convertView.findViewById(itemIDs[i]);
        }
        //holder.txtFirst.setText(map.get(FIRST_COLUMN));
        //holder.txtSecond.setText(map.get(SECOND_COLUMN));
        //holder.txtThird.setText(map.get(THIRD_COLUMN));
        //holder.txtFourth.setText(map.get(FOURTH_COLUMN));

        return convertView;
    }
}