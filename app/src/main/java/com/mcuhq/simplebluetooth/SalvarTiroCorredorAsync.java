package com.mcuhq.simplebluetooth;

import android.app.Activity;
import android.arch.persistence.room.Room;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;

/**
 * Created by eduardo.dall on 23/08/2018.
 */

public class SalvarTiroCorredorAsync extends AsyncTask<Void, Void, Integer> {

    //Prevent leak
    private WeakReference<Activity> weakActivity;
    private TiroCorredor tiro;

    public SalvarTiroCorredorAsync(Activity activity, TiroCorredor tiro) {
        weakActivity = new WeakReference<>(activity);
        this.tiro = tiro;
    }

    @Override
    protected Integer doInBackground(Void... params) {
        Activity activity = weakActivity.get();
        final AppDatabase db = Room.databaseBuilder(activity.getApplicationContext(),
                AppDatabase.class, "database-name").build();
        if(tiro.getNome() == null || tiro.getNome().isEmpty())
            tiro.setNome("Corredor Sem Nome");
        db.tiroCorredorDao().insertAll(tiro);
        return 1;
    }

    @Override
    protected void onPostExecute(Integer agentsCount) {
        Activity activity = weakActivity.get();
        if (activity == null) {
            return;
        }
        ((TiroActivity)activity).refreshActivity(tiro);
    }
}
