package com.sai.example.myapplication;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    final int MENU_NEW_GAME = 0;
    final int MENU_QUIT = 1;
    final int EDIT_ID = 2;
    final int DELETE_ID = 3;

    private Button btn_1;
    private Button btn_2;
    private Menu optionMenu;

    static String data[] = {
            "Data1", "Data2", "Data2", "Data3",
            "Data4", "Data5", "Data6", "Data7",
            "Data8", "Data9", "Data10", "Data11"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_1 = (Button) findViewById(R.id.button);
        btn_2 = (Button) findViewById(R.id.button2);

        btn_1.setOnClickListener(this);
        btn_2.setOnClickListener(this);

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, data);
        ListView lv = (ListView) findViewById(R.id.lvList);
        lv.setAdapter(adapter);
        registerForContextMenu(lv);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),"Item clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == btn_1) {
            optionMenu.setGroupVisible(0, true);
        } else if (v == btn_2) {
            optionMenu.setGroupVisible(0, false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        optionMenu = menu;

//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu, menu);

        menu.add(0, MENU_NEW_GAME, 0, "Game");
        menu.getItem(0).setIcon(R.drawable.ic_action_new);
//        menu.getItem(0).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        menu.add(0, MENU_QUIT, 0, "Quit");
        menu.getItem(1).setIcon(R.drawable.ic_action_name);
//        menu.getItem(1).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        SubMenu gameMenu = menu.addSubMenu("GameSub");
        SubMenu quitMenu = menu.addSubMenu("QuitSub");
        gameMenu.add("start");
        gameMenu.add("restart");
        quitMenu.add("quit");
        quitMenu.add("pending");

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case MENU_NEW_GAME:
                Toast.makeText(this, "New", Toast.LENGTH_SHORT).show();
                break;
            case MENU_QUIT:
                Toast.makeText(this, "Quit", Toast.LENGTH_SHORT).show();
                break;
        }

        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.add(0, EDIT_ID, 0, "Edit");
        menu.add(0, DELETE_ID, 0, "Delete");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        super.onContextItemSelected(item);

        switch(item.getItemId()) {
            case EDIT_ID:
                Toast.makeText(this, "Edit", Toast.LENGTH_SHORT).show();
                break;
            case DELETE_ID:
                Toast.makeText(this, "Remove", Toast.LENGTH_SHORT).show();
                break;
        }

        return true;
    }
}
