package com.bancode.sangue.api.models;

import jakarta.persistence.*;

@Entity
@Table(name = "tipos_sanguineos")
public class TipoSanguineo {
    @Id
    private String tipo;

    private String podeDoarPara;
    private String podeReceberDe;

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getPodeDoarPara() {
        return podeDoarPara;
    }

    public void setPodeDoarPara(String podeDoarPara) {
        this.podeDoarPara = podeDoarPara;
    }

    public String getPodeReceberDe() {
        return podeReceberDe;
    }

    public void setPodeReceberDe(String podeReceberDe) {
        this.podeReceberDe = podeReceberDe;
    }

    @Override
    public String toString() {
        return "TipoSanguineo{" +
                "tipo='" + tipo + '\'' +
                ", podeDoarPara='" + podeDoarPara + '\'' +
                ", podeReceberDe='" + podeReceberDe + '\'' +
                '}';
    }
}