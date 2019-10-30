package com.starter.code.camera;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    // Declaring imageview variable
    private ImageView imageView;

    // Any integer value is valid.
    // Since this value is just used to by this activity to Uniquely identify external app calls
    // Check this for more info: https://stackoverflow.com/questions/38507965/what-does-camera-request-code-mean-in-android
    private static final int IMAGE_CAPTURE_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Identifying the UI element.
        imageView = findViewById(R.id.imageView);
    }

    // Triggered when the capture button is clicked
    public void capture(View view){
        // Initializing a camera intent
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Open the camera app and once the photo is taken it will automatically trigger the
        // onActivityResult() method present below
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE);

    }

    // Triggered once the user comes bask to this activity after performing actions on camera app
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Checking if the requested code is of type camera
        if (requestCode == IMAGE_CAPTURE_CODE) {
            // Checking if there is a success on the camera app to render the image
            if (resultCode == RESULT_OK) {
                // This is more like a positive block
                Bitmap bp = (Bitmap) data.getExtras().get("data");
                imageView.setImageBitmap(bp);
                // You can store it the image
                // Store logic starts here
                ContextWrapper cw = new ContextWrapper(getApplicationContext());
                File directory = cw.getDir("images", Context.MODE_PRIVATE);
                SimpleDateFormat gmtDateFormat = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
                File file = new File(directory, gmtDateFormat.format(new Date()) + ".jpg");
                if (!file.exists()) {
                    Log.d("path", file.toString());
                    FileOutputStream fos = null;
                    try {
                        fos = new FileOutputStream(file);
                        bp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                        fos.flush();
                        fos.close();
                    } catch (java.io.IOException e) {
                        e.printStackTrace();
                    }
                }
                // Store logic ends here

            } else if (resultCode == RESULT_CANCELED) {
                // If you have cancelled the image capture actions it will reach here
                // This is more like a negative block
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            }
        }
    }
}
