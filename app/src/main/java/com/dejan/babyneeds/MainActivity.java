package com.dejan.babyneeds;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dejan.babyneeds.data.DatabaseHandler;
import com.dejan.babyneeds.model.Item;

public class MainActivity extends AppCompatActivity {

    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private Button saveButton;
    private EditText babyItem, itemQuantity, itemColor, itemSize;
    private DatabaseHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbHandler = new DatabaseHandler(this);

        byPassActivity();


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPopupDialog();
            }
        });


    }

    private void byPassActivity() {

        if (dbHandler.getItemCount() > 0) {
            startActivity(new Intent(MainActivity.this, ListActivity.class));
            finish();
        }


    }

    private void saveItem(View view) {
        // save each baby item to database
        Item item = new Item();
        String newItem = babyItem.getText().toString().trim();
        String newColor = itemColor.getText().toString().trim();
        int newQuantity = Integer.parseInt(itemQuantity.getText().toString().trim());
        int newSize = Integer.parseInt(itemSize.getText().toString().trim());

        item.setItemName(newItem);
        item.setItemColor(newColor);
        item.setItemQuantity(newQuantity);
        item.setItemSize(newSize);

        dbHandler.addItem(item);
        Snackbar.make(view, "Item saved", Snackbar.LENGTH_SHORT).show();

        // move to next screen = details screen
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();

                startActivity(new Intent(MainActivity.this, ListActivity.class));

            }
        }, 1200);
    }

    private void createPopupDialog() {

        builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup, null);

        babyItem = view.findViewById(R.id.babyItem);
        itemQuantity = view.findViewById(R.id.itemQuantity);
        itemColor = view.findViewById(R.id.itemColor);
        itemSize = view.findViewById(R.id.itemSize);

        saveButton = view.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!babyItem.getText().toString().isEmpty()
                        && !itemColor.getText().toString().isEmpty()
                        && !itemQuantity.getText().toString().isEmpty()
                        && !itemSize.getText().toString().isEmpty()) {
                    saveItem(v);
                } else {
                    Snackbar.make(v, "Empty fields not allowed", Snackbar.LENGTH_SHORT).show();

                }
            }
        });

        builder.setView(view);
        dialog = builder.create(); // creating our dialog object
        dialog.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
