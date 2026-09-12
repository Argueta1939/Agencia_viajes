package agencia.viajes.data

import com.google.firebase.firestore.FirebaseFirestore

class DestinoRepository {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("destinos")

    fun getDestinos(onResult: (List<Destino>) -> Unit) {
        collection.addSnapshotListener { snapshot, _ ->
            val lista = snapshot?.documents?.mapNotNull { doc ->
                doc.toObject(Destino::class.java)?.copy(id = doc.id)
            } ?: emptyList()
            onResult(lista)
        }
    }

    fun getDestinoById(id: String, onResult: (Destino?) -> Unit) {
        collection.document(id).get()
            .addOnSuccessListener { doc ->
                onResult(doc.toObject(Destino::class.java)?.copy(id = doc.id))
            }
            .addOnFailureListener { onResult(null) }
    }

    fun addDestino(destino: Destino, onResult: (String?) -> Unit) {
        collection.add(destino)
            .addOnSuccessListener { onResult(null) }
            .addOnFailureListener { onResult(it.message) }
    }

    fun updateDestino(destino: Destino, onResult: (String?) -> Unit) {
        collection.document(destino.id).set(destino)
            .addOnSuccessListener { onResult(null) }
            .addOnFailureListener { onResult(it.message) }
    }

    fun deleteDestino(id: String, onResult: (String?) -> Unit) {
        collection.document(id).delete()
            .addOnSuccessListener { onResult(null) }
            .addOnFailureListener { onResult(it.message) }
    }
}