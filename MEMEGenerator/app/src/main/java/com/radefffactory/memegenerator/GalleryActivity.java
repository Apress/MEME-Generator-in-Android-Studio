package com.radefffactory.memegenerator;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity {

    private final Integer[] image_ids = {
            R.drawable.meme1,
            R.drawable.meme2,
            R.drawable.meme3,
            R.drawable.meme4,
            R.drawable.meme5,
            R.drawable.meme6
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        RecyclerView recyclerView = findViewById(R.id.gallery);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 3);
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<GalleryCell> cells = prepareData();
        GalleryAdapter adapter = new GalleryAdapter(cells, GalleryActivity.this);
        recyclerView.setAdapter(adapter);
    }

    private ArrayList<GalleryCell> prepareData() {
        ArrayList<GalleryCell> theImage = new ArrayList<>();
        for (int i = 0; i < image_ids.length; i++) {
            GalleryCell cell = new GalleryCell();
            cell.setImage(image_ids[i]);
            theImage.add(cell);
        }
        return theImage;
    }
}
