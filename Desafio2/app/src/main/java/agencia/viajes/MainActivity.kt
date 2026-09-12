package agencia.viajes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import agencia.viajes.ui.theme.AgenciaDeViajesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AgenciaDeViajesTheme {
                Surface {
                    AppNavigation()
                }
            }
        }
    }
}