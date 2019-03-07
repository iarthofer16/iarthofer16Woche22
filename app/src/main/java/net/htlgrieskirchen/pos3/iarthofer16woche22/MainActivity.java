package net.htlgrieskirchen.pos3.iarthofer16woche22;

import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "AndroidDemo";
    List cars = new ArrayList<>();
    ArrayAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        readAssets();
        ListView lv = findViewById(R.id.listView);
        bindAdapterToListView(lv,cars);
        SearchView s = findViewById(R.id.searchView);
        s.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(TAG, "doMySearch");
                doMySearch(newText);
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                doMySearch(query);
                return true;
            }
        });
    }
    private InputStream getInputStreamForAsset (String filename)
    {
        AssetManager assets = getAssets();
        try {
            return assets.open(filename);
        }
        catch (IOException e)
        {
            return null;
        }
    }
    private void readAssets ()
    { Log.d(TAG,"readAssets: Read");
        InputStream in = getInputStreamForAsset("cars.csv");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
        String line;
        try {
            line = bufferedReader.readLine();
            while((line = bufferedReader.readLine()) != null)
            {
                String[] split = line.split(",");
                cars.add(new Car(split[1],split[2], split[11],split[12]));

            }
        }
        catch (Exception e)
        {

        }

        Collections.sort(cars);
    }
    private void bindAdapterToListView (ListView lv, List list)
    {
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,cars);
        lv.setAdapter(mAdapter);
    }
    private void doMySearch(String string)
    {
        Log.d(TAG,"readAssets: Search");
        ArrayList newList = new ArrayList<>();
        for(Car c : cars)
        {
            if(c.getFirstname().toLowerCase().startsWith(string.toLowerCase())||c.getLastname().toLowerCase().startsWith(string.toLowerCase()))
            {
                newList.add(c);
            }
        }
        ListView view = findViewById(R.id.listView);
        bindAdapterToListView(view,newList);
    }
}
