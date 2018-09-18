package com.mcuhq.simplebluetooth;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

/**
 * Created by eduardo.dall on 01/08/2018.
 */
@Entity
public class TiroCorredor {
    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "nome")
    private String nome;

    @ColumnInfo(name = "primeira_corrida")
    private Date primeiraCorrida;

    @ColumnInfo(name = "tempo_decorrido_plataforma")
    private Date tempoDecorridoPlataforma;

    @ColumnInfo(name = "segunda_corrida")
    private Date segundaCorrida;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Date getPrimeiraCorrida() {
        return primeiraCorrida;
    }

    public void setPrimeiraCorrida(Date primeiraCorrida) {
        this.primeiraCorrida = primeiraCorrida;
    }

    public Date getTempoDecorridoPlataforma() {
        return tempoDecorridoPlataforma;
    }

    public void setTempoDecorridoPlataforma(Date tempoDecorridoPlataforma) {
        this.tempoDecorridoPlataforma = tempoDecorridoPlataforma;
    }

    public Date getSegundaCorrida() {
        return segundaCorrida;
    }

    public void setSegundaCorrida(Date segundaCorrida) {
        this.segundaCorrida = segundaCorrida;
    }

    public boolean isEverythingPopulated() {
        return !this.getNome().isEmpty()
                && this.getPrimeiraCorrida() != null
                && this.getSegundaCorrida() != null
                && this.getTempoDecorridoPlataforma() != null;
    }

}
