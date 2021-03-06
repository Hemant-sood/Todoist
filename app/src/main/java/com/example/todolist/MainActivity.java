package com.example.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private FirebaseDatabase mDataBase;
    private DatabaseReference mRef;
    private Button addTask, showtasks;
    private EditText task, date;
    private boolean flag1,flag2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDataBase = FirebaseDatabase.getInstance();
        mRef = mDataBase.getReference("Users");

        addTask = (Button)findViewById(R.id.send);
        showtasks = (Button)findViewById(R.id.show);
        task = (EditText)findViewById(R.id.editText2);
        date = (EditText)findViewById(R.id.editText3);





        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Task added...",Toast.LENGTH_SHORT).show();

                String key = mRef.push().getKey();

               mRef.child(key).child("Task").setValue(task.getText().toString());
               mRef.child(key).child("Date").setValue(date.getText().toString());


            }
        });

        showtasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "wait a second...",Toast.LENGTH_SHORT).show();

                Intent i = new Intent(MainActivity.this, showTasks.class);
                startActivity(i);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.about :
                                Intent i1 = new Intent(getApplicationContext(),About.class);
                                startActivity(i1);
                                break;
            case R.id.contact :
                                Intent i2 = new Intent(getApplicationContext(),Contact.class);
                                startActivity(i2);
                                break;
            case R.id.version:
                                Toast.makeText(getApplicationContext(),"v1.0",Toast.LENGTH_LONG).show();
                                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
