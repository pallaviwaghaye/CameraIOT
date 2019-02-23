package com.webakruti.iot.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.webakruti.iot.CameraModel;
import com.webakruti.iot.R;


import java.util.List;


public class CamCategoryAdapter extends RecyclerView.Adapter<CamCategoryAdapter.ViewHolder> {

    private Activity context;
    List<CameraModel> list; // = new ArrayList<>();
    int size;

    public CamCategoryAdapter(Activity context, List<CameraModel> list) {
        this.context = context;
        this.size = size;
        this.list = list;

    }

    @NonNull
    @Override
    public CamCategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_category, viewGroup, false);
        CamCategoryAdapter.ViewHolder viewHolder = new CamCategoryAdapter.ViewHolder(v);
        return viewHolder;
    }

    //@SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final CamCategoryAdapter.ViewHolder viewHolder, final int position) {

        final CameraModel cameraModel = list.get(position);

        viewHolder.textViewDate.setText(cameraModel.getDate());
        viewHolder.textViewTime.setText(cameraModel.getTime());

        /*viewHolder.textViewMoved.setText(cameraModel.getCamMove());
        viewHolder.textViewCovered.setText(cameraModel.getCamIr());
        viewHolder.textViewOpened.setText(cameraModel.getCamOpen());*/

        if(cameraModel.getCamMove().equalsIgnoreCase("1") && cameraModel.getCamIr().equalsIgnoreCase("1") && cameraModel.getCamOpen().equalsIgnoreCase("1"))
        {
            if(cameraModel.getCamMove() != null) {
                viewHolder.textViewMoved.setText("Move");
                //viewHolder.textViewMoved.setBackgroundColor(R.color.red);
                viewHolder.textViewMoved.setBackgroundColor(context.getResources().getColor(R.color.red));
            }
            if(cameraModel.getCamIr() != null) {
                viewHolder.textViewCovered.setText("Cover");
                //viewHolder.textViewCovered.setBackgroundColor(R.color.red);
                viewHolder.textViewCovered.setBackgroundColor(context.getResources().getColor(R.color.red));
            }
            if(cameraModel.getCamOpen() != null) {
                viewHolder.textViewOpened.setText("Open");
                //viewHolder.textViewOpened.setBackgroundColor(R.color.red);
                viewHolder.textViewOpened.setBackgroundColor(context.getResources().getColor(R.color.red));
            }
        }else if(cameraModel.getCamMove().equalsIgnoreCase("1") && cameraModel.getCamIr().equalsIgnoreCase("1")){
            if(cameraModel.getCamMove().equalsIgnoreCase("1")) {
                viewHolder.textViewMoved.setText("Move");
                //viewHolder.textViewMoved.setBackgroundColor(R.color.red);
                viewHolder.textViewMoved.setBackgroundColor(context.getResources().getColor(R.color.red));
            }
            if(cameraModel.getCamIr().equalsIgnoreCase("1")) {
                viewHolder.textViewCovered.setText("Cover");
                //viewHolder.textViewCovered.setBackgroundColor(R.color.red);
                viewHolder.textViewCovered.setBackgroundColor(context.getResources().getColor(R.color.red));
            }
            if(cameraModel.getCamOpen().equalsIgnoreCase("0")) {
                viewHolder.textViewOpened.setText("Open");
                viewHolder.textViewOpened.setBackgroundColor(context.getResources().getColor(R.color.green));
            }

        }
        else if(cameraModel.getCamMove().equalsIgnoreCase("1") && cameraModel.getCamOpen().equalsIgnoreCase("1"))
        {
            if(cameraModel.getCamMove().equalsIgnoreCase("1")) {
                viewHolder.textViewMoved.setText("Move");
                //viewHolder.textViewMoved.setBackgroundColor(R.color.red);
                viewHolder.textViewMoved.setBackgroundColor(context.getResources().getColor(R.color.red));
            }
            if(cameraModel.getCamOpen().equalsIgnoreCase("1")) {
                viewHolder.textViewOpened.setText("Open");
                //viewHolder.textViewOpened.setBackgroundColor(R.color.red);
                viewHolder.textViewOpened.setBackgroundColor(context.getResources().getColor(R.color.red));
            }
            if(cameraModel.getCamIr().equalsIgnoreCase("0")) {
                viewHolder.textViewCovered.setText("Cover");
                //viewHolder.textViewCovered.setBackgroundColor(R.color.red);
                viewHolder.textViewCovered.setBackgroundColor(context.getResources().getColor(R.color.green));
            }
        }
        else if(cameraModel.getCamIr().equalsIgnoreCase("1") && cameraModel.getCamOpen().equalsIgnoreCase("1"))
        {
            if(cameraModel.getCamIr().equalsIgnoreCase("1")) {
                viewHolder.textViewCovered.setText("Cover");
                //viewHolder.textViewCovered.setBackgroundColor(R.color.red);
                viewHolder.textViewCovered.setBackgroundColor(context.getResources().getColor(R.color.red));
            }
            if(cameraModel.getCamOpen().equalsIgnoreCase("1")) {
                viewHolder.textViewOpened.setText("Open");
                //viewHolder.textViewOpened.setBackgroundColor(R.color.red);
                viewHolder.textViewOpened.setBackgroundColor(context.getResources().getColor(R.color.red));
            }
            if(cameraModel.getCamMove().equalsIgnoreCase("0")) {
                viewHolder.textViewMoved.setText("Move");
                //viewHolder.textViewMoved.setBackgroundColor(R.color.red);
                viewHolder.textViewMoved.setBackgroundColor(context.getResources().getColor(R.color.green));
            }
        }

        else if(cameraModel.getCamMove().equalsIgnoreCase("0") && cameraModel.getCamIr().equalsIgnoreCase("0") && cameraModel.getCamOpen().equalsIgnoreCase("0"))
        {
            if(cameraModel.getCamMove() != null) {
                viewHolder.textViewMoved.setText("Move");
                viewHolder.textViewMoved.setBackgroundColor(context.getResources().getColor(R.color.green));
            }
            if(cameraModel.getCamIr() != null) {
                viewHolder.textViewCovered.setText("Cover");
                viewHolder.textViewCovered.setBackgroundColor(context.getResources().getColor(R.color.green));
            }
            if(cameraModel.getCamOpen() != null) {
                viewHolder.textViewOpened.setText("Open");
                viewHolder.textViewOpened.setBackgroundColor(context.getResources().getColor(R.color.green));
            }
        }else if(cameraModel.getCamMove().equalsIgnoreCase("0") && cameraModel.getCamIr().equalsIgnoreCase("0")){
            if(cameraModel.getCamMove().equalsIgnoreCase("0")){
                viewHolder.textViewMoved.setText("Move");
                viewHolder.textViewMoved.setBackgroundColor(context.getResources().getColor(R.color.green));
            }
            if(cameraModel.getCamIr().equalsIgnoreCase("0")) {
                viewHolder.textViewCovered.setText("Cover");
                viewHolder.textViewCovered.setBackgroundColor(context.getResources().getColor(R.color.green));
            }
            if(cameraModel.getCamOpen().equalsIgnoreCase("1")) {
                viewHolder.textViewOpened.setText("Open");
                viewHolder.textViewOpened.setBackgroundColor(context.getResources().getColor(R.color.red));
            }

        }
        else if(cameraModel.getCamMove().equalsIgnoreCase("0") && cameraModel.getCamOpen().equalsIgnoreCase("0"))
        {
            if(cameraModel.getCamMove().equalsIgnoreCase("0")) {
                viewHolder.textViewMoved.setText("Move");
                viewHolder.textViewMoved.setBackgroundColor(context.getResources().getColor(R.color.green));
            }
            if(cameraModel.getCamOpen().equalsIgnoreCase("0")) {
                viewHolder.textViewOpened.setText("Open");
                viewHolder.textViewOpened.setBackgroundColor(context.getResources().getColor(R.color.green));
            }
            if(cameraModel.getCamIr().equalsIgnoreCase("1")) {
                viewHolder.textViewCovered.setText("Cover");
                viewHolder.textViewCovered.setBackgroundColor(context.getResources().getColor(R.color.red));
            }
        }
        else if(cameraModel.getCamIr().equalsIgnoreCase("0") && cameraModel.getCamOpen().equalsIgnoreCase("0"))
        {
            if(cameraModel.getCamIr().equalsIgnoreCase("0")) {
                viewHolder.textViewCovered.setText("Cover");
                viewHolder.textViewCovered.setBackgroundColor(context.getResources().getColor(R.color.green));
            }
            if(cameraModel.getCamOpen().equalsIgnoreCase("0")) {
                viewHolder.textViewOpened.setText("Open");
                viewHolder.textViewOpened.setBackgroundColor(context.getResources().getColor(R.color.green));
            }
            if(cameraModel.getCamMove().equalsIgnoreCase("1")) {
                viewHolder.textViewMoved.setText("Move");
                viewHolder.textViewMoved.setBackgroundColor(context.getResources().getColor(R.color.red));
            }
        }else{
            if(cameraModel.getCamMove() != null) {
                viewHolder.textViewMoved.setText("Move");
                //viewHolder.textViewMoved.setBackgroundColor(R.color.green);
                viewHolder.textViewMoved.setBackgroundColor(context.getResources().getColor(R.color.red));
            }
            if(cameraModel.getCamIr() != null) {
                viewHolder.textViewCovered.setText("Cover");
                viewHolder.textViewCovered.setBackgroundColor(context.getResources().getColor(R.color.red));
            }
            if(cameraModel.getCamOpen() != null) {
                viewHolder.textViewOpened.setText("Open");
                viewHolder.textViewOpened.setBackgroundColor(context.getResources().getColor(R.color.red));
            }
        }



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewMoved;
        private TextView textViewCovered;
        private TextView textViewOpened;
        private TextView textViewDate;
        private TextView textViewTime;
       // private ImageView imageViewProduct;
        private CardView cardView;


        public ViewHolder(View itemView) {
            super(itemView);

            cardView = (CardView)itemView.findViewById(R.id.cardView);
            textViewMoved = (TextView)itemView.findViewById(R.id.textViewMoved);
            textViewCovered = (TextView)itemView.findViewById(R.id.textViewCovered);
            textViewOpened = (TextView)itemView.findViewById(R.id.textViewOpened);

            textViewDate = (TextView)itemView.findViewById(R.id.textViewDate);
            textViewTime = (TextView)itemView.findViewById(R.id.textViewTime);
           // imageViewProduct = (ImageView)itemView.findViewById(R.id.imageViewProduct);


        }
    }
}


