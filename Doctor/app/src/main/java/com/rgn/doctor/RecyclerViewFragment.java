package com.rgn.doctor;

import com.dpizarro.autolabel.library.AutoLabelUI;
import com.dpizarro.autolabel.library.Label;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ListViewAutoScrollHelper;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class RecyclerViewFragment extends Fragment implements NumberPicker.OnValueChangeListener,
        TextWatcher,View.OnClickListener{

    private final String KEY_INSTANCE_STATE_PEOPLE = "statePeople";

    private AutoLabelUI mAutoLabel;
    private List<Medicines> mMedicineList;
    private List<Integer> selectedMeds;
    private MyRecyclerAdapter adapter;
    private RecyclerView recyclerView;
    private EditText searchview;
    private Cursor cursor;
    private DatabaseHelper db;
    private final int DATABASE_VERSION = 1;
    private final String DATABASE_NAME="Doctor";
    private TextView dosage,timing;
    private FloatingActionButton fabdonebutton;
    private static final String SHAREDPREFERENCE = "SharedPref";
    private SharedPreferences sharedPreferences;
    private int docid;
    private String docname;
    public static RecyclerViewFragment newInstance() {
        return new RecyclerViewFragment();
    }

    public RecyclerViewFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recyclerview_fragment, container, false);
        searchview=(EditText) view.findViewById(R.id.searchview);
        selectedMeds = new ArrayList<Integer>();
        searchview.addTextChangedListener(this);
        fabdonebutton = (FloatingActionButton)view.findViewById(R.id.fabdone);
        fabdonebutton.setOnClickListener(this);
        db = new DatabaseHelper(getContext(),DATABASE_NAME,null,DATABASE_VERSION);
        sharedPreferences = getActivity().getSharedPreferences(SHAREDPREFERENCE,MODE_PRIVATE);
        docid = sharedPreferences.getInt("Doc_id",0);
        docname = sharedPreferences.getString("Name",null);
        findViews(view);
        setListeners();
        setRecyclerView();
        db.insertsomeentriesinM();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            List<Medicines> medicines = savedInstanceState.getParcelableArrayList(KEY_INSTANCE_STATE_PEOPLE);
            if (medicines != null) {
                mMedicineList = medicines;
                adapter.setMedicines(medicines);
                recyclerView.setAdapter(adapter);
            }
        }
    }

    private void itemListClicked(int position) {
        Medicines medicines = mMedicineList.get(position);
        boolean isSelected = medicines.isSelected();
        boolean success;
        if (isSelected) {
            success = mAutoLabel.removeLabel(position);
            selectedMeds.remove(selectedMeds.indexOf(medicines.getMedid()));
        } else {
            success = mAutoLabel.addLabel(medicines.getMedname(), position);
            selectedMeds.add(medicines.getMedid());
            show(medicines.getMedid(),position);
        }
        if (success) {
            adapter.setItemSelected(position, !isSelected);
        }
    }

    private void setListeners() {
        mAutoLabel.setOnLabelsCompletedListener(new AutoLabelUI.OnLabelsCompletedListener() {
            @Override
            public void onLabelsCompleted() {
                Snackbar.make(recyclerView, "Completed!", Snackbar.LENGTH_SHORT).show();
            }
        });

        mAutoLabel.setOnRemoveLabelListener(new AutoLabelUI.OnRemoveLabelListener() {
            @Override
            public void onRemoveLabel(Label removedLabel, int position) {
                adapter.setItemSelected(position, false);
            }

        });

        mAutoLabel.setOnLabelsEmptyListener(new AutoLabelUI.OnLabelsEmptyListener() {
            @Override
            public void onLabelsEmpty() {
                Snackbar.make(recyclerView, "EMPTY!", Snackbar.LENGTH_SHORT).show();
            }
        });

        mAutoLabel.setOnLabelClickListener(new AutoLabelUI.OnLabelClickListener() {
            @Override
            public void onClickLabel(Label labelClicked) {
                Snackbar.make(recyclerView, labelClicked.getText(), Snackbar.LENGTH_SHORT).show();
            }
        });

    }

    private void findViews(View view) {

        mAutoLabel = (AutoLabelUI) view.findViewById(R.id.label_view);
        mAutoLabel.setBackgroundResource(R.drawable.round_corner_background);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
    }

    private void setRecyclerView() {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);

        mMedicineList = new ArrayList<>();
        GetterSetterMedicines getterSetterMedicines;
        cursor = db.getAllMedsIds();
        if(cursor!=null)
        {
            cursor.moveToFirst();
            while(!cursor.isAfterLast())
            {
                Log.d("RecyclerViewFragment", " initial load");
                getterSetterMedicines = db.getDetailsM(cursor.getInt(0));
                mMedicineList.add(new Medicines(
                        getterSetterMedicines.getMedicine_name(),
                        getterSetterMedicines.getMedicine_dose(),
                        getterSetterMedicines.getMedicine_timing(),
                        getterSetterMedicines.getMedicine_id(),
                        getterSetterMedicines.getMedicine_type(),
                        false
                ));
                cursor.moveToNext();
            }
        }
        adapter = new MyRecyclerAdapter(mMedicineList);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new MyRecyclerAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View v, int position) {
                itemListClicked(position);
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(KEY_INSTANCE_STATE_PEOPLE,
                (ArrayList<? extends Parcelable>) adapter.getMedicines());

    }
    public void show(final int id, final int position)
    {

        final Dialog d = new Dialog(getContext());
        d.setTitle("NumberPicker");
        d.setContentView(R.layout.dialog);
        Button b1 = (Button) d.findViewById(R.id.button1);
        Button b2 = (Button) d.findViewById(R.id.button2);
        final NumberPicker np1 = (NumberPicker) d.findViewById(R.id.numberPicker1);
        final NumberPicker np2 = (NumberPicker) d.findViewById(R.id.numberPicker2);
        np1.setMaxValue(100);
        np1.setMinValue(0);
        np1.setWrapSelectorWheel(false);
        np1.setOnValueChangedListener(RecyclerViewFragment.this);
        np2.setMaxValue(10);
        np2.setMinValue(0);
        np2.setWrapSelectorWheel(false);
        np2.setOnValueChangedListener(RecyclerViewFragment.this);
        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                db.updateDosageinM(np1.getValue(),id);
                db.updateTiminginM(np2.getValue(),id);
                d.dismiss();
                refresh();
            }
        });
        b2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.show();


    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

    }



    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mMedicineList.clear();
        GetterSetterMedicines getterSetterMedicines;
        cursor = db.getDetailsfromChaseq(s.toString());
        if(cursor!=null)
        {
            cursor.moveToFirst();
            while(!cursor.isAfterLast())
            {
                Log.d("RecyclerViewFragment", " laterload");
                getterSetterMedicines = db.getDetailsM(cursor.getInt(0));
                if(selectedMeds.contains(getterSetterMedicines.getMedicine_id()))
                {
                    mMedicineList.add(new Medicines(
                            getterSetterMedicines.getMedicine_name(),
                            getterSetterMedicines.getMedicine_dose(),
                            getterSetterMedicines.getMedicine_timing(),
                            getterSetterMedicines.getMedicine_id(),
                            getterSetterMedicines.getMedicine_type(),
                            true
                    ));
                }else{
                    mMedicineList.add(new Medicines(
                            getterSetterMedicines.getMedicine_name(),
                            getterSetterMedicines.getMedicine_dose(),
                            getterSetterMedicines.getMedicine_timing(),
                            getterSetterMedicines.getMedicine_id(),
                            getterSetterMedicines.getMedicine_type(),
                            false
                    ));
                }
                cursor.moveToNext();
            }
            adapter.setMedicines(mMedicineList);
            recyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(new MyRecyclerAdapter.OnItemClickListener() {

                @Override
                public void onItemClick(View v, int position) {
                    itemListClicked(position);

                }
            });
        }
        }


    @Override
    public void afterTextChanged(Editable s) {

    }
    public void refresh(){
        GetterSetterMedicines getterSetterMedicines;
        cursor = db.getAllMedsIds();
        if(cursor!=null)
        {
            cursor.moveToFirst();
            while(!cursor.isAfterLast())
            {
                Log.d("RecyclerViewFragment", " initial load");
                getterSetterMedicines = db.getDetailsM(cursor.getInt(0));
                mMedicineList.add(new Medicines(
                        getterSetterMedicines.getMedicine_name(),
                        getterSetterMedicines.getMedicine_dose(),
                        getterSetterMedicines.getMedicine_timing(),
                        getterSetterMedicines.getMedicine_id(),
                        getterSetterMedicines.getMedicine_type(),
                        false
                ));
                cursor.moveToNext();
            }
        }
        adapter = new MyRecyclerAdapter(mMedicineList);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new MyRecyclerAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View v, int position) {
                itemListClicked(position);
            }
        });
        searchview.setText("");
    }

    @Override
    public void onClick(View v) {
        final Dialog d = new Dialog(getContext());
        d.setContentView(R.layout.dialog_medicines);
        ListView listView = (ListView)d.findViewById(R.id.listview_dialog);
        List<String> strings = new ArrayList<String>();
        strings = generateString(strings);
        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,strings);
        listView.setAdapter(stringArrayAdapter);
        final Button buttonconfirm = (Button)d.findViewById(R.id.confirmbutton);
        final Button buttoncancel = (Button)d.findViewById(R.id.cancelbutton);
        buttoncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        buttonconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMedicines();
                int patid = sharedPreferences.getInt("temppatient",0);
                db.moveFromAppointmentToCurrentTreatment(patid);
                sendNotificationAboutTreatmentBeginning(patid);
            }
        });
        d.show();
    }
    public List<String> generateString(List<String> stringList){
        GetterSetterMedicines getterSetterMedicines;
        String temp;
        for(int i=0;i<selectedMeds.size();i++){
            getterSetterMedicines = db.getDetailsM(selectedMeds.get(i));
            temp="";
            temp = getterSetterMedicines.getMedicine_name()+"  "+
                    String.valueOf(getterSetterMedicines.getMedicine_dose())+" "+getterSetterMedicines.getMedicine_type()+"  "+
                    String.valueOf(getterSetterMedicines.getMedicine_timing())+" times a day";
            stringList.add(temp);
        }
        return stringList;
    }
    private void sendMedicines(){


    }
    private void sendNotificationAboutTreatmentBeginning(int patid){
        String link = "http://gauravkc.pe.hu/treatment_started_by_doctor.php?"+
                "pat_id="+patid+"&"+
                "doc_id="+docid+"&"+
                "doc_name="+docname;
        new DownloadRawData().execute(link);
    }

    private class DownloadRawData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String link = params[0];
            try {
                URL url = new URL(link);
                InputStream is = url.openConnection().getInputStream();
                StringBuffer buffer = new StringBuffer();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String res) {

        }
    }
}
