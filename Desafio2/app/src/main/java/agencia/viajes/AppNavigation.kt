package agencia.viajes

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import agencia.viajes.data.AuthRepository
import agencia.viajes.data.Destino
import agencia.viajes.data.DestinoRepository
import agencia.viajes.data.LocalStorageRepository
import agencia.viajes.ui.auth.AuthScreen
import agencia.viajes.ui.catalog.CatalogScreen
import agencia.viajes.ui.destination.DestinationFormScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val authRepo = remember { AuthRepository() }
    val destinoRepo = remember { DestinoRepository() }
    val context = LocalContext.current
    val localStorage = remember { LocalStorageRepository(context) }

    var isLoggedIn by remember { mutableStateOf(authRepo.isLoggedIn) }

    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) "catalog" else "auth"
    ) {
        // ---------- AUTH ----------
        composable("auth") {
            AuthScreen(
                onLogin = { email, pass, cb ->
                    authRepo.login(email, pass) { err ->
                        if (err == null) {
                            isLoggedIn = true
                            navController.navigate("catalog") {
                                popUpTo("auth") { inclusive = true }
                            }
                        } else cb(err)
                    }
                },
                onRegister = { email, pass, cb ->
                    authRepo.register(email, pass) { err ->
                        if (err == null) {
                            isLoggedIn = true
                            navController.navigate("catalog") {
                                popUpTo("auth") { inclusive = true }
                            }
                        } else cb(err)
                    }
                }
            )
        }

        // ---------- CATALOG ----------
        composable("catalog") {
            CatalogScreen(
                onLogout = {
                    authRepo.logout()
                    isLoggedIn = false
                    navController.navigate("auth") { popUpTo(0) }
                },
                onAdd = { navController.navigate("add") },
                onEdit = { destino -> navController.navigate("edit/${destino.id}") }
            )
        }

        // ---------- ADD ----------
        composable("add") {
            DestinationFormScreen(
                destino = null,
                onSave = { destino, uri ->
                    val rutaImagen = uri?.let { localStorage.saveImage(it) }
                    val destinoFinal = destino.copy(
                        imagenUrl = rutaImagen ?: destino.imagenUrl
                    )
                    destinoRepo.addDestino(destinoFinal) { error ->
                        if (error == null) navController.popBackStack()
                    }
                },
                onCancel = { navController.popBackStack() }
            )
        }

        // ---------- EDIT ----------
        composable("edit/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: return@composable
            var destino by remember { mutableStateOf<Destino?>(null) }

            LaunchedEffect(id) {
                destinoRepo.getDestinoById(id) { destino = it }
            }

            destino?.let { actual ->
                DestinationFormScreen(
                    destino = actual,
                    onSave = { modificado, uri ->
                        val nuevaRuta = uri?.let {
                            localStorage.deleteImage(actual.imagenUrl) // borra la anterior
                            localStorage.saveImage(it)
                        }
                        val destinoFinal = modificado.copy(
                            imagenUrl = nuevaRuta ?: actual.imagenUrl
                        )
                        destinoRepo.updateDestino(destinoFinal) { error ->
                            if (error == null) navController.popBackStack()
                        }
                    },
                    onCancel = { navController.popBackStack() }
                )
            } ?: Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}