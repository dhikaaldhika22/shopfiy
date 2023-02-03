package com.myproject.app.shopify.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myproject.app.shopify.data.repo.Repository
import com.myproject.app.shopify.data.response.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailProductViewModel(private val repository: Repository): ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun isCheckout(id: Int): Flow<Boolean> = repository.isCheckout(id)

    fun saveToCheckout(product: Product) {
        viewModelScope.launch {
            repository.saveCheckout(product)
        }
    }

    fun deleteCheckout(product: Product) {
        viewModelScope.launch {
            repository.delete(product)
        }
    }
}