package com.mywaytech.puppiessearchclient.controllers;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.mywaytech.puppiessearchclient.R;
import com.mywaytech.puppiessearchclient.controllers.fragments.ReportFragment;
import com.mywaytech.puppiessearchclient.controllers.fragments.WallFragment;
import com.mywaytech.puppiessearchclient.domain.UserSessionManager;
import com.mywaytech.puppiessearchclient.utils.AlertDialogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by m.maigua on 4/13/2016.
 */
public class MainActivity extends BaseActivity {

    public static final String EXTRA_EMAIL_FOR_AUTH = "com.mywaytech.puppiessearchclient.extras.extra_email_for_auth";
    public static final String EXTRA_GOOGLE_CLIENT ="com.mywaytech.puppiessearchclient.extras.extra_google_client";

    private static final int PET_REQUEST = 0;

    private FloatingActionButton btn_add_dog;

    private WallFragment mWallFragment;
    private String mTypeDialog;
private GoogleApiClient mGoogleApiClient;

    public static Intent newIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.main_activity_title);

        btn_add_dog = (FloatingActionButton) findViewById(R.id.fab_add_dog_wall);
        btn_add_dog.setOnClickListener(addListener);


        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        mWallFragment = WallFragment.newInstance();
        ft.replace(R.id.container, mWallFragment).commit();

        mGoogleApiClient=UserSessionManager.getInstance(this).getGoogleApiClient();
        if(mGoogleApiClient!=null){
            mGoogleApiClient.connect();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item_layout, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public View.OnClickListener addListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = ReportActivity.newIntent(MainActivity.this);
            startActivityForResult(intent, PET_REQUEST);
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_filter:
                showDialog();
                return true;
            case R.id.action_contact_us:
                intent = ContactUsActivity.newIntent(MainActivity.this);
                startActivity(intent);
                return true;
            case R.id.action_map:
                intent = MapActivity.newIntent(MainActivity.this);
                startActivity(intent);
                return true;
            case R.id.menuoptions_settings:
                intent = new Intent(MainActivity.this, PreferencesActivity.class);
                startActivity(intent);
                return true;
            case R.id.menuoptions_logout:
                onBackPressed();
                return true;
            case R.id.action_login:
                intent =new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mGoogleApiClient!=null){
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onBackPressed() {
        logout();

    }

    public void logout() {
        new AlertDialogUtils.Builder(this)
                .setResourceMessage(R.string.close_session_message)
                .setTitle(R.string.close_session_title)
                .setPositiveText(R.string.btn_yes)
                .setPositiveButtonListener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(mGoogleApiClient!=null){
                            mGoogleApiClient.connect();
                            mGoogleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                                @Override
                                public void onConnected(@Nullable Bundle bundle) {

                                    FirebaseAuth.getInstance().signOut();
                                    if(mGoogleApiClient.isConnected()) {
                                        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                                            @Override
                                            public void onResult(@NonNull Status status) {
                                                if (status.isSuccess()) {
                                                    Log.d("", "User Logged out");

                                                    Intent mainIntent = new Intent().setClass(
                                                            MainActivity.this, LoginActivity.class);
                                                    startActivity(mainIntent);
                                                    finish();
                                                }
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onConnectionSuspended(int i) {
                                    Log.d("", "Google API Client Connection Suspended");
                                }
                            });
                        }else {
                            Intent mainIntent = new Intent().setClass(
                                    MainActivity.this, LoginActivity.class);
                            startActivity(mainIntent);
                            finish();
                        }
                    }
                })
                .setNegativeText(R.string.btn_no)
                .show();

    }

    public void showDialog() {

        final ListView mChoiceListView = new ListView(this);
        List<String> optionsFilter = new ArrayList<String>();
        optionsFilter.add(ReportFragment.TYPE_PET_ALL_STRING);
        optionsFilter.add(ReportFragment.TYPE_PET_ADOPTION_STRING);
        optionsFilter.add(ReportFragment.TYPE_PET_LOST_STRING);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, optionsFilter);
        mChoiceListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mChoiceListView.setAdapter(arrayAdapter);

        mChoiceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mTypeDialog = (String) parent.getItemAtPosition(position);
            }
        });

        new AlertDialogUtils.Builder(this)
                .setTitle(R.string.txt_title_choice)
                .setPositiveText(R.string.btn_ok)
                .setCustomView(mChoiceListView)
                .setPositiveButtonListener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mWallFragment.sortList(mTypeDialog);
                    }
                })
                .setIsCancelable(false)
                .setNegativeText(R.string.btn_cancel)
                .setNegativeButtonListener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }


}
