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
    public final static String INTENT_SEND_BROADCAST_TIRO = "com.wandall.runtimer.BROADCAST_TEMPO_TIRO";
    public final static String EXTRA_PE_PLATAFORMA = "A";
    public final static String EXTRA_PRIMEIRA_CORRIDA = "I";
    public final static String EXTRA_SEGUNDA_CORRIDA = "V";
    public final static String EXTRA_TEMPO_DECORRIDO_PLATAFORMA = "P";
    //public final static String EXTRA_FIM = "FINISH";
    public final static String EXTRA_INICIO = "C";
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


            Intent intent = new Intent(INTENT_SEND_BROADCAST_TIRO);
            if (readMessage.contains(EXTRA_PE_PLATAFORMA)) {
                intent.putExtra(EXTRA_PE_PLATAFORMA, true);
                context.sendBroadcast(intent);
            }
            if (readMessage.contains(EXTRA_INICIO)) {
                intent.putExtra(EXTRA_INICIO, true);
                context.sendBroadcast(intent);
            }
            if (readMessage.contains(EXTRA_PRIMEIRA_CORRIDA)) {
                sendTime(readMessage, intent, EXTRA_PRIMEIRA_CORRIDA);
            }
            if (readMessage.contains(EXTRA_TEMPO_DECORRIDO_PLATAFORMA)) {
                sendTime(readMessage, intent, EXTRA_TEMPO_DECORRIDO_PLATAFORMA);
            }
            if (readMessage.contains(EXTRA_SEGUNDA_CORRIDA)) {
                sendTime(readMessage, intent, EXTRA_SEGUNDA_CORRIDA);
            }

//            if (readMessage.contains("F")) {
//                intent.putExtra(EXTRA_FIM, true);
//                context.sendBroadcast(intent);
//            }
        }

        if (msg.what == CONNECTING_STATUS) {
            if (msg.arg1 == 1) {
                Toast.makeText(context.getApplicationContext(), "Bluetooth Conectado!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(context, TiroActivity.class);
                context.startActivity(intent);
            } else {
                Toast.makeText(context.getApplicationContext(), "Erro ao conectar com o dispositivo!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void sendTime(String readMessage, Intent intent, String identifier) {
        int startIndex = readMessage.indexOf(identifier) + 1;
        if (startIndex > 0) {
            String valor = readMessage.substring(startIndex);
            int endIndex = valor.indexOf("\r\n");
            if (endIndex > 0) {
                intent.putExtra(identifier, Long.parseLong(valor.substring(0, endIndex)));
                context.sendBroadcast(intent);
            } else {
                Log.d("startIndex", String.valueOf(startIndex));
                Log.d("endIndex", String.valueOf(endIndex));
            }
        }
    }
}