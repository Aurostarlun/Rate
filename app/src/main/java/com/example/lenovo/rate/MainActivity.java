package com.example.lenovo.rate;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements Runnable{

    EditText rmb;
    TextView show;
    Handler handler;

    private final String TAG = "MainActivity";
    private float dollarRate = 0.1f;
    private float euroRate = 0.2f;
    private float wonRate = 0.3f;
    private String updateDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rmb = (EditText) findViewById(R.id.rmb);
        show = (TextView)findViewById(R.id.showOut);

        //获取SP里保存的数据
        SharedPreferences sharedPreferences = getSharedPreferences("myrate", Activity.MODE_PRIVATE);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        dollarRate = sharedPreferences.getFloat("dollar_rate",0.0f);
        euroRate = sharedPreferences.getFloat("euro_rate",0.0f);
        wonRate = sharedPreferences.getFloat("won_rate",0.0f);
        updateDate = sharedPreferences.getString("update_date","");

        //获取当前系统时间
        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        final String todayStr = sdf.format(today);

        Log.i(TAG,"onCreate:sp dollarRate="+dollarRate);
        Log.i(TAG,"onCreate:sp euroRate="+euroRate);
        Log.i(TAG,"onCreate:sp wonRate="+wonRate);
        Log.i(TAG,"onCreate:sp updateDate="+updateDate);
        Log.i(TAG,"onCreate:sp todayStr="+todayStr);

        //判断时间
        if(!todayStr.equals(updateDate)){
            //开启子线程
            Thread t = new Thread(this);
            t.start();
            Log.i(TAG,"onCreate: 需要更新");
        }else{
            Log.i(TAG,"onCreate: 不需要更新");
        }

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg)
            {
                if (msg.what==5){
                   Bundle bdl = (Bundle) msg.obj;
                   dollarRate = bdl.getFloat("dollar-rate");
                   euroRate = bdl.getFloat("euro-rate");
                   wonRate = bdl.getFloat("won-rate");

                   Log.i(TAG,"hanleMessage:dollarRate"+dollarRate);
                   Log.i(TAG,"hanleMessage:euroRate"+euroRate);
                   Log.i(TAG,"hanleMessage:wonRate"+wonRate);

                   //保存更新的日期
                    SharedPreferences sharedPreferences = getSharedPreferences("myrate", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("update_date",todayStr);
                    editor.putFloat("dollar_rate",dollarRate);
                    editor.putFloat("euro_rate",euroRate);
                    editor.putFloat("won_rate",wonRate);
                    editor.apply();

                   Toast.makeText(MainActivity.this,"汇率已更新",Toast.LENGTH_SHORT).show();
                }
                super.handleMessage(msg);
            }
        };

    }

    public void onClick(View btn){
        //获取用户输入内容
        String str = rmb.getText().toString();
        float r = 0;
        if(str.length()>0){
            r = Float.parseFloat(str);
        }
        else{
            Toast.makeText(this, "请输入金额", Toast.LENGTH_SHORT).show();
        }

        float val = 0;
        if(btn.getId()==R.id.btn_dollar){
            val = r / 6.7F;
            show.setText(String.format("%.4f",dollarRate));
        }
        else if(btn.getId()==R.id.btn_euro){
            val = r / 11;
            show.setText(String.format("%.4f",euroRate));
        }
        else if(btn.getId()==R.id.btn_won){
            val = r * 500;
            show.setText(String.format("%.4f",wonRate));
        }

    }

    public void openCount(View btn)
    {
        Log.i("open", "openCount: ");
        Intent count = new Intent(this,CountActivity.class);
        startActivity(count);
    }

    public void openConfig(View btn)
    {
        Log.i("open", "openConfig: ");
        Intent config = new Intent(this, ConfigActivity.class);
        config.putExtra("dollar_rate_key",dollarRate);
        config.putExtra("euro_rate_key",euroRate);
        config.putExtra("won_rate_key",wonRate);

        Log.i(TAG,"openConfig:dollarRate"+dollarRate);
        Log.i(TAG,"openConfig:euroRate"+euroRate);
        Log.i(TAG,"openConfig:wonRate"+wonRate);

        //startActivity(config);
        startActivityForResult(config,1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.rate,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId()==R.id.menu_set){
            Log.i("open", "openConfig: ");
            Intent config = new Intent(this, ConfigActivity.class);
            config.putExtra("dollar_rate_key",dollarRate);
            config.putExtra("euro_rate_key",euroRate);
            config.putExtra("won_rate_key",wonRate);

            Log.i(TAG,"openConfig:dollarRate"+dollarRate);
            Log.i(TAG,"openConfig:euroRate"+euroRate);
            Log.i(TAG,"openConfig:wonRate"+wonRate);

            //startActivity(config);
            startActivityForResult(config,1);
        }else if (item.getItemId()==R.id.open_list){
            //打开列表窗口
            Intent list = new Intent(this,MyList2Activity.class);
            startActivity(list);
            Log.i(TAG,"openRateListActivity");
        }

        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode==1&&resultCode==2){
            /*bdl.putFloat("key_dollar",newDollar);
        bdl.putFloat("key_euro",newEuro);
        bdl.putFloat("key_dollar",newWon);*/
            Bundle bundle = data.getExtras();
            dollarRate = bundle.getFloat("key_dollar",0.1f);
            euroRate = bundle.getFloat("key_euro",0.1f);
            wonRate = bundle.getFloat("key_won",0.1f);
            Log.i(TAG,"onActivityResult:dollarRate"+dollarRate);
            Log.i(TAG,"onActivityResult:euroRate"+euroRate);
            Log.i(TAG,"onActivityResult:wonRate"+wonRate);

            //将新设置的汇率写到SP里
            SharedPreferences sharedPreferences = getSharedPreferences("myrate", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putFloat("dollar_rate",dollarRate);
            editor.putFloat("euro_rate",euroRate);
            editor.putFloat("won_rate",wonRate);
            editor.commit();
            Log.i(TAG,"onActivityResult:数据已保存到sharedPreferences");

        }

        super.onActivityResult(requestCode,resultCode,data);
    }

    public void openWeb(View btn)
    {
        Log.i("open", "openWeb: ");
        Intent web = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.bilibili.com"));
        startActivity(web);
    }

    @Override
    public void run() {
        Log.i(TAG,"run:run().....");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //用于保存获取的汇率
        Bundle bundle = new Bundle();

        //获取网络数据
        /*URL url = null;
        try {
            url = new URL("http://www.usd-cny.com/bankofchina.htm");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            InputStream in = http.getInputStream();

            String html = inputStream2String(in);
            Log.i(TAG,"run:html="+html);
            Document doc = Jsoup.parse(html);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        Document doc = null;
        try {
            doc = Jsoup.connect("http://www.usd-cny.com/bankofchina.htm").get();
            //doc = Jsoup.parse(html);
            Log.i(TAG,"run:"+doc.title());
            Elements tables = doc.getElementsByTag("table");
            /*int i = 1;
            for(Element table:tables){
                Log.i(TAG,"run:table["+i+"]="+table);
                i++;
            }*/

            Element table1 = tables.get(0);
            //Log.i(TAG,"run:table6" + table6);
            //获取TD中的数据
            Elements tds = table1.getElementsByTag("td");
            for (int i=0;i<tds.size();i+=6){
                Element td1 = tds.get(i);
                Element td2 = tds.get(i+5);
                Log.i(TAG,"run:"+td1.text()+"==>"+td2.text());
                String str1 = td1.text();
                String val = td2.text();

                if ("美元".equals(str1)){
                    bundle.putFloat("dollar-rate",Float.parseFloat(val)/100);
                }else if ("欧元".equals(str1)){
                    bundle.putFloat("euro-rate",Float.parseFloat(val)/100);
                }else if ("韩元".equals(str1)){
                    bundle.putFloat("won-rate",Float.parseFloat(val)/100);
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        //bundle中保存所获取的汇率

        //获取Msg对象，用于返回主线程
        Message msg = handler.obtainMessage(5);
        //msg.what = 5;
        //msg.obj = "Hello from run();";
        msg.obj = bundle;
        handler.sendMessage(msg);

    }

    private String inputStream2String(InputStream inputStream) throws IOException {
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(inputStream,"gb2312");
        for (; ; ){
            int rsz = in.read(buffer,0,buffer.length);
            if (rsz<0){
                break;
            }
            out.append(buffer,0,rsz);
        }
        return out.toString();
    }
}
