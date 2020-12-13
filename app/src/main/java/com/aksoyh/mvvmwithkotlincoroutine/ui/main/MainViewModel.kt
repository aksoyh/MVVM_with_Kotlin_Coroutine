package com.aksoyh.mvvmwithkotlincoroutine.ui.main

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.aksoyh.mvvmwithkotlincoroutine.BaseApplication
import com.aksoyh.mvvmwithkotlincoroutine.data.UserDetail
import com.aksoyh.mvvmwithkotlincoroutine.data.Users
import com.aksoyh.mvvmwithkotlincoroutine.repository.MainRepository
import com.aksoyh.mvvmwithkotlincoroutine.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class MainViewModel(app: Application, private val mainRepository: MainRepository) : AndroidViewModel(app) {

    val allUsers: MutableLiveData<Resource<Users>> = MutableLiveData()
    var allUsersResponse: Users? = null

    val userDetail: MutableLiveData<Resource<UserDetail>> = MutableLiveData()
    var userDetailResponse: UserDetail? = null

    /**
     * Functions below this expression are called from fragment
     */
    fun allUsersFun() = viewModelScope.launch {
        safeUsersCall()
    }

    fun userDetailFun(userId: String) = viewModelScope.launch {
        safeUserDetailCall(userId)
    }

    /**
     * Safe network calls
     */
    private suspend fun safeUsersCall() {
        allUsers.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = mainRepository.getUsers()
                allUsers.postValue(handleUserResponse(response))
            } else {
                allUsers.postValue(Resource.Error("İnternet bağlantısı yok!"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> allUsers.postValue(Resource.Error("Network Hatası"))
                else -> allUsers.postValue(Resource.Error("Bir hata oluştu, yeniden deneyiniz. \n $t"))
            }
        }
    }

    private suspend fun safeUserDetailCall(userId: String) {
        userDetail.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = mainRepository.getUserDetail(userId)
                userDetail.postValue(handleUserDetailResponse(response))
            } else {
                allUsers.postValue(Resource.Error("İnternet bağlantısı yok!"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> allUsers.postValue(Resource.Error("Network Hatası"))
                else -> allUsers.postValue(Resource.Error("Bir hata oluştu, yeniden deneyiniz."))
            }
        }
    }

    /**
     * Transferring data
     */
    private fun handleUserResponse(response: Response<Users>): Resource<Users> {
        if (response.isSuccessful && response.body()!!.total > 0) {
            response.body()?.let { resultResponse ->
                return Resource.Success(allUsersResponse ?: resultResponse)
            }
        }
        return Resource.Error("Kullanıcılar bulunamadı.")
    }

    private fun handleUserDetailResponse(response: Response<UserDetail>): Resource<UserDetail> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(userDetailResponse ?: resultResponse)
            }
        }
        return Resource.Error("Cevap alınamadı")
    }



    /**
     * Internet Connection Check.
     * This func works all request
     */
    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<BaseApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities =
            connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}