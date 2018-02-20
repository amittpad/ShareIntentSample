package com.example.admin.shareintentsample;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;

public class MainActivity extends AppCompatActivity {


    private EditText editString;
    private Button shareImage;
    private Button shareText;

    // Uri for image path
    private static Uri imageUri = null;
    private final int select_photo = 1; // request code fot gallery intent
    private Button selectImage;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editString = (EditText) findViewById(R.id.edit_text);
        imageView = (ImageView) findViewById(R.id.shared_imageview);
        shareText = (Button) findViewById(R.id.share_text);
        shareImage = (Button) findViewById(R.id.share_image);
        selectImage = (Button) findViewById(R.id.select_image);

        shareText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Share text
                String getText = editString.getText().toString();
                if (!getText.equals("") && getText.length() != 0)
                    shareText(getText);
                else
                    Toast.makeText(MainActivity.this, "Please enter something to share.", Toast.LENGTH_SHORT).show();
            }
        });

        shareImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // share image
                shareImage(imageUri);
            }
        });

        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Intent to gallery
                Intent in = new Intent(Intent.ACTION_PICK);
                in.setType("image/*");
                startActivityForResult(in, select_photo);// start
                // activity
                // for
                // result
            }
        });


    }

    // Share text
    private void shareText(String text) {

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");// Plain format text

        // You can add subject also
		/*
		 * sharingIntent.putExtra( android.content.Intent.EXTRA_SUBJECT,
		 * "Subject Here");
		 */
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);
        startActivity(Intent.createChooser(sharingIntent, "Share on"));
    }

    // Share image
    private void shareImage(Uri imagePath) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        sharingIntent.setType("image/*");
        sharingIntent.putExtra(Intent.EXTRA_STREAM, imagePath);
        startActivity(Intent.createChooser(sharingIntent, "Share Image Using"));
    }

    protected void onActivityResult(int requestcode, int resultcode, Intent imagereturnintent) {
        super.onActivityResult(requestcode, resultcode, imagereturnintent);
        switch (requestcode) {
            case select_photo:
                if (resultcode == RESULT_OK) {
                    try {

                        imageUri = imagereturnintent.getData();// Get intent
                        // data

                        Bitmap bitmap = Utils.decodeUri(MainActivity.this, imageUri, 200);// call
                        // deocde
                        // uri
                        // method
                        // Check if bitmap is not null then set image else show
                        // toast
                        if (bitmap != null) {
                            imageView.setImageBitmap(bitmap);// Set image over
                            // bitmap
                            shareImage.setVisibility(View.VISIBLE);// Visible button
                            // if bitmap is
                            // not null
                        } else {
                            shareImage.setVisibility(View.GONE);
                            Toast.makeText(MainActivity.this, "Error while decoding image.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "File not found.", Toast.LENGTH_SHORT).show();
                    }
                }
        }
    }
}
