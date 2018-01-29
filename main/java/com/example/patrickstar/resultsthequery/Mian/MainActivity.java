package com.example.patrickstar.resultsthequery.Mian;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.patrickstar.resultsthequery.R;
import com.example.patrickstar.resultsthequery.Utils.HandlerUtil;
import com.example.patrickstar.resultsthequery.Utils.Md5Util;
import com.example.patrickstar.resultsthequery.Utils.OkhttpUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends Activity implements HandlerUtil.OnReceiveMessageListener, View.OnClickListener{

    EditText Student_ID_editText;
    EditText Password_editText;
    EditText ValidateCode_editText;
    ImageView ValidateCodeImg;
    Button Login;

    String Student_ID = null;
    String Password = null;
    String ValidateCode = null;

    public String s ;

    private OkHttpClient okHttpClient;
    private HandlerUtil.HandlerHolder handlerHolder;


    private static final int SUCCESS = 101;
    private static final int FALL = 102;
    private static final int Student_ID_null = 103;
    private static final int Password_null = 104;
    private static final int ValidateCode_null = 105;
    private static final int Student_ID_and_Password_error = 106;
    private static final int ValidateCode_error = 107;

    final String LoginURL = "http://jw.fzrjxy.com/_data/index_LOGIN.aspx";
    final String ValidateCodeURL = "http://jw.fzrjxy.com/sys/ValidateCode.aspx";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
//        getValidateCode();
        getValidateCode();

        Student_ID_editText.setText("14023152");
        Password_editText.setText("ztx20sxmx");

        // 监听
        Login.setOnClickListener(this);
        ValidateCodeImg.setOnClickListener(this);

    }

    private void init() {
        Student_ID_editText = (EditText) findViewById(R.id.Student_ID_editText);
        Password_editText = (EditText) findViewById(R.id.Password_editText);
        ValidateCode_editText = (EditText) findViewById(R.id.ValidateCode_editText);
        ValidateCodeImg = (ImageView) findViewById(R.id.ValidateCodeImg);
        Login = (Button) findViewById(R.id.Login);

        handlerHolder = new HandlerUtil.HandlerHolder(this);
        okHttpClient = new OkHttpClient();
    }

//    获取验证码
    private void getValidateCode() {
        if(OkhttpUtil.isNetworkAvailable(this) == false)
        {
            Toast.makeText(this, "网络不可用！", Toast.LENGTH_SHORT).show();
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkhttpUtil okhttpUtil = OkhttpUtil.getInstance();
                Response response = okhttpUtil.getDataSynFromNet(ValidateCodeURL);
                if (response.isSuccessful()) {
                    byte[] byte_image = new byte[0];
                    try {
                        byte_image = response.body().bytes();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //通过handler更新UI
                    Message message = handlerHolder.obtainMessage();
                    message.obj = byte_image;
                    message.what = SUCCESS;
                    Log.i("info_handler", "handler");
                    handlerHolder.sendMessage(message);

                    Headers headers = response.headers();
                    Log.d("info_headers", "header " + headers);
                    List<String> cookies = headers.values("Set-Cookie");
                    String session = cookies.get(0);
                    Log.d("info_cookies", "onResponse-size: " + cookies);

                    s = session.substring(0, session.indexOf(";"));
                    Log.i("info_s", "session is  :" + s);
                }
            }
        }).start();
    }

    // 获取验证码
//    private void getValidateCode() {
//        String updateTime = String.valueOf(System.currentTimeMillis()); // 在需要重新获取更新的图片时调用
//        Glide.with(this)
//                .load(ValidateCodeURL)
//                .signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
//                .into(ValidateCodeImg);
//    }


//    // 获取验证码
//    private void getValidateCode() {
//        OkHttpClient client = new OkHttpClient();
//        //创建一个Request
//        Request request = new Request.Builder()
//                .get()
//                .url(ValidateCodeURL)
//                .build();
//        //通过client发起请求
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.i("info_callFailure",e.toString());
//            }
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if (response.isSuccessful()) {
//                    byte[] byte_image =  response.body().bytes();
//                    //通过handler更新UI
//                    Message message = handlerHolder.obtainMessage();
//                    message.obj = byte_image;
//                    message.what = SUCCESS;
//                    Log.i("info_handler","handler");
//                    handlerHolder.sendMessage(message);
//
//                    //获取session的操作，session放在cookie头，且取出后含有“；”，取出后为下面的 s （也就是jsesseionid）
//                    Headers headers = response.headers();
//                    Log.d("info_headers", "header " + headers);
//                    List<String> cookies = headers.values("Set-Cookie");
//                    String session = cookies.get(0);
//                    Log.d("info_cookies", "onResponse-size: " + cookies);
//
//                    s = session.substring(0, session.indexOf(";"));
//                    Log.i("info_s", "session is  :" + s);
//                }
//            }
//        });
//    }

    // 登录事件
    private void LoginServer(String Student_ID, String Password, String ValidateCode) {

        String dsdsdsdsdxcxdfgfg = Md5Util.ecodeTwice(Student_ID + Md5Util.ecodeTwice(Password).substring(0,30).toUpperCase()+ "13978").substring(0,30).toUpperCase();
        String fgfggfdgtyuuyyuuckjg = Md5Util.ecodeTwice(Md5Util.ecodeTwice(ValidateCode.toUpperCase()).substring(0,30).toUpperCase()+ "13978").substring(0,30).toUpperCase();

        OkHttpClient client = new OkHttpClient();
        FormBody body = new FormBody.Builder()
                .add("__VIEWSTATE", "dDwtMTMwOTYyOTQ5Mjt0PDtsPGk8MD47aTwxPjtpPDI+O2k8Mz47PjtsPHQ8cDxsPFRleHQ7PjtsPOemj+W3nui9r+S7tuiBjOS4muaKgOacr+WtpumZojs+Pjs7Pjt0PHA8bDxUZXh0Oz47bDxcPHNjcmlwdCB0eXBlPSJ0ZXh0L2phdmFzY3JpcHQiXD4KXDwhLS0KZnVuY3Rpb24gQ2hrVmFsdWUoKXsKIHZhciB2VT0kKCdVSUQnKS5pbm5lckhUTUxcOwogdlU9dlUuc3Vic3RyaW5nKDAsMSkrdlUuc3Vic3RyaW5nKDIsMylcOwogdmFyIHZjRmxhZyA9ICJZRVMiXDsgaWYgKCQoJ3R4dF9hc21jZGVmc2Rkc2QnKS52YWx1ZT09JycpewogYWxlcnQoJ+mhu+W9leWFpScrdlUrJ++8gScpXDskKCd0eHRfYXNtY2RlZnNkZHNkJykuZm9jdXMoKVw7cmV0dXJuIGZhbHNlXDsKfQogZWxzZSBpZiAoJCgndHh0X3Bld2Vyd2Vkc2Rmc2RmZicpLnZhbHVlPT0nJyl7CiBhbGVydCgn6aG75b2V5YWl5a+G56CB77yBJylcOyQoJ3R4dF9wZXdlcndlZHNkZnNkZmYnKS5mb2N1cygpXDtyZXR1cm4gZmFsc2VcOwp9CiBlbHNlIGlmICgkKCd0eHRfc2RlcnRmZ3NhZHNjeGNhZHNhZHMnKS52YWx1ZT09JycgJiYgdmNGbGFnID09ICJZRVMiKXsKIGFsZXJ0KCfpobvlvZXlhaXpqozor4HnoIHvvIEnKVw7JCgndHh0X3NkZXJ0ZmdzYWRzY3hjYWRzYWRzJykuZm9jdXMoKVw7cmV0dXJuIGZhbHNlXDsKfQogZWxzZSB7ICQoJ2RpdkxvZ05vdGUnKS5pbm5lckhUTUw9J1w8Zm9udCBjb2xvcj0icmVkIlw+5q2j5Zyo6YCa6L+H6Lqr5Lu96aqM6K+BLi4u6K+356iN5YCZIVw8L2ZvbnRcPidcOwogcmV0dXJuIHRydWVcO30KfQpmdW5jdGlvbiBTZWxUeXBlKG9iail7CiB2YXIgcz1vYmoub3B0aW9uc1tvYmouc2VsZWN0ZWRJbmRleF0uZ2V0QXR0cmlidXRlKCd1c3JJRCcpXDsKIHZhciB3PW9iai5vcHRpb25zW29iai5zZWxlY3RlZEluZGV4XS5nZXRBdHRyaWJ1dGUoJ1B3ZElEJylcOwogJCgnVUlEJykuaW5uZXJIVE1MPXNcOwogc2VsVHllTmFtZSgpXDsKIGlmKG9iai52YWx1ZT09IlNUVSIpIHsKICAgZG9jdW1lbnQuYWxsLmJ0bkdldFN0dVB3ZC5zdHlsZS5kaXNwbGF5PScnXDsKICAgZG9jdW1lbnQuYWxsLmJ0blJlc2V0LnN0eWxlLmRpc3BsYXk9J25vbmUnXDsKICB9CiBlbHNlIHsKICAgIGRvY3VtZW50LmFsbC5idG5SZXNldC5zdHlsZS5kaXNwbGF5PScnXDsKICAgIGRvY3VtZW50LmFsbC5idG5HZXRTdHVQd2Quc3R5bGUuZGlzcGxheT0nbm9uZSdcOwogIH19CmZ1bmN0aW9uIG9wZW5XaW5Mb2codGhlVVJMLHcsaCl7CnZhciBUZm9ybSxyZXRTdHJcOwpldmFsKCJUZm9ybT0nd2lkdGg9Iit3KyIsaGVpZ2h0PSIraCsiLHNjcm9sbGJhcnM9bm8scmVzaXphYmxlPW5vJyIpXDsKcG9wPXdpbmRvdy5vcGVuKHRoZVVSTCwnd2luS1BUJyxUZm9ybSlcOyAvL3BvcC5tb3ZlVG8oMCw3NSlcOwpldmFsKCJUZm9ybT0nZGlhbG9nV2lkdGg6Iit3KyJweFw7ZGlhbG9nSGVpZ2h0OiIraCsicHhcO3N0YXR1czpub1w7c2Nyb2xsYmFycz1ub1w7aGVscDpubyciKVw7CnBvcC5tb3ZlVG8oKHNjcmVlbi53aWR0aC13KS8yLChzY3JlZW4uaGVpZ2h0LWgpLzIpXDtpZih0eXBlb2YocmV0U3RyKSE9J3VuZGVmaW5lZCcpIGFsZXJ0KHJldFN0cilcOwp9CmZ1bmN0aW9uIHNob3dMYXkoZGl2SWQpewp2YXIgb2JqRGl2ID0gZXZhbChkaXZJZClcOwppZiAob2JqRGl2LnN0eWxlLmRpc3BsYXk9PSJub25lIikKe29iakRpdi5zdHlsZS5kaXNwbGF5PSIiXDt9CmVsc2V7b2JqRGl2LnN0eWxlLmRpc3BsYXk9Im5vbmUiXDt9Cn0KZnVuY3Rpb24gc2VsVHllTmFtZSgpewogICQoJ3R5cGVOYW1lJykudmFsdWU9JE4oJ1NlbF9UeXBlJylbMF0ub3B0aW9uc1skTignU2VsX1R5cGUnKVswXS5zZWxlY3RlZEluZGV4XS50ZXh0XDsKfQp3aW5kb3cub25sb2FkPWZ1bmN0aW9uKCl7Cgl2YXIgc1BDPU1TSUU/d2luZG93Lm5hdmlnYXRvci51c2VyQWdlbnQrd2luZG93Lm5hdmlnYXRvci5jcHVDbGFzcyt3aW5kb3cubmF2aWdhdG9yLmFwcE1pbm9yVmVyc2lvbisnIFNOOk5VTEwnOndpbmRvdy5uYXZpZ2F0b3IudXNlckFnZW50K3dpbmRvdy5uYXZpZ2F0b3Iub3NjcHUrd2luZG93Lm5hdmlnYXRvci5hcHBWZXJzaW9uKycgU046TlVMTCdcOwp0cnl7JCgncGNJbmZvJykudmFsdWU9c1BDXDt9Y2F0Y2goZXJyKXt9CnRyeXskKCd0eHRfYXNtY2RlZnNkZHNkJykuZm9jdXMoKVw7fWNhdGNoKGVycil7fQp0cnl7JCgndHlwZU5hbWUnKS52YWx1ZT0kTignU2VsX1R5cGUnKVswXS5vcHRpb25zWyROKCdTZWxfVHlwZScpWzBdLnNlbGVjdGVkSW5kZXhdLnRleHRcO31jYXRjaChlcnIpe30KfQpmdW5jdGlvbiBvcGVuV2luRGlhbG9nKHVybCxzY3IsdyxoKQp7CnZhciBUZm9ybVw7CmV2YWwoIlRmb3JtPSdkaWFsb2dXaWR0aDoiK3crInB4XDtkaWFsb2dIZWlnaHQ6IitoKyJweFw7c3RhdHVzOiIrc2NyKyJcO3Njcm9sbGJhcnM9bm9cO2hlbHA6bm8nIilcOwp3aW5kb3cuc2hvd01vZGFsRGlhbG9nKHVybCwxLFRmb3JtKVw7Cn0KZnVuY3Rpb24gb3Blbldpbih0aGVVUkwpewp2YXIgVGZvcm0sdyxoXDsKdHJ5ewoJdz13aW5kb3cuc2NyZWVuLndpZHRoLTEwXDsKfWNhdGNoKGUpe30KdHJ5ewpoPXdpbmRvdy5zY3JlZW4uaGVpZ2h0LTMwXDsKfWNhdGNoKGUpe30KdHJ5e2V2YWwoIlRmb3JtPSd3aWR0aD0iK3crIixoZWlnaHQ9IitoKyIsc2Nyb2xsYmFycz1ubyxzdGF0dXM9bm8scmVzaXphYmxlPXllcyciKVw7CnBvcD1wYXJlbnQud2luZG93Lm9wZW4odGhlVVJMLCcnLFRmb3JtKVw7CnBvcC5tb3ZlVG8oMCwwKVw7CnBhcmVudC5vcGVuZXI9bnVsbFw7CnBhcmVudC5jbG9zZSgpXDt9Y2F0Y2goZSl7fQp9CmZ1bmN0aW9uIGNoYW5nZVZhbGlkYXRlQ29kZShPYmopewp2YXIgZHQgPSBuZXcgRGF0ZSgpXDsKT2JqLnNyYz0iLi4vc3lzL1ZhbGlkYXRlQ29kZS5hc3B4P3Q9IitkdC5nZXRNaWxsaXNlY29uZHMoKVw7Cn0KZnVuY3Rpb24gY2hrcHdkKG9iaikgeyAgaWYob2JqLnZhbHVlIT0nJykgIHsgICAgdmFyIHM9bWQ1KGRvY3VtZW50LmFsbC50eHRfYXNtY2RlZnNkZHNkLnZhbHVlK21kNShvYmoudmFsdWUpLnN1YnN0cmluZygwLDMwKS50b1VwcGVyQ2FzZSgpKycxMzk3OCcpLnN1YnN0cmluZygwLDMwKS50b1VwcGVyQ2FzZSgpXDsgICBkb2N1bWVudC5hbGwuZHNkc2RzZHNkeGN4ZGZnZmcudmFsdWU9c1w7fSBlbHNlIHsgZG9jdW1lbnQuYWxsLmRzZHNkc2RzZHhjeGRmZ2ZnLnZhbHVlPW9iai52YWx1ZVw7fSB9ICBmdW5jdGlvbiBjaGt5em0ob2JqKSB7ICBpZihvYmoudmFsdWUhPScnKSB7ICAgdmFyIHM9bWQ1KG1kNShvYmoudmFsdWUudG9VcHBlckNhc2UoKSkuc3Vic3RyaW5nKDAsMzApLnRvVXBwZXJDYXNlKCkrJzEzOTc4Jykuc3Vic3RyaW5nKDAsMzApLnRvVXBwZXJDYXNlKClcOyAgIGRvY3VtZW50LmFsbC5mZ2ZnZ2ZkZ3R5dXV5eXV1Y2tqZy52YWx1ZT1zXDt9IGVsc2UgeyAgICBkb2N1bWVudC5hbGwuZmdmZ2dmZGd0eXV1eXl1dWNramcudmFsdWU9b2JqLnZhbHVlLnRvVXBwZXJDYXNlKClcO319Ly8tLVw+Clw8L3NjcmlwdFw+Oz4+Ozs+O3Q8O2w8aTwxPjs+O2w8dDw7bDxpPDA+Oz47bDx0PHA8bDxUZXh0Oz47bDxcPG9wdGlvbiB2YWx1ZT0nU1RVJyB1c3JJRD0n5a2m44CA5Y+3J1w+5a2m55SfXDwvb3B0aW9uXD4KXDxvcHRpb24gdmFsdWU9J1RFQScgdXNySUQ9J+W3peOAgOWPtydcPuaVmeW4iOaVmei+heS6uuWRmFw8L29wdGlvblw+Clw8b3B0aW9uIHZhbHVlPSdTWVMnIHVzcklEPSfluJDjgIDlj7cnXD7nrqHnkIbkurrlkZhcPC9vcHRpb25cPgpcPG9wdGlvbiB2YWx1ZT0nQURNJyB1c3JJRD0n5biQ44CA5Y+3J1w+6Zeo5oi357u05oqk5ZGYXDwvb3B0aW9uXD4KOz4+Ozs+Oz4+Oz4+O3Q8cDxwPGw8VGV4dDs+O2w86aqM6K+B56CB6ZSZ6K+v77yBXDxiclw+55m75b2V5aSx6LSl77yBOz4+Oz47Oz47Pj47Po2tenwi75+SZUWvIyabrC/X6+1X")
                .add("__VIEWSTATEGENERATOR", "CAA0A5A7")
                .add("typeName", "%D1%A7%C9%FA")
                .add("pcInfo", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36undefined5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36 SN:NULL")
                .add("dsdsdsdsdxcxdfgfg", dsdsdsdsdxcxdfgfg)
                .add("fgfggfdgtyuuyyuuckjg", fgfggfdgtyuuyyuuckjg)
                .add("Sel_Type", "STU")
                .add("txt_asmcdefsddsd", Student_ID)
                .add("txt_pewerwedsdfsdff", Password)
                .add("txt_sdertfgsadscxcadsads",ValidateCode)
                .add("sbtState","")
                .build();

        Request request = new Request.Builder()
                .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                .addHeader("Accept-Encoding", "gzip, deflate")
                .addHeader("Accept-Language", "zh-CN,zh;q=0.9")
                .addHeader("Cache-Control", "max-age=0")
                .addHeader("Connection", "keep-alive")
                .addHeader("Content-Length", "5751")
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Cookie", s)
                .addHeader("Host", "jw.fzrjxy.com")
                .addHeader("Origin", "http://jw.fzrjxy.com")
                .addHeader("Referer", "http://jw.fzrjxy.com/_data/index_LOGIN.aspx")
                .addHeader("Upgrade-Insecure-Requests", "1")
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36")
                .url(LoginURL)
                .post(body)
                .build();

        Call call2 = okHttpClient.newCall(request);
        call2.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("info_call2fail",e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
//                    Log.i("info_call2success",response.body().string());
                    Document doc = Jsoup.parseBodyFragment(response.body().string());
                    Element divLogNote = doc.getElementById("divLogNote");
                    Elements hint = divLogNote.getElementsByTag("font");
                    String hinttext = hint.text();
                    Log.i("1245678945465",hinttext);
                    if (hinttext.equals("帐号或密码不正确！请重新输入。")) {
                        handlerHolder.sendEmptyMessage(Student_ID_and_Password_error);
                    }else if (hinttext.equals("验证码错误！ 登录失败！")){
                        handlerHolder.sendEmptyMessage(ValidateCode_error);
                    }else if (hinttext.equals("正在加载权限数据...")) {
                        Intent intent = new Intent();
                        intent.setClass(MainActivity.this, MenuActivity.class);
                        intent.putExtra("s", s);
                        startActivity(intent);

                    }
                }
                Headers headers = response.headers();
                Log.i("info_respons.headers",headers + "");

            }
        });
    }

    // 点击事件
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Login:
                Student_ID = Student_ID_editText.getText().toString();
                Password = Password_editText.getText().toString();
                ValidateCode = ValidateCode_editText.getText().toString();
                // 为空判断
                if (Student_ID.equals("")) {
                    handlerHolder.sendEmptyMessage(Student_ID_null);
                }else if (Password.equals("")) {
                    handlerHolder.sendEmptyMessage(Password_null);
                }else if (ValidateCode.equals("")) {
                    handlerHolder.sendEmptyMessage(ValidateCode_null);
                }else {
                    LoginServer(Student_ID, Password, ValidateCode);
                }
                break;
            case R.id.ValidateCodeImg:
                getValidateCode();
                break;
        }
    }

    @Override
    public void handlerMessage(Message msg) {
        switch (msg.what){
            case SUCCESS:
                byte[] Picture = (byte[]) msg.obj;
                Bitmap bitmap = BitmapFactory.decodeByteArray(Picture, 0, Picture.length);
                ValidateCodeImg.setImageBitmap(bitmap);
                break;
            case FALL:
                Toast.makeText(MainActivity.this, "网络出现了问题", Toast.LENGTH_SHORT).show();
                break;
            case Student_ID_null:
                Toast.makeText(MainActivity.this, "请输入学号", Toast.LENGTH_SHORT).show();
                break;
            case Password_null:
                Toast.makeText(MainActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                break;
            case ValidateCode_null:
                Toast.makeText(MainActivity.this, "请输入验证码", Toast.LENGTH_SHORT).show();
                break;
            case Student_ID_and_Password_error:
                Toast.makeText(MainActivity.this, "学号或密码不正确！请重新输入。", Toast.LENGTH_SHORT).show();
                getValidateCode();
                break;
            case ValidateCode_error:
                Toast.makeText(MainActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
                getValidateCode();
                break;
        }
    }

}
