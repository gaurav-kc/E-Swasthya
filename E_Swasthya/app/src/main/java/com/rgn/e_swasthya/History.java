package com.rgn.e_swasthya;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
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

public class History extends Fragment {
    private Context mContext;
    private MaterialListView mListView;
    private DatabaseHelper databaseHelper;
    private final int DATABASE_VERSION = 1;
    private final String DATABASE_NAME="Doctor";
    private OnFragmentInteractionListener mListener;

    public History() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mListener.HistoryUpdate();
        mContext = getContext();
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        // Bind the MaterialListView to a variable
        mListView = (MaterialListView) view.findViewById(R.id.material_listview_history);
        databaseHelper=new DatabaseHelper(getContext(),DATABASE_NAME,null,DATABASE_VERSION);
        insertHistory();
        mListView.setItemAnimator(new SlideInLeftAnimator());
        mListView.getItemAnimator().setAddDuration(300);
        mListView.getItemAnimator().setRemoveDuration(300);
        showCards();
        return view;    }

    private void showCards(){
        Cursor cursor = databaseHelper.getAllH();
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
            void HistoryUpdate();
    }

    @Override
    public void onDestroyView() {
        Log.d("D History","");
        super.onDestroyView();
    }
    private void addCard(String title,int tag) {
        mListView.getAdapter().add(new Card.Builder(mContext)
                .setTag(tag)
                .withProvider(new CardProvider())
                .setLayout(R.layout.history_card_view)
                .setTitle(title)
                .setDividerVisible(true)
                .addAction(R.id.h_info, new TextViewAction(mContext)
                        .setListener(new OnActionClickListener() {
                            @Override
                            public void onActionClicked(View view, Card card) {
                                int pos = Integer.parseInt(card.getTag().toString());
                                //pos is very important else you will never know which cards icon was clicked
                                AlertDialog.Builder al = new AlertDialog.Builder(getContext());
                                al.
                                        setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        });
                                al.setTitle("Some Information");
                                al.setMessage("Treatment Started from 22-4-2017 and ended at 28-4-2017\n");
                                al.show();
                            }
                        }))
                .addAction(R.id.right_text_button, new TextViewAction(mContext))
                .endConfig()
                .build());
    }
    private void insertHistory() {
        GetterSetterHistory getterSetterHistory = new GetterSetterHistory();
        getterSetterHistory.setH_id(656);
        getterSetterHistory.setDoc_name("Dr. Avinash Mishra");
        getterSetterHistory.setDuration("7 days from 26-2-2017");
        getterSetterHistory.setCost(200);
        databaseHelper.insertinH(getterSetterHistory);
        GetterSetterHistory getterSetterHistory1= new GetterSetterHistory();
        getterSetterHistory1.setH_id(657);
        getterSetterHistory1.setDoc_name("Dr. Milind Chaudhari");
        getterSetterHistory1.setDuration("8 days from 3-3-2017");
        getterSetterHistory1.setCost(450);
        databaseHelper.insertinH(getterSetterHistory1);
        GetterSetterHistory getterSetterHistory2 = new GetterSetterHistory();
        getterSetterHistory2.setH_id(658);
        getterSetterHistory2.setDoc_name("Dr. Prakash Kale");
        getterSetterHistory2.setDuration("7 days from 3-4-2017");
        getterSetterHistory2.setCost(300);
        databaseHelper.insertinH(getterSetterHistory2);
    }
}
