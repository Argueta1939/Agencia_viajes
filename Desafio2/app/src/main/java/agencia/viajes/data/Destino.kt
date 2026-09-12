package agencia.viajes.data

data class Destino(
    val id: String = "",
    val nombre: String = "",
    val pais: String = "",
    val precio: Double = 0.0,
    val descripcion: String = "",
    val imagenUrl: String = ""   // Ahora almacena ruta local del archivo, no URL http
)