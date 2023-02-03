package com.myproject.app.shopify.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myproject.app.shopify.data.repo.Repository
import com.myproject.app.shopify.data.response.Product
import com.myproject.app.shopify.data.response.ResultResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: Repository) : ViewModel() {
    private val _product = MutableStateFlow<ResultResponse<Product>>(ResultResponse.Loading)
    val product = _product.asStateFlow()

    private val _products = MutableStateFlow<ResultResponse<ArrayList<Product>>>(ResultResponse.Loading)
    val products = _products.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun getProduct() {
        viewModelScope.launch {
            repository.getProduct().collect {
                _products.value = it
            }
        }
    }

    fun getProductDetail(id: Int) {
        _product.value = ResultResponse.Loading

        viewModelScope.launch {
            repository.getDetailProduct(id).collect {
                _product.value = it
            }
        }
        _isLoading.value = true
    }
}