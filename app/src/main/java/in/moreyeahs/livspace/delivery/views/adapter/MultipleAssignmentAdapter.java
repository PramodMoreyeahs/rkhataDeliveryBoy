package in.moreyeahs.livspace.delivery.views.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import in.moreyeahs.livspace.delivery.R;
import in.moreyeahs.livspace.delivery.databinding.MultipleassginmentAdapterBinding;
import in.moreyeahs.livspace.delivery.listener.MultipleAssignmentInterface;
import in.moreyeahs.livspace.delivery.model.AcceptedAssginmentListModel;
import in.moreyeahs.livspace.delivery.model.SortOrderModel;
import in.moreyeahs.livspace.delivery.utilities.CustomRunnable;
import in.moreyeahs.livspace.delivery.utilities.GPSTracker;
import in.moreyeahs.livspace.delivery.utilities.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class MultipleAssignmentAdapter extends RecyclerView.Adapter<MultipleAssignmentAdapter.ViewHolder> {
    private LayoutInflater layoutInflater;
    Context context;
    private ArrayList<AcceptedAssginmentListModel> assignmentAcceptPending;
    private MultipleAssignmentInterface multipleAssignmentInterface;
    private boolean isTimerstart = false;


    public MultipleAssignmentAdapter(Context context, ArrayList<AcceptedAssginmentListModel> assignmentAcceptPending, MultipleAssignmentInterface multipleAssignmentInterface) {
        this.assignmentAcceptPending = assignmentAcceptPending;
        this.context = context;
        this.multipleAssignmentInterface = multipleAssignmentInterface;
    }

    public void setItemListCategory(ArrayList<AcceptedAssginmentListModel> assignmentAcceptPending, MultipleAssignmentInterface multipleAssignmentInterface) {
        this.assignmentAcceptPending = assignmentAcceptPending;
        this.multipleAssignmentInterface = multipleAssignmentInterface;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MultipleAssignmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }
        MultipleassginmentAdapterBinding mBinding = DataBindingUtil.inflate(layoutInflater, R.layout.multipleassginment_adapter, viewGroup, false);
        return new ViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MultipleAssignmentAdapter.ViewHolder viewHolder, int i) {
        try {
            AcceptedAssginmentListModel assginmentListModel = assignmentAcceptPending.get(i);
            viewHolder.assignId.setText(context.getString(R.string.assignment_id) + assginmentListModel.getDeliveryIssuanceId());
            viewHolder.date.setText(context.getString(R.string.assignment_date) + Utils.getDateFormat(assginmentListModel.getAssignmentDate()));
            long currMillis = System.currentTimeMillis();
            SimpleDateFormat sdf1 = new SimpleDateFormat(Utils.myFormat, Locale.getDefault());
            sdf1.setTimeZone(TimeZone.getDefault());
            if (assginmentListModel.getStartDateTime() != null) {
                viewHolder.tvTimerLayout.setVisibility(View.VISIBLE);
                viewHolder.llStart.setVisibility(View.GONE);
                viewHolder.mBinding.llDirection.setVisibility(View.VISIBLE);
                Date startTime = sdf1.parse(assginmentListModel.getStartDateTime());
                long startEpoch = startTime.getTime();
                System.out.println("startEpoch" + startEpoch);
                long millse = currMillis - startEpoch;
                System.out.println("millse" + millse);
                viewHolder.timer(millse, viewHolder.tvtimer);

            } else {
                viewHolder.tvTimerLayout.setVisibility(View.GONE);
                viewHolder.llStart.setVisibility(View.VISIBLE);
                viewHolder.mBinding.llDirection.setVisibility(View.GONE);
//                viewHolder.timer("2019-07-09T15:17:15.8404991+05:30", viewHolder.tvTtimer);
            }

            viewHolder.llStart.setOnClickListener(v -> {
                viewHolder.mBinding.llDirection.setVisibility(View.VISIBLE);
                multipleAssignmentInterface.StartTimer(assginmentListModel.getDeliveryIssuanceId());

            });
            viewHolder.llOrderList.setOnClickListener(v -> {
                isTimerstart = assginmentListModel.getStartDateTime() != null;
                multipleAssignmentInterface.Details(assginmentListModel.getDeliveryIssuanceId(), isTimerstart, i);
            });

            viewHolder.mBinding.llDirection.setOnClickListener(view -> {
                GPSTracker gpsTracker = new GPSTracker(context);
                double latitude = 0, longitutde = 0;
                if (gpsTracker != null) {
                    latitude = gpsTracker.getLatitude();
                    longitutde = gpsTracker.getLongitude();
                }

                SortOrderModel sortOrderModel = new SortOrderModel(assginmentListModel.getDeliveryIssuanceId(), latitude, longitutde);
                multipleAssignmentInterface.shortPathData(sortOrderModel, i);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return assignmentAcceptPending.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        MultipleassginmentAdapterBinding mBinding;
        private TextView tvtimer, assignId, date;
        LinearLayout llStart, tvTimerLayout, llOrderList;
        private CustomRunnable customRunnable;
        Handler handler = new Handler();

        public ViewHolder(MultipleassginmentAdapterBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
            assignId = mBinding.assignId;
            date = mBinding.date;
            tvtimer = mBinding.tvTimer;
            llStart = mBinding.llStart;
            tvTimerLayout = mBinding.tvTimerLayout;
            llOrderList = mBinding.llOrderList;
            customRunnable = new CustomRunnable(handler, tvtimer, 10000);
        }

        public void timer(long millse, TextView tvtimer) {
            try {
                handler.removeCallbacks(customRunnable);
                customRunnable.holder = tvtimer;
                customRunnable.millisUntilFinished = millse;
                handler.postDelayed(customRunnable, 1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}