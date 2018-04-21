package com.jxaummd.dormcontrol;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Fragment mCurrentFragment;
    private FragmentManager mFragManager = getSupportFragmentManager();
    private String sMain;
    private String sSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppPref mPref = AppPref.getInstance();

        setContentView(R.layout.activity_main);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (mCurrentFragment == null) {
            this.sMain = "main";
            this.sSet = "set";
        }

        if (mPref.isFirstRun()) {
            addFragment(sSet);
            Toast.makeText(MainActivity.this,
                    R.string.app_first_run_notifaction,
                    Toast.LENGTH_LONG)
                    .show();
        } else {
            MqttRequest mReq = MqttRequest.getInstance(
                    mPref.getPref(AppPref.tControlAddr),
                    AppPref.deviceId,
                    mPref.getPref(AppPref.tUserName),
                    mPref.getPref(AppPref.tPassWord));
            addFragment(sMain);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.action_main)
            if (mFragManager.findFragmentByTag(sMain) != mCurrentFragment)
                addFragment(sMain);

        if (id == R.id.action_settings)
            if (mFragManager.findFragmentByTag(sSet) != mCurrentFragment)
                addFragment(sSet);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void addFragment(String mCurFragName) {
        //判断这个标签是否存在Fragment对象于栈中,如果存在则返回，不存在返回null
        Fragment mFragment = mFragManager.findFragmentByTag(mCurFragName);
        FragmentTransaction mTransaction = mFragManager.beginTransaction();
        //如果这个fragment不存于栈中
        if (mFragment == null) {

            // 添加显示不同的Fragment
            if (mCurFragName.equals(sMain))
                mFragment = new MainFragment();
            else if (mCurFragName.equals(sSet))
                mFragment = new SetFragment();

            if (mCurrentFragment != null)
                mTransaction.hide(mCurrentFragment);

            mTransaction.add(R.id.frag_space, mFragment, mCurFragName).commit();

        } else {
            //如果添加的Fragment已经存在,fragment不为空，直接显示fragment
            mTransaction.show(mFragment).hide(mCurrentFragment).commit();

        }
        mCurrentFragment = mFragment;
    }

}
