package com.rgn.doctor;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.CardProvider;
import com.dexafree.materialList.card.OnActionClickListener;
import com.dexafree.materialList.card.action.TextViewAction;
import com.dexafree.materialList.view.MaterialListView;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link History.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link History#newInstance} factory method to
 * create an instance of this fragment.
 */
public class History extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Context mContext;
    private MaterialListView mListView;
    private OnFragmentInteractionListener mListener;
    private DatabaseHelper db;
    private final int DATABASE_VERSION = 1;
    private final String DATABASE_NAME="Doctor";
    private GetterSetterHistory getterSetterHistory;
    public History() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment History.
     */
    // TODO: Rename and change types and number of parameters
    public static History newInstance(String param1, String param2) {
        History fragment = new History();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mContext = getContext();
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        // Bind the MaterialListView to a variable
        mListView = (MaterialListView) view.findViewById(R.id.material_listview_history);
        mListView.setItemAnimator(new SlideInLeftAnimator());
        mListView.getItemAnimator().setAddDuration(300);
        mListView.getItemAnimator().setRemoveDuration(300);
        db = new DatabaseHelper(getContext(),DATABASE_NAME, null, DATABASE_VERSION);
        showCards();
        return view;
    }

    private void showCards(){
        Cursor cursor = db.getAllH();
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            addCard(cursor.getString(1),cursor.getInt(0));
            cursor.moveToNext();
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
    private void addCard(String title,int tag) {
        mListView.getAdapter().add(new Card.Builder(mContext)
                .setTag(tag)
                .withProvider(new CardProvider())
                .setLayout(R.layout.history_card_view)
                .addAction(R.id.h_info,new TextViewAction(getContext()).setListener(new OnActionClickListener() {
                    @Override
                    public void onActionClicked(View view, Card card) {
                        AlertDialog.Builder al = new AlertDialog.Builder(getContext());
                        al.setTitle("Information");
                        al.setMessage("Treatment started at 22-4-2017\nTreatment ended at 28-4-2017\nTreatment was marked as successful");
                        al.show();
                    }
                }))
                .setTitle(title)
                .setDividerVisible(true)
                .endConfig()
                .build());
    }
}
