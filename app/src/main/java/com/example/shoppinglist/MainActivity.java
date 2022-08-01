package com.example.shoppinglist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements MyAdapter.OnItemClickListener {
    private Database database;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private BottomNavigationView bottomNavigationView;
    private FloatingActionButton floating_button_addItem;
    private TextView title_toolbar;
    private MyAdapter myAdapter;
    private ArrayList<Item> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottomNavView);
        title_toolbar = findViewById(R.id.title_toolbar);
        floating_button_addItem = findViewById(R.id.additem_button);
        viewPager = findViewById(R.id.viewpager);

        database = new Database(this, null, null, 1);
        addItemsToDatabase();
        itemList = database.getAllItem();

        myAdapter = new MyAdapter("Home", itemList, this);

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        FragmentOne fragmentOne = FragmentOne.newInstance("Urgent");
        fragmentOne.setMyAdapter(myAdapter);
        viewPagerAdapter.addFragment(fragmentOne);

        fragmentOne = FragmentOne.newInstance("Home");
        fragmentOne.setMyAdapter(myAdapter);
        viewPagerAdapter.addFragment(fragmentOne);

        fragmentOne = FragmentOne.newInstance("Completed");
        fragmentOne.setMyAdapter(myAdapter);
        viewPagerAdapter.addFragment(fragmentOne);

        viewPager.setAdapter(viewPagerAdapter);
        myAdapter.setFragment("Home");
        viewPager.setCurrentItem(1);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.fragmentOneLayout:
                        myAdapter.setFragment("Urgent");
                        viewPager.setCurrentItem(0);
                        title_toolbar.setText(getString(R.string.urgent_list));
                        break;
                    case R.id.fragmentTwoLayout:
                        myAdapter.setFragment("Home");
                        viewPager.setCurrentItem(1);
                        title_toolbar.setText(getString(R.string.app_name));
                        break;
                    case R.id.fragmentThreeLayout:
                        myAdapter.setFragment("Completed");
                        viewPager.setCurrentItem(2);
                        title_toolbar.setText(getString(R.string.items_bought));
                        break;
                }
                return false;
            }
        });

        floating_button_addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Go add item then add to database
                Intent intent = new Intent();
                intent.putExtra("ActionType", "Add");
                intent.setClass(getApplicationContext(), MainActivity2.class);
                startActivityForResult(intent,1);
            }
        });
    }

    private void addItemsToDatabase() {
        if (database.CountRows() == 0) {
            database.AddItem(new Item("Juice", "Orange juice", "Large", 2, false));
            database.AddItem(new Item("Bread", "A bread", "Default", 1, true));
            database.AddItem(new Item("Milk", "Low fat milk", "Large", 3, true));
            database.AddItem(new Item("Instant noodle", "Curry Flavour", "Default", 1, false));
            database.AddItem(new Item("Chocolate Bar", "A chocolate Bar", "Small", 1, false));
            database.AddItem(new Item("Shampoo", "100ml shampoo", "Small", 1, false, "22 Nov 2020"));
            database.AddItem(new Item("Shower Gel", "Normal shower gel", "Large", 1, false, "22 Nov 2020"));
        }
    }

    public void UpdateList() {
        if (itemList != null) { itemList.clear(); }
        itemList = database.getAllItem();
    }

    @Override
    public void onToggleSwitch(Item item) {
        item.setBought(true); // Move item to complete
        String currentDate = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(new Date()); // Get today's date
        item.setDate(currentDate);
        database.Update(item); // Update to database
        UpdateList();
        myAdapter.setItemList(itemList);
    }

    @Override
    public void onClick(Item item, String currentFragment) {
        Intent intent = new Intent();
        intent.putExtra("Item", item);
        intent.putExtra("CurrentFragment", currentFragment);
        intent.setClass(this, MainActivity3.class);
        startActivityForResult(intent,2);
    }

    @Override
    public void onLongClick(Item item) {
        String deleteText = "Are you sure you want to delete " + item.getName() + " from the list?";
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Item")
                .setMessage(deleteText)
                .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        database.DeleteItem(item.getId());
                        UpdateList();
                        myAdapter.setItemList(itemList);
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (data.getParcelableExtra("Item") != null) {
                Item item = data.getParcelableExtra("Item");
                if (requestCode == 1) { // Add item
                    if (resultCode == RESULT_OK) {
                        database.AddItem(item);
                        UpdateList();
                        myAdapter.setItemList(itemList);
                    }
                }
                if (requestCode == 2) { // Edit Item
                    if (resultCode == RESULT_OK) { database.Update(item); }
                }
                UpdateList();
                myAdapter.setItemList(itemList);
            }
        }
    }
}
