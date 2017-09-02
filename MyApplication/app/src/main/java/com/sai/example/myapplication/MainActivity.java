package com.sai.example.myapplication;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    final int MENU_NEW_GAME = 100;
    final int MENU_QUIT = MENU_NEW_GAME+1;
    final int EDIT_ID = MENU_NEW_GAME+2;
    final int DELETE_ID = MENU_NEW_GAME+3;
    final int START_ID = MENU_NEW_GAME+4;
    final int RESTART_ID = MENU_NEW_GAME+5;
    final int QUIT_ID = MENU_NEW_GAME+6;
    final int PENDING_ID = MENU_NEW_GAME+7;

    private Button btn_1;
    private Button btn_2;
    private Button btn_3;
    private Button btn_4;

    private Menu optionMenu;

    private View cv;
    private TextView cv_tv;

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
        btn_3 = (Button) findViewById(R.id.button3);
        btn_4 = (Button) findViewById(R.id.button4);

        btn_1.setOnClickListener(this);
        btn_2.setOnClickListener(this);
        btn_3.setOnClickListener(this);
        btn_4.setOnClickListener(this);

        cv = getLayoutInflater().inflate(R.layout.custom_toast_view,
                (ViewGroup)findViewById(R.id.toast_layout_id));
        ImageView iv = (ImageView)cv.findViewById(R.id.toast_image);
        iv.setImageResource(R.drawable.ic_action_new);
        cv_tv = (TextView)cv.findViewById(R.id.toast_text);
        cv_tv.setText("");

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            popupToast(extras.getCharSequence("data"));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);
        ListView lv = (ListView) findViewById(R.id.lvList);
        lv.setAdapter(adapter);
        registerForContextMenu(lv);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                popupToast("Item clicked");
                //Toast.makeText(getApplicationContext(),"Item clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void RegisterNotification(CharSequence msg) {
        Intent notiIntent = new Intent(this, MainActivity.class);
        notiIntent.putExtra("data", "return Text");
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notiIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        RemoteViews remoteViews = new RemoteViews(getApplicationContext().getPackageName(), R.layout.custom_toast_view);
        remoteViews.setOnClickPendingIntent(R.id.toast_layout_id, contentIntent);
        remoteViews.setTextViewText(R.id.toast_text, "RemoteView Text");
        remoteViews.setImageViewResource(R.id.toast_image, R.drawable.ic_action_name);

        NotificationCompat.Builder notiBuilder = new NotificationCompat.Builder(this)
                .addAction(R.drawable.ic_action_new, "Action Button", contentIntent)
                .setContentText(msg)
                .setTicker("Test Notification")
                .setContentTitle("Test Title")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setDefaults(Notification.DEFAULT_ALL)
                .setContent(remoteViews);

        NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(123, notiBuilder.build());
    }

    void UnregisterNotification(int id) {
        NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(id);
    }

    @Override
    public void onClick(View v) {
        if (v == btn_1) {
            optionMenu.setGroupVisible(0, true);
        } else if (v == btn_2) {
            optionMenu.setGroupVisible(0, false);
        } else if (v == btn_3) {
            RegisterNotification("Test Notification");
        } else if (v == btn_4) {
            UnregisterNotification(123);
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
        menu.getItem(0).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        menu.add(0, MENU_QUIT, 0, "Quit");
        menu.getItem(1).setIcon(R.drawable.ic_action_name);
        menu.getItem(1).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        SubMenu gameMenu = menu.addSubMenu("GameSub");
        SubMenu quitMenu = menu.addSubMenu("QuitSub");
        gameMenu.add(0, START_ID, 0, "Start");
        gameMenu.add(0, RESTART_ID, 0, "Restart");
        quitMenu.add(0, QUIT_ID, 0, "Quit");
        quitMenu.add(0, PENDING_ID, 0, "Pending");

        return true;
    }

    public void popupToast(CharSequence text) {
        cv_tv.setText(text);
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(cv);
        toast.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case MENU_NEW_GAME:
                popupToast("New");
                //Toast.makeText(this, "New", Toast.LENGTH_SHORT).show();
                break;
            case MENU_QUIT:
                popupToast("Quit");
                //Toast.makeText(this, "Quit", Toast.LENGTH_SHORT).show();
                break;
            case START_ID:
                popupToast("Start");
                //Toast.makeText(this, "Quit", Toast.LENGTH_SHORT).show();
                break;
            case RESTART_ID:
                popupToast("Restart");
                //Toast.makeText(this, "Quit", Toast.LENGTH_SHORT).show();
                break;
            case QUIT_ID:
                popupToast("Quit");
                //Toast.makeText(this, "Quit", Toast.LENGTH_SHORT).show();
                break;
            case PENDING_ID:
                popupToast("Pending");
                //Toast.makeText(this, "Quit", Toast.LENGTH_SHORT).show();
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
                popupToast("Edit");
                //Toast.makeText(this, "Edit", Toast.LENGTH_SHORT).show();
                break;
            case DELETE_ID:
                popupToast("Remove");
                //Toast.makeText(this, "Remove", Toast.LENGTH_SHORT).show();
                break;
        }

        return true;
    }
}
