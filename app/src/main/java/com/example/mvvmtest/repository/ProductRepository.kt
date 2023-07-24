package com.example.mvvmtest.repository

import com.example.mvvmtest.api.ProductApi
import com.example.mvvmtest.models.ProductListItem
import com.example.mvvmtest.utils.NetworkResult

class ProductRepository(private val prodApi: ProductApi) {

    suspend fun getProduct(): NetworkResult<List<ProductListItem>> {

        return try {
            val response = prodApi.getProducts()
            val body = response.body()
            if (response.isSuccessful && body != null)
                NetworkResult.Success(body)
            else
                NetworkResult.Error("Somthing went wrong")
        } catch (e: Exception) {
            NetworkResult.Error("Somthing went wrong")
        }

    }
}