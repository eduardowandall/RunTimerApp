package com.mcuhq.simplebluetooth;

import android.arch.persistence.room.Room;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

//example: http://mcuhq.com/27/simple-android-bluetooth-application-with-arduino-example
public class TiroActivity extends AppCompatActivity {

    // GUI Components
    private TextView tempoPrimeiraCorrida;
    private TextView tempoDecorridoPlataforma;
    private TextView tempoSegundaCorrida;
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

    public void refreshListView() {
        final AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "database-name").allowMainThreadQueries().build();
        historicoTiroAdapter.addAll(db.tiroCorredorDao().getAll());
        historicoTiroAdapter.notifyDataSetChanged();
    }

    public void mostrarMsgAguardandoTiro() {
    }

    public void mostrarMsgInicio() {
    }

//    private void exportDB() {
//
//        DBHelper dbhelper = new DBHelper(getApplicationContext());
//        File exportDir = new File(Environment.getExternalStorageDirectory(), "");
//        if (!exportDir.exists())
//        {
//            exportDir.mkdirs();
//        }
//
//        File file = new File(exportDir, "csvname.csv");
//        try
//        {
//            file.createNewFile();
//            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
//            SQLiteDatabase db = dbhelper.getReadableDatabase();
//            Cursor curCSV = db.rawQuery("SELECT * FROM contacts",null);
//            csvWrite.writeNext(curCSV.getColumnNames());
//            while(curCSV.moveToNext())
//            {
//                //Which column you want to exprort
//                String arrStr[] ={curCSV.getString(0),curCSV.getString(1), curCSV.getString(2)};
//                csvWrite.writeNext(arrStr);
//            }
//            csvWrite.close();
//            curCSV.close();
//        }
//        catch(Exception sqlEx)
//        {
//            Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
//        }
//    }
}
