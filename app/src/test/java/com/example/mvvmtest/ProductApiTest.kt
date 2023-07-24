package com.example.mvvmtest

import com.example.mvvmtest.api.ProductApi
import com.google.common.truth.Truth
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class ProductApiTest {

    lateinit var mockWebServer:MockWebServer
    lateinit var productApi: ProductApi

    @Before
    fun setUp(){
        mockWebServer = MockWebServer()
        productApi = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build().create()
    }

    @Test
    fun test_empty_getProduct() = runTest {
        val mockResponse = MockResponse()
        mockResponse.setBody("[]")
        mockWebServer.enqueue(mockResponse)
        val respose = productApi.getProducts()
        mockWebServer.takeRequest()

        Truth.assertThat(respose.body()!!.isEmpty()).isTrue()
    }

    @Test
    fun `test getproduct with some data`() = runTest {
        val mockResponse = MockResponse()
        val content = Helper.readFileResource("/response.json")
        mockResponse.setResponseCode(200)
        mockResponse.setBody(content)
        mockWebServer.enqueue(mockResponse)
        val respose = productApi.getProducts()
        mockWebServer.takeRequest()

        Truth.assertThat(respose.isSuccessful).isTrue()
        Truth.assertThat(respose.body()!!.isEmpty()).isFalse()
        Truth.assertThat(respose.body()!!.size).isEqualTo(2)
    }
    @After
    fun tearDown(){
        mockWebServer.shutdown()
    }
}