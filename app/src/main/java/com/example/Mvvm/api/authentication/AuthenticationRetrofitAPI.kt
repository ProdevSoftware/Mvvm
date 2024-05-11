package com.example.Mvvm.api.authentication

import com.example.Mvvm.api.authentication.model.*
import com.example.Mvvm.base.network.ErrorType
import com.example.Mvvm.base.network.model.MvvmResponse
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.*

interface AuthenticationRetrofitAPI {

    @GET("v1/get-location-by-pos/{serial-number}")
    @ErrorType
    fun getLocation(@Path("serial-number") serialNumber: String): Single<MvvmResponse<LocationResponse>>

    @POST("v1/employee-access")
    @ErrorType
    fun loginCrew(@Body request: LoginCrewRequest): Single<MvvmResponse<LoginCrewResponse>>

    @GET("v1/get-user/{user_id}")
    @ErrorType
    fun getUserDetails(@Path("user_id") userId: Int): Single<MvvmResponse<MvvmUser>>

    @GET("api/users")
    fun getUsers(@Query("page") page: Int): Call<MvvmUserData>
}