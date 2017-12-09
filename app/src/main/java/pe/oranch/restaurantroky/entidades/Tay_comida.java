package pe.oranch.restaurantroky.entidades;

/**
 * Created by Daniel on 15/11/2017.
 */

public class Tay_comida {
    private Integer tay_tipocomida_id;
    private Integer tay_asoc_tipc_id;
    private String tay_tipocomida_nombre;
    private Integer tay_asoc_estado;
    private boolean selected;

    public Integer getTay_tipocomida_id() {
        return tay_tipocomida_id;
    }

    public void setTay_tipocomida_id(Integer tay_tipocomida_id) {
        this.tay_tipocomida_id = tay_tipocomida_id;
    }

    public Integer getTay_asoc_tipc_id() {
        return tay_asoc_tipc_id;
    }

    public void setTay_asoc_tipc_id(Integer tay_asoc_tipc_id) {
        this.tay_asoc_tipc_id = tay_asoc_tipc_id;
    }

    public String getTay_tipocomida_nombre() {
        return tay_tipocomida_nombre;
    }

    public void setTay_tipocomida_nombre(String tay_tipocomida_nombre) {
        this.tay_tipocomida_nombre = tay_tipocomida_nombre;
    }

    public Integer getTay_asoc_estado() {
        return tay_asoc_estado;
    }

    public void setTay_asoc_estado(Integer tay_asoc_estado) {
        this.tay_asoc_estado = tay_asoc_estado;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

}
