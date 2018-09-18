package com.mcuhq.simplebluetooth;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

//example: http://mcuhq.com/27/simple-android-bluetooth-application-with-arduino-example
public class TiroActivity extends AppCompatActivity {

    // GUI Components
    private TextView tempoPrimeiraCorrida;
    private TextView tempoDecorridoPlataforma;
    private TextView tempoSegundaCorrida;
    private TextView mensagens;
    private EditText nomeCorredor;
    private HistoricoTiroAdapter historicoTiroAdapter;
    private ListView tirosListView;
    private TempoTiroBroadcastReceiver receiver;
    SimpleDateFormat formatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tiro);
        formatter = new SimpleDateFormat("mm:ss");
        tempoPrimeiraCorrida = findViewById(R.id.tempoPrimeiraCorrida);
        tempoDecorridoPlataforma = findViewById(R.id.tempoDecorridoPlataforma);
        tempoSegundaCorrida = findViewById(R.id.tempoSegundaCorrida);
        mensagens = findViewById(R.id.mensagens);

        nomeCorredor = findViewById(R.id.nomeCorredor);

        final AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "database-name").allowMainThreadQueries().build();
        historicoTiroAdapter = new HistoricoTiroAdapter(this, 0, db.tiroCorredorDao().getAll());

        tirosListView = findViewById(R.id.infoListView);
        tirosListView.setAdapter(historicoTiroAdapter);
        //tiro.setNome(contextTiro.getNomeCorredor());
        //if (tiro.isEverythingPopulated())
        //{
        //    agentAsyncTask.execute();
        //}
        receiver = new TempoTiroBroadcastReceiver(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter("com.mcuhq.simplebluetooth.BROADCAST_TEMPO_TIRO"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    public void setTempoPrimeiraCorrida(Date date) {
        tempoPrimeiraCorrida.setText(formatter.format(date));
    }

    public void setTempoSegundaCorrida(Date date) {
        tempoSegundaCorrida.setText(formatter.format(date));
    }

    public void setTempoDecorridoPlataforma(Date date) {
        tempoDecorridoPlataforma.setText(formatter.format(date));
    }

    public String getNomeCorredor() {
        return nomeCorredor.getText().toString();
    }

    public void refreshActivity(TiroCorredor addedItem) {
        clearFields();
        historicoTiroAdapter.insert(addedItem, 0);
        historicoTiroAdapter.notifyDataSetChanged();
    }

    public void mostrarMsgAguardandoTiro() {
        mensagens.setText("Aguardando Tiro...");
    }

    public void mostrarMsgInicio() {
        mensagens.setText("Corrida Iniciada");
    }

    private void clearFields() {
        nomeCorredor.setText(null);
        tempoPrimeiraCorrida.setText("00:00");
        tempoDecorridoPlataforma.setText("00:00");
        tempoSegundaCorrida.setText("00:00");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.export_button, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        exportDB();
        return true;
    }
    private void exportDB() {

        final AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "database-name").allowMainThreadQueries().build();
//        File exportDir = new File(Environment.getExternalStorageDirectory(), "");
//        if (!exportDir.exists())
//        {
//            exportDir.mkdirs();
//        }

        try
        {
            String csvFile = "Id;Nome;Primeira Corrida;Tempo Pe Plataforma;Segunda Corrida\r\n";
            List<TiroCorredor> historico = db.tiroCorredorDao().getAll();
            for (TiroCorredor tiro:
                    historico) {
                csvFile += createStringCsvLine(tiro);
            }
            try {
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("teste.csv", Context.MODE_PRIVATE));
                outputStreamWriter.write(csvFile);
                outputStreamWriter.close();
            }
            catch (IOException e) {
                Log.e("Exception", "File write failed: " + e.toString());
            }
        }
        catch(Exception sqlEx)
        {
            Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
        }
    }

    public String createStringCsvLine(TiroCorredor tiroCorredor)
    {
        return tiroCorredor.getUid()
                +";"+tiroCorredor.getNome()
                +";"+formatter.format(tiroCorredor.getPrimeiraCorrida())
                +";"+formatter.format(tiroCorredor.getTempoDecorridoPlataforma())
                +";"+formatter.format(tiroCorredor.getSegundaCorrida())
                +"\r\n";

    }
}
