package com.example.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class showTasks extends AppCompatActivity {

    FirebaseDatabase mDataBase;
    DatabaseReference mRef;
    ArrayList<String> al;
    ArrayList<String> keys;
    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_tasks);
        listView = (ListView)findViewById(R.id.listview);

        mDataBase = FirebaseDatabase.getInstance();
        mRef = mDataBase.getReference("Users");
        al = new ArrayList<>();
        keys = new ArrayList<>();


        readTask();                                    // data is retrieved from firebase For showing on Listview

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {


                        AlertDialog.Builder alertMsg = new AlertDialog.Builder(showTasks.this);
                        alertMsg.setMessage("Want to delete")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Task<Void> task =  mRef.child(keys.get(position)).removeValue();

                                        task.addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(showTasks.this,"Task removed",Toast.LENGTH_LONG).show();
                                                readTask();

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(showTasks.this,e.getMessage(),Toast.LENGTH_LONG).show();
                                            }
                                        });

                                    }
                                })
                                .setNegativeButton("No",null);

                                AlertDialog alert = alertMsg.create();
                                alert.show();

                return false;
            }
        });
    }

    private void readKeys(){
        readDataKeys(new FirebaseRollback() {
            @Override
            public void onCallback(List<String> list) {

            }
        });
    }

    private void readTask(){                    //this read method is for displaying All the tasks form firebase to the listview
        al.clear();
        keys.clear();

        readDataTask(new FirebaseRollback() {
            @Override
            public void onCallback(List<String> list) {
                ArrayAdapter adapter = new ArrayAdapter(showTasks.this ,android.R.layout.simple_list_item_1, list);
                listView.setAdapter(adapter);

            }
        });
        readKeys();
    }

    private void readDataKeys(final  FirebaseRollback firebaseRollback){            // For Keys


        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    keys.add(ds.getKey());
                }
                firebaseRollback.onCallback(keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    private void readDataTask(final FirebaseRollback firebaseRollback){             //For listview
            mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    for(DataSnapshot ds : dataSnapshot.getChildren()){

                       Map<String , Object> map = (Map<String,Object>) ds.getValue();

                        String task = (String)map.get("Task");
                        String date = (String)map.get("Date");

                        String combinedValue = task.concat("\n"+date);

                        al.add(combinedValue);

                    }
                    firebaseRollback.onCallback(al);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
    }

    private interface FirebaseRollback{
        void onCallback(List<String> list );
    }

}
