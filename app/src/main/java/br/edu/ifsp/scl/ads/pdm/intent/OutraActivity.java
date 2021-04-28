package br.edu.ifsp.scl.ads.pdm.intent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import br.edu.ifsp.scl.ads.pdm.intent.databinding.ActivityOutraBinding;

public class OutraActivity extends AppCompatActivity {

    private ActivityOutraBinding activityOutraBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityOutraBinding = ActivityOutraBinding.inflate(getLayoutInflater());

        setContentView(activityOutraBinding.getRoot());

        Bundle parametrosBundle = getIntent().getExtras();
        if(parametrosBundle != null){
            String parametro = parametrosBundle.getString(MainActivity.PARAMETRO, "");

            activityOutraBinding.recebidoTv.setText(parametro);
        }


        Log.v(getString(R.string.app_name) + "/" + getLocalClassName(), "OnCreate: Iniciando ciclo completo");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v(getString(R.string.app_name) + "/" + getLocalClassName(), "OnStart: Iniciando ciclo visivel");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v(getString(R.string.app_name) + "/" + getLocalClassName(), "OnResumo: Iniciando ciclo foreground");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(getString(R.string.app_name) + "/" + getLocalClassName(), "OnPause: Finalizando ciclo foreground");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v(getString(R.string.app_name) + "/" + getLocalClassName(), "OnStop: Finalizando ciclo visivel");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v(getString(R.string.app_name) + "/" + getLocalClassName(), "OnDestroy: Finalizando ciclo completo");
    }

    public void onClick(View view) {
        finish(); // Fecha a activity explicitamente  -> sequencia Onpause, Stop, Destroy
    }
}