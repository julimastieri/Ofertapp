package com.example.mariapiagoicoechea.ofertas;

import android.os.Parcel;

import java.io.Serializable;

public class Oferta implements Serializable {

    private double precio;
    private String marca;
    private String categoria;
    private String tipo;
    private String comercio;
    private String fecha_vencimiento;
    private Double latitud;
    private Double longitud;

    public Oferta(double precio, String marca, String categoria, String tipo, String comercio, String fecha_vencimiento, Double lat, Double lon) {
        this.precio = precio;
        this.marca = marca;
        this.categoria = categoria;
        this.tipo = tipo;
        this.comercio = comercio;
        this.fecha_vencimiento = fecha_vencimiento;
        this.latitud = lat;
        this.longitud = lon;
    }

    public double getPrecio() {
        return precio;
    }

    public String getMarca() {
        return marca;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getTipo() {
        return tipo;
    }

    public String getComercio() {
        return comercio;
    }

    public String getFecha_vencimiento() {
        return fecha_vencimiento;
    }

    public Double getLatitud() {
        return latitud;
    }

    public Double getLongitud() {
        return longitud;
    }


    public Oferta(Parcel in) {
        precio = in.readDouble();
        marca = in.readString();
        categoria = in.readString();
        tipo = in.readString();
        comercio = in.readString();
        fecha_vencimiento = in.readString();
        latitud = in.readDouble();
        longitud = in.readDouble();
    }

    public boolean equals(Oferta oferta) {
        if ( (this.precio == oferta.getPrecio()) && this.marca.equals(oferta.getMarca())
                && this.categoria.equals(oferta.getCategoria()) && this.tipo.equals(oferta.tipo)
                && this.comercio.equals(oferta.getComercio()) && this.fecha_vencimiento.equals(oferta.getFecha_vencimiento())
                && (this.latitud == oferta.getLatitud()) && (this.getLongitud() == oferta.getLongitud()) )
            return true;
        else
            return false;
    }
}
