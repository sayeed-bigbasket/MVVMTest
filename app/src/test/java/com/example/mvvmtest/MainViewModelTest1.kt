package com.example.mvvmtest.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvmtest.models.ProductListItem
import com.example.mvvmtest.repository.ProductRepository
import com.example.mvvmtest.utils.NetworkResult
import com.google.common.truth.Truth.assertThat
import getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class MainViewModelsTest1 {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: ProductRepository

    private lateinit var mainViewModels: MainViewModels

    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        mainViewModels = MainViewModels(repository)
    }

    @Test
    fun `getProducts should return success with empty list when repository returns empty list`() = runTest {
        Mockito.`when`(repository.getProduct()).thenReturn(NetworkResult.Success(emptyList()))

        mainViewModels.getProducts()
        testDispatcher.advanceUntilIdle()

        val result = mainViewModels.products.getOrAwaitValue()
        assertThat(result).isInstanceOf(NetworkResult.Success::class.java)
        assertThat(result.data).isEmpty()
    }

    @Test
    fun `getProducts should return success with non-empty list when repository returns non-empty list`() = runTest {
        val productList = listOf(ProductListItem("test1", "img1"))
        Mockito.`when`(repository.getProduct()).thenReturn(NetworkResult.Success(productList))

        mainViewModels.getProducts()
        testDispatcher.advanceUntilIdle()

        val result = mainViewModels.products.getOrAwaitValue()
        assertThat(result).isInstanceOf(NetworkResult.Success::class.java)
        assertThat(result.data).isEqualTo(productList)
    }

    @Test
    fun `getProducts should return error when repository returns error`() = runTest {
        val errorMessage = "Something went wrong"
        Mockito.`when`(repository.getProduct()).thenReturn(NetworkResult.Error(errorMessage))

        mainViewModels.getProducts()
        testDispatcher.advanceUntilIdle()

        val result = mainViewModels.products.getOrAwaitValue()
        assertThat(result).isInstanceOf(NetworkResult.Error::class.java)
        assertThat(result.message).isEqualTo(errorMessage)
    }
}
