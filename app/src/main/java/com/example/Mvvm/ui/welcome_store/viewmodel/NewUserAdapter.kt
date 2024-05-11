package com.example.Mvvm.ui.welcome_store.viewmodel

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.Mvvm.api.authentication.model.Data
import com.example.Mvvm.ui.welcome_store.view.UserView

class NewUserAdapter(private val context: Context): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var adapterItems = listOf<AdapterItem>()

    var listUser: ArrayList<Data>? = null
        set(listOfUserInfo) {
            field = listOfUserInfo
            updateAdapterItem()
        }

    private fun updateAdapterItem() {
        val adapterItems = mutableListOf<AdapterItem>()
        listUser?.let {
            it.forEach { data ->
                adapterItems.add(AdapterItem.UserTypeViewItem(data))
            }
        }
        this.adapterItems = adapterItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ViewType.UserViewItemType.ordinal -> {
                UserInfoViewHolder(UserView(context))
            }

            else -> throw IllegalAccessException("Unsupported ViewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val adapterItem = adapterItems.getOrNull(position) ?: return
        when (adapterItem) {
            is AdapterItem.UserTypeViewItem -> {
                (holder.itemView as UserView).bind(adapterItem.userResponse)
            }
        }
    }

    override fun getItemCount(): Int {
        return adapterItems.size
    }

    private class UserInfoViewHolder(view: View) : RecyclerView.ViewHolder(view)

    sealed class AdapterItem(val type: Int) {
        data class UserTypeViewItem(val userResponse: Data) :
            AdapterItem(ViewType.UserViewItemType.ordinal)
    }

    private enum class ViewType {
        UserViewItemType
    }
}