package pe.oranch.restaurantroky.entidades;

/**
 * Created by Daniel on 19/11/2017.
 */

public class Comentario_innerjoin_cliente {
    Integer tay_comentario_id;
	String tay_comentario_descripcion;
	Integer tay_comentario_empresa;
	Integer tay_cliente_id;
    Integer tay_comentario_positivo;
    Integer tay_comentario_negativo;
    Integer tay_comentario_estado;
	String tay_cliente_nombre;
	String tay_cliente_email;
	String tay_cliente_contraseña;
	Integer tay_cliente_tipo;
    String tay_cliente_fecha;
	Integer tay_cliente_estado;

    private boolean selected;

    public Integer getTay_comentario_id() {
        return tay_comentario_id;
    }

    public void setTay_comentario_id(Integer tay_comentario_id) {
        this.tay_comentario_id = tay_comentario_id;
    }

    public String getTay_comentario_descripcion() {
        return tay_comentario_descripcion;
    }

    public void setTay_comentario_descripcion(String tay_comentario_descripcion) {
        this.tay_comentario_descripcion = tay_comentario_descripcion;
    }

    public Integer getTay_comentario_empresa() {
        return tay_comentario_empresa;
    }

    public void setTay_comentario_empresa(Integer tay_comentario_empresa) {
        this.tay_comentario_empresa = tay_comentario_empresa;
    }

    public Integer getTay_cliente_id() {
        return tay_cliente_id;
    }

    public void setTay_cliente_id(Integer tay_cliente_id) {
        this.tay_cliente_id = tay_cliente_id;
    }

    public Integer getTay_comentario_positivo() {
        return tay_comentario_positivo;
    }

    public void setTay_comentario_positivo(Integer tay_comentario_positivo) {
        this.tay_comentario_positivo = tay_comentario_positivo;
    }

    public Integer getTay_comentario_negativo() {
        return tay_comentario_negativo;
    }

    public void setTay_comentario_negativo(Integer tay_comentario_negativo) {
        this.tay_comentario_negativo = tay_comentario_negativo;
    }

    public Integer getTay_comentario_estado() {
        return tay_comentario_estado;
    }

    public void setTay_comentario_estado(Integer tay_comentario_estado) {
        this.tay_comentario_estado = tay_comentario_estado;
    }

    public String getTay_cliente_nombre() {
        return tay_cliente_nombre;
    }

    public void setTay_cliente_nombre(String tay_cliente_nombre) {
        this.tay_cliente_nombre = tay_cliente_nombre;
    }

    public String getTay_cliente_email() {
        return tay_cliente_email;
    }

    public void setTay_cliente_email(String tay_cliente_email) {
        this.tay_cliente_email = tay_cliente_email;
    }

    public String getTay_cliente_contraseña() {
        return tay_cliente_contraseña;
    }

    public void setTay_cliente_contraseña(String tay_cliente_contraseña) {
        this.tay_cliente_contraseña = tay_cliente_contraseña;
    }

    public Integer getTay_cliente_tipo() {
        return tay_cliente_tipo;
    }

    public void setTay_cliente_tipo(Integer tay_cliente_tipo) {
        this.tay_cliente_tipo = tay_cliente_tipo;
    }

    public String getTay_cliente_fecha() {
        return tay_cliente_fecha;
    }

    public void setTay_cliente_fecha(String tay_cliente_fecha) {
        this.tay_cliente_fecha = tay_cliente_fecha;
    }

    public Integer getTay_cliente_estado() {
        return tay_cliente_estado;
    }

    public void setTay_cliente_estado(Integer tay_cliente_estado) {
        this.tay_cliente_estado = tay_cliente_estado;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
