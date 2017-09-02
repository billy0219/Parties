package com.now.parties;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class RecyclerList extends Fragment {

    private RecyclerView mRecyclerMainView;
    private LinearLayoutManager mLinearLayoutManager;
    private FirebaseAuth mFirebaseAuth;

    private FirebaseRecyclerAdapter mFirebaseRecyclerAdapter;

    private DatabaseReference mDatabaseArticleReference;

    public RecyclerList() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler_list, container, false);

        mRecyclerMainView = (RecyclerView) view.findViewById(R.id.recyclerMainView);
        mRecyclerMainView.setItemAnimator(new DefaultItemAnimator());
//        MainActivity mainActivity = (MainActivity) getActivity();
//        mainActivity.hideFloatingActionButton();

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseArticleReference = FirebaseDatabase.getInstance().getReference().child("articlesList");

        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerMainView.setLayoutManager(mLinearLayoutManager);

        mFirebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ViewItem, ArticlesViewHolder>(
                ViewItem.class,
                R.layout.recycler_list_components,
                ArticlesViewHolder.class,
                mDatabaseArticleReference ){
            @Override
            protected void populateViewHolder(ArticlesViewHolder viewHolder, ViewItem model, int position) {
                final String article_key = getRef(position).getKey();

                viewHolder.setPlaceName(model.getPlaceName());
                viewHolder.setPlaceDesc(model.getPlaceDesc());
                viewHolder.setPlaceImage(getActivity().getApplicationContext(), model.getPlaceImage());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                                ArticleContents articleContents = new ArticleContents();
                                Bundle bundle = new Bundle(1);
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                fragmentManager.beginTransaction().addToBackStack(null)
                                        .add(R.id.fragment_container, articleContents)
                                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                        .commit();
                                bundle.putString("article_key", article_key);
                                articleContents.setArguments(bundle);
                    }
                });
            }
        };
        mFirebaseRecyclerAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int articleListCount = mFirebaseRecyclerAdapter.getItemCount();
                int lastVisiblePosition =
                        mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                if (lastVisiblePosition == 0 ||
                        (positionStart >= (articleListCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    mLinearLayoutManager.scrollToPosition(positionStart);
                }
            }
        });
        mRecyclerMainView.setAdapter(mFirebaseRecyclerAdapter);
    }

    public static class ArticlesViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public ArticlesViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setPlaceName(String placeName) {
            TextView article_place_name = (TextView) mView.findViewById(R.id.placeNameTextView);
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
