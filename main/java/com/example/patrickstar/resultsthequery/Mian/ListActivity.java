package com.example.patrickstar.resultsthequery.Mian;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.patrickstar.resultsthequery.R;

import org.jsoup.select.Elements;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by PatrickStar on 2018/1/29.
 */

public class ListActivity extends Activity{
    ListView listView;
    ImageView imageView;

    String s;
    private List<String> imgUrl = new LinkedList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);
        Intent intent = getIntent();
        s = intent.getStringExtra("s");
        imgUrl = intent.getStringArrayListExtra("imgUrl");
        System.out.println(imgUrl);
        init();
    }

    private void init() {
        listView = (ListView)findViewById(R.id.listView);
        imageView = (ImageView)findViewById(R.id.imageView);
    }



}
