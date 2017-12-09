package pe.oranch.restaurantroky.entidades;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

/**
 * Created by Daniel on 15/11/2017.
 */

public class Tay_carta {
    private Integer tay_carta_id;
    private String tay_carta_nombre;
    private Double tay_carta_precio;
    private Integer tay_carta_empresa_id;
    private String dato;
    private Bitmap tay_carta_imagen;
    private String tay_carta_ruta_imagen;

    public Integer getTay_carta_id() {
        return tay_carta_id;
    }

    public void setTay_carta_id(Integer tay_carta_id) {
        this.tay_carta_id = tay_carta_id;
    }

    public String getTay_carta_nombre() {
        return tay_carta_nombre;
    }

    public void setTay_carta_nombre(String tay_carta_nombre) {
        this.tay_carta_nombre = tay_carta_nombre;
    }

    public Double getTay_carta_precio() {
        return tay_carta_precio;
    }

    public void setTay_carta_precio(Double tay_carta_precio) {
        this.tay_carta_precio = tay_carta_precio;
    }

    public Integer getTay_carta_empresa_id() {
        return tay_carta_empresa_id;
    }

    public void setTay_carta_empresa_id(Integer tay_carta_empresa_id) {
        this.tay_carta_empresa_id = tay_carta_empresa_id;
    }

    public String getDato() {
        return dato;
    }

    public void setDato(String dato) {
        this.dato = dato;

        try {
            byte[] byteCode= Base64.decode(dato,Base64.DEFAULT);
            //this.imagen= BitmapFactory.decodeByteArray(byteCode,0,byteCode.length);

            int alto=100;//alto en pixeles
            int ancho=150;//ancho en pixeles

            Bitmap foto= BitmapFactory.decodeByteArray(byteCode,0,byteCode.length);
            this.tay_carta_imagen=Bitmap.createScaledBitmap(foto,alto,ancho,true);


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Bitmap getTay_carta_imagen() {
        return tay_carta_imagen;
    }

    public void setTay_carta_imagen(Bitmap tay_carta_imagen) {
        this.tay_carta_imagen = tay_carta_imagen;
    }

    public String getTay_carta_ruta_imagen() {
        return tay_carta_ruta_imagen;
    }

    public void setTay_carta_ruta_imagen(String tay_carta_ruta_imagen) {
        this.tay_carta_ruta_imagen = tay_carta_ruta_imagen;
    }
}
