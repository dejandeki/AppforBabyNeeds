package com.dejan.babyneeds.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dejan.babyneeds.R;
import com.dejan.babyneeds.data.DatabaseHandler;
import com.dejan.babyneeds.model.Item;

import java.text.MessageFormat;
import java.util.List;

public class RecyclerViewAdapter extends Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Item> itemList;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private LayoutInflater inflater;

    public RecyclerViewAdapter(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }


    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_row, viewGroup, false);


        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder viewHolder, int position) {

        Item item = itemList.get(position); // object item
        viewHolder.itemName.setText(MessageFormat.format("Name: {0}", item.getItemName()));
        viewHolder.itemColor.setText(MessageFormat.format("Color: {0}", item.getItemColor()));
        viewHolder.itemQuantity.setText(MessageFormat.format("Quantity: {0}", item.getItemQuantity()));
        viewHolder.itemSize.setText(MessageFormat.format("Size: {0}", item.getItemSize()));
        viewHolder.dateAdded.setText(MessageFormat.format("Added on: {0}", item.getDateItemAdded()));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView itemName, itemColor, itemQuantity, itemSize, dateAdded;
        public int id;
        public Button editButton, deleteButton, saveButton;

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            RecyclerViewAdapter.this.context = ctx;

            itemName = itemView.findViewById(R.id.item_name);
            itemColor = itemView.findViewById(R.id.item_color);
            itemQuantity = itemView.findViewById(R.id.item_quantity);
            itemSize = itemView.findViewById(R.id.item_size);
            dateAdded = itemView.findViewById(R.id.item_date);

            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);

            editButton.setOnClickListener(this);
            deleteButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int position;
            position = getAdapterPosition();
            Item item = itemList.get(position);
            switch (v.getId()) {
                case R.id.editButton:
                    editItem(item);
                    break;
                case R.id.deleteButton:

                    deleteItem(item.getId());
                    break;
            }
        }

        private void editItem(final Item newItem) {

            builder = new AlertDialog.Builder(context);
            inflater = LayoutInflater.from(context);
            final View view = inflater.inflate(R.layout.popup, null);

            final EditText babyItem, itemQuantity, itemColor, itemSize;
            TextView title;

            babyItem = view.findViewById(R.id.babyItem);
            itemQuantity = view.findViewById(R.id.itemQuantity);
            itemColor = view.findViewById(R.id.itemColor);
            itemSize = view.findViewById(R.id.itemSize);
            title = view.findViewById(R.id.title);
            title.setText(R.string.edit_time);

            saveButton = view.findViewById(R.id.saveButton);
            saveButton.setText(R.string.updateIsUpdate);

            babyItem.setText(newItem.getItemName());
            itemQuantity.setText(String.valueOf(newItem.getItemQuantity()));
            itemColor.setText(newItem.getItemColor());
            itemSize.setText(String.valueOf(newItem.getItemSize()));


            builder.setView(view);
            dialog = builder.create();
            dialog.show();

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    DatabaseHandler dbHandler = new DatabaseHandler(context);
                    newItem.setItemName(babyItem.getText().toString());
                    newItem.setItemColor(itemColor.getText().toString());
                    newItem.setItemSize(Integer.parseInt(itemSize.getText().toString()));
                    newItem.setItemQuantity(Integer.parseInt(itemQuantity.getText().toString()));

                    if (!babyItem.getText().toString().isEmpty()
                            && !itemColor.getText().toString().isEmpty()
                            && !itemSize.getText().toString().isEmpty()
                            && !itemQuantity.getText().toString().isEmpty()) {
                        dbHandler.updateItem(newItem);
                        notifyItemChanged(getAdapterPosition(), newItem);
                    } else {
                        Snackbar.make(view, "FIelds Empty", Snackbar.LENGTH_SHORT).show();

                    }
                    dialog.dismiss();
                }
            });
        }

        private void deleteItem(final int id) {

            builder = new AlertDialog.Builder(context);
            inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.confirmation_pop, null);

            Button noButton = view.findViewById(R.id.conf_no_button);
            Button yesButton = view.findViewById(R.id.conf_yes_button);

            builder.setView(view);
            dialog = builder.create();
            dialog.show();

            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseHandler db = new DatabaseHandler(context);
                    db.deleteItem(id);
                    itemList.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());

                    dialog.dismiss();

                }
            });

            noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog.dismiss();
                }
            });


        }
    }


}
