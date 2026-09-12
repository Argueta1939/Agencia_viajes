package agencia.viajes.ui.destination

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import agencia.viajes.R
import agencia.viajes.data.Destino
import java.io.File

val paises = listOf(
    "Argentina", "Brasil", "Chile", "Colombia", "Ecuador",
    "México", "Perú", "España", "Francia", "Italia"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DestinationFormScreen(
    destino: Destino? = null,
    onSave: (Destino, Uri?) -> Unit,
    onCancel: () -> Unit
) {
    var nombre by remember { mutableStateOf(destino?.nombre ?: "") }
    var pais by remember { mutableStateOf(destino?.pais ?: paises[0]) }
    var precio by remember { mutableStateOf(destino?.precio?.toString() ?: "") }
    var descripcion by remember { mutableStateOf(destino?.descripcion ?: "") }
    var imagenUri by remember { mutableStateOf<Uri?>(null) }
    var expanded by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> imagenUri = uri }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            stringResource(if (destino == null) R.string.add_destination else R.string.edit_destination),
            style = MaterialTheme.typography.headlineSmall
        )

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it; error = null },
            label = { Text(stringResource(R.string.name_hint)) },
            modifier = Modifier.fillMaxWidth()
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = pais,
                onValueChange = {},
                readOnly = true,
                label = { Text(stringResource(R.string.country_hint)) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                modifier = Modifier
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                paises.forEach {
                    DropdownMenuItem(
                        text = { Text(it) },
                        onClick = { pais = it; expanded = false }
                    )
                }
            }
        }

        OutlinedTextField(
            value = precio,
            onValueChange = { precio = it; error = null },
            label = { Text(stringResource(R.string.price_hint)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = descripcion,
            onValueChange = { descripcion = it; error = null },
            label = { Text(stringResource(R.string.description_hint)) },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
        )

        Button(
            onClick = { launcher.launch("image/*") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.select_image))
        }

        // Preview: imagen nueva (Uri) o la existente (ruta local)
        val previewModel: Any? = imagenUri ?: destino?.imagenUrl?.takeIf { it.isNotBlank() }?.let { File(it) }
        previewModel?.let {
            AsyncImage(
                model = it,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        }

        error?.let { Text(it, color = MaterialTheme.colorScheme.error) }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = {
                    val p = precio.toDoubleOrNull()
                    when {
                        nombre.isBlank() || pais.isBlank() || precio.isBlank() || descripcion.isBlank() ->
                            error = "Ningún campo puede estar vacío"
                        p == null || p <= 0 ->
                            error = "El precio debe ser mayor a 0"
                        descripcion.length < 20 ->
                            error = "La descripción debe tener mínimo 20 caracteres"
                        imagenUri == null && destino?.imagenUrl.isNullOrBlank() ->
                            error = "Debes seleccionar una imagen"
                        else -> {
                            val nuevo = Destino(
                                id = destino?.id ?: "",
                                nombre = nombre,
                                pais = pais,
                                precio = p,
                                descripcion = descripcion,
                                imagenUrl = destino?.imagenUrl ?: ""
                            )
                            onSave(nuevo, imagenUri)
                        }
                    }
                },
                modifier = Modifier.weight(1f)
            ) { Text(stringResource(R.string.save)) }

            OutlinedButton(onClick = onCancel, modifier = Modifier.weight(1f)) {
                Text(stringResource(R.string.cancel))
            }
        }
    }
}