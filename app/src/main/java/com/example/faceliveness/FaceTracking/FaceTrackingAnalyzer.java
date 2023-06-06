package com.example.faceliveness.FaceTracking;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.util.Base64;
import android.util.Log;
import android.view.TextureView;
import android.widget.ImageView;

import androidx.camera.core.CameraX;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;

import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class FaceTrackingAnalyzer implements ImageAnalysis.Analyzer {
    private static final String TAG = "MLKitFacesAnalyzer";
    private TextureView tv;
    private ImageView iv;
    private Bitmap bitmap;
    private Canvas canvas;
    private Paint linePaint;
    private float widthScaleFactor = 1.0f;
    private float heightScaleFactor = 1.0f;
    private FirebaseVisionImage fbImage;
    private CameraX.LensFacing lens;

    private final List<String> imageList = new ArrayList<String>();

    FaceTrackingAnalyzer(TextureView tv, ImageView iv, CameraX.LensFacing lens) {
        this.tv = tv;
        this.iv = iv;
        this.lens = lens;
    }

    @Override
    public void analyze(ImageProxy image, int rotationDegrees) {
        if (image == null || image.getImage() == null) {
            return;
        }
        int rotation = degreesToFirebaseRotation(rotationDegrees);
        fbImage = FirebaseVisionImage.fromMediaImage(image.getImage(), rotation);
        initDrawingUtils();

        initDetector();
    }

    private void initDetector() {
        FirebaseVisionFaceDetectorOptions detectorOptions = new FirebaseVisionFaceDetectorOptions
                .Builder()
                .enableTracking()
                .build();
        FirebaseVisionFaceDetector faceDetector = FirebaseVision.getInstance().getVisionFaceDetector(detectorOptions);
        faceDetector.detectInImage(fbImage).addOnSuccessListener(firebaseVisionFaces -> {
            if (!firebaseVisionFaces.isEmpty()) {
                processFaces(firebaseVisionFaces);
            } else {
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.MULTIPLY);
            }
        }).addOnFailureListener(e -> Log.i(TAG, e.toString()));
    }

    private void initDrawingUtils() {
        bitmap = Bitmap.createBitmap(tv.getWidth(), tv.getHeight(), Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        linePaint = new Paint();
        linePaint.setColor(Color.GREEN);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(2f);
        linePaint.setTextSize(40);
        widthScaleFactor = canvas.getWidth() / (fbImage.getBitmap().getWidth() * 1.0f);
        heightScaleFactor = canvas.getHeight() / (fbImage.getBitmap().getHeight() * 1.0f);
    }

    private void processFaces(List<FirebaseVisionFace> faces) {
        for (FirebaseVisionFace face : faces) {

            Rect box = new Rect((int) translateX(face.getBoundingBox().left),
                    (int) translateY(face.getBoundingBox().top),
                    (int) translateX(face.getBoundingBox().right),
                    (int) translateY(face.getBoundingBox().bottom));
            canvas.drawText(String.valueOf(face.getTrackingId()),
                    translateX(face.getBoundingBox().centerX()),
                    translateY(face.getBoundingBox().centerY()),
                    linePaint);
            Log.i(TAG, "top: " + (int) translateY(face.getBoundingBox().top)
                    + "left: " + (int) translateX(face.getBoundingBox().left)
                    + "bottom: " + (int) translateY(face.getBoundingBox().bottom)
                    + "right: " + (int) translateX(face.getBoundingBox().right));

            Log.i(TAG, "top: " + face.getBoundingBox().top
                    + " left: " + face.getBoundingBox().left
                    + " bottom: " + face.getBoundingBox().bottom
                    + " right: " + face.getBoundingBox().right);

            canvas.drawRect(box, linePaint);
        }
        iv.setImageBitmap(bitmap);

        if(imageList.size() <= 4){
            String data = encodeImage(bitmap);
            imageList.add(data);
        }
        Log.d(TAG, "imageList: " + imageList.size());
    }

    private String encodeImage(Bitmap bit)
    {
        ByteArrayOutputStream arrayBytes = new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.JPEG,100,arrayBytes);
        byte[] b = arrayBytes.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }
    private float translateY(float y) {
        return y * heightScaleFactor;
    }

    private float translateX(float x) {
        float scaledX = x * widthScaleFactor;
        if (lens == CameraX.LensFacing.FRONT) {
            return canvas.getWidth() - scaledX;
        } else {
            return scaledX;
        }
    }

    private int degreesToFirebaseRotation(int degrees) {
        switch (degrees) {
            case 0:
                return FirebaseVisionImageMetadata.ROTATION_0;
            case 90:
                return FirebaseVisionImageMetadata.ROTATION_90;
            case 180:
                return FirebaseVisionImageMetadata.ROTATION_180;
            case 270:
                return FirebaseVisionImageMetadata.ROTATION_270;
            default:
                throw new IllegalArgumentException("Rotation must be 0, 90, 180, or 270.");
        }
    }
}
