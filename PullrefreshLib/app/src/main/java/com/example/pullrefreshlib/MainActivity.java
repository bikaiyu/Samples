package com.example.pullrefreshlib;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    private PullToRefreshListView mPullToRefreshListView;

    MyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAdapter = new MyAdapter(new ArrayList<String>());
        mPullToRefreshListView = (PullToRefreshListView)findViewById(R.id.pull_to_refresh_listview);
        mPullToRefreshListView.setAdapter(mAdapter);
        mPullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> listViewPullToRefreshBase) {
                new GetDataTask().execute();
            }
        });
    }

    private class GetDataTask extends AsyncTask<Void,Void,ArrayList<String>>{

        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            try{
                Thread.sleep(1000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            return Cheeses.randomList(20);
        }

        @Override
        protected void onPostExecute(ArrayList<String> arrayList) {
            mAdapter = new MyAdapter(arrayList);
            mPullToRefreshListView.setAdapter(mAdapter);
            mPullToRefreshListView.onRefreshComplete();
            super.onPostExecute(arrayList);
        }
    }

    private class MyAdapter extends BaseAdapter{

        private ArrayList<String> data;

        private MyAdapter(ArrayList<String> data) {
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public String getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return data.get(position).hashCode();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = getLayoutInflater().inflate(R.layout.item,parent,false);
            }

            String result = data.get(position);
            TextView textView = (TextView)convertView.findViewById(R.id.text);
            textView.setText(result);

            return convertView;
        }
    }
}
