package agencia.viajes.data

import com.google.firebase.auth.FirebaseAuth

class AuthRepository {
    private val auth = FirebaseAuth.getInstance()

    val currentUser get() = auth.currentUser
    val isLoggedIn get() = currentUser != null

    fun login(email: String, password: String, onResult: (String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) onResult(null)
                else onResult(task.exception?.message ?: "Error al iniciar sesión")
            }
    }

    fun register(email: String, password: String, onResult: (String?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) onResult(null)
                else onResult(task.exception?.message ?: "Error al registrar")
            }
    }

    fun logout() = auth.signOut()
}