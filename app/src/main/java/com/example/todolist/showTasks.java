package com.example.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_tasks);
        listView = (ListView)findViewById(R.id.listview);

        mDataBase = FirebaseDatabase.getInstance();
        mRef = mDataBase.getReference("Users");
        al = new ArrayList<>();
        read();
    }
    private void read(){
        readData(new FirebaseRollback() {
            @Override
            public void onCallback(List<String> list) {
                ArrayAdapter adapter = new ArrayAdapter(showTasks.this ,android.R.layout.simple_list_item_1, list);
                listView.setAdapter(adapter);
            }
        });
    }

    private void readData(final FirebaseRollback firebaseRollback){
            mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for(DataSnapshot ds : dataSnapshot.getChildren()){

                        Map<String ,Object> map = (Map<String,Object>)ds.getValue();
                        String task = (String)map.get("Task");
                        String date = (String)map.get("Date");

                        String combinedValue = task.concat("\n"+date);
                        Log.d("hh",combinedValue);
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
