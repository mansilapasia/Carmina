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

public class Old extends AppCompatActivity {

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
            Glide.with(this).load(R.drawable.old).into((ImageView) findViewById(R.id.backdrop));
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
                R.drawable.chand,
                R.drawable.bade,
                R.drawable.lagja,
                R.drawable.meresamne,
                R.drawable.ajeeb,
                R.drawable.aajkal,
                R.drawable.ekjanabee,
                R.drawable.ekladki,
                R.drawable.gulabi,
                R.drawable.kyahua};

        Album a = new Album("Chand Sa Roshan Chehra", covers[0]);
        a.setName("Chand Sa Roshan Chehra");
        a.setFileName("test/songs/chandsa.mp3");
        a.setImagePath("test/images/chand.jpg");
        albumList.add(a);

        a = new Album("Bade Acche Lagte Hai", covers[1]);
        a.setName("Bade Acche Lagte Hai");
        a.setFileName("test/songs/bade.mp3");
        a.setImagePath("test/images/bade.jpg");
        albumList.add(a);

        a = new Album("La Ja Gale", covers[2]);
        a.setName("Lag Ja Gale");
        a.setFileName("test/songs/lag.mp3");
        a.setImagePath("test/images/lagja.jpg");
        albumList.add(a);

        a = new Album("Mere Samne Vali Khidki Main", covers[3]);
        a.setName("Mere Samne Vali Khidki Main");
        a.setFileName("test/songs/samne.mp3");
        a.setImagePath("test/images/meresaamne.jpg");
        albumList.add(a);

        a = new Album("Ajeeb Dastaan Hai Yeh",  covers[4]);
        a.setName("Ajeeb Dastaan Hai Yeh");
        a.setFileName("test/songs/ajeeb.mp3");
        a.setImagePath("test/images/ajeeb.jpg");
        albumList.add(a);

        a = new Album("Aaj Kal Tere Mere Pyaar",covers[5]);
        a.setName("Aaj Kal Tere Mere Pyaar ");
        a.setFileName("test/songs/aaj.mp3");
        a.setImagePath("test/images/aajkal.jpg");
        albumList.add(a);

        a = new Album("Ek Ajnabee", covers[6]);
        a.setName("Ek Ajnabee");
        a.setFileName("test/songs/ekajanabee.mp3");
        a.setImagePath("test/images/ekajanabee.jpg");
        albumList.add(a);

        a = new Album("Ek Ladki", covers[7]);
        a.setName("Ek ladki ");
        a.setFileName("test/songs/ekladki.mp3");
        a.setImagePath("test/images/ekladki.jpg");
        albumList.add(a);

        a = new Album("Gulabi Ankhein",  covers[8]);
        a.setName("Gulabi Ankhein");
        a.setFileName("test/songs/gulaabi.mp3");
        a.setImagePath("test/images/gulabi.jpg");
        albumList.add(a);

        a = new Album("Kya Hua Tera Vada",  covers[9]);
        a.setName("Kya Hua Tera Vada");
        a.setFileName("test/songs/kyahua.mp3");
        a.setImagePath("test/images/kyahua.jpg");
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

