package com.example.meetishah.carmina;

import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class TopHindi extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AlbumsAdapter adapter;
    private List<Album> albumList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_english);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);

        initCollapsingToolbar();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        albumList = new ArrayList<>();
        adapter = new AlbumsAdapter(this, albumList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        // recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        //recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        prepareAlbums();

        try {
            Glide.with(this).load(R.drawable.hindi).into((ImageView) findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    /**
     * Adding few albums for testing
     */
    private void prepareAlbums() {
        int[] covers = new int[]{
                R.drawable.dil,
                R.drawable.swag,
                R.drawable.naah,
                R.drawable.ban,
                R.drawable.mere,
                R.drawable.tera,
                R.drawable.aaj,
                R.drawable.laedooba,
                R.drawable.hawa,
                R.drawable.mainphir};

        Album a = new Album("Dil Diyan Gallan", covers[0]);
        a.setName("Dil Diyan Gallan");
        a.setFileName("test/songs/dil.mp3");
        a.setImagePath("test/images/dil.jpg");
        albumList.add(a);

        a = new Album("Swag Se Swagat", covers[1]);
        a.setName("Swag Se Swagat");
        a.setFileName("test/songs/swag.mp3");
        a.setImagePath("test/images/swag.jpg");
        albumList.add(a);

        a = new Album("Naah", covers[2]);
        a.setName("Naah");
        a.setFileName("test/songs/naah.mp3");
        a.setImagePath("test/images/naah.jpg");
        albumList.add(a);

        a = new Album("Banja Meri Rani", covers[3]);
        a.setName("Banja Tu Meri Rani");
        a.setFileName("test/songs/banja.mp3");
        a.setImagePath("test/images/ban.jpg");
        albumList.add(a);

        a = new Album("Mere Rash Ke Qamar",  covers[4]);
        a.setName("Mere Rash Ke Qamar");
        a.setFileName("test/songs/mererashke.mp3");
        a.setImagePath("test/images/mere.jpg");
        albumList.add(a);

        a = new Album("Tera Zikr",covers[5]);
        a.setName("Tera Zikr");
        a.setFileName("test/songs/terazikr.mp3");
        a.setImagePath("test/images/tera.jpg");
        albumList.add(a);

        a = new Album("Aaj Se Teri", covers[6]);
        a.setName("Aaj Se Teri");
        a.setFileName("test/songs/aajse.mp3");
        a.setImagePath("test/images/aaj.jpg");
        albumList.add(a);

        a = new Album("Lae Dooba", covers[7]);
        a.setName("Lae Dooba");
        a.setFileName("test/songs/lae.mp3");
        a.setImagePath("test/images/lae.jpg");
        albumList.add(a);

        a = new Album("Hawaayein",  covers[8]);
        a.setName("Hawaayein");
        a.setFileName("test/songs/hawayein.mp3");
        a.setImagePath("test/images/hawa.jpg");
        albumList.add(a);

        a = new Album("Main Phir Bhi Tumko Chahunga",  covers[9]);
        a.setName("Main Phir Bhi Tumko Chahunga");
        a.setFileName("test/songs/phir.mp3");
        a.setImagePath("test/images/mainphir.jpg");
        albumList.add(a);

        adapter.notifyDataSetChanged();
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}

