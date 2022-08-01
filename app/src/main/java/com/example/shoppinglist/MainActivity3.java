package com.example.shoppinglist;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity3 extends AppCompatActivity {
    private TextView textView_itemName, textView_details, textView_number, textView_size;
    private ImageView imageView_urgent;
    private ImageButton imageButton_edit;
    private Item item;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        context = this.getApplicationContext();

        textView_itemName = findViewById(R.id.textView_display_itemName);
        textView_details = findViewById(R.id.textView_display_details);
        textView_number = findViewById(R.id.textView_display_number);
        textView_size = findViewById(R.id.textView_display_size);
        imageView_urgent = findViewById(R.id.imageView_urgent);
        imageButton_edit = findViewById(R.id.imageButton_edit);

        imageButton_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // Go to edit activity
                Intent intent = new Intent();
                intent.putExtra("Item", item);
                intent.putExtra("ActionType", "Edit");
                intent.setClass(context, MainActivity2.class);
                startActivityForResult(intent,2);
            }
        });

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            item = intent.getExtras().getParcelable("Item");
            if (item != null) {
                String currentFragment = intent.getExtras().getString("CurrentFragment");
                textView_itemName.setText(item.getName());
                textView_details.setText(item.getDetails());
                textView_number.setText(String.valueOf(item.getQty()));
                textView_size.setText(item.getSize());
                if (item.getUrgent()==1) { imageView_urgent.setImageResource(R.drawable.checked); }
                else { imageView_urgent.setImageResource(R.drawable.unchecked); }

                // edit button won't show for completed items
                if (currentFragment != null) {
                    if (currentFragment.equals("Completed")) { imageButton_edit.setVisibility(View.GONE); }
                }
            }
            else {
                Toast.makeText(context, "Item clicked is invalid!", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (data.getParcelableExtra("Item") != null) {
                Item item = data.getParcelableExtra("Item");
                String action = data.getStringExtra("Action");
                if (requestCode == 2) {
                    if (resultCode == RESULT_OK) {
                        Intent intent = new Intent();
                        intent.putExtra("Item", item);
                        intent.putExtra("Action", action);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }
            }
        }
    }
}