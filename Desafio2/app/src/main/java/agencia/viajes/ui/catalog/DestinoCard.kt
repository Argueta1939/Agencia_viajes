package agencia.viajes.ui.catalog

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import agencia.viajes.R
import agencia.viajes.data.Destino
import java.io.File

@Composable
fun DestinoCard(
    destino: Destino,
    onEdit: (Destino) -> Unit,
    onDelete: (Destino) -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column {
            if (destino.imagenUrl.isNotBlank()) {
                AsyncImage(
                    model = File(destino.imagenUrl),   // 👈 carga archivo local con Coil
                    contentDescription = destino.nombre,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    Text("Sin imagen")
                }
            }

            Column(modifier = Modifier.padding(12.dp)) {
                Text(destino.nombre, style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(4.dp))
                Text(
                    "${destino.pais} • $${"%.2f".format(destino.precio)}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    destino.descripcion,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2
                )
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = { onEdit(destino) }) {
                        Text(stringResource(R.string.update))
                    }
                    TextButton(onClick = { onDelete(destino) }) {
                        Text(stringResource(R.string.delete))
                    }
                }
            }
        }
    }
}