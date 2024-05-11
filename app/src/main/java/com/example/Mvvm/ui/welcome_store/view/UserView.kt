package com.example.Mvvm.ui.welcome_store.view

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.view.View
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.example.Mvvm.R
import com.example.Mvvm.api.authentication.model.Data
import com.example.Mvvm.api.authentication.model.MvvmUserData
import com.example.Mvvm.base.ConstraintLayoutWithLifecycle
import com.example.Mvvm.databinding.UserItemViewBinding

class UserView(context: Context) : ConstraintLayoutWithLifecycle(context) {
    private lateinit var binding: UserItemViewBinding
    private lateinit var data: Data

    init {
        inflateUi()
    }

    private fun inflateUi() {
        val view = View.inflate(context, R.layout.user_item_view, this)
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        binding = UserItemViewBinding.bind(view)

    }

    @SuppressLint("SetTextI18n")
    fun bind(data: Data) {
        this.data = data
        binding.apply {
            Glide.with(context)
                .load(data.avatar)
                .centerCrop()
                .placeholder(resources.getDrawable(R.drawable.ic_launcher_background, null))
                .into(ivUserProfileImage)
            tvUserName.text = data.first_name + data.last_name
            tvUserEmail.text = data.email
        }
    }
}