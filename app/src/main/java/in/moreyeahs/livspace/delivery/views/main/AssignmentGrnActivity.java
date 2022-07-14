package in.moreyeahs.livspace.delivery.views.main;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.List;

import in.moreyeahs.livspace.delivery.R;
import in.moreyeahs.livspace.delivery.databinding.ActivityAssignmentGrnBinding;
import in.moreyeahs.livspace.delivery.model.AssignmentDetailsModel;
import in.moreyeahs.livspace.delivery.utilities.CommonClassForAPI;
import in.moreyeahs.livspace.delivery.utilities.Utils;
import in.moreyeahs.livspace.delivery.views.adapter.AssignmentGrnAdapter;
import io.reactivex.observers.DisposableObserver;

public class AssignmentGrnActivity extends AppCompatActivity implements AssignmentGrnAdapter.AssignmentListInterface {
    ActivityAssignmentGrnBinding mbinding;
    CommonClassForAPI commonClassForAPI;
    Utils utils;
    int poId;
    AssignmentDetailsModel modifiedList;

    DisposableObserver<AssignmentDetailsModel> assignmentDetailsDis = new DisposableObserver<AssignmentDetailsModel>() {
        @Override
        public void onNext(AssignmentDetailsModel response) {
            Utils.hideProgressDialog();
            try {
                modifiedList = response;
                LinearLayoutManager lm = new LinearLayoutManager(AssignmentGrnActivity.this, LinearLayoutManager.VERTICAL, false);
                AssignmentGrnAdapter adapter = new AssignmentGrnAdapter(AssignmentGrnActivity.this, response.getPurdetails(), AssignmentGrnActivity.this);
                mbinding.rvGrn.setLayoutManager(lm);
                mbinding.rvGrn.setAdapter(adapter);

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

    DisposableObserver<Boolean> postGrnDis = new DisposableObserver<Boolean>() {
        @Override
        public void onNext(Boolean response) {
            Utils.hideProgressDialog(AssignmentGrnActivity.this);
            if (response){
                Utils.setToast(AssignmentGrnActivity.this, "GRN done");
                finish();
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
        mbinding = DataBindingUtil.setContentView(this, R.layout.activity_assignment_grn);

        if (getIntent().getExtras() != null) {
            poId = getIntent().getIntExtra("PO_ID", 0);
        }

        initialization();
        fetchAssignmentDetails();
    }

    public void initialization() {
        commonClassForAPI = CommonClassForAPI.getInstance(this);
        utils = new Utils(this);

        mbinding.tvAssignmentId.setText("PoId: " + poId);
        mbinding.ivBack.setOnClickListener(V -> onBackPressed());
        mbinding.btnPickAssignment.setOnClickListener(V -> sendGrnToServer());
    }

    public void fetchAssignmentDetails() {
        if (utils.isNetworkAvailable()) {
            if (commonClassForAPI != null) {
                commonClassForAPI.fetchAssignmentDetails(assignmentDetailsDis, poId);
            }
        } else {
            utils.setToast(AssignmentGrnActivity.this, getString(R.string.internet_connection));
        }
    }

    @Override
    public void syncValues(List<AssignmentDetailsModel.PurdetailsEntity> list) {
        modifiedList.setPurdetails(list);
    }

    public void sendGrnToServer() {
        if (utils.isNetworkAvailable()) {
            if (commonClassForAPI != null) {
                commonClassForAPI.postGrnToServer(postGrnDis, modifiedList);
            }
        } else {
            utils.setToast(AssignmentGrnActivity.this, getString(R.string.internet_connection));
        }
    }
}