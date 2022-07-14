package in.moreyeahs.livspace.delivery.views.fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.gson.JsonObject;
import in.moreyeahs.livspace.delivery.R;
import in.moreyeahs.livspace.delivery.api.RestClient;
import in.moreyeahs.livspace.delivery.databinding.AssginmentSettleDetailFragmentBinding;
import in.moreyeahs.livspace.delivery.databinding.SignaturePopupBinding;
import in.moreyeahs.livspace.delivery.listener.AssginmentSettleViewDetailInterface;
import in.moreyeahs.livspace.delivery.model.AssginmentSettleResponseModel;
import in.moreyeahs.livspace.delivery.utilities.SharePrefs;
import in.moreyeahs.livspace.delivery.utilities.Utils;
import in.moreyeahs.livspace.delivery.views.adapter.AssginmentSettleDetailAdapter;
import in.moreyeahs.livspace.delivery.views.main.MainActivity;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import id.zelory.compressor.Compressor;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class AssginmentSettleDetailFragment extends Fragment implements AssginmentSettleViewDetailInterface {

    private MainActivity activity;
    private AssginmentSettleDetailFragmentBinding mBinding;
    private ArrayList<AssginmentSettleResponseModel.DBoyAssignmentDeposits> model;
    private String filename = "";
    private String pdfurl = "";
    private String uploadFilePath = "";
    private int id = 0;
    private File compressedImage;


    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.assginment_settle_detail_fragment, container, false);
        Bundle bundle = this.getArguments();
        model = (ArrayList<AssginmentSettleResponseModel.DBoyAssignmentDeposits>) bundle.getSerializable("list");
        id = bundle.getInt("id");
        pdfurl = bundle.getString("pdfurl");
        initialization();
        return mBinding.getRoot();
    }


    private void initialization() {
        setActionBarConfiguration();
        mBinding.rvMyTask.setLayoutManager(new LinearLayoutManager(activity));
        mBinding.rvMyTask.setHasFixedSize(true);
        AssginmentSettleDetailAdapter adapter = new AssginmentSettleDetailAdapter(activity, model, this);
        mBinding.rvMyTask.setAdapter(adapter);
        mBinding.tvSign.setOnClickListener(v -> requestWritePermission());
    }


    private void SignPaidPopup() {
        SignaturePopupBinding signaturePopupBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.signature_popup, null, false);
        Dialog customDialog = new Dialog(activity, R.style.CustomDialog);
        customDialog.setContentView(signaturePopupBinding.getRoot());
        Button saveSignBtn = signaturePopupBinding.saveButton;
        Button clearSignBtn = signaturePopupBinding.clearButton;
        SignaturePad mSignaturePad = signaturePopupBinding.signaturePad;
        saveSignBtn.setEnabled(false);
        clearSignBtn.setEnabled(false);
        signaturePopupBinding.toolbar.skip.setVisibility(View.GONE);

        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {

            @Override
            public void onStartSigning() {

            }

            @Override
            public void onSigned() {
                saveSignBtn.setBackgroundResource(R.drawable.drawable_corner_white);
                clearSignBtn.setBackgroundResource(R.drawable.orangerectangle);
                saveSignBtn.setEnabled(true);
                clearSignBtn.setEnabled(true);
            }

            @Override
            public void onClear() {
                saveSignBtn.setBackgroundResource(R.drawable.drawable_corner_green);
                clearSignBtn.setBackgroundResource(R.drawable.diable_view);
                saveSignBtn.setEnabled(false);
                clearSignBtn.setEnabled(false);
            }
        });

        saveSignBtn.setOnClickListener(v -> {
            try {
                saveSignBtn.setClickable(false);
                saveSignBtn.setEnabled(false);
                Bitmap signatureBitmap = mSignaturePad.getSignatureBitmap();
                filename = SharePrefs.getInstance(getActivity()).getInt(SharePrefs.PEOPLE_ID) + "_" + id + "Signature.jpg";
                SavedImages(signatureBitmap);
                Toast.makeText(activity, "Signature saved into the Gallery", Toast.LENGTH_SHORT).show();
                customDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        signaturePopupBinding.toolbar.back.setOnClickListener(View -> customDialog.dismiss());

        clearSignBtn.setOnClickListener(View -> {
            mSignaturePad.clear();
        });
        customDialog.show();
    }

    private void requestWritePermission() {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        } else {
            SignPaidPopup();
        }
    }

    private void SavedImages(Bitmap bm) {
        String root = Environment.getExternalStorageDirectory().toString();
        try {
            File myDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/ShopKirana");
            myDir.mkdirs();
            File file = new File(myDir, filename);
            if (file.exists())
                file.delete();
            FileOutputStream out = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            uploadFilePath = root + "/ShopKirana/" + filename;
            uploadMultipart();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void uploadMultipart() {
        try {
            final File fileToUpload = new File(uploadFilePath);
            Disposable subscribe = new Compressor(activity)
                    .compressToFileAsFlowable(fileToUpload)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(file -> {
                        compressedImage = file;
                        RequestBody fileReqBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                        MultipartBody.Part part = MultipartBody.Part.createFormData("file", fileToUpload.getName(), fileReqBody);
                        UploadChequeImage(getimageResponse, part);
                    }, throwable -> {
                        throwable.printStackTrace();
                        showError(throwable.getMessage());
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void UploadChequeImage(DisposableObserver observer, MultipartBody.Part image) {
        RestClient.getInstance().getService().UploadDboySignature(image)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String object) {
                        observer.onNext(object);
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }

    private DisposableObserver<String> getimageResponse = new DisposableObserver<String>() {
        @Override
        public void onNext(String response) {
            try {
                Utils.hideProgressDialog(activity);
                if (response != null) {
                    hitPostAssignmentApi(new AssginmentSettleResponseModel(
                            id, SharePrefs.getInstance(getActivity()).getInt(SharePrefs.PEOPLE_ID), response.replace("\"", ""), pdfurl, model));
                    Toast.makeText(activity, "Image Uploaded", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(activity, "Image Not Uploaded", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            Utils.hideProgressDialog(activity);
        }

        @Override
        public void onComplete() {
            Utils.hideProgressDialog(activity);
        }
    };
   /* private void uploadImagePath() {
        try {
            ProgressDialog progressDialog;
            progressDialog = new ProgressDialog(activity);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Image Uploading.");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
            Ion.with(this)
                    .load("POST", Constant.AssignmentSettleURL)
                    .setMultipartFile("file", "image/jpeg", compressedImage)
                    .asString()
                    .setCallback((e, result) -> {
                        if (result != null) {
                            Log.e("Success++", result.replace("\"", ""));
                            hitPostAssignmentApi(new AssginmentSettleResponseModel(
                                    id, SharePrefs.getInstance(getActivity()).getInt(SharePrefs.PEOPLE_ID),  result.replace("\"", ""), pdfurl, model));
                            Toast.makeText(activity, "Image Uploaded", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        } else {
                            Log.e("Failed++", "Failed ####  " + result);
                            Toast.makeText(activity, "Image Not Uploaded", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    private void hitPostAssignmentApi(AssginmentSettleResponseModel model) {
        RestClient.getInstance().getService().PostAssginmentToSettle(model)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(JsonObject response) {
                        Utils.hideProgressDialog(activity);
                        if (response != null) {
                            activity.onBackPressed();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    private void showError(String errorMessage) {
        Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show();
    }

    private void setActionBarConfiguration() {
        final DrawerLayout drawer = getActivity().findViewById(R.id.container);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        TextView tittleTextView = getActivity().findViewById(R.id.toolbar_title);
        TextView assignmentid = getActivity().findViewById(R.id.assignmentid);
        assignmentid.setVisibility(View.GONE);
        tittleTextView.setVisibility(View.VISIBLE);
        tittleTextView.setText("Assignment Settle");
        TextView starttimer = getActivity().findViewById(R.id.start_timer);
        starttimer.setVisibility(View.GONE);
        TextView timer = getActivity().findViewById(R.id.tv_timer);
        timer.setVisibility(View.GONE);
        toolbar.setNavigationOnClickListener(v -> {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                drawer.openDrawer(GravityCompat.START);
            }
        });
        activity.toggle.syncState();
    }

    @Override
    public void viewDetailClick(int deliveryIssuanceId) {

    }

    @Override
    public void saveCommentClick(ArrayList<AssginmentSettleResponseModel.DBoyAssignmentDeposits> list) {
        this.model = list;
    }
}
