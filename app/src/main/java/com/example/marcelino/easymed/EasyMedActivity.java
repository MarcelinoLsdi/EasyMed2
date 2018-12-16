package com.example.marcelino.easymed;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ChangedPackages;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marcelino.easymed.GPSService.GPSTracker;
import com.example.marcelino.easymed.Remote.RetrofitConfig;
import com.example.marcelino.easymed.model.Capabilities;
import com.example.marcelino.easymed.model.Capability;
import com.example.marcelino.easymed.model.CapabilityData;
import com.example.marcelino.easymed.model.Collector;
import com.example.marcelino.easymed.model.ContextData;
import com.example.marcelino.easymed.model.DataHelper;
import com.example.marcelino.easymed.model.Resource;
import com.example.marcelino.easymed.model.ResourceData;
import com.example.marcelino.easymed.model.Resources;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EasyMedActivity extends AppCompatActivity {

    private Capability capability;
    private Capabilities capabilities;
    private Resources resources2;

    private List<Resource> resources = new ArrayList<>();

    GPSTracker gps = new GPSTracker(EasyMedActivity.this);

    EditText nome;
    EditText distancia;
    ListView result;
    Button btnBuscarMed;

    String capabilityName;

    DataHelper dataHelper;

    ArrayAdapter<CapabilityData> resourceArrayAdapter;
    List<CapabilityData> capabilityDatas;

    final int MY_ACCESS_FINE_LOCATION = 9;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easy_med);

        nome = findViewById(R.id.editText);
        distancia = findViewById(R.id.editText2);
        result = (ListView) findViewById(R.id.listItem);
        btnBuscarMed = findViewById(R.id.button);
        capabilityDatas = new ArrayList<>();

        requestPermissionForAccessLocation();
        askPermissions();

        btnBuscarMed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    buscar();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        result.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                CapabilityData mCapabilityData = (CapabilityData) adapterView.getItemAtPosition(i);

                Uri gmmIntentUri = Uri.parse("google.navigation:q=" +
                        mCapabilityData.getResource().getLat() + "," +
                        mCapabilityData.getResource().getLon() + "&mode=d");

                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                }

            }
        });


    }


    private void buscar() throws Exception {
        capabilityName = nome.getText().toString().toUpperCase().trim();

        Call<Resources> call = new RetrofitConfig()
                .getFarmacia()
                .buscarFarmaciaPeloMedicamentoEPosicao(capabilityName,
                        gps.getLocation().getLatitude(),
                        gps.getLocation().getLongitude(),
                        Integer.parseInt(distancia.getText().toString()));
        try {
            call.enqueue(new Callback<Resources>() {
                @Override
                public void onResponse(Call<Resources> call, Response<Resources> response) {
                    resources2 = response.body();

                    if (resources2 != null) {
                        resources.clear();
                        for (Resource resource : resources2.getResources()) {
                            resources.add(resource);
                        }
                    }

                    if (!resources.isEmpty()) {
                        Collector collector = new Collector();
                        List<String> uuids = new ArrayList<>();
                        for (Resource resource : resources) {
                            uuids.add(resource.getUuid());
                        }

                        collector.getUuids().addAll(uuids);
                        collector.getCapabilities().addAll(Arrays.asList(capabilityName));
                        try {
                            buscarUltimoDado(collector);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Resources> call, Throwable t) {
                    Log.e("MedicamentoService", "Erro ao buscar Med: " + t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void buscarUltimoDado(Collector collector) throws Exception {

        Call<DataHelper> call = new RetrofitConfig().getFarmacia().buscarUltimoDado(collector);

        try {
            call.enqueue(new Callback<DataHelper>() {
                @Override
                public void onResponse(Call<DataHelper> call, Response<DataHelper> response) {
                    dataHelper = new DataHelper();
                    if (response.body() != null) {
                        dataHelper = response.body();

                        if (dataHelper != null && dataHelper.getResources() != null) {


                            capabilityDatas = new ArrayList<>();

                            for (ContextData contextData : dataHelper.getResources()) {
                                Map<String, List<Map<String, Object>>> capability = contextData.getCapabilities();

                                List<Map<String, Object>> dataSensor = capability.get(capabilityName);

                                ResourceData resourceData = new ResourceData();

                                resourceData.setUuid(contextData.getUuid());
                                if (dataSensor != null) {
                                    for (Map<String, Object> ob : dataSensor) {
                                        CapabilityData capabilityData = new CapabilityData(ob);
                                        if (capabilityData.getResource() != null && capabilityData.getResource().getLat() != null) {
                                            if (capabilityData.getValue() != null) {
                                                for (Resource resource : resources) {
                                                    if (resource.getUuid().equals(capabilityData.getResource().getUuid())) {
                                                        capabilityData.setDistance(resource.getDistance());
                                                        capabilityDatas.add(capabilityData);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                            }

                            if (!capabilityDatas.isEmpty()) {
                                try {

                                    resourceArrayAdapter = new ArrayAdapter<CapabilityData>(getApplicationContext(),
                                            android.R.layout.simple_list_item_1,
                                            capabilityDatas);

                                    result.setAdapter(resourceArrayAdapter);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(getApplicationContext(), "Não encontrado", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Não encontrado", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<DataHelper> call, Throwable t) {
                    t.printStackTrace();
                }
            });

        } catch (
                Exception e)

        {
            e.printStackTrace();
        }

    }

    private void requestPermissionForAccessLocation() {

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_ACCESS_FINE_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

    }

    protected void askPermissions() {
        String[] permissions = {
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE"
        };
        int requestCode = 200;
        requestPermissions(permissions, requestCode);
    }

}
