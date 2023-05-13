package com.bishal.lazyreader.repository


import android.util.Log
import com.bishal.lazyreader.data.DataOrException
import com.bishal.lazyreader.model.MBook
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject



class FireRepository @Inject constructor(
    private val queryBook: Query
) {
    suspend fun getAllBooksFromDatabase(): Flow<DataOrException<List<MBook>, Boolean, Exception>> =
        flow {
            val dataOrException = DataOrException<List<MBook>, Boolean, Exception>()

            try {
                dataOrException.loading = true
                val querySnapshot = queryBook.get().await()

                dataOrException.data = querySnapshot.documents.map { documentSnapshot ->
                    documentSnapshot.toObject(MBook::class.java)!!
                }
                emit(dataOrException)

                queryBook.addSnapshotListener { snapshot, exception ->
                    if (exception != null) {
                        dataOrException.e = exception
                        Log.d("exception", "ex${exception.message.toString()}")
                    } else {
                        dataOrException.loading = false
                        dataOrException.data = snapshot!!.documents.map { documentSnapshot ->
                            documentSnapshot.toObject(MBook::class.java)!!
                        }
                    }
                }
                emit(dataOrException)

            } catch (exception: FirebaseFirestoreException) {
                dataOrException.e = exception
                emit(dataOrException)
            }
        }
}
