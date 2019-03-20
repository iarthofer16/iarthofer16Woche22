package net.htlgrieskirchen.pos3.iarthofer16woche22;

import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "AndroidDemo";
    ArrayList<Car> cars = new ArrayList<>();
    ArrayList<Car> backup = new ArrayList<>();
    MyAdapter listViewAdapter;
    Map<String, List<String>> map = new HashMap<>();

    ListView listView;
    Spinner main_producers;
    Spinner dialog_producers;
    Spinner dialog_models;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        readAssets();
        listView = findViewById(R.id.listView);
        listViewAdapter = new MyAdapter(this, cars);
        listView.setAdapter(listViewAdapter);

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
        main_producers = findViewById(R.id.spinner);
        dialog_producers = findViewById(R.id.dialog_spinner);
        dialog_models = findViewById(R.id.dialog_model);

        fillSpinner();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });
    }

    private InputStream getInputStreamForAsset(String filename) {
        AssetManager assets = getAssets();
        try {
            return assets.open(filename);
        } catch (IOException e) {
            return null;
        }
    }

    private void readAssets() {
        Log.d(TAG, "readAssets: Read");
        InputStream in = getInputStreamForAsset("cars.csv");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
        String line;
        try {
            line = bufferedReader.readLine();
            while ((line = bufferedReader.readLine()) != null) {
                String[] split = line.split(",");
                backup.add(new Car(split[1], split[2], split[11], split[12]));
                if (map.keySet().contains(split[11])) {
                    map.get(split[11]).add(split[12]);

                } else {
                    List<String> values = new ArrayList<>();
                    values.add(split[12]);
                    map.put(split[11], values);
                }
                writeCsv();

            }
        } catch (Exception e) {

        }

        Log.d(TAG, "readAssets: sort");
        Collections.sort(cars);
    }

    private void doMySearch(String string) {
        cars.clear();
        for (Car c : backup) {
            if (c.getFirstName().toLowerCase().startsWith(string.toLowerCase()) || c.getLastName().toLowerCase().startsWith(string.toLowerCase())) {
                cars.add(c);
            }
        }
        listViewAdapter.notifyDataSetChanged();
    }

    private void fillSpinner() {
        Log.d(TAG, "fill Spinner");
        ArrayList list = new ArrayList<>();
        for (String s : map.keySet()) {
            list.add(s);
        }

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, list);
        main_producers.setAdapter(adapter);

        main_producers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Spinner s = findViewById(R.id.spinner);
                cars.clear();
                for (Car c : backup) {
                    if (c.getProducer().toLowerCase().startsWith(String.valueOf(s.getSelectedItem()).toLowerCase())) {
                        cars.add(c);
                    }
                }

                listViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


    }




    private void openDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_layout, null);
        dialogBuilder.setView(dialogView);

        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText firstName = dialogView.findViewById(R.id.dialog_firstName);
                EditText lastName = dialogView.findViewById(R.id.dialog_lastName);
                Spinner producer = dialogView.findViewById(R.id.dialog_spinner);
                Spinner model = dialogView.findViewById(R.id.dialog_model);

                Car newCar = new Car(String.valueOf(firstName.getText()), String.valueOf(lastName.getText()), String.valueOf(producer.getSelectedItem()), String.valueOf(model.getSelectedItem()));
                backup.add(newCar);
                cars.add(newCar);

                listViewAdapter.notifyDataSetChanged();
            }
        });

        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });


        fillDialogSpinner();

        AlertDialog ad = dialogBuilder.create();
        ad.show();

        writeCsv();

    }

    private void fillDialogSpinner() {
        Log.d(TAG, "fill Spinner");
        ArrayList producers = new ArrayList<>();
        for (String s : map.keySet()) {
            producers.add(s);
        }
        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, producers);
        dialog_producers.setAdapter(adapter);

        dialog_producers.setSelection(0);


//        dialog_producers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String chosen = String.valueOf(dialog_producers.getSelectedItem());
//                List<String> models = map.get(chosen);
//
//                ArrayAdapter m_adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, models);
//                dialog_models.setAdapter(m_adapter);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });
    }


    private void writeCsv() {
        Log.d(TAG, "writeCsv");

        String fileName = "cars.csv";

        try (PrintWriter out = new PrintWriter(new OutputStreamWriter(openFileOutput(fileName, MODE_APPEND)))) {
            out.println(cars.get(cars.size() - 1).toString());
            out.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
