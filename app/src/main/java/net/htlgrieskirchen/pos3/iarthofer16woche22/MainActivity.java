package net.htlgrieskirchen.pos3.iarthofer16woche22;

import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "AndroidDemo";
    ArrayList<Car> cars = new ArrayList<>();
    MyAdapter mAdapter;
    Map<String, Integer> map = new HashMap<>();

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
        fillSpinner1();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               openDialog();
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
                map.put(split[11], 1);
            }
        }
        catch (Exception e)
        {

        }

        Log.d(TAG,"readAssets: sort");
        Collections.sort(cars);
    }
    private void bindAdapterToListView (ListView lv, ArrayList list)
    {
        mAdapter = new MyAdapter(this, list);
        lv.setAdapter(mAdapter);
    }

    private void doMySearch(String string)
    {
        Log.d(TAG,"readAssets: Search");
        ArrayList newList = new ArrayList<>();
        for(Car c : cars)
        {
            if(c.getFirstName().toLowerCase().startsWith(string.toLowerCase())||c.getLastName().toLowerCase().startsWith(string.toLowerCase()))
            {
                newList.add(c);
            }
        }
        ListView view = findViewById(R.id.listView);
        bindAdapterToListView(view,newList);
    }

    private void fillSpinner1()
    {
        Spinner spinner = findViewById(R.id.spinner);
        ArrayList list = new ArrayList<>();
        for(String s : map.keySet())
        {
            list.add(s);
        }
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, list);
        spinner.setAdapter(adapter);

        spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }


    private void createDialog()
    {


    }
}
