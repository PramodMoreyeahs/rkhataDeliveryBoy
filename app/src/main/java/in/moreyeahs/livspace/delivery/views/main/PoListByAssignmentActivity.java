package in.moreyeahs.livspace.delivery.views.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.List;

import in.moreyeahs.livspace.delivery.R;
import in.moreyeahs.livspace.delivery.databinding.ActivityAssignmentPoBinding;
import in.moreyeahs.livspace.delivery.model.PoListResponse;
import in.moreyeahs.livspace.delivery.utilities.CommonClassForAPI;
import in.moreyeahs.livspace.delivery.utilities.Utils;
import in.moreyeahs.livspace.delivery.views.adapter.AssignmentPoListAdapter;
import io.reactivex.observers.DisposableObserver;

public class PoListByAssignmentActivity extends AppCompatActivity implements AssignmentPoListAdapter.PoListInterface {
    ActivityAssignmentPoBinding mbinding;
    CommonClassForAPI commonClassForAPI;
    Utils utils;
    int assignmentId;
    AssignmentPoListAdapter adapter;

    DisposableObserver<List<PoListResponse>> poListDis = new DisposableObserver<List<PoListResponse>>() {
        @Override
        public void onNext(List<PoListResponse> response) {
            Utils.hideProgressDialog();
            try {
                adapter.setResponseList(response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onComplete() {
        }
    };

    DisposableObserver<String> ewayBill = new DisposableObserver<String>() {
        @Override
        public void onNext(String response) {
            Utils.hideProgressDialog();
            try {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://"+response));
                startActivity(browserIntent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onComplete() {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mbinding = DataBindingUtil.setContentView(this, R.layout.activity_assignment_po);

        if (getIntent().getExtras() != null) {
            assignmentId = getIntent().getIntExtra("ASSIGNMENT_ID", 0);
        }

        mbinding.tvAssignmentId.setText("Assignment Id: " + assignmentId);
        mbinding.ivBack.setOnClickListener(V -> onBackPressed());

        initialization();
    }

    public void initialization() {
        commonClassForAPI = CommonClassForAPI.getInstance(this);
        utils = new Utils(this);

        LinearLayoutManager lm = new LinearLayoutManager(PoListByAssignmentActivity.this, LinearLayoutManager.VERTICAL, false);
        adapter = new AssignmentPoListAdapter(PoListByAssignmentActivity.this, this);
        mbinding.rvPoList.setLayoutManager(lm);
        mbinding.rvPoList.setAdapter(adapter);
    }

    public void onResume() {
        super.onResume();
        fetchPoDetails();
    }

    public void fetchPoDetails() {
        if (utils.isNetworkAvailable()) {
            if (commonClassForAPI != null) {
                commonClassForAPI.fetchPoListByAssignmentId(poListDis, assignmentId);
            }
        } else {
            utils.setToast(PoListByAssignmentActivity.this, getString(R.string.internet_connection));
        }
    }

    @Override
    public void getEWayBill(int poId) {
        if (utils.isNetworkAvailable()) {
            if (commonClassForAPI != null) {
                commonClassForAPI.fetchEwayBill(ewayBill, poId);
            }
        } else {
            utils.setToast(PoListByAssignmentActivity.this, getString(R.string.internet_connection));
        }
    }
}