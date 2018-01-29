package com.example.patrickstar.resultsthequery.Mian;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.patrickstar.resultsthequery.R;
import com.example.patrickstar.resultsthequery.Utils.HandlerUtil;
import com.example.patrickstar.resultsthequery.Utils.OkhttpUtil;

import org.angmarch.views.NiceSpinner;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by PatrickStar on 2018/1/25 0025.
 */

public class MenuActivity extends Activity implements HandlerUtil.OnReceiveMessageListener, AdapterView.OnItemSelectedListener, View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private NiceSpinner school_year_spinner;
    private NiceSpinner school_term_spinner;
    private RadioGroup scoretype;
    private RadioGroup rangen;
    private RadioGroup coursetype;
    private Button inquiry;

    private HandlerUtil.HandlerHolder handlerHolder;

    private static final int SUCCESS = 201;
    private static final int LOAD = 202;

    private String xn = "";
    private String xq = "0";
    private String SJ = "";
    private String sel_xn = "";
    private String sel_xq = "";
    private String zfx_flag = "0";
    private String SelXNXQ = "0";
    private String Url = "http://jw.fzrjxy.com/xscj/Stu_MyScore_rpt.aspx";
    private String s = null;
    int getitems = 0;

    Elements imgs;

    private List<String> datas = new LinkedList<>();
    private List<String> datas2 = new LinkedList<>();
    private List<String> datas3 = new LinkedList<String>(Arrays.asList("第一学期","第二学期"));
    private List<String> imgUrl = new ArrayList<>();

    final String Stu_MyScore_URL = "http://jw.fzrjxy.com/xscj/Stu_MyScore.aspx";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Intent intent = getIntent();
        s = intent.getStringExtra("s");

        init();
        getStu_MyScorepage();

        school_year_spinner.setOnItemSelectedListener(this);
        school_term_spinner.setOnItemSelectedListener(this);
        inquiry.setOnClickListener(this);
        rangen.setOnCheckedChangeListener(this);
    }

    private void init() {
        school_year_spinner = (NiceSpinner)findViewById(R.id.nice_spinner1);
        school_term_spinner = (NiceSpinner)findViewById(R.id.nice_spinner2);
        scoretype = (RadioGroup)findViewById(R.id.scoretype);
        rangen = (RadioGroup)findViewById(R.id.rangen);
        coursetype = (RadioGroup)findViewById(R.id.coursetype);
        inquiry = (Button)findViewById(R.id.inquiry);
        handlerHolder = new HandlerUtil.HandlerHolder(this);
    }

    // 获取页面数据
    private void getStu_MyScorepage() {
        if(!OkhttpUtil.isNetworkAvailable(this))
        {
            Toast.makeText(this, "网络不可用！", Toast.LENGTH_SHORT).show();
            return;
        }
        OkhttpUtil okhttpUtil = OkhttpUtil.getInstance();
        okhttpUtil.getDataAsynFromNet(Stu_MyScore_URL, s, new OkhttpUtil.MyNetCall()  {
            @Override
            public void success(Call call, Response response) throws IOException {
                Document doc = Jsoup.parseBodyFragment(response.body().string());
                Element vis_xnxq = doc.getElementById("vis_xnxq");
                Elements options = vis_xnxq.select("option");
                getitems = options.size()-1;
                for (Element option : options) {
                    datas.add(option.text().trim());
                    datas2.add(option.attr("value").trim());
                }
                handlerHolder.sendEmptyMessage(LOAD);
                System.out.println(datas+" and "+datas2);
                xn = datas2.get(0);
            }
            @Override
            public void failed(Call call, IOException e) {
                Toast.makeText(MenuActivity.this, "加载失败！", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 获取成绩图片url
    private void getresultsdata() {
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("sel_xq", xq)
                .add("sel_xn", xn)
                .add("SJ", SJ)
                .add("btn_search", "=%BC%EC%CB%F7")
                .add("SelXNXQ", SelXNXQ)
                .add("zfx_flag", zfx_flag)
                .add("zxf", "0")
                .build();
        System.out.println(body);
        Request request = new Request.Builder()
                .addHeader("Cookie", s)
                .url(Url)
                .post(body)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("info_callfail",e.toString());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Document doc = Jsoup.parseBodyFragment(response.body().string());
                imgs = doc.getElementsByTag("img");
                for (Element img : imgs) {
                    imgUrl.add(imgs.attr("src").trim());
                }
                handlerHolder.sendEmptyMessage(SUCCESS);
            }
        });

    }

    // Spinnner事件监听
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.nice_spinner1:
                Toast.makeText(MenuActivity.this,datas.get(i),Toast.LENGTH_LONG).show();
//                niceSpinner2.setEnabled(true);
                xn = datas2.get(i);
                break;
            case R.id.nice_spinner2:
                Toast.makeText(MenuActivity.this,datas3.get(i),Toast.LENGTH_LONG).show();
                xq = Integer.toString(i);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void handlerMessage(Message msg) {
        switch (msg.what){
            case SUCCESS:
                Intent intent = new Intent();
                intent.setClass(MenuActivity.this, ListActivity.class);
                intent.putExtra("s", s);
                intent.putStringArrayListExtra("imgUrl", (ArrayList<String>) imgUrl);
                startActivity(intent);
                break;
            case LOAD:
                school_year_spinner.attachDataSource(datas);
                school_term_spinner.attachDataSource(datas3);
                break;
        }
    }

    // Button监听
    @Override
    public void onClick(View view) {
        int _scoretype = scoretype.getCheckedRadioButtonId();
        int _rangen = rangen.getCheckedRadioButtonId();
        int _coursetype = coursetype.getCheckedRadioButtonId();
        switch (view.getId()) {
            case R.id.inquiry:

                //成绩类型
                if (_scoretype == R.id.yx_score){
                    Toast.makeText(MenuActivity.this,"有效成绩",Toast.LENGTH_SHORT).show();
                    SJ = "1";
                }
                if (_scoretype == R.id.ys_score){
                    Toast.makeText(MenuActivity.this,"原始成绩",Toast.LENGTH_SHORT).show();
                    SJ = "0";
                }
                // 成绩范围
                if (_rangen == R.id.rxyl){
                    Toast.makeText(MenuActivity.this,"入学以来",Toast.LENGTH_SHORT).show();
                    sel_xn = "";
                    sel_xq = "";
                    SelXNXQ = "0";
                }
                if (_rangen == R.id.xn){
                    Toast.makeText(MenuActivity.this,"学年",Toast.LENGTH_SHORT).show();
                    sel_xn = xn;
                    sel_xq = "";
                    SelXNXQ = "1";
                }
                if (_rangen == R.id.xq){
                    Toast.makeText(MenuActivity.this,"学期",Toast.LENGTH_SHORT).show();
                    sel_xn = xn;
                    sel_xq= xq;
                    SelXNXQ = "2";
                }
                // 课程类型
                if (_coursetype == R.id.required){
                    Toast.makeText(MenuActivity.this,"主修",Toast.LENGTH_SHORT).show();
                    zfx_flag = "0";
                }
                if (_coursetype == R.id.elective){
                    Toast.makeText(MenuActivity.this,"辅修",Toast.LENGTH_SHORT).show();
                    zfx_flag = "1";
                }
                System.out.println(Url + "?sel_xn="+ sel_xn + "&sel_xq=" + sel_xq + "&SJ=" + SJ + "&btn_search=%BC%EC%CB%F7&SelXNXQ=" + SelXNXQ + "&zfx_flag="+zfx_flag+ "&zxf=0");
                getresultsdata();
                break;
        }
    }

    // radioGroup事件监听
    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (rangen.getCheckedRadioButtonId()) {
            case  R.id.rxyl:
                school_year_spinner.setEnabled(false);
                school_term_spinner.setEnabled(false);
                school_year_spinner.setTextColor(ContextCompat.getColor(MenuActivity.this,R.color.gray));
                school_term_spinner.setTextColor(ContextCompat.getColor(MenuActivity.this,R.color.gray));
                break;
            case R.id.xn:
                school_year_spinner.setEnabled(true);
                school_term_spinner.setEnabled(false);
                school_year_spinner.setTextColor(ContextCompat.getColor(MenuActivity.this,R.color.black));
                school_term_spinner.setTextColor(ContextCompat.getColor(MenuActivity.this,R.color.gray));
                break;
            case R.id.xq:
                school_year_spinner.setEnabled(true);
                school_term_spinner.setEnabled(true);
                school_year_spinner.setTextColor(ContextCompat.getColor(MenuActivity.this,R.color.black));
                school_term_spinner.setTextColor(ContextCompat.getColor(MenuActivity.this,R.color.black));
                break;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
