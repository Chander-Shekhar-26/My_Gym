package com.example.my_gym;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

import static com.example.my_gym.TrainingActivity.TRAINING_KEY;

public class PlanAdpter extends RecyclerView.Adapter<PlanAdpter.ViewHolder>{

    public interface RemovePlan{
        void onRemoveResult(Plan plan);
    }

    private RemovePlan removePlan;

     private ArrayList<Plan>plans=new ArrayList<>();
     private Context context;
     private String type="";

    public PlanAdpter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.plan_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.txtName.setText(plans.get(position).getTraining().getName());
        holder.txtDescription.setText(plans.get(position).getTraining().getShortDesc());
        holder.txtTime.setText(String.valueOf(plans.get(position).getMinutes()));
        Glide.with(context)
                .asBitmap()
                .load(plans.get(position).getTraining().getImageUrl())
                .into(holder.image);

        if (plans.get(position).isAccomplished()){
            holder.emptyCircle.setVisibility(View.GONE);
            holder.checkedCircle.setVisibility(View.VISIBLE);
        }else {
            holder.emptyCircle.setVisibility(View.VISIBLE);
            holder.checkedCircle.setVisibility(View.GONE);
        }
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,TrainingActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(TRAINING_KEY,plans.get(position).getTraining());
                context.startActivity(intent);
            }
        });

        if (type.equals("edit")){
         holder.emptyCircle.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 AlertDialog.Builder builder=new AlertDialog.Builder(context)
                         .setTitle("Finished")
                         .setMessage("Have You Finished " + plans.get(position).getTraining().getName()+"?")
                         .setNegativeButton("No", new DialogInterface.OnClickListener() {
                             @Override
                             public void onClick(DialogInterface dialog, int which) {

                             }
                         }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                             @Override
                             public void onClick(DialogInterface dialog, int which) {
                                 for (Plan p:Utils.getPlans()){
                                     if (p.equals(plans.get(position))){
                                         p.setAccomplished(true);
                                     }
                                 }
                                 notifyDataSetChanged();
                             }
                         });
                 builder.create().show();
             }
         });

         holder.parent.setOnLongClickListener(new View.OnLongClickListener() {
             @Override
             public boolean onLongClick(View v) {
                 AlertDialog.Builder builder=new AlertDialog.Builder(context)
                         .setTitle("Remove")
                         .setMessage("Are You Sure You Want To Delete "+plans.get(position).getTraining().getName())
                         .setNegativeButton("No", new DialogInterface.OnClickListener() {
                             @Override
                             public void onClick(DialogInterface dialog, int which) {

                             }
                         }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                             @Override
                             public void onClick(DialogInterface dialog, int which) {
                                 try {
                                     removePlan=(RemovePlan) context;
                                     removePlan.onRemoveResult(plans.get(position));

                                 }catch (ClassCastException e){
                                         e.printStackTrace();
                                 }
                             }
                         });
                 builder.create().show();
                 return true;
             }
         });
        }

    }

    @Override
    public int getItemCount() {
        return plans.size();
    }

    public void setPlans(ArrayList<Plan> plans) {
        this.plans = plans;
        notifyDataSetChanged();
    }

    public void setType(String type) {
        this.type = type;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView txtName,txtTime,txtDescription;
        private MaterialCardView parent;
        private ImageView image,checkedCircle,emptyCircle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTime=itemView.findViewById(R.id.txtTime);
            txtName=itemView.findViewById(R.id.txtTimeDescription);
            txtDescription=itemView.findViewById(R.id.txtDescription69);
            parent=itemView.findViewById(R.id.parent);
            image=itemView.findViewById(R.id.trainingImage);
            checkedCircle=itemView.findViewById(R.id.checkedCircle);
            emptyCircle=itemView.findViewById(R.id.emptyCircle);


        }
    }
}
