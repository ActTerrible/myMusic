package com.wm.remusic.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bilibili.magicasakura.utils.ThemeUtils;
import com.wm.remusic.MainApplication;
import com.wm.remusic.R;
import com.wm.remusic.adapter.MenuItemAdapter;
import com.wm.remusic.dialog.CardPickerDialog;
import com.wm.remusic.fragment.BitSetFragment;
import com.wm.remusic.fragment.FriendFragment;
import com.wm.remusic.fragment.MainFragment;
import com.wm.remusic.fragment.TimingFragment;
import com.wm.remusic.fragmentnet.TabNetPagerFragment;
import com.wm.remusic.service.MusicPlayer;
import com.wm.remusic.uitl.L;
import com.wm.remusic.uitl.ThemeHelper;
import com.wm.remusic.uitl.ToastUtil;
import com.wm.remusic.widget.CustomViewPager;
import com.wm.remusic.widget.SplashScreen;

import java.util.ArrayList;
import java.util.List;

import static com.wm.remusic.MainApplication.context;

public class MainActivity extends BaseActivity implements CardPickerDialog.ClickListener, View.OnClickListener {
    private ActionBar ab;
    private ImageView barnet, barmusic, barfriends, search;
    private ArrayList<ImageView> tabs = new ArrayList<>();
    private DrawerLayout drawerLayout;
    private ListView mLvLeftMenu;
    private long time = 0;
    private SplashScreen splashScreen;

    public void onCreate(Bundle savedInstanceState) {
//        if(getIntent().getIntExtra("from",0)==0){
//            splashScreen = new SplashScreen(this);
//            splashScreen.show(R.drawable.login,
//                    SplashScreen.SLIDE_LEFT);
//        }
        super.onCreate(savedInstanceState);
        L.e("main","new");
        setContentView(R.layout.activity_main);
        getWindow().setBackgroundDrawableResource(R.color.background_material_light_1);

        barnet = (ImageView) findViewById(R.id.bar_net);
        barmusic = (ImageView) findViewById(R.id.bar_music);
//        barfriends = (ImageView) findViewById(R.id.bar_friends);
        search = (ImageView) findViewById(R.id.bar_search);
        drawerLayout = (DrawerLayout) findViewById(R.id.fd);
        mLvLeftMenu = (ListView) findViewById(R.id.id_lv_left_menu);

        setToolBar();
        setViewPager();
        setUpDrawer();
//        HandlerUtil.getInstance(this).postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                splashScreen.removeSplashScreen();
//            }
//        }, 3000);

    }


    private void setToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setTitle("");

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    private void setViewPager() {
        tabs.add(barnet);
        tabs.add(barmusic);
//        tabs.add(barfriends);
        final CustomViewPager customViewPager = (CustomViewPager) findViewById(R.id.main_viewpager);
        final MainFragment mainFragment = new MainFragment();
        final TabNetPagerFragment tabNetPagerFragment = new TabNetPagerFragment();
        final FriendFragment friendFragment = new FriendFragment();
        CustomViewPagerAdapter customViewPagerAdapter = new CustomViewPagerAdapter(getSupportFragmentManager());
        customViewPagerAdapter.addFragment(tabNetPagerFragment);
        customViewPagerAdapter.addFragment(mainFragment);
//        customViewPagerAdapter.addFragment(friendFragment);
        customViewPager.setAdapter(customViewPagerAdapter);
        customViewPager.setCurrentItem(1);
        customViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switchTabs(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        barnet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customViewPager.setCurrentItem(0);
            }
        });
        barmusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customViewPager.setCurrentItem(1);
            }
        });
//        barfriends.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                customViewPager.setCurrentItem(2);
//            }
//        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(MainActivity.this, NetSearchWordsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                MainActivity.this.startActivity(intent);
            }
        });
    }


    private void setUpDrawer() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.nav_header_main, mLvLeftMenu, false);
        mLvLeftMenu.addHeaderView(view);
        mLvLeftMenu.setAdapter(new MenuItemAdapter(this));
        TextView textView = (TextView) view.findViewById(R.id.name);
        if (textView != null) {
            L.e("main", "text不为空");
            textView.setOnClickListener(this);
        }
        mLvLeftMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 1:
                        CardPickerDialog dialog = new CardPickerDialog();
                        dialog.setClickListener(MainActivity.this);
                        dialog.show(getSupportFragmentManager(), "theme");
                        drawerLayout.closeDrawers();

                        break;
//                    case 2:
//                        TimingFragment fragment3 = new TimingFragment();
//                        fragment3.show(getSupportFragmentManager(), "timing");
//                        drawerLayout.closeDrawers();
//
//                        break;
//                    case 3:
//                        BitSetFragment bfragment = new BitSetFragment();
//                        bfragment.show(getSupportFragmentManager(), "bitset");
//                        drawerLayout.closeDrawers();
//                        break;
                    case 2:
                        drawerLayout.closeDrawers();
                        Intent intent = new Intent(MainActivity.this, simpleActivity.class);
                        MainApplication.setSimple(true);
                        startActivity(intent);
                        finish();
                        break;
                    case 4:
                        if (MusicPlayer.isPlaying()) {
                            MusicPlayer.playOrPause();
                        }
                        unbindService();
                        finish();
                        drawerLayout.closeDrawers();
                        break;
                    case 3:
                        Intent intent1 = new Intent(context, UserActivity.class);
                        startActivity(intent1);
                        finish();
                        break;
                }
            }
        });
    }


    private void switchTabs(int position) {
        for (int i = 0; i < tabs.size(); i++) {
            if (position == i) {
                tabs.get(i).setSelected(true);
            } else {
                tabs.get(i).setSelected(false);
            }
        }
    }


    @Override
    public void onConfirm(int currentTheme) {
        L.e("MainActivity","执行MAIN当中的");
        //如果传过来的主题和当前的不一样
        if (ThemeHelper.getTheme(MainActivity.this) != currentTheme) {
            //让首选项存住当前主题
            ThemeHelper.setTheme(MainActivity.this, currentTheme);
            //刷新UI
            ThemeUtils.refreshUI(MainActivity.this, new ThemeUtils.ExtraRefreshable() {
                        @Override
                        public void refreshGlobal(Activity activity) {
                            //对于全局设置，只需要做一次
                            if (Build.VERSION.SDK_INT >= 21) {
                                final MainActivity context = MainActivity.this;
                                //创建一个实体
                                ActivityManager.TaskDescription taskDescription = new ActivityManager.TaskDescription(null, null, ThemeUtils.getThemeAttrColor(context, android.R.attr.colorPrimary));
                                setTaskDescription(taskDescription);
                                //设置状态栏颜色
                                getWindow().setStatusBarColor(ThemeUtils.getColorById(context, R.color.theme_color_primary));
                            }
                        }

                        @Override
                        public void refreshSpecificView(View view) {
                        }
                    }
            );
        }
        //改变歌曲主题
        changeTheme();
    }

    @Override
    public void onClick(View v) {
        L.e("main", "onClick");
        Intent intent = new Intent(context, UserActivity.class);
        startActivity(intent);
    }

    static class CustomViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();

        public CustomViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment) {
            mFragments.add(fragment);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case android.R.id.home: //Menu icon
                drawerLayout.openDrawer(Gravity.LEFT);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        L.e("main","destro");
//        splashScreen.removeSplashScreen();
    }

    /**
     * 双击返回桌面
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - time > 1000)) {
                Toast.makeText(this, "再按一次返回桌面", Toast.LENGTH_SHORT).show();
                time = System.currentTimeMillis();
            } else {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
            }
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment != null) {
                    fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        moveTaskToBack(true);
        // System.exit(0);
        // finish();
    }

    private long FirstTime = 0;

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                long SecondTime = System.currentTimeMillis();
                if (SecondTime - FirstTime > 2000) {
                    ToastUtil.showToast(context, "再按一次退出应用");
                    FirstTime = SecondTime;
                    return true;
                } else {
                    System.exit(0);
                }
                break;
        }
        return super.onKeyUp(keyCode, event);
    }

}
