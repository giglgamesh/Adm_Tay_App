package pe.oranch.restaurantroky;

import android.app.Application;

/**
 * Created by Daniel on 05/11/2017.
 */

public class Config extends Application{
    public static final String APP_API_URL = "http://tayapp.azurewebsites.net/restaurantes/";
    //public static final String APP_API_URL = "http://192.168.0.25/restaurantes/";
    public static final String IMAGENES_URL = "imagenes/perfil/";
    public static final String POST_USER_LOGIN = "Login.php";
    public static final String REGISTRAR_REQUEST = "Register.php";
    public static final String CANTIDAD_COMENTARIOS = "CantComentarios.php";
    public static final String CANTIDAD_CALIFICACION = "CantCalificaciones.php";
    public static final String PROMEDIO_CALIFICACION = "PromedioCalificacion.php";
    public static final String CALIFICACION_POR_PERSONA = "CalificacionPorPersona.php";
    public static final String TIPO_COMIDA = "PerfilEmpresa.php";
    public static final String LISTA_USUARIOS = "wsJSONConsultarLista.php";
    public static final String SERVICIOS_EMPRESA = "ObtenerServicios.php";
    public static final String LISTA_SERVICIOS = "ListarServicios.php";
    public static final String LISTA_COMIDAS = "ListarComidas.php";
    public static final String LISTA_COMENTARIOS = "ComentarioEmpresa.php";
    public static final String ACTUALIZA_COMENTARIOS = "ActualizarComentario.php";
    public static final String LISTA_DESCUENTO = "DescuentoListar.php";
    public static final String ACTUALIZAR_DESCUENTO = "DescuentoActualizar.php";
    public static final String OBTENER_ENTRADAS = "ObtenerEntradas.php";
    public static final String OBTENER_SEGUNDOS = "ObtenerSegundos.php";
    public static final String REGISTRAR_MENU = "RegistrarMenu.php";
    public static final String ESTADO_MENU = "ActualizarMenu.php";
    public static final String REGISTRAR_MENU_FOTO = "RegistrarMenuFoto.php";
    public static final String REGISTRAR_CARTA = "RegistrarCarta.php";
    public static final String OBTENER_CARTA = "ObtenerCarta.php";
    public static final String ESTADO_CARTA = "ActualizarCarta.php";
    public static final String ACTUALIZA_SERVICIOS = "ActualizarServicios.php";
    public static final String ACTUALIZA_COMIDAS = "ActualizarComidas.php";
    public static final String REGISTRAR_SERVICIOS_ASOCIADOS = "AgregarServiciosAsoc.php";
    public static final String REGISTRAR_COMIDAS_ASOCIADOS = "AgregarComidasAsoc.php";
    public static final String ACTUALIZA_EMPRESA = "ActualizarEmpresa.php";
    public static final String ACTUALIZA_EMPRESA_IMAGEN = "ActualizarPerfilImagen.php";
}
