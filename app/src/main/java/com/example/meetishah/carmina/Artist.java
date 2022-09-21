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

public class Artist extends AppCompatActivity {

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
            Glide.with(this).load(R.drawable.youmayalsolike).into((ImageView) findViewById(R.id.backdrop));
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
                R.drawable.gonna,
                R.drawable.best,
                R.drawable.love,
                R.drawable.myheart,
                R.drawable.muskurane,
                R.drawable.dua,
                R.drawable.tujhe,
                R.drawable.happier,
                R.drawable.sorry,
                R.drawable.closer};

        Album a = new Album("Gonna Fly Now", covers[0]);
        a.setName("Gonna Fly Now");
        a.setFileName("test/songs/gonna.mp3");
        a.setImagePath("test/images/gonna.jpg");
        albumList.add(a);

        a = new Album("Story Of My Life", covers[1]);
        a.setName("Story Of My Life");
        a.setFileName("test/songs/best.mp3");
        a.setImagePath("test/images/best.jpg");
        albumList.add(a);

        a = new Album("Love Dose", covers[2]);
        a.setName("Love Dose");
        a.setFileName("test/songs/love.mp3");
        a.setImagePath("test/images/love.jpg");
        albumList.add(a);

        a = new Album("My Heart Will Go On", covers[3]);
        a.setName("My Heart Will Go On");
        a.setFileName("test/songs/myheart.mp3");
        a.setImagePath("test/images/myheart.jpg");
        albumList.add(a);

        a = new Album("Muskurane",  covers[4]);
        a.setName("Muskurane");
        a.setFileName("test/songs/muskurane.mp3");
        a.setImagePath("test/images/muskurane.jpg");
        albumList.add(a);

        a = new Album("Dua",covers[5]);
        a.setName("Dua");
        a.setFileName("test/songs/dua.mp3");
        a.setImagePath("test/images/dua.jpg");
        albumList.add(a);

        a = new Album("Tujhe Bhula Diya", covers[6]);
        a.setFileName("test/songs/tujhe.mp3");
        a.setImagePath("test/images/tujhe.jpg");
        albumList.add(a);

        a = new Album("Happier", covers[7]);
        a.setName("Happier");
        a.setFileName("test/songs/happier.mp3");
        a.setImagePath("test/images/happier.jpg");
        albumList.add(a);

        a = new Album("Sorry",  covers[8]);
        a.setName("Sorry");
        a.setFileName("test/songs/sorry.mp3");
        a.setImagePath("test/images/sorry.jpg");
        albumList.add(a);

        a = new Album("Closer",  covers[9]);
        a.setName("Closer");
        a.setFileName("test/songs/closer.mp3");
        a.setImagePath("test/images/closer.jpg");
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

