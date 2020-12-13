package com.aksoyh.mvvmwithkotlincoroutine.ui.main

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aksoyh.mvvmwithkotlincoroutine.repository.MainRepository

@Suppress("UNCHECKED_CAST")
class MainViewModelProviderFactory(val app: Application,
                                   private val mainRepository: MainRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(app, mainRepository) as T
    }
}