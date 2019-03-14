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
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
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
    Map<String, List<String>> map = new HashMap<>();

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
                if(map.keySet().contains(split[11])){
                    List<String> values = map.get(split[11]);
                    values.add(split[12]);
                }else{
                    List<String> values = new ArrayList<>();
                    values.add(split[12]);
                    map.put(split[11], values);
                }

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
        Log.d(TAG,"fill Spinner");
        Spinner spinner = findViewById(R.id.spinner);
        ArrayList list = new ArrayList<>();
        for(String s : map.keySet())
        {
            list.add(s);
        }
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, list);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Spinner s = findViewById(R.id.spinner);
                    ArrayList newList = new ArrayList<>();
                    for(Car c : cars)
                    {
                        if(c.getProducer().toLowerCase().startsWith(String.valueOf(s.getSelectedItem()).toLowerCase()))
                        {
                            newList.add(c);
                        }
                    }
                    ListView viewById = findViewById(R.id.listView);
                    bindAdapterToListView(viewById, newList);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


    }

    private void fillDialogSpinner(Spinner spinner)
    {
        Log.d(TAG,"fill Spinner");
        ArrayList list = new ArrayList<>();
        for(String s : map.keySet())
        {
            list.add(s);
        }
        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, list);
        spinner.setAdapter(adapter);

//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Spinner s = findViewById(R.id.spinner);
//                String chosen = String.valueOf(s.getSelectedItem()).toLowerCase();
//                List<String> models = map.get(chosen);
//
//                ArrayAdapter m_adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, models);
//                Spinner spinner1 = findViewById(R.id.dialog_spinner);
//                spinner1.setAdapter(m_adapter);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });
    }


    private void openDialog() {
//        Log.d(TAG,"open Dialog");
//        ExampleDialog exampleDialog = new ExampleDialog();
//
//        fillDialogSpinner(exampleDialog);
//
//        exampleDialog.show(getSupportFragmentManager(), "example dialog");

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_layout, null);
        dialogBuilder.setView(dialogView);

        Spinner dialogSpinner = (Spinner) dialogView .findViewById(R.id.dialog_spinner);

        fillDialogSpinner(dialogSpinner);

        dialogBuilder.show();
    }
}
