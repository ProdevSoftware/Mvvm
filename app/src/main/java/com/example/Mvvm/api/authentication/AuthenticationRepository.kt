package com.example.Mvvm.api.authentication

import android.annotation.SuppressLint
import com.example.Mvvm.api.authentication.model.*
import com.example.Mvvm.base.network.MvvmResponseConverter
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AuthenticationRepository(
    private val authenticationRetrofitAPI: AuthenticationRetrofitAPI,
    private val loggedInUserCache: LoggedInUserCache
) {

    private val hotBoxResponseConverter: MvvmResponseConverter = MvvmResponseConverter()

    private val userDataStateObjectRepository: PublishSubject<UserDataStateRepository> = PublishSubject.create()
    val userDataStateRepository: Observable<UserDataStateRepository>? = userDataStateObjectRepository.hide()

    fun loginCrew(request: LoginCrewRequest): Single<MvvmUser> {
        return authenticationRetrofitAPI.loginCrew(request)
            .flatMap { hotBoxResponseConverter.convertToSingle(it) }
            .flatMap { storeUserToken(it) }
            .flatMap { loginCrew ->
                getUserDetails(loginCrew.userId).flatMap {
                    Single.just(LoggedInUser(loginCrew, it))
                }
            }
            .flatMap {
                storeUserInformation(it)
            }
    }



    fun getLocation(serialNumber: String): Single<LocationResponse> {
        return authenticationRetrofitAPI.getLocation(serialNumber).flatMap {
            hotBoxResponseConverter.convertToSingle(it)
        }.doAfterSuccess {
            loggedInUserCache.setLocationInfo(it)
        }
    }

    private fun storeUserToken(loginCrewResponse: LoginCrewResponse): Single<LoginCrewResponse> {
        loggedInUserCache.setLoggedInUserToken(loginCrewResponse.token)
        return Single.just(loginCrewResponse)
    }

    private fun storeUserInformation(loggedInUser: LoggedInUser): Single<MvvmUser> {
        loggedInUserCache.setLoggedInUser(loggedInUser.crewResponse, loggedInUser.hotBoxUser)
        return Single.just(loggedInUser.hotBoxUser)
    }

    private fun getUserDetails(userId: Int): Single<MvvmUser> {
        return authenticationRetrofitAPI.getUserDetails(userId)
            .flatMap { hotBoxResponseConverter.convertToSingle(it) }
    }

    @SuppressLint("CheckResult")
     fun getUsers(page: Int) {
         val retrofit = Retrofit.Builder()
             .baseUrl("https://reqres.in/")
             .addConverterFactory(GsonConverterFactory.create())
             .build()
             .create(AuthenticationRetrofitAPI::class.java)

         val call = retrofit.getUsers(page)

         call.enqueue(object : Callback<MvvmUserData> {
             override fun onResponse(call: Call<MvvmUserData>, response: Response<MvvmUserData>) {
                 if (response.isSuccessful) {
                     if (response.code() == 200) {
                         if(response.body() != null){
                             userDataStateObjectRepository.onNext(UserDataStateRepository.GetUserSuccessMessageRepository(response.body()!!))
                         } else {
                             userDataStateObjectRepository.onNext(UserDataStateRepository.ErrorMessageRepository("Failed"))
                         }
                     }else {
                         userDataStateObjectRepository.onNext(UserDataStateRepository.ErrorMessageRepository("Failed ${response.code()}"))
                     }
                 }else {
                     userDataStateObjectRepository.onNext(UserDataStateRepository.ErrorMessageRepository("Failed"))
                 }
             }

             override fun onFailure(call: Call<MvvmUserData>, t: Throwable) {
                 println("t.message = ${t.message}")
                 userDataStateObjectRepository.onNext(UserDataStateRepository.ErrorMessageRepository("Failed ${t.message}"))
             }
         })
    }

    sealed class UserDataStateRepository {
        data class GetUserSuccessMessageRepository (val users: MvvmUserData) : UserDataStateRepository()
        data class ErrorMessageRepository (val errorMessage: String) : UserDataStateRepository()
    }

}