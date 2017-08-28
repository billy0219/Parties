package com.now.parties;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
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

//    public RecyclerList() {
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseArticleReference = FirebaseDatabase.getInstance().getReference().child("articles");
//        if (getArguments() != null) {
//            mArticleKey = getArguments().getString(ARG_ARTICLE_KEY);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler_list, container, false);

        mRecyclerMainView = (RecyclerView) view.findViewById(R.id.recyclerMainView);

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
//                                ARG_ARTICLE_KEY = article_key;
//                                newInstance(ARG_ARTICLE_KEY);
                        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
//                                ArticleContents articleContents = new ArticleContents();
//                                Bundle bundle = new Bundle(1);
//                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                                fragmentManager.beginTransaction().add(R.id.fragment_default, articleContents).commit();
//                                bundle.putString("article_key", article_key);
//                                articleContents.setArguments(bundle);
//                                getActivity().overridePendingTransition(R.anim.fade, R.anim.hold);
                    }
                });
            }
        };
        mFirebaseRecyclerAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = mFirebaseRecyclerAdapter.getItemCount();
                int lastVisiblePosition =
                        mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the
                // user is at the bottom of the list, scroll to the bottom
                // of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    mLinearLayoutManager.scrollToPosition(positionStart);
                }
            }
        });
        mRecyclerMainView.setAdapter(mFirebaseRecyclerAdapter);
        mRecyclerMainView.setHasFixedSize(true);
        mRecyclerMainView.setItemAnimator(new DefaultItemAnimator());
        // Inflate the layout for this fragment
        return view;
    }

//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }

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
