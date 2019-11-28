package com.dejan.babyneeds;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dejan.babyneeds.data.DatabaseHandler;
import com.dejan.babyneeds.model.Item;
import com.dejan.babyneeds.ui.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<Item> itemList;
    private DatabaseHandler dbHandler;
    private FloatingActionButton fab;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    private Button saveButton;
    private EditText babyItem, itemQuantity, itemColor, itemSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        recyclerView = findViewById(R.id.recyclerview);
        fab = findViewById(R.id.fab);

        dbHandler = new DatabaseHandler(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        itemList = new ArrayList<>();

        // get items from db
        itemList = dbHandler.getAllItems();
        for (Item item : itemList) {
            Log.d("ActivityList", "onCreate " + item.getItemName());

        }

        recyclerViewAdapter = new RecyclerViewAdapter(this, itemList);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPopDialog();
            }
        });
    }

    private void createPopDialog() {
        builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup, null);

        babyItem = view.findViewById(R.id.babyItem);
        itemQuantity = view.findViewById(R.id.itemQuantity);
        itemColor = view.findViewById(R.id.itemColor);
        itemSize = view.findViewById(R.id.itemSize);
        saveButton = view.findViewById(R.id.saveButton);

        builder.setView(view);
        alertDialog = builder.create();
        alertDialog.show();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!babyItem.getText().toString().isEmpty()
                        && !itemSize.getText().toString().isEmpty()
                        && !itemQuantity.getText().toString().isEmpty()
                        && !itemColor.getText().toString().isEmpty()) {
                    saveItem(v);
                } else {
                    Snackbar.make(v, "Empty fields not allowed", Snackbar.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void saveItem(View v) {

        Item item = new Item();

        String newItem = babyItem.getText().toString().trim();
        String newColor = itemColor.getText().toString().trim();
        int newSize = Integer.parseInt(itemSize.getText().toString().trim());
        int newQuantity = Integer.parseInt(itemQuantity.getText().toString().trim());

        item.setItemName(newItem);
        item.setItemColor(newColor);
        item.setItemQuantity(newQuantity);
        item.setItemSize(newSize);

        dbHandler.addItem(item);

        Snackbar.make(v, "Item Saved", Snackbar.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                alertDialog.dismiss();
                startActivity(new Intent(ListActivity.this, ListActivity.class));
                finish();
            }
        }, 1200);

    }

}
