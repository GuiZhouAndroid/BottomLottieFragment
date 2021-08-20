package com.zs.itking.BottomLottieFragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.zs.itking.BottomLottieFragment.R;
import com.zs.itking.BottomLottieFragment.bottom.BottomBarItem;
import com.zs.itking.BottomLottieFragment.bottom.BottomBarLayout;
import com.zs.itking.BottomLottieFragment.fragment.BottomCategoryFragment;
import com.zs.itking.BottomLottieFragment.fragment.BottomHomeFragment;
import com.zs.itking.BottomLottieFragment.fragment.BottomMineFragment;
import com.zs.itking.BottomLottieFragment.fragment.BottomShopFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * created by on 2021/8/20
 * 描述：
 *
 * @author ZSAndroid
 * @create 2021-08-20-15:55
 */
public class BottomLottieActivity extends AppCompatActivity {
    private static final String TAG = "BottomLottieActivity";
    private ViewPager mVpContent;//左右滑动
    private BottomBarLayout mBottomBarLayout;//底部导航
    //切换的4个Fragment页面
    private Fragment[] fragments = new Fragment[]{new BottomHomeFragment(), new BottomCategoryFragment(), new BottomShopFragment(), new BottomMineFragment()};
    //创建Fragment集合，ViewPager适配器遍历绑定数组fragments
    List<Fragment> fragmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_lottie_activity);
        initView();
        initData();
        initListener();
    }

    /**
     * 初始化View
     */
    public void initView() {
        mVpContent = findViewById(R.id.vp_content);
        mBottomBarLayout = findViewById(R.id.bbl);
    }

    /**
     * Fragment数组全部添加到List<Fragment>集合中
     */
    public void initData() {
        fragmentList.addAll(Arrays.asList(fragments));
    }

    /**
     * 初始化底部导航
     */
    public void initListener() {
        //ViewPager设置MyAdapter适配器，遍历List<Fragment>集合，填充Fragment页面
        mVpContent.setAdapter(new MyAdapter(getSupportFragmentManager()));
        //底部导航加载ViewPager
        mBottomBarLayout.setViewPager(mVpContent);
        //position：底部导航索引，unreadNum：页签未读数
        mBottomBarLayout.setUnread(0, 20);//设置第一个页签的未读数为20,
        mBottomBarLayout.setUnread(1, 1001);//设置第二个页签的未读数
        mBottomBarLayout.showNotify(2);//设置第三个页签显示提示的小红点
        mBottomBarLayout.setMsg(3, "未登录");//设置第四个页签显示NEW提示文字


        mBottomBarLayout.setOnItemSelectedListener(new BottomBarLayout.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final BottomBarItem bottomBarItem, int previousPosition, final int currentPosition) {
                Log.i("MainActivity", "position: " + currentPosition);
                if (currentPosition == 0) {
                    //如果是第一个，即首页
                    if (previousPosition == currentPosition) {
                        bottomBarItem.setSelectedIcon(R.mipmap.tab_loading);//更换成加载图标
                    }
                }
            }
        });
    }

    class MyAdapter extends FragmentStatePagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_demo, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        switch (id) {
//            case R.id.action_clear_unread:
//                mBottomBarLayout.setUnread(0, 0);
//                mBottomBarLayout.setUnread(1, 0);
//                break;
//            case R.id.action_clear_notify:
//                mBottomBarLayout.hideNotify(2);
//                break;
//            case R.id.action_clear_msg:
//                mBottomBarLayout.hideMsg(3);
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }

}

