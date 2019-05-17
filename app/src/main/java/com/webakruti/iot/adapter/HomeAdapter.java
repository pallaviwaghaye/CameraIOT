package com.webakruti.iot.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.webakruti.iot.DeviceDetailsActivity;
import com.webakruti.iot.EditDeviceActivity;
import com.webakruti.iot.Model.cameraList;
import com.webakruti.iot.Model.deleteDevice;
import com.webakruti.iot.R;
import com.webakruti.iot.retrofit.ApiConstants;
import com.webakruti.iot.retrofit.service.RestClient;

import java.io.Serializable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private Activity context;
    List<cameraList.Datum> list;
    int size;
    RecyclerView recyclerView;
    TextView textView;
    private ProgressDialog progressDialogForAPI;
    String id;

    public HomeAdapter(Activity context, List<cameraList.Datum> list,RecyclerView recyclerView,TextView textView) {
        this.context = context;
        this.list = list;
        this.recyclerView = recyclerView;
        this.textView = textView;

    }

    @NonNull
    @Override
    public HomeAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_home_adapter, viewGroup, false);
        HomeAdapter.ViewHolder viewHolder = new HomeAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final HomeAdapter.ViewHolder viewHolder, final int position) {


        final cameraList.Datum cameralist =  list.get(position);
        id = cameralist.getId();


        viewHolder.textViewCameraName.setText(cameralist.getCamId());
        viewHolder.textViewDate.setText(cameralist.getDate());
        viewHolder.textViewTime.setText(cameralist.getTime());

        if(cameralist.getCamlogId().equalsIgnoreCase("0") ||
                (cameralist.getCamMove().equalsIgnoreCase("0") &&
                        cameralist.getCamIr().equalsIgnoreCase("0") &&
                        cameralist.getCamOpen().equalsIgnoreCase("0"))) {
            viewHolder.textViewCameraStatus.setText("Active");
            viewHolder.textViewCameraStatus.setTextColor(context.getResources().getColor(R.color.green));
        }else{
            viewHolder.textViewCameraStatus.setText("Threat Detected");
            viewHolder.textViewCameraStatus.setTextColor(context.getResources().getColor(R.color.red));
        }
        viewHolder.relativeLayoutViewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DeviceDetailsActivity.class);
                intent.putExtra("DeviceDetails",(Serializable)cameralist);
                context.startActivity(intent);

            }
        });

       /* viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DeviceDetailsActivity.class);
                intent.putExtra("DeviceDetails",(Serializable) cameralist);
                context.startActivity(intent);
            }
        });
*/
        viewHolder.textViewOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context,viewHolder.textViewOptions);
                popupMenu.inflate(R.menu.dotmenu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch(item.getItemId()) {
                            case R.id.delete:

                                new AlertDialog.Builder(context,R.style.alertDialog)
                                        .setMessage("Want to delete this device?")
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                //delete device
                                                deleteAndUpdate(position);
                                                notifyDataSetChanged();
                                                /*Intent intent = new Intent(context, AddDeviceActivity.class);
                                                context.startActivity(intent);*/
                                            }
                                        })
                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        })
                                        .show();



                                break;
                            case R.id.edit:
                                Intent intent1 = new Intent(context, EditDeviceActivity.class);
                                intent1.putExtra("DeviceDetails",(Serializable)cameralist);
                                context.startActivity(intent1);

                                break;
                            default:
                                    break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });

    }


    private void deleteAndUpdate(int position) {
        // int id = databaseManager.deleteCallEntry(callId);
        //if (id > 0) {
        deleteFromServer();
        list.remove(position);
        this.notifyDataSetChanged();

        if(list.size() == 0) {
            recyclerView.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
        }
        //}
    }

    private void deleteFromServer() {

            progressDialogForAPI = new ProgressDialog(context);
            progressDialogForAPI.setCancelable(false);
            progressDialogForAPI.setIndeterminate(true);
            progressDialogForAPI.setMessage("Please wait...");
            progressDialogForAPI.show();

            Call<deleteDevice> requestCallback = RestClient.getApiService(ApiConstants.BASE_URL).delete(id);
            requestCallback.enqueue(new Callback<deleteDevice>() {
                @Override
                public void onResponse(Call<deleteDevice> call, Response<deleteDevice> response) {
                    if (response.isSuccessful() && response.body() != null && response.code() == 200) {

                        deleteDevice result = response.body();

                        if(result.getStatus() == true ) {
                            //Toast.makeText(AddDeviceActivity.this, result.getMsg().toString(), Toast.LENGTH_SHORT).show();
                            new AlertDialog.Builder(context,R.style.alertDialog)
                                    .setMessage(result.getMsg().toString())
                                    .setPositiveButton("Ok", null)
                                    .show();
                        }else{
                            //Toast.makeText(AddDeviceActivity.this, result.getMsg().toString(), Toast.LENGTH_SHORT).show();
                            new AlertDialog.Builder(context,R.style.alertDialog)
                                    .setMessage(result.getMsg().toString())
                                    .setPositiveButton("Ok", null)
                                    .show();
                        }

                    } else {
                        // Response code is 401
                        //  Toast.makeText(UserLoginActivity.this, "Unauthorized User!! MobileNo or Password is incorrect.", Toast.LENGTH_SHORT).show();

                        new AlertDialog.Builder(context)
                                .setMessage("Unauthorized User!!")
                                .setPositiveButton("OK", null)
                                .show();

                    }

                    if (progressDialogForAPI != null) {
                        progressDialogForAPI.cancel();
                    }
                }

                @Override
                public void onFailure(Call<deleteDevice> call, Throwable t) {

                    if (t != null) {

                        if (progressDialogForAPI != null) {
                            progressDialogForAPI.cancel();
                        }
                        if (t.getMessage() != null)
                            Log.e("error", t.getMessage());
                    }

                }
            });


    }




    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewCameraName;
        private TextView textViewCameraStatus;
        private TextView textViewDate;
        private TextView textViewTime;
        private RelativeLayout relativeLayoutViewDetails;
        private TextView textViewOptions;

        private CardView cardView;


        public ViewHolder(View itemView) {
            super(itemView);

            cardView = (CardView)itemView.findViewById(R.id.cardView);
            textViewOptions = (TextView)itemView.findViewById(R.id.textViewOptions);
            textViewCameraName = (TextView)itemView.findViewById(R.id.textViewCameraName);
            textViewCameraStatus = (TextView)itemView.findViewById(R.id.textViewCameraStatus);
            textViewDate = (TextView)itemView.findViewById(R.id.textViewDate);
            textViewTime = (TextView)itemView.findViewById(R.id.textViewTime);
            relativeLayoutViewDetails = (RelativeLayout)itemView.findViewById(R.id.relativeLayoutViewDetails);

        }
    }
}
