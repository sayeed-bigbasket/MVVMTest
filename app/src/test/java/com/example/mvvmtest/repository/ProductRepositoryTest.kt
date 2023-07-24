package com.example.mvvmtest.repository

import com.example.mvvmtest.api.ProductApi
import com.example.mvvmtest.models.ProductListItem
import com.example.mvvmtest.utils.NetworkResult
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.Response

class ProductRepositoryTest {

    @Mock
    lateinit var prodApi: ProductApi
    private lateinit var sut: ProductRepository
    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        /*System under test*/
        sut = ProductRepository(prodApi)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `get product api empty list`() = runTest {

        Mockito.`when`(prodApi.getProducts()).thenReturn(Response.success(emptyList()))

        val result = sut.getProduct()

        Truth.assertThat(result is NetworkResult.Success).isTrue()
        Truth.assertThat(result.data).isEmpty()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `get product api list`() = runTest {
        val productList = listOf(ProductListItem("item", "item image"))
        Mockito.`when`(prodApi.getProducts())
            .thenReturn(Response.success(productList))

        val result = sut.getProduct()

        Truth.assertThat(result is NetworkResult.Success).isTrue()
        Truth.assertThat(result.data).isEqualTo(productList)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `get product api error`() = runTest {

        Mockito.`when`(prodApi.getProducts())
            .thenReturn(Response.error(401, "unauthorized".toResponseBody()))

        val result = sut.getProduct()

        Truth.assertThat(result is NetworkResult.Error).isTrue()
        Truth.assertThat(result.message).isEqualTo("Somthing went wrong")
    }
    @Test
    fun `get product throws exception`() = runTest {

        Mockito.`when`(prodApi.getProducts()).thenThrow(RuntimeException("API call failed"))
        val result = sut.getProduct()

        Truth.assertThat(result is NetworkResult.Error).isTrue()
        Truth.assertThat(result.message).isEqualTo("Somthing went wrong")
    }

    @After
    fun tearDown() {
      // Mockito.verifyNoMoreInteractions(prodApi)
    }
}