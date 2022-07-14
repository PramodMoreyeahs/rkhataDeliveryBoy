package in.moreyeahs.livspace.delivery.views.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import in.moreyeahs.livspace.delivery.R;
import in.moreyeahs.livspace.delivery.databinding.MyTaskAdapterBinding;
import in.moreyeahs.livspace.delivery.listener.MyTaskListener;
import in.moreyeahs.livspace.delivery.listener.orderdetailClick;
import in.moreyeahs.livspace.delivery.model.MyTaskModel;
import in.moreyeahs.livspace.delivery.utilities.DateUtils;
import in.moreyeahs.livspace.delivery.utilities.OrderCustomRunnable;
import in.moreyeahs.livspace.delivery.utilities.Utils;
import in.moreyeahs.livspace.delivery.viewmodels.MyTaskInfo;
import in.moreyeahs.livspace.delivery.views.main.NewOrderPlaceActivity;
import in.moreyeahs.livspace.delivery.views.main.RejectedOrderPlaceActivity;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class MyTaskAdapter extends RecyclerView.Adapter<MyTaskAdapter.ViewHolder> {
    private Activity context;
    private ArrayList<MyTaskModel> myTaskList;
    private LayoutInflater layoutInflater;
    private String time,type;
    private orderdetailClick orderdetailClick;

    public MyTaskAdapter(Activity context, ArrayList<MyTaskModel> myTaskList, String type, String time, orderdetailClick orderdetailClick) {
        this.context = context;
        this.myTaskList = myTaskList;
        this.time = time;
        this.orderdetailClick = orderdetailClick;
        this.type = type;
        notifyDataSetChanged();
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        return new ViewHolder(DataBindingUtil.inflate(layoutInflater, R.layout.my_task_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyTaskAdapter.ViewHolder viewHolder, int i) {
        try {
            MyTaskModel myTaskModel = myTaskList.get(i);
            MyTaskInfo info = new MyTaskInfo();
            info.setCustomerName(myTaskModel.getCustomerName() + "(" + myTaskModel.getSkcode() + ")");
            if (myTaskModel.getColorCode() != null && !myTaskModel.getColorCode().isEmpty()) {
                viewHolder.mBinding.tvName.setTextColor(Color.parseColor("" + myTaskModel.getColorCode()));
            } else {
                viewHolder.mBinding.tvName.setTextColor(Color.BLACK);
            }
            viewHolder.mBinding.tvDistance.setText(Math.round(myTaskModel.getDistance()) + "KM");
            // viewHolder.mBinding.tvDistance.setText(myTaskModel.getDistance()+"KM");
            info.setOrderId(context.getString(R.string.OrderID) + myTaskModel.getOrderId());
            if (myTaskModel.getCreatedDate() != null && !myTaskModel.getCreatedDate().isEmpty()) {
                info.setDate(DateUtils.getDateFormat(myTaskModel.getCreatedDate()));
            }
            info.setShopName(myTaskModel.getShopName());
            info.setAddress(myTaskModel.getShippingAddress());
            info.setTotalAmount(context.getString(R.string.rs) + " " + new DecimalFormat("##.##").format(myTaskModel.getTotalAmount()));
            info.setItemCount(context.getString(R.string.item_no) + myTaskModel.getOrderDetailsCount());
            viewHolder.bind(info);

            if (myTaskModel.getOrderDate() != null) {
                try {
                    long currMillis = System.currentTimeMillis();
                    SimpleDateFormat sdf1 = new SimpleDateFormat(Utils.myFormat, Locale.getDefault());
                    sdf1.setTimeZone(TimeZone.getDefault());
                    Calendar c = Calendar.getInstance();
                    Date startTime = sdf1.parse(myTaskModel.getOrderDate());
                    long startEpoch = startTime.getTime();
                    c.setTime(startTime);
                    c.add(Calendar.HOUR, 48);
                    Date date = c.getTime();
                    long endEpoch = date.getTime();
                    System.out.println("endEpoch" + endEpoch);
                    System.out.println("startEpoch" + startEpoch);
                    System.out.println("startTime" + startTime);
                    System.out.println("time" + startTime);
                    long millse = endEpoch - currMillis;
                    System.out.println("millse" + millse);
                    viewHolder.timer(millse, viewHolder.timer);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
//            if (!myTaskModel.getStatus().equalsIgnoreCase("Shipped")) {
//
//            }

            if (type!=null&&type.equalsIgnoreCase("rejectAssign")) {
                viewHolder.mBinding.statusLayout.setVisibility(View.GONE);
                viewHolder.mBinding.timerLayout.setVisibility(View.GONE);
                viewHolder.mBinding.orderdetailLayout.setVisibility(View.VISIBLE);
            }
            else{
                if (myTaskModel.getStatus().equalsIgnoreCase("Delivered")) {
                    viewHolder.mBinding.statusLayout.setVisibility(View.VISIBLE);
                    viewHolder.mBinding.status.setText(myTaskModel.getStatus());
                    viewHolder.mBinding.orderdetailLayout.setVisibility(View.GONE);
                    viewHolder.mBinding.status.setTextColor(Color.parseColor("#38a561"));
                } else if (myTaskModel.getStatus().equalsIgnoreCase("Delivery Canceled")) {
                    viewHolder.mBinding.statusLayout.setVisibility(View.VISIBLE);
                    viewHolder.mBinding.status.setText(myTaskModel.getStatus());
                    viewHolder.mBinding.orderdetailLayout.setVisibility(View.GONE);
                    viewHolder.mBinding.status.setTextColor(Color.parseColor("#b5a667"));

                } else if (myTaskModel.getStatus().equalsIgnoreCase("Delivery Redispatch")) {
                    viewHolder.mBinding.statusLayout.setVisibility(View.VISIBLE);
                    viewHolder.mBinding.status.setText(myTaskModel.getStatus());
                    viewHolder.mBinding.orderdetailLayout.setVisibility(View.GONE);
                    viewHolder.mBinding.status.setTextColor(Color.parseColor("#b5a667"));

                } else {
                    viewHolder.mBinding.statusLayout.setVisibility(View.GONE);
                    viewHolder.mBinding.orderdetailLayout.setVisibility(View.VISIBLE);
                }
                viewHolder.mBinding.timerLayout.setVisibility(View.VISIBLE);
            }


            if (myTaskModel.getLat() != 0.0 && myTaskModel.getLg() != 0.0) {
                viewHolder.mBinding.llDirection.setVisibility(View.VISIBLE);

            } else {
                viewHolder.mBinding.llDirection.setVisibility(View.GONE);
            }

            viewHolder.mBinding.setMyTaskListener(new MyTaskListener() {
                @Override
                public void taskViewClicked() {
                    if (!Utils.checkInternetConnection(context)) {
                        Utils.setToast(context, context.getResources().getString(R.string.network_error));
                    } else {
                        orderdetailClick.onlineLayHideUnHideClicked(true);
                        /*Bundle bundle = new Bundle();
                        bundle.putInt("ORDER_ID", myTaskModel.getOrderId());
                        bundle.putInt("assignmentID", myTaskModel.getDeliveryIssuanceId());
                        bundle.putString("SkCode", myTaskModel.getSkcode());
                        bundle.putDouble("lat", myTaskModel.getLat());
                        bundle.putDouble("lg", myTaskModel.getLg());
                        bundle.putInt("position", i);
                        bundle.putString("time", time);
                        bundle.putString("CUSTOMER_NAME", myTaskModel.getCustomerName());
                        bundle.putString("MOBILE_NUMBER", myTaskModel.getCustomerphonenum());
                        bundle.putString("SHIPPING_ADDRESS", myTaskModel.getShippingAddress());
                        bundle.putInt("Customerid", myTaskModel.getCustomerId());
                        Fragment fragment = new CropImageFragment();
                        fragment.setArguments(bundle);
                        ((MainActivity) context).switchContentWithStack(fragment);*/


                       if (type!=null&&type.equalsIgnoreCase("rejectAssign")){
                           Intent intent = new Intent(context, RejectedOrderPlaceActivity.class);
                           Bundle bundle = new Bundle();
                           intent.putExtra("ORDER_ID", myTaskModel.getOrderId());
                           intent.putExtra("assignmentID", myTaskModel.getDeliveryIssuanceId());
                           intent.putExtra("SkCode", myTaskModel.getSkcode());
                           intent.putExtra("lat", myTaskModel.getLat());
                           intent.putExtra("lg", myTaskModel.getLg());
                           intent.putExtra("position", i);
                           intent.putExtra("time", time);
                           intent.putExtra("CUSTOMER_NAME", myTaskModel.getCustomerName());
                           intent.putExtra("MOBILE_NUMBER", myTaskModel.getCustomerphonenum());
                           intent.putExtra("SHIPPING_ADDRESS", myTaskModel.getShippingAddress());
                           intent.putExtra("Customerid", myTaskModel.getCustomerId());
                           intent.putExtra("colorCode", myTaskModel.getColorCode());
                           intent.putExtra("type", type);
                           context.startActivity(intent);
                        }
                        else{
                           Intent intent = new Intent(context, NewOrderPlaceActivity.class);
                           Bundle bundle = new Bundle();
                           intent.putExtra("ORDER_ID", myTaskModel.getOrderId());
                           intent.putExtra("assignmentID", myTaskModel.getDeliveryIssuanceId());
                           intent.putExtra("SkCode", myTaskModel.getSkcode());
                           intent.putExtra("lat", myTaskModel.getLat());
                           intent.putExtra("lg", myTaskModel.getLg());
                           intent.putExtra("position", i);
                           intent.putExtra("time", time);
                           intent.putExtra("CUSTOMER_NAME", myTaskModel.getCustomerName());
                           intent.putExtra("MOBILE_NUMBER", myTaskModel.getCustomerphonenum());
                           intent.putExtra("SHIPPING_ADDRESS", myTaskModel.getShippingAddress());
                           intent.putExtra("Customerid", myTaskModel.getCustomerId());
                           intent.putExtra("colorCode", myTaskModel.getColorCode());
                           intent.putExtra("type", type);
                           context.startActivity(intent);
                       }
                    }
                }

                @Override
                public void callBtnClicked() {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "+91" + myTaskModel.getCustomerphonenum()));
                    context.startActivity(intent);
                }

                @Override
                public void locationBtnClicked() {
                    try {
                        if (!Utils.checkInternetConnection(context)) {
                            Utils.setToast(context, context.getResources().getString(R.string.network_error));
                        } else {
                            LatLng latlong = new LatLng(myTaskModel.getLat(), myTaskModel.getLg());
                            if (myTaskModel.getLat() != 0.0 && myTaskModel.getLg() != 0.0) {
                                Intent navigation = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" + myTaskModel.getLat() + "," + myTaskModel.getLg()));
                                context.startActivity(navigation);
                            } else {
                                Intent navigation = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" + latlong.latitude + "," + latlong.longitude));
                                context.startActivity(navigation);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return myTaskList == null ? 0 : myTaskList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        MyTaskAdapterBinding mBinding;
        private OrderCustomRunnable orderCustomRunnable;
        Handler handler = new Handler();
        TextView timer;

        public ViewHolder(MyTaskAdapterBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
            this.timer = mBinding.tvOrdertimer;
            orderCustomRunnable = new OrderCustomRunnable(handler, timer, 10000);

        }

        public void timer(long millse, TextView tvtimer) {
            try {

                handler.removeCallbacks(orderCustomRunnable);
                orderCustomRunnable.holder = tvtimer;
                orderCustomRunnable.millisUntilFinished = millse; //Current time - received time
                handler.postDelayed(orderCustomRunnable, 1000);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void bind(MyTaskInfo item) {
            mBinding.setMyTaskInfo(item);
            mBinding.executePendingBindings();
        }
    }
}