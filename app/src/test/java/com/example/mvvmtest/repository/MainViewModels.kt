package com.example.mvvmtest.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvmtest.models.ProductListItem
import com.example.mvvmtest.repository.ProductRepository
import com.example.mvvmtest.utils.NetworkResult
import kotlinx.coroutines.launch

class MainViewModels(private val repository: ProductRepository):ViewModel() {

    private val _products = MutableLiveData<NetworkResult<List<ProductListItem>>>()
    val products: LiveData<NetworkResult<List<ProductListItem>>>
    get() = _products

    fun getProducts(){
        viewModelScope.launch {
            val result = repository.getProduct()
            _products.postValue(result)
        }
    }
}