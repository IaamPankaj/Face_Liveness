package com.example.faceliveness;

import androidx.appcompat.app.AppCompatActivity;
//import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.Preview;
//import androidx.camera.lifecycle.ProcessCameraProvider;
//import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.faceliveness.Models.Request.IdentifyPersonRequest;
import com.example.faceliveness.Services.RetrofitInterceptor;
import com.example.faceliveness.core.Api.RootApi;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Headers;

public class MainActivity extends AppCompatActivity {

    private static final int MY_CAMERA_REQUEST_CODE = 100;

    private static final int pic_id = 123;


//    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
//
//    PreviewView previewView;

    Button btnCapture;

    private ImageCapture imageCapture;

    Retrofit retrofit;

    RootApi rootApi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        retrofit = RetrofitInterceptor.getService();

        rootApi = retrofit.create(RootApi.class);

//        previewView = findViewById(R.id.previewView);
//
//        btnCapture = findViewById(R.id.btnCapture);

        identifyPerson();

        String[] permissions = {android.Manifest.permission.CAMERA, android.Manifest.permission.ACCESS_FINE_LOCATION};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(permissions, MY_CAMERA_REQUEST_CODE);
            }
        }

//        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
//        cameraProviderFuture.addListener(() -> {
//
//            try {
//                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
//                startCameraX(cameraProvider);
//            }catch (ExecutionException e){
//                e.printStackTrace();
//            }catch (InterruptedException e){
//                e.printStackTrace();
//            }
//        },getExecuter());
//
//        btnCapture.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(camera_intent, pic_id);
//            }
//        });
    }

//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == pic_id) {
//            Bitmap photo = (Bitmap) data.getExtras().get("data");
//            iv_cameraFrameLayout.setImageBitmap(photo);
//        }
//    }

//    private Executor getExecuter() {
//        return ContextCompat.getMainExecutor(this);
//    }

//    private void startCameraX(ProcessCameraProvider cameraProvider) {
//        cameraProvider.unbindAll();
//
//        CameraSelector cameraSelector = new CameraSelector.Builder()
//                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
//                .build();
//
//        Preview preview = new Preview.Builder().build();
//
//        preview.setSurfaceProvider(previewView.getSurfaceProvider());
//
//        imageCapture = new ImageCapture.Builder()
//                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
//                .build();
//
//        cameraProvider.bindToLifecycle((LifecycleOwner) this,cameraSelector,preview,imageCapture);
//
//    }

    private void identifyPerson() {
        IdentifyPersonRequest body = new IdentifyPersonRequest();
        body.setImage("nsnsnsnsnsnsnsn");


        Log.d("API_LOG", body.getImage());

        Call<ResponseBody> call = rootApi.identifyPerson(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("API_LOG", response.toString());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }



}