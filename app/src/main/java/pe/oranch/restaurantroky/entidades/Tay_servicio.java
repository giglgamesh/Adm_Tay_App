package pe.oranch.restaurantroky.entidades;

/**
 * Created by Daniel on 15/11/2017.
 */

public class Tay_servicio {
    private Integer tay_servicio_id;
    private String tay_servicio_nombre;
    private Integer tay_servicio_estado;
    private String tay_servicio_imagenurl;
    private boolean selected;

    public Integer getTay_servicio_id() {
        return tay_servicio_id;
    }

    public void setTay_servicio_id(Integer tay_servicio_id) {
        this.tay_servicio_id = tay_servicio_id;
    }

    public String getTay_servicio_nombre() {
        return tay_servicio_nombre;
    }

    public void setTay_servicio_nombre(String tay_servicio_nombre) {
        this.tay_servicio_nombre = tay_servicio_nombre;
    }

    public Integer getTay_servicio_estado() {
        return tay_servicio_estado;
    }

    public void setTay_servicio_estado(Integer tay_servicio_estado) {
        this.tay_servicio_estado = tay_servicio_estado;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getTay_servicio_imagenurl() {
        return tay_servicio_imagenurl;
    }

    public void setTay_servicio_imagenurl(String tay_servicio_imagenurl) {
        this.tay_servicio_imagenurl = tay_servicio_imagenurl;
    }
}
