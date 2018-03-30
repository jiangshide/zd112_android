package com.android.zd112.fragment;

import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by etongdai on 2017/11/20.
 */

public class BaseFragment extends Fragment{

    protected View view;

    protected View viewId(int id){
        return view.findViewById(id);
    }
}
