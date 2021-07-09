package com.leon.su.data

import android.content.SharedPreferences
import com.blankj.utilcode.util.PathUtils
import com.blankj.utilcode.util.UriUtils
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.leon.su.domain.State
import com.soywiz.klock.DateTime
import com.tonyodev.fetch2.*
import com.tonyodev.fetch2core.DownloadBlock
import java.io.File
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class StorageRepository(
    val mSharedPreferences: SharedPreferences,
    val fetch: Fetch
) {
    suspend fun upload(file: File) = flow {
        emit(State.Loading())
        val upload = Firebase.storage.reference
            .child(DateTime.nowLocal().toString("dd/MM/yyyy"))
            .child(
                "${mSharedPreferences.users?.data?.nama} - ${
                DateTime.nowLocal().toString("HH:mm")
                }"
            )
            .putFile(UriUtils.file2Uri(file))
        upload.await()
        emit(State.Success(true))
    }

    suspend fun fetch() = flow {
        emit(State.Loading())
        val request = Firebase.storage.getReference("/")
            .listAll()
        val result = request.await().prefixes
        emit(State.Success(result))
    }

    suspend fun fetchByReferences(ref: StorageReference) = flow {
        emit(State.Loading())
        val request = ref.listAll()
        val result = request.await().items
        emit(State.Success(result))
    }

    @ExperimentalCoroutinesApi
    suspend fun download(ref: StorageReference) = callbackFlow {
        trySend(State.Loading())
        val file = File("${PathUtils.getInternalAppFilesPath()}/${ref.name}")
        val request = ref.downloadUrl
        val url = request.await()
        val requestDownload = Request(
            url.toString(), UriUtils.file2Uri(file)
        ).apply {
            priority = Priority.HIGH
            networkType = NetworkType.ALL
        }
        val listener = object : FetchListener {
            override fun onAdded(download: Download) {
                // DO Nothing
            }

            override fun onCancelled(download: Download) {
                // DO Nothing
            }

            override fun onCompleted(download: Download) {
                trySend(State.Success(UriUtils.uri2File(download.fileUri)))
                close()
            }

            override fun onDeleted(download: Download) {}
            override fun onDownloadBlockUpdated(
                download: Download,
                downloadBlock: DownloadBlock,
                totalBlocks: Int
            ) {
                // DO Nothing
            }

            override fun onError(download: Download, error: Error, throwable: Throwable?) {
                trySend(State.Failed(throwable ?: Throwable("Gagal Download!")))
                close(throwable)
            }

            override fun onPaused(download: Download) {
                // DO Nothing
            }

            override fun onProgress(
                download: Download,
                etaInMilliSeconds: Long,
                downloadedBytesPerSecond: Long
            ) {
                // DO Nothing
            }

            override fun onQueued(download: Download, waitingOnNetwork: Boolean) {
                // DO Nothing
            }

            override fun onRemoved(download: Download) {
                // DO Nothing
            }

            override fun onResumed(download: Download) {
                // DO Nothing
            }

            override fun onStarted(
                download: Download,
                downloadBlocks: List<DownloadBlock>,
                totalBlocks: Int
            ) {
                // DO Nothing
            }

            override fun onWaitingNetwork(download: Download) {
                // DO Nothing
            }
        }
        fetch.addListener(listener)
        fetch.enqueue(requestDownload)
        awaitClose {
            fetch.removeListener(listener)
            fetch.removeAll()
        }
    }
}
