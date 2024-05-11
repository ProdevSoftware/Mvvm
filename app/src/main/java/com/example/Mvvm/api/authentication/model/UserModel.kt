package com.example.Mvvm.api.authentication.model


data class MvvmUserData(
    var data: List<Data>,
    var page: Int,
    var per_page: Int,
    var support: Support,
    var total: Int,
    var total_pages: Int
)

data class Data(
    var avatar: String,
    var email: String,
    var first_name: String,
    var id: Int,
    var last_name: String
)

data class Support(
    var text: String,
    var url: String
)