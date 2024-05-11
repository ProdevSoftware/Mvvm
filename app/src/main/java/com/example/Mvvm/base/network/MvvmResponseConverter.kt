package com.example.Mvvm.base.network

import com.example.Mvvm.base.extension.onSafeError
import com.example.Mvvm.base.extension.onSafeSuccess
import com.example.Mvvm.base.network.model.MvvmCommonResponse
import com.example.Mvvm.base.network.model.MvvmResponse
import io.reactivex.Single

class MvvmResponseConverter {

    fun <T> convert(hotBoxResponse: MvvmResponse<T>?): Single<T> {
        return convertToSingle(hotBoxResponse)
    }

    fun <T> convertToSingle(hotBoxResponse: MvvmResponse<T>?): Single<T> {
        return Single.create { emitter ->
            when {
                hotBoxResponse == null -> emitter.onSafeError(Exception("Failed to process the info"))
                !hotBoxResponse.success -> {
                    emitter.onSafeError(Exception(hotBoxResponse.message))
                }
                hotBoxResponse.success -> {
                    emitter.onSafeSuccess(hotBoxResponse.data)
                }
                else -> emitter.onSafeError(Exception("Failed to process the info"))
            }
        }
    }

    fun <T> convertToSingleWithFullResponse(hotBoxResponse: MvvmResponse<T>?): Single<MvvmResponse<T>> {
        return Single.create { emitter ->
            when {
                hotBoxResponse == null -> emitter.onSafeError(Exception("Failed to process the info"))
                !hotBoxResponse.success -> {
                    emitter.onSafeError(Exception(hotBoxResponse.message))
                }
                hotBoxResponse.success -> {
                    emitter.onSafeSuccess(hotBoxResponse)
                }
                else -> emitter.onSafeError(Exception("Failed to process the info"))
            }
        }
    }

    fun convertCommonResponse(hotBoxCommonResponse: MvvmCommonResponse?): Single<MvvmCommonResponse> {
        return Single.create { emitter ->
            when {
                hotBoxCommonResponse == null -> emitter.onSafeError(Exception("Failed to process the info"))
                !hotBoxCommonResponse.success -> {
                    emitter.onSafeError(Exception(hotBoxCommonResponse.message))
                }
                hotBoxCommonResponse.success -> {
                    emitter.onSafeSuccess(hotBoxCommonResponse)
                }
                else -> emitter.onSafeError(Exception("Failed to process the info"))
            }
        }
    }
}