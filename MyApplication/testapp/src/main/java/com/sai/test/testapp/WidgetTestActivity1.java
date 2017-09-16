package com.sai.test.testapp;

import android.Manifest;
import android.app.LoaderManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.Spinner;
import android.widget.TextView;

public class WidgetTestActivity1 extends AppCompatActivity implements View.OnClickListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final int PERMISSIONS_REQUEST = 100;
    private static final int NOTIFICATION_ID = 100;

    private static final int MENU_NEW_GAME = 100;
    private static final int MENU_QUIT = MENU_NEW_GAME + 1;
    private static final int EDIT_ID = MENU_NEW_GAME + 2;
    private static final int DELETE_ID = MENU_NEW_GAME + 3;
    private static final int START_ID = MENU_NEW_GAME + 4;
    private static final int RESTART_ID = MENU_NEW_GAME + 5;
    private static final int QUIT_ID = MENU_NEW_GAME + 6;
    private static final int PENDING_ID = MENU_NEW_GAME + 7;
    static String mAryData[] = {
            "Data1", "Data2", "Data2", "Data3",
            "Data4", "Data5", "Data6", "Data7",
            "Data8", "Data9", "Data10", "Data11"};
    private Menu mMenuOption;
    private View mCustomView;
    private Spinner mContactSpinner;
    private SimpleCursorAdapter mContactSpinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_test1);

        Button buttonLocalStart = (Button) findViewById(R.id.button_show_menu);
        Button buttonLocalEnd = (Button) findViewById(R.id.button_hide_menu);
        Button buttonRemoteStart = (Button) findViewById(R.id.button_create_noti);
        Button buttonRemoteEnd = (Button) findViewById(R.id.button_remove_noti);
        Button buttonNewActivity = (Button) findViewById(R.id.button_launch_activity);

        buttonLocalStart.setOnClickListener(this);
        buttonLocalEnd.setOnClickListener(this);
        buttonRemoteStart.setOnClickListener(this);
        buttonRemoteEnd.setOnClickListener(this);
        buttonNewActivity.setOnClickListener(this);

        // Simple spinner with array data
        Spinner spinner = (Spinner) findViewById(R.id.spinner_simple);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mAryData);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(spinnerAdapter);

        // Contact spinner using cursorAdapter
        mContactSpinner = (Spinner) findViewById(R.id.spinner_contact);

        // Dynamic permission check and request
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST);
        } else {
            retrieveContactData();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mAryData);
        ListView lv = (ListView) findViewById(R.id.listview_items);
        lv.setAdapter(adapter);
        registerForContextMenu(lv);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                popupToast("Item clicked : " + ((TextView) view).getText());
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                retrieveContactData();
            } else {
                popupToast("Permission not granted");
            }
        }
    }

    void retrieveContactData() {
        mContactSpinnerAdapter = new SimpleCursorAdapter(
                this,
                android.R.layout.simple_list_item_2,
                null,
                new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.NUMBER},
                new int[]{android.R.id.text1,
                        android.R.id.text2},
                0);

        mContactSpinner.setAdapter(mContactSpinnerAdapter);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (intent != null) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                // this activity is launched by pending intent from a notificaiton
                CharSequence cs = extras.getCharSequence("data");
                if (cs != null) {
                    popupToast(cs);
                }
            }
        }
    }

    void RegisterNotification(CharSequence msg) {
        Intent notiIntent = new Intent(this, WidgetTestActivity1.class);
        notiIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        notiIntent.putExtra("data", "return Text");
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notiIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Create custom remote view
        RemoteViews remoteViews = new RemoteViews(getApplicationContext().getPackageName(), R.layout.remote_view_notificaiton);
        remoteViews.setOnClickPendingIntent(R.id.remoteview_noti_layout_id, contentIntent);
        remoteViews.setTextViewText(R.id.custom_view_text, "RemoteView Text");
        remoteViews.setImageViewResource(R.id.toast_image, R.drawable.ic_action_name);

        NotificationCompat.Builder notiBuilder = new NotificationCompat.Builder(this)
                //.addAction(R.drawable.ic_action_new, "Action Button", contentIntent)
                .setContentText(msg)
                .setTicker("Test Notification")
                .setContentTitle("Test Title")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setDefaults(Notification.DEFAULT_ALL)
                .setContent(remoteViews);

        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(NOTIFICATION_ID, notiBuilder.build());
    }

    void UnregisterNotification(int id) {
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(id);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_show_menu) {
            mMenuOption.setGroupVisible(0, true);
        } else if (v.getId() == R.id.button_hide_menu) {
            mMenuOption.setGroupVisible(0, false);
        } else if (v.getId() == R.id.button_create_noti) {
            RegisterNotification("Test Notification");
        } else if (v.getId() == R.id.button_remove_noti) {
            UnregisterNotification(NOTIFICATION_ID);
        } else if (v.getId() == R.id.button_launch_activity) {
            Intent intent = new Intent(this, IntentTestActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        mMenuOption = menu;

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
        if (mCustomView == null) {
            mCustomView = getLayoutInflater().inflate(R.layout.custom_popup_toast,
                    (ViewGroup) findViewById(R.id.custom_popup_toast_layout_id));
        }

        new CustomPopupToast(this, mCustomView, text);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case MENU_NEW_GAME:
                popupToast("New");
                break;
            case MENU_QUIT:
                popupToast("Quit");
                break;
            case START_ID:
                popupToast("Start");
                break;
            case RESTART_ID:
                popupToast("Restart");
                break;
            case QUIT_ID:
                popupToast("Quit");
                break;
            case PENDING_ID:
                popupToast("Pending");
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

        switch (item.getItemId()) {
            case EDIT_ID:
                popupToast("Edit");
                break;
            case DELETE_ID:
                popupToast("Remove");
                break;
        }

        return true;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                this,
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone._ID,
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.NUMBER},
                null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mContactSpinnerAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mContactSpinnerAdapter.swapCursor(null);
    }
}
