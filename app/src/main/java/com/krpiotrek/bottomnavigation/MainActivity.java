package com.krpiotrek.bottomnavigation;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.krpiotrek.library.BottomNavigation;

public class MainActivity extends AppCompatActivity {

    private BottomNavigation mNagivation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNagivation = (BottomNavigation) findViewById(R.id.bottom_navigation);

        mNagivation.setOnItemSelectedListener(new BottomNavigation.OnItemSelectedListener() {
            @Override
            public void itemSelected(int id) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content, new SimpleFragment())
                        .commit();
            }
        });

        mNagivation.builder()
                .items(
                        new BottomNavigation.NavigationItem(0, getResources().getDrawable(R.drawable.ic_face_white_24dp), "People", Color.parseColor("#7C4DFF")),
                        new BottomNavigation.NavigationItem(1, getResources().getDrawable(R.drawable.ic_computer_white_24dp), "Computers", Color.parseColor("#795548")),
                        new BottomNavigation.NavigationItem(2, getResources().getDrawable(R.drawable.ic_build_white_24dp), "Tools", Color.parseColor("#03A9F4")))
                .withColorChanges(true)
                .hideOnScroll(true)
                .startingPosition(1)
                .build();
    }

    public BottomNavigation getNagivation() {
        return mNagivation;
    }
}
