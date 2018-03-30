package com.android.zd112.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.zd112.R;
import com.android.zd112.data.CircleListData;
import com.android.zd112.ui.adapter.CommAdapter;
import com.android.zd112.ui.view.player.media.VideoPlayer;
import com.android.zd112.ui.view.player.media.VideoPlayerNormal;
import com.android.zd112.utils.Constant;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by etongdai on 2017/11/17.
 */

public class CircleFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    private ListView circleListView;
    private CommAdapter<CircleListData> circleListDataCommAdapter;
    private List<CircleListData> listData;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setView(R.layout.tab_fragment_circle);
//        topView("圈子");
        circleListView = viewId(R.id.circleListView);
    }

    @Override
    protected void setListener() {
        circleListView.setOnItemClickListener(this);
        circleListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                VideoPlayer.onScrollAutoTiny(view, firstVisibleItem, visibleItemCount, totalItemCount);
            }
        });
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        listData = new ArrayList<>();
        for (int i = 0; i < Constant.videoUrls[0].length; i++) {
            CircleListData circleListData = new CircleListData();
            circleListData.name = Constant.videoTitles[0][i];
            circleListData.url = Constant.videoUrls[0][i];
            circleListData.thumb = Constant.videoThumbs[0][i];
            listData.add(circleListData);
        }
        circleListView.setAdapter(new CommAdapter<CircleListData>(getActivity(), listData, R.layout.tab_fragment_circle_item) {
            @Override
            protected void convertView(int position, View item, CircleListData circleListData) {
                VideoPlayerNormal circleListViewItemVideo = get(item, R.id.circleListViewItemVideo);
                circleListViewItemVideo.setUp(circleListData.url, VideoPlayer.SCREEN_WINDOW_LIST, circleListData.name);
                Glide.with(item.getContext()).load(circleListData.thumb).into(circleListViewItemVideo.thumbImageView);
                circleListViewItemVideo.positionInList = position;
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onPause() {
        super.onPause();
        VideoPlayer.releaseAllVideos();
    }
}
