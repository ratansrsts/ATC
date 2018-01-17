package com.example.ratan.atc;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;


public class chatlistadapter extends ArrayAdapter<chatlistdataset> implements View.OnClickListener{

    private ArrayList<chatlistdataset> dataSet;
    Context mContext;
    private static class ViewHolder {
        ImageView usr;
        ImageView me;
        RelativeLayout layouta;
        TextView txtmessage;

    }

    public chatlistadapter(ArrayList<chatlistdataset> data, Context context) {
        super(context, R.layout.chatviewlayout, data);
        this.dataSet = data;
        this.mContext=context;

    }

    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        chatlistdataset dataModel=(chatlistdataset)object;

    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        chatlistdataset dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        chatlistadapter.ViewHolder viewHolder; // view lookup cache stored in tag
        LayoutInflater inflater  = LayoutInflater.from(mContext);;
        final View result;

        if (convertView == null) {

            viewHolder = new chatlistadapter.ViewHolder();

            convertView = inflater.inflate(R.layout.chatviewlayout, parent, false);
            viewHolder.usr = (ImageView) convertView.findViewById(R.id.user_img);
            viewHolder.layouta = (RelativeLayout) convertView.findViewById(R.id.even_container);
            viewHolder.txtmessage = (TextView) convertView.findViewById(R.id.text);


            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (chatlistadapter.ViewHolder) convertView.getTag();
            result=convertView;
        }

        System.out.println("ramiz testing: Datamodel user:" +dataModel.getUsername());
        System.out.println("ramiz testing: getuser user:" +new getuser().getUsername(inflater.getContext()));

        if((dataModel.getUid().equals(new getuser().getUid(inflater.getContext())))||dataModel.getTime().equals("true")){
            viewHolder.layouta.setBackgroundColor(Color.parseColor("#66cc66"));
            viewHolder.txtmessage.setTextColor(Color.WHITE);
        }
        else
        {
            viewHolder.layouta.setBackgroundColor(Color.parseColor("#ffffff"));
            viewHolder.txtmessage.setTextColor(Color.BLACK);
        }
       // viewHolder.usr.setText(dataModel.getUsername());
        viewHolder.txtmessage.setText(dataModel.getMessage());

        // Return the completed view to render on screen
        return convertView;
    }
}
