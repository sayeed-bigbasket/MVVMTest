package com.example.mvvmtest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmtest.adapter.ProdAdapter
import com.example.mvvmtest.utils.NetworkResult
import com.example.mvvmtest.viewmodels.MainViewModels
import com.example.mvvmtest.viewmodels.ViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModels
    lateinit var recyclerView: RecyclerView
    lateinit var adapter:ProdAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById<RecyclerView>(R.id.rcl)
        recyclerView.layoutManager = GridLayoutManager(this,2)

        val repository = (application as StoreApplication).productRepository

         mainViewModel = ViewModelProvider(this, ViewModelFactory(repository)).get(MainViewModels::class.java)

        mainViewModel.getProducts()

        mainViewModel.products.observe(this, Observer {
            when(it){
                is NetworkResult.Success -> {
                    adapter = ProdAdapter(it.data!!)
                    recyclerView.adapter = adapter
                }
                is NetworkResult.Error -> {}
                else -> {

                }
            }
        })

    }
}