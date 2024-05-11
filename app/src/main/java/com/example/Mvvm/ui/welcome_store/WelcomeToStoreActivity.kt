package com.example.Mvvm.ui.welcome_store

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.Mvvm.api.authentication.model.Data
import com.example.Mvvm.application.MvvmApplication
import com.example.Mvvm.base.BaseActivity
import com.example.Mvvm.base.ViewModelFactory
import com.example.Mvvm.base.extension.getViewModelFromFactory
import com.example.Mvvm.base.extension.subscribeAndObserveOnMainThread
import com.example.Mvvm.databinding.ActivityWelcomeToStoreBinding
import com.example.Mvvm.ui.welcome_store.viewmodel.LoginViewModel
import com.example.Mvvm.ui.welcome_store.viewmodel.NewUserAdapter
import com.example.Mvvm.ui.welcome_store.viewmodel.UserDataState
import javax.inject.Inject

class WelcomeToStoreActivity : BaseActivity() {

    private lateinit var binding: ActivityWelcomeToStoreBinding
    private var mvvmUserData: List<Data>? = null
    private lateinit var newUserAdapter: NewUserAdapter
    private var pageNo: Int = 1

    @Inject
    internal lateinit var viewModelFactory: ViewModelFactory<LoginViewModel>
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeToStoreBinding.inflate(layoutInflater)
        MvvmApplication.component.inject(this)
        setContentView(binding.root)

        loginViewModel = getViewModelFromFactory(viewModelFactory)
        initUI()
        listenToViewModel()
    }

    @SuppressLint("CheckResult")
    private fun initUI() {
//        {"location_id":7,"user_email":"emp3@gmail.com","user_password":"pass@1234"}
//        loginViewModel.loginCrew(LoginCrewRequest(userEmail = "emp3@gmail.com", userPassword = "pass@1234", locationId = 7))
        newUserAdapter = NewUserAdapter(this@WelcomeToStoreActivity)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@WelcomeToStoreActivity, RecyclerView.VERTICAL, true)
            adapter = newUserAdapter

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, state: Int) {
                    super.onScrollStateChanged(recyclerView, state)
                    if (state == RecyclerView.SCROLL_STATE_IDLE) {
                        val layoutManager = recyclerView.layoutManager ?: return
                        if (pageNo < 2) {
                            pageNo++
                            binding.progressBar.visibility = View.VISIBLE
                            loginViewModel.getUsers(pageNo)
                        } else {
                           showToast("No More Data")
                        }
                    }
                }
            })
        }
        binding.progressBar

    }

    private fun showToast(text: String) {
        Toast.makeText(this,text,Toast.LENGTH_SHORT).show()
    }

    private fun listenToViewModel() {
        loginViewModel.getUsers(pageNo)
        loginViewModel.userDataState?.subscribeAndObserveOnMainThread { it ->
            when (it) {
                is UserDataState.GetUserSuccessMessage -> {
                    Log.d("testing", "in main = ${it.users.data}")
                    mvvmUserData = if(mvvmUserData.isNullOrEmpty()){
                        it.users.data.reversed()
                    } else{
                        it.users.data.reversed() + mvvmUserData!!
                    }
                    newUserAdapter.listUser = mvvmUserData as ArrayList<Data>
                    binding.progressBar.visibility = View.INVISIBLE
                }
                is UserDataState.ErrorMessage -> {
                    Log.d("testing", "errorMessage in main = ${it.errorMessage}")
                }
            }
        }
    }
}