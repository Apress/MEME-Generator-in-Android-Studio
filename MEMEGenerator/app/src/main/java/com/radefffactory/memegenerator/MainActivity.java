package com.radefffactory.memegenerator;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    private static final int LOAD_IMAGE = 1000;

    Button load, share;

    TextView text1, text2;

    EditText line1, line2;

    SquareImageView imageView;

    String currentImage = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        load = findViewById(R.id.b_load);
        share = findViewById(R.id.b_share);

        text1 = findViewById(R.id.text1);
        text2 = findViewById(R.id.text2);

        line1 = findViewById(R.id.et_line1);
        line2 = findViewById(R.id.et_line2);

        imageView = findViewById(R.id.imageView);

        share.setVisibility(View.INVISIBLE);

        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GalleryActivity.class);
                startActivityForResult(intent, LOAD_IMAGE);
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View content = findViewById(R.id.lay);
                Bitmap bitmap = getScreenshot(content);
                long timeStamp = System.currentTimeMillis();
                storeImage(bitmap, "meme" + timeStamp + ".png"); //you must save before share
                shareImage("meme" + timeStamp + ".png");

                hideKeyboard();
            }
        });

        line1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                text1.setText(line1.getText().toString().toUpperCase());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        line2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                text2.setText(line2.getText().toString().toUpperCase());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            int image = Integer.parseInt(data.getStringExtra("image"));
            imageView.setImageResource(image);
            share.setVisibility(View.VISIBLE);
        }
    }

    private void shareImage(String fileName) {
        File file = new File(getExternalFilesDir(null), fileName);
        if (file.exists()) {
            Uri uri = FileProvider.getUriForFile(getApplicationContext(), "com.radefffactory.memegenerator", file);

            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("image/*");

            intent.putExtra(Intent.EXTRA_SUBJECT, "");
            intent.putExtra(Intent.EXTRA_TEXT, "");
            intent.putExtra(Intent.EXTRA_STREAM, uri);

            try {
                startActivity(Intent.createChooser(intent, "Share via"));
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "No sharing app available!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Error sharing!", Toast.LENGTH_SHORT).show();
        }
    }

    private void storeImage(Bitmap bm, String fileName) {
        File file = new File(getExternalFilesDir(null), fileName);
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 0, bos);
            byte[] bitmapData = bos.toByteArray();
            ByteArrayInputStream bs = new ByteArrayInputStream(bitmapData);

            OutputStream os = new FileOutputStream(file);
            byte[] data = new byte[bs.available()];
            bs.read(data);
            os.write(data);
            bs.close();
            os.close();
        } catch (IOException e) {
            Toast.makeText(this, "Writing error!", Toast.LENGTH_SHORT).show();
        }
    }

    private static Bitmap getScreenshot(View view) {
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        return bitmap;
    }

    private void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(line1.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}