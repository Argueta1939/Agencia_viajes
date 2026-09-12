package agencia.viajes.data

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class LocalStorageRepository(private val context: Context) {

    private val imagesDir: File
        get() = File(context.filesDir, "destinos").apply { if (!exists()) mkdirs() }

    /**
     * Copia la imagen seleccionada a almacenamiento interno de la app.
     * @return ruta absoluta del archivo guardado, o null si falla.
     */
    fun saveImage(uri: Uri): String? {
        return try {
            val fileName = "img_${UUID.randomUUID()}.jpg"
            val destFile = File(imagesDir, fileName)

            context.contentResolver.openInputStream(uri)?.use { input ->
                FileOutputStream(destFile).use { output ->
                    input.copyTo(output)
                }
            }
            destFile.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Elimina una imagen guardada localmente.
     */
    fun deleteImage(path: String?) {
        if (path.isNullOrBlank()) return
        try {
            val file = File(path)
            if (file.exists()) file.delete()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}