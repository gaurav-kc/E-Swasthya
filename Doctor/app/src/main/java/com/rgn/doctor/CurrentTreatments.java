package com.rgn.doctor;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
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
 * {@link CurrentTreatments.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CurrentTreatments#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CurrentTreatments extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private DatabaseHelper db;
    private final int DATABASE_VERSION = 1;
    private final String DATABASE_NAME="Doctor";
    private GetterSetterCurrentTreatments getterSetterCurrentTreatments;
    private String mParam1;
    private String mParam2;
    private Context mContext;
    private MaterialListView mListView;
    private OnFragmentInteractionListener mListener;

    public CurrentTreatments() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CurrentTreatments.
     */
    // TODO: Rename and change types and number of parameters
    public static CurrentTreatments newInstance(String param1, String param2) {
        CurrentTreatments fragment = new CurrentTreatments();
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
        View view = inflater.inflate(R.layout.fragment_current_treatments, container, false);
        // Bind the MaterialListView to a variable
        mListView = (MaterialListView) view.findViewById(R.id.material_listview_current_treatments);
        mListView.setItemAnimator(new SlideInLeftAnimator());
        mListView.getItemAnimator().setAddDuration(300);
        mListView.getItemAnimator().setRemoveDuration(300);
        db = new DatabaseHelper(getContext(),DATABASE_NAME, null, DATABASE_VERSION);
        showCards();
        return view;
    }

    private void showCards(){
        Cursor cursor = db.getAllCT();
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
                .setLayout(R.layout.current_treatments_card_view)
                .setTitle(title)
                .setDividerVisible(true)
                .addAction(R.id.CT_message_button, new TextViewAction(mContext)
                        .setListener(new OnActionClickListener() {
                            @Override
                            public void onActionClicked(View view, Card card) {
                                String pos = card.getTag().toString();
                                //pos is very important else you will never know which cards icon was clicked
                                Intent i = new Intent(getContext(),FriendlyChatManager.class);
                                i.putExtra("Pat_id",pos);
                                startActivity(i);
                            }
                        }))
                .addAction(R.id.CT_info_button, new TextViewAction(mContext)
                        .setListener(new OnActionClickListener() {
                            @Override
                            public void onActionClicked(View view, Card card) {
                                int pos = Integer.parseInt(card.getTag().toString());
                                //pos is very important else you will never know which cards icon was clicked
                                AlertDialog.Builder al = new AlertDialog.Builder(getContext());
                                al.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                al.setTitle("Some info");
                                al.setMessage("Treatment started since 22-4-2017");
                                al.show();
                                }
                        }))
                /*.addAction(R.id.movetohistory,new TextViewAction(mContext)
                        .setListener(new OnActionClickListener() {
                            @Override
                            public void onActionClicked(View view, Card card) {
                                int id = Integer.parseInt(card.getTag().toString());
                                db.moveFromCurrentTreatmentToHistory(id);
                                db.removefromCT(id);
                                card.setDismissible(true);
                                card.dismiss();
                            }
                        })
                )*/
                .endConfig()
                .build());
    }

}
