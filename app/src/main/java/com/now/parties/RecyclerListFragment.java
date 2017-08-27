package com.now.parties;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class RecyclerListFragment extends Fragment {

    public static final String ANONYMOUS = "anonymous";

    private RecyclerView mRecyclerMainView;
    private GridLayoutManager mGridLayoutManager;
    private FirebaseAuth mFirebaseAuth;

    private DatabaseReference mDatabaseArticleReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recycler_fragment, container, false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mGridLayoutManager = new GridLayoutManager(getActivity(), 1);
        mRecyclerMainView = (RecyclerView) getView().findViewById(R.id.recyclerMainView);
        mRecyclerMainView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerMainView.setLayoutManager(mGridLayoutManager);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseArticleReference = FirebaseDatabase.getInstance().getReference().child("articles");

        FirebaseRecyclerAdapter<ViewItem, ArticlesViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<ViewItem, ArticlesViewHolder>(
                        ViewItem.class,
                        R.layout.recycler_list_components,
                        ArticlesViewHolder.class,
                        mDatabaseArticleReference
                ){
                    @Override
                    protected void populateViewHolder(ArticlesViewHolder viewHolder, ViewItem model, int position) {
                        final String article_key = getRef(position).getKey();

                        viewHolder.setPlaceName(model.getPlaceName());
                        viewHolder.setPlaceDesc(model.getPlaceDesc());
                        viewHolder.setPlaceImage(getActivity().getApplicationContext(), model.getPlaceImage());

                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
//                                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                                        .setAction("Action", null).show();
                                ArticleContentsFragment articleContentsFragment = new ArticleContentsFragment();
                                Bundle bundle = new Bundle(1);
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                fragmentManager.beginTransaction().add(R.id.fragment_default, articleContentsFragment).commit();
                                bundle.putString("article_key", article_key);
                                articleContentsFragment.setArguments(bundle);
                                getActivity().overridePendingTransition(R.anim.fade, R.anim.hold);
                            }
                        });
                    }
                };
        mRecyclerMainView.setAdapter(firebaseRecyclerAdapter);
    }

    public static class ArticlesViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public ArticlesViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setPlaceName(String placeName) {
            TextView article_place_name = (TextView) mView.findViewById(R.id.placeTitleTextView);
            article_place_name.setText(placeName);
        }
        public void setPlaceDesc(String placeDesc) {
            TextView article_place_desc = (TextView) mView.findViewById(R.id.placeDescTextView);
            article_place_desc.setText(placeDesc);
        }
        public void setPlaceImage(Context context, String placeImage) {
            ImageView article_place_image = (ImageView) mView.findViewById(R.id.placeImageView);
            Picasso.with(context).load(placeImage).into(article_place_image);
        }

    }

}
