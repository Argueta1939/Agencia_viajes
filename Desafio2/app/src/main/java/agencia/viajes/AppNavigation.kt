package agencia.viajes

import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import agencia.viajes.data.AuthRepository
import agencia.viajes.ui.auth.AuthScreen
import agencia.viajes.ui.catalog.CatalogScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val authRepo = remember { AuthRepository() }
    var isLoggedIn by remember { mutableStateOf(authRepo.isLoggedIn) }

    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) "catalog" else "auth"
    ) {
        composable("auth") {
            AuthScreen(
                onLogin = { e, p, cb -> authRepo.login(e, p) { err ->
                    if (err == null) { isLoggedIn = true; navController.navigate("catalog") }
                    else cb(err)
                }},
                onRegister = { e, p, cb -> authRepo.register(e, p) { err ->
                    if (err == null) { isLoggedIn = true; navController.navigate("catalog") }
                    else cb(err)
                }}
            )
        }
        composable("catalog") {
            CatalogScreen(onLogout = {
                authRepo.logout(); isLoggedIn = false
                navController.navigate("auth") { popUpTo(0) }
            })
        }
    }
}