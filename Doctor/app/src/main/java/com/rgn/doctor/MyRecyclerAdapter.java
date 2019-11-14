package com.rgn.doctor;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by dpizarro
 */
public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MedicineViewHolder> {

    List<Medicines> medicines;
    OnItemClickListener mItemClickListener;

    public MyRecyclerAdapter(List<Medicines> medicines) {
        this.medicines = medicines;
    }

    @Override
    public MedicineViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_item, viewGroup, false);
        return new MedicineViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MedicineViewHolder personViewHolder, int i) {
        personViewHolder.medname.setText(medicines.get(i).getMedname());
        personViewHolder.meddosage.setText(String.valueOf(medicines.get(i).getMeddosage()) + " "+ medicines.get(i).getType());
        personViewHolder.medtiming.setText(String.valueOf(medicines.get(i).getMedtimings()) + " times a day");
        personViewHolder.cbSelected.setChecked(medicines.get(i).isSelected());

    }

    public void setItemSelected(int position, boolean isSelected) {
        if (position != -1) {
            medicines.get(position).setSelected(isSelected);
            notifyDataSetChanged();
        }
    }

    List<Medicines> getMedicines(){
        return medicines;
    }

    @Override
    public int getItemCount() {
        return medicines.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public void setMedicines(List<Medicines> medicines) {
        this.medicines = medicines;
    }


    public interface OnItemClickListener {

        void onItemClick(View view, int position);
    }

    public class MedicineViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {

        CardView cv;
        TextView medname;
        TextView meddosage,medtiming;
        CheckBox cbSelected;

        MedicineViewHolder(final View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            cv = (CardView) itemView.findViewById(R.id.cv);
            cv.setTag(1);
            medname = (TextView) itemView.findViewById(R.id.medname);
            meddosage = (TextView) itemView.findViewById(R.id.meddosage);
            medtiming = (TextView) itemView.findViewById(R.id.medtiming);
            cbSelected = (CheckBox) itemView.findViewById(R.id.cbSelected);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition());
            }
        }
    }


}
