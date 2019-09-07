package com.example.mprojects.classplus;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class classcreated extends AppCompatActivity {
    Toolbar mtool;
    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;
    TabLayout tabLayout;
    FloatingActionButton fab,fb1,fb2,fb3;
    Animation fabOpen,fabClose,rotateForward,rotateBackward;
    Boolean isOpen=false;
    TextView aft,nft,mft;
    @Override

    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.class_created);
        SharedPreferences sp=getSharedPreferences(Config.SPName,MODE_PRIVATE);

        Toolbar toolbar = findViewById(R.id.mtoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(sp.getString(Config.Classname,"Not Found"));

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        aft=findViewById(R.id.aft);
        mft=findViewById(R.id.mft);
        nft=findViewById(R.id.nft);
        fab = (FloatingActionButton) findViewById(R.id.fab1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFab();
            }
        });
        fb1 = (FloatingActionButton) findViewById(R.id.assign);
        fb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFab();
                Intent i=new Intent(classcreated.this,assignment.class);
                startActivity(i);


            }
        });
        fb2 = (FloatingActionButton)findViewById(R.id.notice);
        fb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFab();
                Intent i=new Intent(classcreated.this,announcement.class);
                startActivity(i);
            }
        });
        fb3 = (FloatingActionButton)findViewById(R.id.material);
        fb3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFab();
                Intent i=new Intent(classcreated.this,material.class);
                startActivity(i);
            }
        });

        fabOpen= AnimationUtils.loadAnimation(this,R.anim.fab_open);
        fabClose= AnimationUtils.loadAnimation(this,R.anim.fab_close);
        rotateForward=AnimationUtils.loadAnimation(this,R.anim.rotate_forward);
        rotateBackward=AnimationUtils.loadAnimation(this,R.anim.rotate_backward);

    }
    private void animateFab() {
        if (isOpen) {
            fab.startAnimation(rotateBackward);
            fb1.startAnimation(fabClose);
            fb2.startAnimation(fabClose);
            fb3.startAnimation(fabClose);
            aft.startAnimation(fabClose);
            mft.startAnimation(fabClose);
            nft.startAnimation(fabClose);
            fb1.setClickable(false);
            fb2.setClickable(false);
            fb3.setClickable(false);
            isOpen = false;
        } else {
            fab.startAnimation(rotateForward);
            fb1.startAnimation(fabOpen);
            fb2.startAnimation(fabOpen);
            fb3.startAnimation(fabOpen);
            aft.startAnimation(fabOpen);
            mft.startAnimation(fabOpen);
            nft.startAnimation(fabOpen);
            fb1.setClickable(true);
            fb2.setClickable(true);
            fb3.setClickable(true);
            isOpen = true;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_classcreated, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i=new Intent(this,info.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position){
                case 0:
                    cnotice n=new cnotice();
                    return n;
                case 1:
                    cclasswork c=new cclasswork();
                    return c;
                case 2:
                    cpeople p=new cpeople();
                    return p;
                default:
                    return null;
            }
        }

        @Override

        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
        @Override
        public CharSequence getPageTitle(int position)
        {
            switch (position){
                case 0:
                    return "NOTICE";
                case 1:
                    return "CLASSWORK";
                case 2:
                    return "PEOPLE";
            }

            return null;
        }
    }
}
