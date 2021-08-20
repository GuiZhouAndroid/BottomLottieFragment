package com.zs.itking.BottomLottieFragment.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.zs.itking.BottomLottieFragment.R;

/**
 * created by on 2021/8/20
 * 描述：
 *
 * @author ZSAndroid
 * @create 2021-08-20-15:01
 */
public class BottomShopFragment extends Fragment {
    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.bottom_fragment_shop,container,false);
        return view;
    }
}
