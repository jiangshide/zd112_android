package com.android.zd112.ui.activity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.android.zd112.R;
import com.android.zd112.data.City;
import com.android.zd112.data.db.DBHelper;
import com.android.zd112.ui.adapter.CommAdapter;
import com.android.zd112.ui.view.MyLetterListView;
import com.android.zd112.utils.Constant;
import com.android.zd112.utils.FileUtils;
import com.android.zd112.utils.LocationUtils;
import com.android.zd112.utils.LogUtils;
import com.android.zd112.utils.PingYinUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class AddressActivity extends BaseActivity implements AbsListView.OnScrollListener, MyLetterListView.OnTouchingLetterChangedListener {

    private BaseAdapter adapter;
    private CommAdapter<City> resultListAdapter;
    private ListView personList, resultList;
    private TextView overlay;
    private MyLetterListView letterListView; // A-Z listview
    private HashMap<String, Integer> alphaIndexer;
    private String[] sections;
    private Handler handler;
    private OverlayThread overlayThread;
    private ArrayList<City> allCity_lists;
    private ArrayList<City> city_lists;
    private ArrayList<City> city_result;
    private ArrayList<String> city_history;
    private EditText searchEdit;
    private TextView tv_noresult;

    private String currentCity;
    private int locateProcess = 1;
    private boolean isNeedFresh;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_address);
        personList = findViewById(R.id.list_view);
        overlay = findViewById(R.id.overlay);
        resultList = findViewById(R.id.search_result);
        searchEdit = findViewById(R.id.searchEdit);
        tv_noresult = findViewById(R.id.tv_noresult);
        letterListView = findViewById(R.id.MyLetterListView01);
    }

    @Override
    protected void setListener() {
        searchEdit.addTextChangedListener(this);
        letterListView
                .setOnTouchingLetterChangedListener(this);
        personList.setOnItemClickListener(this);
        personList.setOnScrollListener(this);
        resultList.setOnItemClickListener(this);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        letterListView.setLetter(getResStringArr(R.array.letter));
        allCity_lists = new ArrayList<City>();
        city_result = new ArrayList<City>();
        city_history = new ArrayList<String>();
        alphaIndexer = new HashMap<String, Integer>();
        handler = new Handler();
        overlayThread = new OverlayThread();
        isNeedFresh = true;
        locateProcess = 1;
        resultList.setAdapter(resultListAdapter = new CommAdapter<City>(this, city_result, R.layout.list_item) {
            @Override
            protected void convertView(int position, View item, City city) {
                ((TextView) get(item, R.id.name)).setText(city.name);
            }
        });
        cityInit();
//        hisCityInit();

        alphaIndexer = new HashMap<String, Integer>();
        sections = new String[allCity_lists.size()];
        for (int i = 0; i < allCity_lists.size(); i++) {
            // 当前汉语拼音首字母
            String currentStr = getAlpha(allCity_lists.get(i).getPinyi());
            // 上一个汉语拼音首字母，如果不存在为" "
            String previewStr = (i - 1) >= 0 ? getAlpha(allCity_lists.get(i - 1)
                    .getPinyi()) : " ";
            if (!previewStr.equals(currentStr)) {
                String name = getAlpha(allCity_lists.get(i).getPinyi());
                alphaIndexer.put(name, i);
                sections[i] = name;
            }
        }
//        personList.setAdapter(adapter = new ListAdapter(this, allCity_lists, Constant.getHotCityArr(), city_history));
        personList.setAdapter(adapter = new CommAdapter<City>(this,allCity_lists,R.layout.frist_list_item) {
            @Override
            protected void convertView(int position, View item, City city) {

            }
        }.setViewTypeCount(5).setItemViewType(4));
        LocationUtils.INSTANCE.setLocationListener(new LocationUtils.AmapLocationListener() {
            @Override
            public void onLocation(AMapLocation aMapLocation, Location location) {
                if (aMapLocation != null) {
                    if (!isNeedFresh) {
                        return;
                    }
                    isNeedFresh = false;

                    String city = aMapLocation.getCity();
                    if (city == null) {
                        locateProcess = 3; // 定位失败
                        personList.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        return;
                    }
                    currentCity = city;
                    locateProcess = 2;// 定位成功
                    personList.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        super.onTextChanged(s, start, before, count);
        if (s.toString() == null || "".equals(s.toString())) {
            letterListView.setVisibility(View.VISIBLE);
            personList.setVisibility(View.VISIBLE);
            resultList.setVisibility(View.GONE);
            tv_noresult.setVisibility(View.GONE);
        } else {
            city_result.clear();
            letterListView.setVisibility(View.GONE);
            personList.setVisibility(View.GONE);
            getResultCityList(s.toString());
            if (city_result.size() <= 0) {
                tv_noresult.setVisibility(View.VISIBLE);
                resultList.setVisibility(View.GONE);
            } else {
                tv_noresult.setVisibility(View.GONE);
                resultList.setVisibility(View.VISIBLE);
                resultListAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        super.onItemClick(parent, view, position, id);
        switch (parent.getId()) {
            case R.id.search_result:
                show("parent:" + parent.getId() + " | " + city_result.get(position).getName());
                break;
            case R.id.list_view:
                if (position >= 4) {
                    show(allCity_lists.get(position).getName());
                }
                break;
        }
    }

    private void cityInit() {
        City city = new City("定位", "0"); // 当前定位城市
        allCity_lists.add(city);
        city = new City("最近", "1"); // 最近访问的城市
        allCity_lists.add(city);
        city = new City("热门", "2"); // 热门城市
        allCity_lists.add(city);
        city = new City("全部", "3"); // 全部城市
        allCity_lists.add(city);
        city_lists = getCityList();
        allCity_lists.addAll(city_lists);
    }

//    private void hisCityInit() {
//        SQLiteDatabase db = mDbHelper.getReadableDatabase();
//        Cursor cursor = db.rawQuery(
//                "select * from recentcity order by date desc limit 0, 3", null);
//        while (cursor.moveToNext()) {
//            city_history.add(cursor.getString(1));
//        }
//        cursor.close();
//        db.close();
//    }

    private ArrayList<City> getCityList() {
        ArrayList<City> list = new ArrayList<City>();
        DBHelper dbHelper = new DBHelper(this);
        try {
            dbHelper.createDataBase();
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery("select * from city", null);
            City city;
            while (cursor.moveToNext()) {
                city = new City(cursor.getString(1), cursor.getString(2));
                LogUtils.e("-------city~name:", city.getName(), " | pinyin:", city.getPinyi());
                String content = city.getName() + "|" + city.getPinyi() + "\n";
                FileUtils.appendText(new File("/sdcard/city.txt"), content);
                list.add(city);
            }
            cursor.close();
            db.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Collections.sort(list, comparator);
        return list;
    }

    private void getResultCityList(String keyword) {
        try {
            DBHelper dbHelper = new DBHelper(this);
            dbHelper.createDataBase();
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery(
                    "select * from city where name like \"%" + keyword
                            + "%\" or pinyin like \"%" + keyword + "%\"", null);
            City city;
            LogUtils.e("length = " + cursor.getCount());
            while (cursor.moveToNext()) {
                city = new City(cursor.getString(1), cursor.getString(2));
                city_result.add(city);
            }
            cursor.close();
            db.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Collections.sort(city_result, comparator);
    }

    /**
     * a-z排序
     */
    @SuppressWarnings("rawtypes")
    Comparator comparator = new Comparator<City>() {
        @Override
        public int compare(City lhs, City rhs) {
            String a = lhs.getPinyi().substring(0, 1);
            String b = rhs.getPinyi().substring(0, 1);
            int flag = a.compareTo(b);
            if (flag == 0) {
                return a.compareTo(b);
            } else {
                return flag;
            }
        }
    };

    @Override
    public void onTouchingLetterChanged(String s) {
        isScroll = false;
        if (alphaIndexer.get(s) != null) {
            int position = alphaIndexer.get(s);
            personList.setSelection(position);
            overlay.setText(s);
            overlay.setVisibility(View.VISIBLE);
            handler.removeCallbacks(overlayThread);
            // 延迟一秒后执行，让overlay为不可见
            handler.postDelayed(overlayThread, 1000);
        }
    }

    public class ListAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;
        private List<City> list;
        private List<City> hotList;
        private List<String> hisCity;
        final int VIEW_TYPE = 5;

        public ListAdapter(Context context, List<City> list,
                           List<City> hotList, List<String> hisCity) {
            this.inflater = LayoutInflater.from(context);
            this.list = list;
            this.context = context;
            this.hotList = hotList;
            this.hisCity = hisCity;
            alphaIndexer = new HashMap<String, Integer>();
            sections = new String[list.size()];
            for (int i = 0; i < list.size(); i++) {
                // 当前汉语拼音首字母
                String currentStr = getAlpha(list.get(i).getPinyi());
                // 上一个汉语拼音首字母，如果不存在为" "
                String previewStr = (i - 1) >= 0 ? getAlpha(list.get(i - 1)
                        .getPinyi()) : " ";
                if (!previewStr.equals(currentStr)) {
                    String name = getAlpha(list.get(i).getPinyi());
                    alphaIndexer.put(name, i);
                    sections[i] = name;
                }
            }
        }

        @Override
        public int getViewTypeCount() {
            return VIEW_TYPE;
        }

        @Override
        public int getItemViewType(int position) {
            return position < 4 ? position : 4;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        ViewHolder holder;

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final TextView city;
            int viewType = getItemViewType(position);
            if (viewType == 0) { // 定位
                convertView = inflater.inflate(R.layout.frist_list_item, null);
                TextView locateHint = (TextView) convertView
                        .findViewById(R.id.locateHint);
                city = (TextView) convertView.findViewById(R.id.lng_city);
                city.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (locateProcess == 2) {

                            Toast.makeText(getApplicationContext(),
                                    city.getText().toString(),
                                    Toast.LENGTH_SHORT).show();
                        } else if (locateProcess == 3) {
                            locateProcess = 1;
                            personList.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            isNeedFresh = true;
                            currentCity = "";
                        }
                    }
                });
                ProgressBar pbLocate = (ProgressBar) convertView
                        .findViewById(R.id.pbLocate);
                if (locateProcess == 1) { // 正在定位
                    locateHint.setText("正在定位");
                    city.setVisibility(View.GONE);
                    pbLocate.setVisibility(View.VISIBLE);
                } else if (locateProcess == 2) { // 定位成功
                    locateHint.setText("当前定位城市");
                    city.setVisibility(View.VISIBLE);
                    city.setText(currentCity);
                    pbLocate.setVisibility(View.GONE);
                } else if (locateProcess == 3) {
                    locateHint.setText("未定位到城市,请选择");
                    city.setVisibility(View.VISIBLE);
                    city.setText("重新选择");
                    pbLocate.setVisibility(View.GONE);
                }
            } else if (viewType == 1) { // 最近访问城市
                convertView = inflater.inflate(R.layout.recent_city, null);
                GridView rencentCity = (GridView) convertView
                        .findViewById(R.id.recent_city);
                rencentCity
                        .setAdapter(new CommAdapter<String>(context, this.hisCity, R.layout.item_city) {
                            @Override
                            protected void convertView(int position, View item, String s) {
                                ((TextView) get(item, R.id.city)).setText(s);
                            }
                        });
                rencentCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {

                        Toast.makeText(getApplicationContext(),
                                city_history.get(position), Toast.LENGTH_SHORT)
                                .show();

                    }
                });
                TextView recentHint = (TextView) convertView
                        .findViewById(R.id.recentHint);
                recentHint.setText("最近访问的城市");
            } else if (viewType == 2) {
                convertView = inflater.inflate(R.layout.recent_city, null);
                GridView hotCity = (GridView) convertView
                        .findViewById(R.id.recent_city);
                hotCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {

                        Toast.makeText(getApplicationContext(),
                                Constant.getHotCityArr().get(position).getName(),
                                Toast.LENGTH_SHORT).show();

                    }
                });
                hotCity.setAdapter(new CommAdapter<City>(context, this.hotList, R.layout.item_city) {
                    @Override
                    protected void convertView(int position, View item, City city) {
                        ((TextView) get(item, R.id.city)).setText(city.name);
                    }
                });
                TextView hotHint = (TextView) convertView
                        .findViewById(R.id.recentHint);
                hotHint.setText("热门城市");
            } else if (viewType == 3) {
                convertView = inflater.inflate(R.layout.total_item, null);
            } else {
                if (convertView == null) {
                    convertView = inflater.inflate(R.layout.list_item, null);
                    holder = new ViewHolder();
                    holder.alpha = (TextView) convertView
                            .findViewById(R.id.alpha);
                    holder.name = (TextView) convertView
                            .findViewById(R.id.name);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                if (position >= 1) {
                    holder.name.setText(list.get(position).getName());
                    String currentStr = getAlpha(list.get(position).getPinyi());
                    String previewStr = (position - 1) >= 0 ? getAlpha(list
                            .get(position - 1).getPinyi()) : " ";
                    if (!previewStr.equals(currentStr)) {
                        holder.alpha.setVisibility(View.VISIBLE);
                        holder.alpha.setText(currentStr);
                    } else {
                        holder.alpha.setVisibility(View.GONE);
                    }
                }
            }
            return convertView;
        }

        private class ViewHolder {
            TextView alpha; // 首字母标题
            TextView name; // 城市名字
        }
    }

    private boolean mReady;

    private boolean isScroll = false;

    // 设置overlay不可见
    private class OverlayThread implements Runnable {
        @Override
        public void run() {
            overlay.setVisibility(View.GONE);
        }
    }

    // 获得汉语拼音首字母
    private String getAlpha(String str) {
        if (str == null) {
            return "#";
        }
        if (str.trim().length() == 0) {
            return "#";
        }
        char c = str.trim().substring(0, 1).charAt(0);
        // 正则表达式，判断首字母是否是英文字母
        Pattern pattern = Pattern.compile("^[A-Za-z]+$");
        if (pattern.matcher(c + "").matches()) {
            return (c + "").toUpperCase();
        } else if (str.equals("0")) {
            return "定位";
        } else if (str.equals("1")) {
            return "最近";
        } else if (str.equals("2")) {
            return "热门";
        } else if (str.equals("3")) {
            return "全部";
        } else {
            return "#";
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_TOUCH_SCROLL
                || scrollState == SCROLL_STATE_FLING) {
            isScroll = true;
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        if (!isScroll) {
            return;
        }
        if (mReady) {
            String text;
            String name = allCity_lists.get(firstVisibleItem).getName();
            String pinyin = allCity_lists.get(firstVisibleItem).getPinyi();
            if (firstVisibleItem < 4) {
                text = name;
            } else {
                text = PingYinUtil.converterToFirstSpell(pinyin)
                        .substring(0, 1).toUpperCase();
            }
            overlay.setText(text);
            overlay.setVisibility(View.VISIBLE);
            handler.removeCallbacks(overlayThread);
            // 延迟一秒后执行，让overlay为不可见
            handler.postDelayed(overlayThread, 1000);
        }
    }
}
