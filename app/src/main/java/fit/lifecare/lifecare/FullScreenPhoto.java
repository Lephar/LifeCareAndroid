package fit.lifecare.lifecare;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;

public class FullScreenPhoto extends AppCompatActivity {
    
    private ImageView imageView;
    private ImageView download_button;
    private ImageView back_button;
    private ImageView transparent_background;
    private Bitmap bmp;
    
    private boolean visibility = true;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_photo);
        
        imageView = findViewById(R.id.full_screen_img);
        download_button = findViewById(R.id.download_icon);
        back_button = findViewById(R.id.back_button);
        transparent_background = findViewById(R.id.transparent_background);
        
        Bundle extras = getIntent().getExtras();
        byte[] b = extras.getByteArray("picture");
        
        bmp = BitmapFactory.decodeByteArray(b, 0, b.length);
        imageView.setImageBitmap(bmp);
        
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                if (visibility == true) {
                    
                    visibility = false;
                    transparent_background.setVisibility(View.GONE);
                    download_button.setVisibility(View.GONE);
                    back_button.setVisibility(View.GONE);
                } else {
                    visibility = true;
                    transparent_background.setVisibility(View.VISIBLE);
                    download_button.setVisibility(View.VISIBLE);
                    back_button.setVisibility(View.VISIBLE);
                }
                
            }
        });
        
        download_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                Log.d("haciiii", "clicked");
                if (Build.VERSION.SDK_INT >= 23) {
                    if (ContextCompat.checkSelfPermission(FullScreenPhoto.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(FullScreenPhoto.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(FullScreenPhoto.this,
                                new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE},
                                1);
                    }
                }
                saveImage(bmp);
            }
        });
        
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                
            }
        });
        
    }
    
    private String saveImage(Bitmap image) {
        String savedImagePath = null;
        
        Log.d("haciiii", "buralarda");
        
        String imageFileName = new Date().getTime() + ".jpg";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                + "/LifeCarePhotos");
        boolean success = true;
        if (!storageDir.exists()) {
            success = storageDir.mkdirs();
        }
        if (success) {
            File imageFile = new File(storageDir, imageFileName);
            savedImagePath = imageFile.getAbsolutePath();
            try {
                OutputStream fOut = new FileOutputStream(imageFile);
                image.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            // Add the image to the system gallery
            galleryAddPic(savedImagePath);
            Toast.makeText(getApplicationContext(), getString(R.string.photo_saved), Toast.LENGTH_SHORT).show();
        }
        return savedImagePath;
    }
    
    private void galleryAddPic(String imagePath) {
        
        Log.d("haciiii", "buralarda4");
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(imagePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
    }
}
