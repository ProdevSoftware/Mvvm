package com.example.Mvvm.ui.welcome_store.viewmodel

import com.example.Mvvm.api.authentication.AuthenticationRepository
import com.example.Mvvm.api.authentication.model.MvvmUser
import com.example.Mvvm.api.authentication.model.LoginCrewRequest
import com.example.Mvvm.api.authentication.model.MvvmUserData
import com.example.Mvvm.base.BaseViewModel
import com.example.Mvvm.base.extension.subscribeAndObserveOnMainThread
import com.example.Mvvm.base.extension.subscribeWithErrorParsing
import com.example.Mvvm.base.network.model.ErrorResult
import com.example.Mvvm.base.network.model.MvvmBoxError
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import timber.log.Timber

class LoginViewModel(private val authenticationRepository: AuthenticationRepository) : BaseViewModel() {

    private val loginStateSubject: PublishSubject<LoginViewState> = PublishSubject.create()
    val loginState: Observable<LoginViewState> = loginStateSubject.hide()

    private val userDataStateObject: PublishSubject<UserDataState> = PublishSubject.create()
    val userDataState: Observable<UserDataState>? = userDataStateObject.hide()

    private var callBackCount: Int = 0

    fun loginCrew(request: LoginCrewRequest) {
        authenticationRepository.loginCrew(request)
            .doOnSubscribe {
                loginStateSubject.onNext(LoginViewState.LoadingState(true))
            }
            .doAfterTerminate {
                loginStateSubject.onNext(LoginViewState.LoadingState(false))
            }.subscribeWithErrorParsing<MvvmUser, MvvmBoxError>({
                loginStateSubject.onNext(LoginViewState.LoginSuccess)
            }, {
                when(it) {
                    is ErrorResult.ErrorMessage -> {
                        loginStateSubject.onNext(LoginViewState.ErrorMessage(it.errorResponse.safeErrorMessage))
                    }
                    is ErrorResult.ErrorThrowable -> {
                        Timber.e(it.throwable)
                    }
                }
            }).autoDispose()
    }

    fun getUsers(page: Int) {
        callBackCount++
        authenticationRepository.getUsers(page)
        authenticationRepository.userDataStateRepository?.subscribeAndObserveOnMainThread {
            when(it){
                is AuthenticationRepository.UserDataStateRepository.GetUserSuccessMessageRepository ->{
                    if(callBackCount < 4){
                        userDataStateObject.onNext(UserDataState.GetUserSuccessMessage(it.users))
                        callBackCount++
                    }
                }
                is AuthenticationRepository.UserDataStateRepository.ErrorMessageRepository ->{
                    userDataStateObject.onNext(UserDataState.ErrorMessage(it.errorMessage))
                }
            }
        }
    }


}

sealed class LoginViewState {
    data class ErrorMessage(val errorMessage: String) : LoginViewState()
    data class LoadingState(val isLoading: Boolean) : LoginViewState()
    object LoginSuccess : LoginViewState()
}

sealed class UserDataState {
    data class GetUserSuccessMessage(val users: MvvmUserData) : UserDataState()
    data class ErrorMessage(val errorMessage: String) : UserDataState()
}