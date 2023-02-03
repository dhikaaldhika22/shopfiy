package com.myproject.app.shopify.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myproject.app.shopify.data.repo.Repository
import com.myproject.app.shopify.data.response.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CheckoutViewModel(private val repository: Repository) : ViewModel() {
    private val _checkout = MutableStateFlow(listOf<Product>())
    val checkout = _checkout.asStateFlow()

    init {
        getCheckout()
    }

    private fun getCheckout() {
        viewModelScope.launch {
            repository.getCheckout().collect {
                _checkout.value = it
            }
        }
    }
}