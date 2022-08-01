package com.example.shoppinglist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {
    private EditText editText_itemName, editText_itemDetail, editText_quantity;
    private TextView title_toolbar;
    private Button button;
    private ImageButton imageButtonUp, imageButtonDown;
    private Spinner spinner;
    private CheckBox checkBox;
    private Context context;
    private String name, details, size, action;
    private int quantity;
    private boolean urgent;
    private Item item;
    private ArrayList<String> sizeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        sizeList.add("Default");
        sizeList.add("Small");
        sizeList.add("Medium");
        sizeList.add("Large");

        context = this.getApplicationContext();
        editText_itemName = findViewById(R.id.editTextShoppingItem);
        editText_itemDetail = findViewById(R.id.editText_multiline);
        editText_quantity = findViewById(R.id.editText_number);
        title_toolbar = findViewById(R.id.title_toolbar2);
        spinner = findViewById(R.id.spinner_size);
        button = findViewById(R.id.button_addToList);
        checkBox = findViewById(R.id.checkBox_urgent);
        imageButtonUp = findViewById(R.id.imageButton_arrow_up);
        imageButtonDown = findViewById(R.id.imageButton_arrow_down);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sizeList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                size = adapter.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        imageButtonUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantity++;
                editText_quantity.setText(String.valueOf(quantity));
            }
        });

        imageButtonDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (quantity > 1) { quantity--; }
                editText_quantity.setText(String.valueOf(quantity));
            }
        });

        spinner.setSelection(0);
        quantity = 0;
        Intent intent = getIntent();

        if (intent.getExtras() != null) {
            action = intent.getExtras().getString("ActionType");
            if (action != null) {
                if (action.equals("Edit")) {
                    button.setText(R.string.update_item);
                    title_toolbar.setText(R.string.edit_items);
                    item = intent.getExtras().getParcelable("Item");
                    if (item != null) {
                        name = item.getName();
                        details = item.getDetails();
                        quantity = item.getQty();
                        size = item.getSize();
                        urgent = item.getUrgent()==1;

                        editText_itemName.setText(name);
                        editText_itemDetail.setText(details);
                        editText_quantity.setText(String.valueOf(quantity));
                        spinner.setSelection(sizeList.indexOf(size));
                        checkBox.setChecked(urgent);
                    }
                    else {
                        Toast.makeText(context, "Item not found!", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
                else {
                    title_toolbar.setText(R.string.add_items);
                    button.setText(R.string.add_to_list);
                }
            }
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean validate = true;
                // Check empty
                if (editText_itemName.getText().toString().equals("")) {
                    editText_itemName.setError("Please enter the item to be purchased");
                    validate = false;
                }
                if (validate) {
                    name = editText_itemName.getText().toString();
                    details = editText_itemDetail.getText().toString();
                    urgent = checkBox.isChecked();

                    if (action.equals("Add")) { // For add
                        item = new Item(name, details, size, quantity, urgent);
                    }
                    if (action.equals("Edit")) { // For edit
                        item.setName(name);
                        item.setDetails(details);
                        item.setSize(size);
                        item.setQty(quantity);
                        item.setUrgent(urgent);
                    }

                    if (item != null) { // back to main activity
                        Intent intent = new Intent();
                        intent.putExtra("Item", item);
                        intent.putExtra("Action", action);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                    else {
                        Toast.makeText(context, "Item is invalid!", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
            }
        });
    }
}
