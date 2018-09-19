package com.wandall.runtimer;

import android.app.Activity;
import android.content.Intent;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * Created by eduardo.dall on 21/08/2018.
 */

public class BluetoothListener {
    private final static int MESSAGE_READ = 2; // used in bluetooth handler to identify message update
    private final static int CONNECTING_STATUS = 3; // used in bluetooth handler to identify message status
    private final static String INTENT_SEND_BROACAST_TIRO = "com.mcuhq.simplebluetooth.BROADCAST_TEMPO_TIRO";
    public final static String EXTRA_PE_PLATAFORMA = "PePlataforma";
    public final static String EXTRA_PRIMEIRA_CORRIDA = "primeiraCorrida";
    public final static String EXTRA_SEGUNDA_CORRIDA = "segundaCorrida";
    public final static String EXTRA_TEMPO_DECORRIDO_PLATAFORMA = "tempoDecorridoPlataforma";
    public final static String EXTRA_FIM = "FINISH";
    public final static String EXTRA_INICIO = "START";
    final Activity context;

    public BluetoothListener(Activity _context) {
        context = _context;
    }

    public void handleMessage(Message msg) {
        if (msg.what == MESSAGE_READ) {
            String readMessage = null;
            try {
                readMessage = new String((byte[]) msg.obj, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Log.i("received", readMessage);

            Intent intent = new Intent(INTENT_SEND_BROACAST_TIRO);
            if (readMessage.contains("MTapete A")) {
                intent.putExtra(EXTRA_PE_PLATAFORMA,true);
                context.sendBroadcast(intent);
            }
            if (readMessage.contains("START")) {
                intent.putExtra(EXTRA_INICIO,true);
                context.sendBroadcast(intent);
            }
            if (readMessage.contains("MIda")) {
                intent.putExtra(EXTRA_PRIMEIRA_CORRIDA, new Date(666666).getTime());
                context.sendBroadcast(intent);
            }
            if (readMessage.contains("MP")) {
                intent.putExtra(EXTRA_TEMPO_DECORRIDO_PLATAFORMA, new Date(33221333).getTime());
                context.sendBroadcast(intent);
            }
            if (readMessage.contains("MVolta")) {
                intent.putExtra(EXTRA_SEGUNDA_CORRIDA, new Date(9999999).getTime());
                context.sendBroadcast(intent);
            }
            if (readMessage.contains("FINISH")) {
                intent.putExtra(EXTRA_FIM, true);
                context.sendBroadcast(intent);
            }
        }

        if (msg.what == CONNECTING_STATUS) {
            if (msg.arg1 == 1) {
                Toast.makeText(context.getApplicationContext(), "Bluetooth Conectado!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(context, TiroActivity.class);
                context.startActivity(intent);
            } else {
                Toast.makeText(context.getApplicationContext(), "Erro ao conectar com o dispositivo!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, TiroActivity.class);
                context.startActivity(intent);
            }
        }
    }
}