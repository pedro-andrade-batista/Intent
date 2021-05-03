package br.edu.ifsp.scl.ads.pdm.intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import br.edu.ifsp.scl.ads.pdm.intent.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding activityMainBinding;

    public static final String PARAMETRO = "PARAMETRO";

    public static final int OUTRA_ACTIVITY_REQUEST_CODE = 0;

    private final int CALL_PHONE_PERMISSION_REQUEST = 1;

    private final int PICK_IMAGE_FILE_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());

        getSupportActionBar().setTitle("Tratando intents");
        getSupportActionBar().setSubtitle("Tem subtitulo também");
        setContentView(activityMainBinding.getRoot());

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.outraActivityMi:
                //Intent outraActivityIntent = new Intent(this, OutraActivity.class);
                Intent outraActivityIntent = new Intent("RECEBER_E_RETORNAR_ACTION");
                Bundle paremetrosBundle = new Bundle();
                paremetrosBundle.putString("PARAMETRO", activityMainBinding.parametroEt.getText().toString());

                outraActivityIntent.putExtras(paremetrosBundle);

                startActivityForResult(outraActivityIntent, OUTRA_ACTIVITY_REQUEST_CODE);
                return true;
            case R.id.viewMi:
                //Abrindo navegador
                Intent abrirNavegadorIntent = new Intent(Intent.ACTION_VIEW);
                abrirNavegadorIntent.setData(Uri.parse("http://scl.ifsp.edu.br"));
                startActivity(abrirNavegadorIntent);
                return true;
            case R.id.callMi:
                // fazendo uma ligação
                verifyCallPhonePermission();
                return true;
            case R.id.dialMi:
                Intent discarIntent = new Intent(Intent.ACTION_DIAL);
                discarIntent.setData(Uri.parse("tel:" + activityMainBinding.parametroEt.getText().toString()));
                startActivity(discarIntent);
                return true;
            case R.id.pickMi:
                Intent pegarImagemIntent = new Intent(Intent.ACTION_PICK);
                String diretorioImagens = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath();
                pegarImagemIntent.setDataAndType(Uri.parse(diretorioImagens), "image/*");
                startActivityForResult(pegarImagemIntent, PICK_IMAGE_FILE_REQUEST_CODE);
                return true;
        }
        return false;
    }

    private void verifyCallPhonePermission(){
        Intent ligarIntent = new Intent(Intent.ACTION_CALL);
        ligarIntent.setData(Uri.parse("tel:" + activityMainBinding.parametroEt.getText().toString()));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED)
                startActivity(ligarIntent);
            else{
                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, CALL_PHONE_PERMISSION_REQUEST);
            }
        }
        else
            startActivity(ligarIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == CALL_PHONE_PERMISSION_REQUEST){
            if(permissions[0].equals(Manifest.permission.CALL_PHONE) && grantResults[0] != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permissão de ligação é necessaria para essa aplicação", Toast.LENGTH_SHORT).show();
            }
            verifyCallPhonePermission();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == OUTRA_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            Toast.makeText(this, "voltou para main activity", Toast.LENGTH_SHORT);
            String retorno = data.getStringExtra(OutraActivity.RETORNO);
            if(retorno != null){
                activityMainBinding.retornoTv.setText(retorno);
            }
        }
        else{
            if(requestCode == PICK_IMAGE_FILE_REQUEST_CODE && resultCode == RESULT_OK){
                Uri imagemUri = data.getData();
                Toast.makeText(this, imagemUri.toString(), Toast.LENGTH_SHORT).show();

                Intent visualizarImagem= new Intent(Intent.ACTION_VIEW, imagemUri);
                startActivity(visualizarImagem);
            }
        }
    }
}