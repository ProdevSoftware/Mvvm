package com.example.Mvvm.api.authentication

import com.example.Mvvm.api.authentication.model.MvvmUser
import com.example.Mvvm.api.authentication.model.LocationResponse
import com.example.Mvvm.api.authentication.model.LoggedInUser
import com.example.Mvvm.api.authentication.model.LoginCrewResponse
import com.example.Mvvm.base.prefs.LocalPrefs
import com.google.gson.Gson
import timber.log.Timber

class LoggedInUserCache(private val localPrefs: LocalPrefs) {
    private var loggedInUser: LoggedInUser? = null

    enum class PreferenceKey(val identifier: String) {
        LOGGED_IN_USER_JSON_KEY("loggedInUser"),
        LOGGED_IN_USER_TOKEN("token"),
        LOCATION_INFO("location_info_json"),
        LOGGED_IN_USER_DETAIL_JSON_KEY("logged_in_user_detail"),
        CART_GROUP_ID("cart_group_id"),
        STRIPE_TOKEN("stripe_token"),
        LOYALTY_USER_DETAILS("loyalty_qr_scan_user_reponse"),
        ORDER_TYPE_ID("order_type_id")
    }

    private var locationInfoLocalPref: LocationResponse?
        get() {
            val locationResponseJson =
                localPrefs.getString(PreferenceKey.LOCATION_INFO.identifier, null)
            try {
                return Gson().fromJson(locationResponseJson, LocationResponse::class.java)
            } catch (e: Exception) {
                Timber.e(e, "Failed to parse logged in user from json string")
            }
            return null
        }
        set(value) {
            val locationResponseJson = Gson().toJson(value)
            localPrefs.putString(PreferenceKey.LOCATION_INFO.identifier, locationResponseJson)
        }



    private var loggedInUserTokenLocalPref: String?
        get() {
            return localPrefs.getString(PreferenceKey.LOGGED_IN_USER_TOKEN.identifier, null)
        }
        set(value) {
            localPrefs.putString(PreferenceKey.LOGGED_IN_USER_TOKEN.identifier, value)
        }

    private var cartGroupId: Int?
        get() {
            return localPrefs.getInt(PreferenceKey.CART_GROUP_ID.identifier, 0)
        }
        set(value) {
            value?.let { localPrefs.putInt(PreferenceKey.CART_GROUP_ID.identifier, it) }
        }

    private var loggedInUserStripeTokenLocalPref: String?
        get() {
            return localPrefs.getString(PreferenceKey.STRIPE_TOKEN.identifier, null)
        }
        set(value) {
            localPrefs.putString(PreferenceKey.STRIPE_TOKEN.identifier, value)
        }

    private var orderTypeId: Int?
        get() {
            return localPrefs.getInt(PreferenceKey.ORDER_TYPE_ID.identifier, 0)
        }
        set(value) {
            value?.let { localPrefs.putInt(PreferenceKey.ORDER_TYPE_ID.identifier, it) }
        }

    init {
        loadLoggedInUserFromLocalPrefs()
    }

    fun getLoginUserToken(): String? {
        return loggedInUserTokenLocalPref
    }

    fun isUserLoggedIn(): Boolean {
        return loggedInUser != null
    }

    private fun getLoggedInUser(): LoggedInUser? {
        return loggedInUser
    }

    fun getLoggedInUserCartGroupId(): Int? {
        return cartGroupId
    }

    fun setLoggedInUserCartGroupId(cartGroupId: Int) {
        this.cartGroupId = cartGroupId
    }

    fun getLoggedInUserId(): Int? {
        return getLoggedInUser()?.crewResponse?.userId
    }

    fun getLoggedInUserDetail(): MvvmUser? {
        return loggedInUser?.hotBoxUser
    }

    fun getLoggedInUserFullName(): String {
        val hotBoxUser = getLoggedInUserDetail()
        return "${hotBoxUser?.firstName ?: ""} ${hotBoxUser?.lastName ?: ""}"
    }

    fun getLoggedInUserRole(): String {
        return getLoggedInUser()?.crewResponse?.roleName ?: ""
    }

    fun getLoggedInUserRoleId(): Int {
        return getLoggedInUser()?.crewResponse?.roleId ?: 0
    }

    fun isAdmin(): Boolean {
        return when (getLoggedInUserRoleId()) {
            1, 2, 3, 4 -> true
            else -> false
        }
    }

    fun setLoggedInUserToken(token: String) {
        loggedInUserTokenLocalPref = token
    }

    fun setLoggedInUserStripeToken(token: String) {
        loggedInUserStripeTokenLocalPref = token
    }

    fun getLoggedInUserStripeToken(): String? {
        return loggedInUserStripeTokenLocalPref
    }

    fun setLoggedInUser(loginCrewResponse: LoginCrewResponse, hotBoxUser: MvvmUser) {
        localPrefs.putString(
            PreferenceKey.LOGGED_IN_USER_JSON_KEY.identifier,
            Gson().toJson(loginCrewResponse)
        )
        localPrefs.putString(
            PreferenceKey.LOGGED_IN_USER_DETAIL_JSON_KEY.identifier,
            Gson().toJson(hotBoxUser)
        )
        loadLoggedInUserFromLocalPrefs()
    }

    private fun loadLoggedInUserFromLocalPrefs() {
        val userJsonString =
            localPrefs.getString(PreferenceKey.LOGGED_IN_USER_JSON_KEY.identifier, null)
        val userDetailJsonString =
            localPrefs.getString(PreferenceKey.LOGGED_IN_USER_DETAIL_JSON_KEY.identifier, null)
        var loggedInUser: LoggedInUser? = null
        if (userJsonString != null && userDetailJsonString != null) {
            try {
                loggedInUser = LoggedInUser(
                    Gson().fromJson(userJsonString, LoginCrewResponse::class.java),
                    Gson().fromJson(userDetailJsonString, MvvmUser::class.java)
                )
            } catch (e: Exception) {
                Timber.e(e, "Failed to parse logged in user from json string")
            }
        }
        this.loggedInUser = loggedInUser
    }

    fun clearLoggedInUserLocalPrefs() {
        clearUserPreferences()
    }

    /**
     * Clear previous user preferences, if the current logged in user is different
     */
    private fun clearUserPreferences() {
        try {
            loggedInUser = null
            for (preferenceKey in PreferenceKey.values()) {
                localPrefs.removeValue(preferenceKey.identifier)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun getLocationInfo(): LocationResponse? {
        return locationInfoLocalPref
    }

    fun setLocationInfo(locationResponse: LocationResponse) {
        locationInfoLocalPref = locationResponse
    }
}