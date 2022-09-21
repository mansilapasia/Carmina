package com.example.meetishah.carmina;


import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

import static android.app.Activity.RESULT_OK;

public class HomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    android.support.v7.widget.SearchView searchBar;
    TextView top_en,top_hi,old,artists;

    private final int REQ_CODE_SPEECH_INPUT = 100;
    private BottomNavigationView bottomNavigation;
    private Fragment fragment;
    private FragmentManager fragmentManager;
    Intent i;
    String q;
    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    DrawerLayout drawer;
    AppBarLayout appBarLayout;
    String s,username,file="",c="";
    SharedPreferences sharedPreferences;
    TextView navtext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
         drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        top_en=(TextView) findViewById(R.id.TopEnglish);
        top_hi= (TextView) findViewById(R.id.TopHindi);
        old= (TextView) findViewById(R.id.OldSongs);
        artists= (TextView) findViewById(R.id.Artists);
        top_en.setOnClickListener(this);
        top_hi.setOnClickListener(this);
        old.setOnClickListener(this);
        artists.setOnClickListener(this);

        bottomNavigation = (BottomNavigationView)findViewById(R.id.bottom_navigation);

        bottomNavigation.inflateMenu(R.menu.bottom_menu);
        fragmentManager = getSupportFragmentManager();
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id){
                    case R.id.action_send:
                        Intent i=new Intent(getApplicationContext(),Lyrics.class);
                        startActivity(i);
                        toolbar.setVisibility(View.VISIBLE);

                        searchBar.setVisibility(View.GONE);
                        break;
                    case R.id.action_search:
                        toolbar.setVisibility(View.GONE);

                        searchBar.setVisibility(View.VISIBLE);
                        searchBar.setIconified(false);



                        break;
                    case R.id.action_speech:
                        promptSpeechInput();
                        toolbar.setVisibility(View.VISIBLE);
                        searchBar.setVisibility(View.GONE);

                        break;
                }
                final FragmentTransaction transaction = fragmentManager.beginTransaction();
                //transaction.replace(R.id.main_container, fragment).commit();
                return true;
            }
        });


        viewPager = (ViewPager) findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);


        // layouts of all welcome sliders
        // add few more layouts if you want
        layouts = new int[]{
                R.layout.welcome_side4,
                R.layout.welcome_side3,
                R.layout.welcome_side2,
                R.layout.welcome_side1};

        // adding bottom dots
        addBottomDots(0);

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        //SEARCH BAR

        searchBar = (android.support.v7.widget.SearchView) findViewById(R.id.searchBar);
       searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
           @Override
           public boolean onQueryTextSubmit(String query) {
               if (query.length() > 0) {
                 //  new GetSearchByNameResultTask(searchView).execute(query);

                   q=query;
                   new SendPostRequest().execute();
                   Log.e("nhn",query.toString());

               } else {
                   return false;
               }

               return true;
           }

           @Override
           public boolean onQueryTextChange(String newText) {
               return false;
           }
       });



        sharedPreferences=getSharedPreferences("login",0);
        if(sharedPreferences.contains("userid"))
        {
            username=sharedPreferences.getString("userid",null);
            Log.e("shred","string"+username);
        }


    }

    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.length - 1) {
                // last page. make button text to GOT IT

            } else {
                // still pages are left

            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };


            /**
     * View pager adapter
     */
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }



    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }



    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    q=result.get(0).toString();
                    Log.e("mansi",q);
                    Toast.makeText(getApplicationContext(),result.get(0).toString(),Toast.LENGTH_LONG);
                    new SendPostRequest().execute();
                }
                break;
            }

        }
    }


    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        navtext=(TextView)findViewById(R.id.navtext1);
        navtext.setText(username);

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {

            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            i=new Intent(this,Feedback.class);
            startActivity(i);

        } else if (id == R.id.nav_slideshow) {
            i=new Intent(this,AboutUs.class);
            startActivity(i);

        } else if (id == R.id.nav_manage) {
            sharedPreferences=getSharedPreferences("login",0);
            sharedPreferences.edit().remove("userid").commit();
            Intent in=new Intent(this,Login.class);
            startActivity(in);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
       if(v==top_en){
           i=new Intent(this,TopEnglish.class);
           startActivity(i);
       } else if(v==top_hi){

           i=new Intent(this,TopHindi.class);
           startActivity(i);
       } else if(v==old){

           i=new Intent(this,Old.class);
           startActivity(i);

       }else{

           i=new Intent(this,Artist.class);
           startActivity(i);
       }

    }
    public class SendPostRequest extends AsyncTask<String, Void, String> {



        protected void onPreExecute(){

        }

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("http://192.168.0.101/test/search.php");

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("query", q);
               // postDataParams.put("password", s2);
                Log.e("params",postDataParams.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode=conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in=new BufferedReader(new
                            InputStreamReader(
                            conn.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line="";

                    while((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                }
                else {
                    return new String("false : "+responseCode);
                }
            }
            catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(String result) {

Log.e("result",result.toString());
            if(result.contains("[]<br />")){
                Toast.makeText(getApplicationContext(),"Song does not exist",Toast.LENGTH_LONG).show();
            }else {


                i = new Intent(getApplicationContext(), SearchActivity.class);
                i.putExtra("Name", result);
                startActivity(i);
            }
        }
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }

}
