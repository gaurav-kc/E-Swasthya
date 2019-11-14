package com.rgn.e_swasthya;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.CardProvider;
import com.dexafree.materialList.card.OnActionClickListener;
import com.dexafree.materialList.card.action.TextViewAction;
import com.dexafree.materialList.view.MaterialListView;
import com.google.gson.Gson;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

public class DisplayMedicines extends AppCompatActivity {

    private MaterialListView mListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_medicines);
        mListView = (MaterialListView)findViewById(R.id.material_listview_medicines);
        mListView.setItemAnimator(new SlideInLeftAnimator());
        mListView.getItemAnimator().setAddDuration(300);
        mListView.getItemAnimator().setRemoveDuration(300);
        addCard("Dr. Gaurav Chaudhari","Some additional info about gaurav",1);
        addCard("Dr. Sagar Chaudhari","Some additional info about sagar",2);
    }

    private void addCard(String title,String description,int tag) {
        mListView.getAdapter().add(new Card.Builder(this)
                .setTag(tag)
                .withProvider(new CardProvider())
                .setLayout(R.layout.medicines_card_view)
                .setTitle(title)
                .setDividerVisible(true)
                .setDescription(description)
                .addAction(R.id.left_text_button, new TextViewAction(this)
                        .setListener(new OnActionClickListener() {
                            @Override
                            public void onActionClicked(View view, Card card) {
                                int pos = Integer.parseInt(card.getTag().toString());
                                //pos is very important else you will never know which cards icon was clicked
                                Toast.makeText(getApplicationContext(),Integer.toString(pos),Toast.LENGTH_SHORT).show();
                            }
                        }))
                .addAction(R.id.right_text_button, new TextViewAction(this))
                .endConfig()
                .build());
    }
}
