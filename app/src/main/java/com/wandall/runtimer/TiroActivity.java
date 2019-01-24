package com.wandall.runtimer;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.wandall.runtimer.BluetoothListener.INTENT_SEND_BROADCAST_TIRO;

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
        formatter = new SimpleDateFormat("ss.SSS");
        tempoPrimeiraCorrida = findViewById(R.id.tempoPrimeiraCorrida);
        tempoDecorridoPlataforma = findViewById(R.id.tempoDecorridoPlataforma);
        tempoSegundaCorrida = findViewById(R.id.tempoSegundaCorrida);
        mensagens = findViewById(R.id.mensagens);

        nomeCorredor = findViewById(R.id.nomeCorredor);

        final AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "database-name").allowMainThreadQueries().build();
        historicoTiroAdapter = new HistoricoTiroAdapter(this, 0, db.tiroCorredorDao().getAll());

        tirosListView = findViewById(R.id.infoListView);
        tirosListView.setAdapter(historicoTiroAdapter);
        receiver = new TempoTiroBroadcastReceiver(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(INTENT_SEND_BROADCAST_TIRO));
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
        mostrarMsgAguardandoTiro();
        historicoTiroAdapter.insert(addedItem, 0);
        historicoTiroAdapter.notifyDataSetChanged();
    }

    public void mostrarMsgAguardandoTiro() {
        mensagens.setText(getString(R.string.msg_aguardando_tiro));
    }

    public void mostrarMsgInicio() {
        mensagens.setText(getString(R.string.msg_inicio_corrida));
    }

    private void clearFields() {
        nomeCorredor.setText(null);
        String text = "00.000";
        tempoPrimeiraCorrida.setText(text);
        tempoDecorridoPlataforma.setText(text);
        tempoSegundaCorrida.setText(text);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.export_button, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        exportDB();
        return true;
    }

    private void exportDB() {
        try {
            String filename = "relatorio_corredores.csv";
            saveFile(filename);
            shareFile(filename);
        } catch (Exception sqlEx) {
            Log.e("TiroActivity", sqlEx.getMessage(), sqlEx);
        }
    }

    private File getTempFile(Context context, String url) {
        File file = null;
        try {
            String fileName = Uri.parse(url).getLastPathSegment();
            file = File.createTempFile(fileName, null, context.getCacheDir());
        } catch (IOException e) {
            // Error while creating file
        }
        return file;
    }

    private void saveFile(String filename) {
        try {
            File reportFolder = new File(getFilesDir(), "reports");
            if (!reportFolder.exists())
            reportFolder.mkdirs();
            File file = new File(reportFolder, filename);
            if (!file.exists())
                file.createNewFile();

            FileOutputStream stream = new FileOutputStream(file);
            stream.write(createCsvFile().getBytes());
            stream.close();
            //OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput(filename, Context.MODE_PRIVATE));
            //outputStreamWriter.write(createCsvFile());
            //outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private void shareFile(String filename) {

        File reportFolder = new File(getFilesDir(), "reports");
        File file = new File(reportFolder, filename);
        Intent intentShareFile = new Intent(Intent.ACTION_SEND);

        if (file.exists()) {
            intentShareFile.setType("text/csv");
            intentShareFile.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(this, getApplicationContext().getPackageName(), file));
            intentShareFile.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.shared_file_name));

            startActivity(Intent.createChooser(intentShareFile, getString(R.string.title_share_file)));
        }
    }

    private String createCsvFile() {
        final AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "database-name").allowMainThreadQueries().build();
        StringBuilder csvFile = new StringBuilder("Id;Nome;Primeira Corrida;Tempo Pe Plataforma;Segunda Corrida\r\n");
        List<TiroCorredor> historico = db.tiroCorredorDao().getAll();
        for (TiroCorredor tiro :
                historico) {
            csvFile.append(createStringCsvLine(tiro));
        }
        return csvFile.toString();
    }

    public String createStringCsvLine(TiroCorredor tiroCorredor) {
        return tiroCorredor.getUid()
                + ";" + tiroCorredor.getNome()
                + ";" + formatter.format(tiroCorredor.getPrimeiraCorrida())
                + ";" + formatter.format(tiroCorredor.getTempoDecorridoPlataforma())
                + ";" + formatter.format(tiroCorredor.getSegundaCorrida())
                + "\r\n";

    }
}
