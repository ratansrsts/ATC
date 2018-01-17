package com.example.ratan.atc;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class listitemadapter extends ArrayAdapter<listitemdatamodel> implements View.OnClickListener{

    private ArrayList<listitemdatamodel> dataSet;
    Context mContext;
    private static class ViewHolder {
        TextView txtName;
        TextView txtmessage;
        ImageView imagein;
        LinearLayout info;

    }

    public listitemadapter(ArrayList<listitemdatamodel> data, Context context) {
        super(context, R.layout.listitemmess, data);
        this.dataSet = data;
        this.mContext=context;

    }

    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        listitemdatamodel dataModel=(listitemdatamodel)object;

        Intent i=new Intent(getContext(),chatactivity.class);
        i.putExtra("id", dataModel.getUserid());
        i.putExtra("rid", dataModel.getReciverid());
        i.putExtra("fn", dataModel.getFname());

        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(i);

//        int position=(Integer) v.getTag();
//        Object object= getItem(position);
//        listitemdatamodel dataModel=(listitemdatamodel)object;

    }

    private int lastPosition = -1;
    listitemdatamodel dataModel;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
         dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.listitemmess, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.username);
            viewHolder.txtmessage = (TextView) convertView.findViewById(R.id.message);
            viewHolder.info = (LinearLayout) convertView.findViewById(R.id.listitmea);
            viewHolder.imagein = (ImageView) convertView.findViewById(R.id.position);



            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }


        viewHolder.txtName.setText(dataModel.getUsername());
        viewHolder.txtmessage.setText(dataModel.getMessage());
        viewHolder.info.setOnClickListener(this);
        viewHolder.info.setTag(position);
        viewHolder.imagein.setTag(position);
        // Return the completed view to render on screen
        return convertView;
    }
}

