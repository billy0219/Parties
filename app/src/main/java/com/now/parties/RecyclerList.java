package com.now.parties;

import android.content.Context;
import android.net.Uri;
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

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecyclerList.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RecyclerList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecyclerList extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public static final String ANONYMOUS = "anonymous";

    private RecyclerView mRecyclerMainView;
    private GridLayoutManager mGridLayoutManager;
    private FirebaseAuth mFirebaseAuth;

    private DatabaseReference mDatabaseArticleReference;

    private OnFragmentInteractionListener mListener;

    public RecyclerList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecyclerList.
     */
    // TODO: Rename and change types and number of parameters
    public static RecyclerList newInstance(String param1, String param2) {
        RecyclerList fragment = new RecyclerList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        mGridLayoutManager = new GridLayoutManager(getActivity(), 1);
        mRecyclerMainView = (RecyclerView) getView().findViewById(R.id.recyclerMainView);
        mRecyclerMainView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerMainView.setLayoutManager(mGridLayoutManager);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseArticleReference = FirebaseDatabase.getInstance().getReference().child("articles");

        FirebaseRecyclerAdapter<ViewItem, RecyclerList.ArticlesViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<ViewItem, RecyclerList.ArticlesViewHolder>(
                        ViewItem.class,
                        R.layout.recycler_list_components,
                        RecyclerList.ArticlesViewHolder.class,
                        mDatabaseArticleReference
                ){
                    @Override
                    protected void populateViewHolder(RecyclerList.ArticlesViewHolder viewHolder, ViewItem model, int position) {
                        final String article_key = getRef(position).getKey();

                        viewHolder.setPlaceName(model.getPlaceName());
                        viewHolder.setPlaceDesc(model.getPlaceDesc());
                        viewHolder.setPlaceImage(getActivity().getApplicationContext(), model.getPlaceImage());

                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
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
        mRecyclerMainView.setAdapter(firebaseRecyclerAdapter);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recycler_list, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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
