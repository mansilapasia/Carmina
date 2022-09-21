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

public class TopEnglish extends AppCompatActivity {

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
            Glide.with(this).load(R.drawable.english).into((ImageView) findViewById(R.id.backdrop));
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
                R.drawable.godsplan,
                R.drawable.barking,
                R.drawable.river,
                R.drawable.toogood,
                R.drawable.tiptoe,
                R.drawable.finesse,
                R.drawable.perfect,
                R.drawable.thisis,
                R.drawable.breathe,
                R.drawable.iknow};

        Album a = new Album("God's Plan", covers[0]);
        a.setName("God's Plan");
        a.setFileName("test/songs/god.mp3");
        a.setImagePath("test/images/god.jpg");

        albumList.add(a);

        a = new Album("Barking", covers[1]);
        a.setName("Barking");
        a.setFileName("test/songs/bark.mp3");
        a.setImagePath("test/images/bark.jpg");
        albumList.add(a);

        a = new Album("River", covers[2]);
        a.setName("River");
        a.setFileName("test/songs/river.mp3");
        a.setImagePath("test/images/river.jpg");

        albumList.add(a);

        a = new Album("Too Good At Goodbyes", covers[3]);
        a.setName("Too Good At Goodbyes");
        a.setFileName("test/songs/too.mp3");
        a.setImagePath("test/images/too.jpg");
        albumList.add(a);

        a = new Album("Tip Toe",  covers[4]);
        a.setName("Tip Toe");
        a.setFileName("test/songs/tiptoe.mp3");
        a.setImagePath("test/images/tip.jpg");
        albumList.add(a);

        a = new Album("Finesse",covers[5]);
        a.setName("Finesse");
        a.setFileName("test/songs/finesse.mp3");
        a.setImagePath("test/images/finesse.jpg");
        albumList.add(a);

        a = new Album("Perfect", covers[6]);
        a.setName("Perfect");
        a.setFileName("test/songs/perfect.mp3");
        a.setImagePath("test/images/perfect.jpg");
        albumList.add(a);

        a = new Album("This Is Me", covers[7]);
        a.setName("This Is Me");
        a.setFileName("test/songs/thisis.mp3");
        a.setImagePath("test/images/this.jpg");
        albumList.add(a);

        a = new Album("Breathe",  covers[8]);
        a.setName("Breathe");
        a.setFileName("test/songs/breathe.mp3");
        a.setImagePath("test/images/breathe.jpg");
        albumList.add(a);

        a = new Album("I know",  covers[9]);
        a.setName("I Know");
        a.setFileName("test/songs/iknow.mp3");
        a.setImagePath("test/images/iknow.jpg");
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

