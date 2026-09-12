package agencia.viajes.data

data class Destino(
    val id: String = "",
    val nombre: String = "",
    val pais: String = "",
    val precio: Double = 0.0,
    val descripcion: String = "",
    val imagenUrl: String = ""   // ahora guardará ruta local, no URL
)