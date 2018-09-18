package com.mcuhq.simplebluetooth;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Date;

import static com.mcuhq.simplebluetooth.BluetoothListener.EXTRA_INICIO;
import static com.mcuhq.simplebluetooth.BluetoothListener.EXTRA_PE_PLATAFORMA;
import static com.mcuhq.simplebluetooth.BluetoothListener.EXTRA_PRIMEIRA_CORRIDA;
import static com.mcuhq.simplebluetooth.BluetoothListener.EXTRA_SEGUNDA_CORRIDA;
import static com.mcuhq.simplebluetooth.BluetoothListener.EXTRA_TEMPO_DECORRIDO_PLATAFORMA;
import static com.mcuhq.simplebluetooth.BluetoothListener.EXTRA_FIM;

/**
 * Created by eduardo.dall on 23/08/2018.
 */

public class TempoTiroBroadcastReceiver extends BroadcastReceiver {

    TiroActivity tiroActivity;
    TiroCorredor tiro;

    public TempoTiroBroadcastReceiver(TiroActivity _tiroActivity) {
        tiroActivity = _tiroActivity;
        tiro = new TiroCorredor();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Long primeiraCorrida = intent.getLongExtra(EXTRA_PRIMEIRA_CORRIDA, 0);
        Long segundaCorrida = intent.getLongExtra(EXTRA_SEGUNDA_CORRIDA, 0);
        Long tempoDecorridoPlataforma = intent.getLongExtra(EXTRA_TEMPO_DECORRIDO_PLATAFORMA, 0);
        Boolean indicaFim = intent.getBooleanExtra(EXTRA_FIM, false);
        Boolean indicaInicio = intent.getBooleanExtra(EXTRA_INICIO, false);
        Boolean pePlataforma = intent.getBooleanExtra(EXTRA_PE_PLATAFORMA, false);
        if (indicaInicio)
            tiroActivity.mostrarMsgInicio();
        if (pePlataforma)
            tiroActivity.mostrarMsgAguardandoTiro();
        if (primeiraCorrida > 0)
            tiroActivity.setTempoPrimeiraCorrida(new Date(primeiraCorrida));
        if (segundaCorrida > 0)
            tiroActivity.setTempoSegundaCorrida(new Date(segundaCorrida));
        if (tempoDecorridoPlataforma > 0)
            tiroActivity.setTempoDecorridoPlataforma(new Date(tempoDecorridoPlataforma));
        if (indicaFim) {
            tiro.setNome(tiroActivity.getNomeCorredor());
            new SalvarTiroCorredorAsync(tiroActivity, tiro).execute();
        }
    }
}
