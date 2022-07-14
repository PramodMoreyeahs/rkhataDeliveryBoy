package in.moreyeahs.livspace.delivery.views.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import in.moreyeahs.livspace.delivery.BuildConfig;
import in.moreyeahs.livspace.delivery.R;
import in.moreyeahs.livspace.delivery.databinding.AssginmentSettlehistoryAdapterBinding;
import in.moreyeahs.livspace.delivery.model.AssginmentHistoryResponseModel;
import in.moreyeahs.livspace.delivery.utilities.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AssginmentsettleHistoryAdapter extends RecyclerView.Adapter<AssginmentsettleHistoryAdapter.ViewHolder> {
    Context context;
    ArrayList<AssginmentHistoryResponseModel> list;
    String assginid;
    List<String> id = new ArrayList<String>();

    public AssginmentsettleHistoryAdapter(Context context, ArrayList<AssginmentHistoryResponseModel> model) {
        this.context = context;
        this.list = model;

    }

    @NonNull
    @Override
    public AssginmentsettleHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                R.layout.assginment_settlehistory_adapter, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AssginmentsettleHistoryAdapter.ViewHolder holder, int position) {
        holder.mBinding.assignId.setText(list.get(position).getId() + "");
        holder.mBinding.date.setText(Utils.getDateFormat(list.get(position).getCreatedDate()));
        assginid = list.get(position).getAssignmentId();

        String[] str = assginid.split(",");
        ArrayList<String> assginidList = new ArrayList<String>(Arrays.asList(str));
        assginidList.add(0, "View Assginment");
        ArrayAdapter statusAdapter = new ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, assginidList);
        holder.mBinding.spMrpList.setAdapter(statusAdapter);
        holder.mBinding.btViewPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(BuildConfig.apiEndpoint + list.get(position).getSignOffUrl())));

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        AssginmentSettlehistoryAdapterBinding mBinding;

        public ViewHolder(AssginmentSettlehistoryAdapterBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
        }


    }
}
