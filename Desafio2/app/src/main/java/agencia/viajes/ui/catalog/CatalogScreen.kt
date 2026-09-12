package agencia.viajes.ui.catalog

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import agencia.viajes.R
import agencia.viajes.data.Destino
import agencia.viajes.data.DestinoRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    onLogout: () -> Unit,
    onAdd: () -> Unit,
    onEdit: (Destino) -> Unit
) {
    val repo = remember { DestinoRepository() }
    var destinos by remember { mutableStateOf<List<Destino>>(emptyList()) }
    var destinoAEliminar by remember { mutableStateOf<Destino?>(null) }

    LaunchedEffect(Unit) {
        repo.getDestinos { destinos = it }
    }

    // Diálogo de confirmación de eliminación
    destinoAEliminar?.let { destino ->
        AlertDialog(
            onDismissRequest = { destinoAEliminar = null },
            title = { Text(stringResource(R.string.delete)) },
            text = { Text(stringResource(R.string.confirm_delete)) },
            confirmButton = {
                TextButton(onClick = {
                    repo.deleteDestino(destino.id) { destinoAEliminar = null }
                }) { Text(stringResource(R.string.delete)) }
            },
            dismissButton = {
                TextButton(onClick = { destinoAEliminar = null }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.catalog_title)) },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(
                            Icons.Default.ExitToApp,   // 👈 cambio
                            contentDescription = "Logout"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAdd) {
                Icon(Icons.Default.Add, contentDescription = "Agregar")
            }
        }
    ) { padding ->
        if (destinos.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("No hay destinos registrados")
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(padding).fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(destinos, key = { it.id }) { destino ->
                    DestinoCard(
                        destino = destino,
                        onEdit = onEdit,
                        onDelete = { destinoAEliminar = it }
                    )
                }
            }
        }
    }
}