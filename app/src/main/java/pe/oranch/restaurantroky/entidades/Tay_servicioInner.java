package pe.oranch.restaurantroky.entidades;

/**
 * Created by Daniel on 15/11/2017.
 */

public class Tay_servicioInner {
    private Integer tay_servicio_id;
    private Integer tay_asoc_serv_id;
    private String tay_servicio_nombre;
    private Integer tay_asoc_estado;
    private boolean selected;

    public Integer getTay_servicio_id() {
        return tay_servicio_id;
    }

    public void setTay_servicio_id(Integer tay_servicio_id) {
        this.tay_servicio_id = tay_servicio_id;
    }

    public Integer getTay_asoc_serv_id() {
        return tay_asoc_serv_id;
    }

    public void setTay_asoc_serv_id(Integer tay_asoc_serv_id) {
        this.tay_asoc_serv_id = tay_asoc_serv_id;
    }

    public String getTay_servicio_nombre() {
        return tay_servicio_nombre;
    }

    public void setTay_servicio_nombre(String tay_servicio_nombre) {
        this.tay_servicio_nombre = tay_servicio_nombre;
    }

    public Integer getTay_asoc_estado() {
        return tay_asoc_estado;
    }

    public void setTay_asoc_estado(Integer tay_servicio_estado) {
        this.tay_asoc_estado = tay_servicio_estado;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
