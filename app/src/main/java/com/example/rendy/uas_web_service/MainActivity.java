package com.example.rendy.uas_web_service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

// http://cariprogram.blogspot.com
// nuramijaya@gmail.com

public class MainActivity extends Activity {
    private JSONObject jObject;
    private String jsonResult ="";
    private String url = "http://192.168.5.22/json/cuaca.php";
    private String url2 = "http://192.168.43.150/json/delkota.php";
    String[] daftarid;
    String[] daftarnama;
    String[] daftarlatitude;
    String[] daftarlongitude;
    Menu menu;
    public static MainActivity ma;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ma=this;
        RefreshList();
    }

    public void RefreshList() {
        try {
            jsonResult = getRequest(url);

            jObject = new JSONObject(jsonResult);
            JSONArray menuitemArray = jObject.getJSONArray("kota");

            daftarid = new String[menuitemArray.length()];
            daftarnama = new String[menuitemArray.length()];
            daftarlatitude = new String[menuitemArray.length()];
            daftarlongitude = new String[menuitemArray.length()];

            for (int i = 0; i < menuitemArray.length(); i++)
            {
                daftarid[i] = menuitemArray.getJSONObject(i).getString("Kota").toString();
                daftarnama[i] = menuitemArray.getJSONObject(i).getString("siang").toString();
                daftarlatitude[i] = menuitemArray.getJSONObject(i).getString("malam").toString();
                daftarlongitude[i] = menuitemArray.getJSONObject(i).getString("dini_hari").toString();
                daftarlongitude[i] = menuitemArray.getJSONObject(i).getString("suhu").toString();
                daftarlongitude[i] = menuitemArray.getJSONObject(i).getString("Kelembapan").toString();
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        ListView ListView01 = (ListView)findViewById(R.id.ListView01);
        ListView01.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, daftarnama));

        ListView01.setSelected(true);
        ListView01.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                final String selectionid = daftarid[arg2];
                final String selectionnama = daftarnama[arg2];
                final String selectionlatitude = daftarlatitude[arg2];
                final String selectionlongitude = daftarlongitude[arg2];
                final CharSequence[] dialogitem = {"Edit", "Delete"};
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Pilih ?");
                builder.setItems(dialogitem, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        switch(item){
                            case 0 :
       /*Intent i = new Intent(getApplicationContext(), EditActivity.class);
       i.putExtra("id", selectionid);
       i.putExtra("nama", selectionnama);
       i.putExtra("latitude", selectionlatitude);
       i.putExtra("longitude", selectionlongitude);
          startActivity(i);*/

                                break;
                            case 1 :
       /*getRequest(url2 + "?id=" + selectionid);
       RefreshList();
       */
                                break;
                        }
                    }
                });
                builder.create().show();
            }});

        ((ArrayAdapter)ListView01.getAdapter()).notifyDataSetInvalidated();

    }

    /**
     * Method untuk Mengirimkan data ke server
     */
    public String getRequest(String Url){

        String sret="";
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(Url);
        try{
            HttpResponse response = client.execute(request);
            sret =request(response);

        }catch(Exception ex){
            Toast.makeText(this,"Gagal "+sret, Toast.LENGTH_SHORT).show();
        }
        return sret;

    }
    /**
     * Method untuk Menerima data dari server
     */
    public static String request(HttpResponse response){
        String result = "";
        try{
            InputStream in = response.getEntity().getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder str = new StringBuilder();
            String line = null;
            while((line = reader.readLine()) != null){
                str.append(line + "\n");
            }
            in.close();
            result = str.toString();
        }catch(Exception ex){
            result = "Error";
        }
        return result;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;

        menu.add(0, 1, 0, "Tambah").setIcon(android.R.drawable.btn_plus);
        menu.add(0, 2, 0, "Refresh").setIcon(android.R.drawable.ic_menu_rotate);
        menu.add(0, 3, 0, "Exit").setIcon(android.R.drawable.ic_menu_close_clear_cancel);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                //Intent i = new Intent(MainActivity.this, AddActivity.class);
                //startActivity(i);
                return true;
            case 2:
                RefreshList();
                return true;
            case 3:
                finish();
                return true;
        }
        return false;
    }
}

