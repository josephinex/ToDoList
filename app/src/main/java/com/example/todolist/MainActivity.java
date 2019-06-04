package com.example.todolist;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.os.Build.VERSION_CODES.M;

public class MainActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private ArrayAdapter<String> adapter;
    private ListView listItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SystemClock.sleep(2000); 
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);
        listItems = (ListView) findViewById(R.id.listItems);

        showItemList();
    }

    private void showItemList() {
        ArrayList<String> itemList = dbHelper.getToDoList();
        if(adapter == null){
            adapter = new ArrayAdapter<>(this, R.layout.row, R.id.item_title, itemList);
            listItems.setAdapter(adapter);
        }else{
            adapter.clear();
            adapter.addAll(itemList);
            adapter.notifyDataSetChanged();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);

        //change menu icon color
        Drawable icon = menu.getItem(0).getIcon();
        icon.mutate();

        if(Build.VERSION.SDK_INT >= M){
            icon.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.action_add_item:
                final EditText editText = new EditText(this);
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("Add new item")
                        .setView(editText)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String item = String.valueOf(editText.getText());
                                dbHelper.insertNewItem(item);
                                showItemList();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void deleteItem(View view){
        View parent = (View) view.getParent();
        TextView itemTextView = findViewById(R.id.item_title);
        String item = String.valueOf(itemTextView.getText());
        dbHelper.deleteItem(item);
        showItemList();
    }
}
