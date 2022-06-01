package com.example.noytprakt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    DatabaseReference mDatabase;
    String sk = "Noyt";
    ArrayList<String> alist;
    EditText edName;
    EditText edChast;
    EditText edOzy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edName = findViewById(R.id.edName);
        edChast = findViewById(R.id.edChast);
        edOzy = findViewById(R.id.edOzy);
        ListView listView1 = (ListView) findViewById(R.id.listView1);
        mDatabase = FirebaseDatabase.getInstance().getReference(sk);

        alist = new ArrayList<String>(){};
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, alist);
        listView1.setAdapter(arrayAdapter);


        mDatabase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String name = (String) ds.child("name").getValue();
                    String chast = (String) ds.child("chast").getValue();
                    String ozy = (String) ds.child("ozy").getValue();
                    alist.add(name + " с частотой " + chast + " и оперативной памятью объемом " + ozy + " гб");
                }
                mDatabase.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Ошибка :(", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void onClickSave(View view) {
        String name = edName.getText().toString();
        String chast = edChast.getText().toString();
        String ozy = edOzy.getText().toString();
        Toast.makeText(MainActivity.this, "Данные добавлены!", Toast.LENGTH_LONG).show();
        alist.add(name + " с частотой " + chast + " и оперативной памятью объемом " + ozy + " гб");
        Noyt noyt = new Noyt(name, chast, ozy);
        mDatabase.push().setValue(noyt);
    }

}