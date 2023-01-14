package com.example.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.CircleMembersOnMap;
import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class membersadepter extends RecyclerView.Adapter<membersadepter.membersadepterViewHolder> {

    ArrayList<String> namelist, idlist;
    Context context;
    public membersadepter(){
        //default constructor
    }
    public membersadepter(ArrayList<String> namelist, ArrayList<String> idlist, Context context) {
        this.namelist = namelist;
        this.idlist = idlist;
        this.context = context;
    }

    @NonNull
    @Override
    public membersadepterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardrecyclerview, parent,false);
        membersadepterViewHolder membersadepterView = new membersadepterViewHolder(v,context,namelist,idlist);
        return membersadepterView;
    }

    @Override
    public void onBindViewHolder(@NonNull membersadepterViewHolder holder, int position) {
        //users userobj = namelist.get(position);
        //holder.nametxt.setText(userobj.getFirstname());
        String strobj = namelist.get(position);
        holder.nametxt.setText(strobj);

        final String strobj1 = idlist.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, CircleMembersOnMap.class);
                intent.putExtra("joined_uid",strobj1);
                context.startActivity(intent);
                //Toast.makeText(context,"response:"+strobj1,Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        int list1 = namelist.size();
        int list2 = idlist.size();
        return list1 | list2;
    }

    public class membersadepterViewHolder extends RecyclerView.ViewHolder {
        TextView nametxt;
        Context c;
        ArrayList<String> namearraylist, idarraylist;
        FirebaseUser firebaseUser;
        FirebaseAuth firebaseAuth;
        public membersadepterViewHolder (@NonNull View itemView, Context c, ArrayList<String> namearraylist, ArrayList<String> idarraylist)
        {
            super(itemView);
            this.c = c;
            this.namearraylist = namearraylist;
            this.idarraylist = idarraylist;

            firebaseAuth = FirebaseAuth.getInstance();
            firebaseUser = firebaseAuth.getCurrentUser();

            nametxt = itemView.findViewById(R.id.textview_name);
        }
    }
}
