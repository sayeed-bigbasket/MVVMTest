package com.example.mvvmtest

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.mvvmtest.models.ProductListItem
import com.example.mvvmtest.repository.ProductRepository
import com.example.mvvmtest.utils.NetworkResult
import com.example.mvvmtest.viewmodels.MainViewModels
import com.google.common.truth.Truth.assertThat
import getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class MainViewModelsTest {

    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var repository: ProductRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun test_empty_getProducts() = runTest {

        Mockito.`when`(repository.getProduct()).thenReturn(NetworkResult.Success(emptyList()))

        val mainViewModels = MainViewModels(repository)
        mainViewModels.getProducts()
        testDispatcher.scheduler.advanceUntilIdle()
        val result = mainViewModels.products.getOrAwaitValue()

        Assert.assertEquals(0, result.data!!.size)
    }

    @Test
    fun test_getProducts() = runTest {

        Mockito.`when`(repository.getProduct()).thenReturn(
            NetworkResult.Success(
                listOf(
                    ProductListItem("test1", "img1")
                )
            )
        )

        val mainViewModels = MainViewModels(repository)
        mainViewModels.getProducts()
        testDispatcher.scheduler.advanceUntilIdle()
        val result = mainViewModels.products.getOrAwaitValue()

        Assert.assertEquals(1, result.data!!.size)
    }

    @Test
    fun `getproduct Error on Api failure`() = runTest {
        Mockito.`when`(repository.getProduct()).thenReturn(NetworkResult.Error("Something went Wrong"))

        val mainViewModels = MainViewModels(repository)
        mainViewModels.getProducts()

        testDispatcher.scheduler.advanceUntilIdle()

        val result = mainViewModels.products.getOrAwaitValue()
        assertThat(true).isEqualTo(result is NetworkResult.Error)
        assertThat("Something went Wrong").isEqualTo(result.message)
        /*Assert.assertEquals("Something went Wrong",result.message)*/
    }
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

}
