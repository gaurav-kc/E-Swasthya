package com.rgn.e_swasthya;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Toast;

import static java.lang.Integer.parseInt;


public class FindDoctor extends Fragment  {

    private OnFragmentInteractionListener mListener;
    private CheckBox chkboxpain,chkboxinflammation,chkboxcoughing,chkboxinjury,chkboxfever,
            chkboxrashes,chkboxvomiting,chkboxtiredness,chkboxshivering,chkboxloosemotion;
    private RadioButton usemylocation;
    private FloatingActionButton fabdonebutton;
    private dialogBoxOptions dialogboxoptions;
    TextInputEditText neareststreettiet,citytiet;
    private String symptomString="",pain="9",inflammation="9",coughing="9",injury="9",
            fever="9",rashes="9",vomiting="9", tiredness="9",shivering="9",loosemotion="9";
    private String neareststreet,city;
    private boolean my_current_location_preference=true;
    public FindDoctor() {
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
        View view = inflater.inflate(R.layout.fragment_find_doctor,container,false);
        mListener.FindDoctorUpdate();
        dialogboxoptions = new dialogBoxOptions();
        chkboxpain = (CheckBox)view.findViewById(R.id.Internalpainchkbox);
        chkboxinflammation = (CheckBox)view.findViewById(R.id.inflammationchkbox);
        chkboxcoughing = (CheckBox)view.findViewById(R.id.coughingchkbox);
        chkboxtiredness = (CheckBox)view.findViewById(R.id.tirednesschkbox);
        chkboxinjury = (CheckBox)view.findViewById(R.id.injurychkbox);
        chkboxfever = (CheckBox)view.findViewById(R.id.feverchkbox);
        chkboxrashes = (CheckBox)view.findViewById(R.id.rasheschkbox);
        chkboxvomiting = (CheckBox)view.findViewById(R.id.vomitingchkbox);
        chkboxshivering = (CheckBox)view.findViewById(R.id.shiveringchkbox);
        chkboxloosemotion = (CheckBox)view.findViewById(R.id.loosemotionchkbox);
        usemylocation = (RadioButton)view.findViewById(R.id.usemylocation);
        fabdonebutton = (FloatingActionButton)view.findViewById(R.id.fabdone);
        usemylocation.setChecked(true);
        neareststreettiet = (TextInputEditText)view.findViewById(R.id.neareststreet);
        citytiet = (TextInputEditText)view.findViewById(R.id.city);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialog(v);
            }
        };
        chkboxpain.setOnClickListener(listener);
        chkboxinflammation.setOnClickListener(listener);
        chkboxcoughing.setOnClickListener(listener);
        chkboxtiredness.setOnClickListener(listener);
        chkboxinjury.setOnClickListener(listener);
        chkboxfever.setOnClickListener(listener);
        chkboxrashes.setOnClickListener(listener);
        chkboxvomiting.setOnClickListener(listener);
        chkboxshivering.setOnClickListener(listener);
        chkboxloosemotion.setOnClickListener(listener);
        View.OnClickListener fablistener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onFabClick();
            }
        };
        View.OnClickListener teitlistener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTeitClick();
            }
        };
        View.OnClickListener mylocradiolistener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMyLocRadioClick();
            }
        };
        fabdonebutton.setOnClickListener(fablistener);
        neareststreettiet.setOnClickListener(teitlistener);
        citytiet.setOnClickListener(teitlistener);
        usemylocation.setOnClickListener(mylocradiolistener);
        return view;
    }

    private void onMyLocRadioClick() {
        if(usemylocation.isChecked())
        {
            neareststreettiet.setText("");
            citytiet.setText("");
            neareststreet="";
            city="";
            my_current_location_preference = true;
        }
        Toast.makeText(getContext(),"In OnMyLocRadio",Toast.LENGTH_SHORT).show();

    }

    private void onTeitClick() {
        usemylocation.setChecked(false);
        my_current_location_preference = false;
        Toast.makeText(getContext(),"In onTeitClick",Toast.LENGTH_SHORT).show();
    }

    private void onFabClick() {
        //will do some stuff
        symptomString = pain+inflammation+coughing+injury+fever+rashes+vomiting+tiredness+shivering+loosemotion;
        Toast.makeText(getContext(), symptomString,Toast.LENGTH_LONG).show();
        neareststreet = neareststreettiet.getText().toString();
        city = citytiet.getText().toString();
        Intent i = new Intent(getContext(),ShowDoctorsOnMap.class);
        i.putExtra("mode",1);
        i.putExtra("Symptoms",symptomString);
        startActivity(i);
    }

    private void createDialog(View v) {

        AlertDialog.Builder alt_bld = new AlertDialog.Builder(getContext());
        //alt_bld.setIcon(R.drawable.icon);
        if(v.getId() == R.id.Internalpainchkbox)
        {
            if(chkboxpain.isChecked())
            {
                alt_bld.setTitle("Select pain type");
                alt_bld.setSingleChoiceItems(dialogboxoptions.pain, -1, new DialogInterface
                    .OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    Toast.makeText(getContext(),Integer.toString(item),Toast.LENGTH_SHORT).show();
                    pain=Integer.toString(item);
                    chkboxpain.setText(dialogboxoptions.pain[item]);
                    dialog.dismiss();// dismiss the alertbox after chose option
                    }
                })
                .setCancelable(false);
            }else
            {
                pain="9";
                chkboxpain.setText(getResources().getString(R.string.pain));
                return;
            }
        }
        if(v.getId() == R.id.inflammationchkbox)
        {
            if(chkboxinflammation.isChecked())
            {
                alt_bld.setTitle("Select inflammation type");
                alt_bld.setSingleChoiceItems(dialogboxoptions.inflammation, -1, new DialogInterface
                        .OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        inflammation=Integer.toString(item);
                        chkboxinflammation.setText(dialogboxoptions.inflammation[item]);
                        dialog.dismiss();// dismiss the alertbox after chose option

                    }
                }).setCancelable(false);
            }else
            {
                inflammation="9";
                chkboxinflammation.setText(getResources().getString(R.string.inflammation));
                return;
            }
        }
        if(v.getId() == R.id.coughingchkbox)
        {
            if(chkboxcoughing.isChecked())
            {
                alt_bld.setTitle("Select coughing type");
                alt_bld.setSingleChoiceItems(dialogboxoptions.coughing, -1, new DialogInterface
                        .OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        coughing=Integer.toString(item);
                        chkboxcoughing.setText(dialogboxoptions.coughing[item]);
                        dialog.dismiss();// dismiss the alertbox after chose option

                    }
                }).setCancelable(false);
            }else
            {
                coughing="9";
                chkboxcoughing.setText(getResources().getString(R.string.coughing));
                return;
            }
        }
        if(v.getId() == R.id.injurychkbox)
        {
            if(chkboxinjury.isChecked())
            {
                alt_bld.setTitle("Select injury type");
                alt_bld.setSingleChoiceItems(dialogboxoptions.injury, -1, new DialogInterface
                        .OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        injury=Integer.toString(item);
                        chkboxinjury.setText(dialogboxoptions.injury[item]);
                        dialog.dismiss();// dismiss the alertbox after chose option

                    }
                }).setCancelable(false);
            }else
            {
                injury="9";
                chkboxinjury.setText(getResources().getString(R.string.injury));
                return;
            }
        }
        if(v.getId() == R.id.feverchkbox)
        {
            if(chkboxfever.isChecked())
            {
                alt_bld.setTitle("Select fever type");
                alt_bld.setSingleChoiceItems(dialogboxoptions.fever, -1, new DialogInterface
                        .OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        fever=Integer.toString(item);
                        chkboxfever.setText(dialogboxoptions.fever[item]);
                        dialog.dismiss();// dismiss the alertbox after chose option

                    }
                }).setCancelable(false);
            }else
            {
                fever="9";
                chkboxfever.setText(getResources().getString(R.string.fever));
                return;
            }
        }
        if(v.getId() == R.id.rasheschkbox)
        {
            if(chkboxrashes.isChecked())
            {
                alt_bld.setTitle("Select rashes type");
                alt_bld.setSingleChoiceItems(dialogboxoptions.rashes, -1, new DialogInterface
                        .OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        rashes=Integer.toString(item);
                        chkboxrashes.setText(dialogboxoptions.rashes[item]);
                        dialog.dismiss();// dismiss the alertbox after chose option

                    }
                }).setCancelable(false);
            }else
            {
                rashes="9";
                chkboxrashes.setText(getResources().getString(R.string.rashes));
                return;
            }
        }
        if(v.getId() == R.id.vomitingchkbox)
        {
            if(chkboxvomiting.isChecked())
            {
                alt_bld.setTitle("Select vomiting type");
                alt_bld.setSingleChoiceItems(dialogboxoptions.vomiting, -1, new DialogInterface
                        .OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        vomiting=Integer.toString(item);
                        chkboxvomiting.setText(dialogboxoptions.vomiting[item]);
                        dialog.dismiss();// dismiss the alertbox after chose option

                    }
                }).setCancelable(false);
            }else
            {
                vomiting="9";
                chkboxvomiting.setText(getResources().getString(R.string.vomiting));
                return;
            }
        }
        if(v.getId() == R.id.tirednesschkbox)
        {
            if(chkboxtiredness.isChecked())
            {
                alt_bld.setTitle("Select tiredness type");
                alt_bld.setSingleChoiceItems(dialogboxoptions.tiredness, -1, new DialogInterface
                        .OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        tiredness=Integer.toString(item);
                        chkboxtiredness.setText(dialogboxoptions.tiredness[item]);
                        dialog.dismiss();// dismiss the alertbox after chose option

                    }
                }).setCancelable(false);
            }else
            {
                tiredness="9";
                chkboxtiredness.setText(getResources().getString(R.string.tiredness));
                return;
            }
        }
        if(v.getId() == R.id.shiveringchkbox)
        {
            if(chkboxshivering.isChecked())
            {
                alt_bld.setTitle("Select shivering type");
                alt_bld.setSingleChoiceItems(dialogboxoptions.shivering, -1, new DialogInterface
                        .OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        shivering=Integer.toString(item);
                        chkboxshivering.setText(dialogboxoptions.shivering[item]);
                        dialog.dismiss();// dismiss the alertbox after chose option

                    }
                }).setCancelable(false);
            }else
            {
                shivering="9";
                chkboxshivering.setText(getResources().getString(R.string.shivering));
                return;
            }
        }
        if(v.getId() == R.id.loosemotionchkbox)
        {
            if(chkboxloosemotion.isChecked())
            {
                alt_bld.setTitle("Select loose motion type");
                alt_bld.setSingleChoiceItems(dialogboxoptions.loose_motion, -1, new DialogInterface
                        .OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        loosemotion=Integer.toString(item);
                        chkboxloosemotion.setText(dialogboxoptions.loose_motion[item]);
                        dialog.dismiss();// dismiss the alertbox after chose option

                    }
                }).setCancelable(false);
            }else
            {
                loosemotion="9";
                chkboxloosemotion.setText(getResources().getString(R.string.loosemotion));
                return;
            }
        }
        AlertDialog alert = alt_bld.create();
        alert.show();
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
        void FindDoctorUpdate();
    }

    @Override
    public void onDestroyView() {
        Log.d("D FindDoc","");
        super.onDestroyView();
    }
}

