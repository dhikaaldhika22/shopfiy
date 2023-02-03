package com.myproject.app.shopify.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.myproject.app.shopify.adapter.ProductAdapter
import com.myproject.app.shopify.data.response.Product
import com.myproject.app.shopify.data.response.ResultResponse
import com.myproject.app.shopify.databinding.FragmentHomeBinding
import com.myproject.app.shopify.ui.DetailProductActivity.Companion.EXTRA_ID_PRODUCT
import com.myproject.app.shopify.viewmodel.HomeViewModel
import com.myproject.app.shopify.viewmodel.ViewModelFactory
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() =_binding
    private val homeViewModel: HomeViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    homeViewModel.products.collect { result ->
                        showLoading(true)
                        listProductAdapter(result)
                    }
                }

                launch {
                    homeViewModel.product.collect {
                        showLoading(true)
                        homeViewModel.getProduct()
                    }
                }

                launch {
                    homeViewModel.isLoading.collect { loaded ->
                        if (loaded) {
                            showLoading(true)
                        } else {
                            showLoading(false)
                        }
                    }
                }
            }
        }

        return binding?.root
    }

    private fun listProductAdapter(result: ResultResponse<ArrayList<Product>>) {
        when (result) {
            is ResultResponse.Loading -> {
                showLoading(true)
            }

            is ResultResponse.Error -> {
                showLoading(false)
            }

            is ResultResponse.Success -> {
                val listProductAdapter = ProductAdapter(result.data)

                binding?.rvListProduct?.apply {
                    layoutManager = LinearLayoutManager(requireContext())
                    adapter = listProductAdapter
                    setHasFixedSize(true)
                }

                listProductAdapter.setOnItemClickCallback(object : ProductAdapter.OnItemClickCallback {
                    override fun onItemClicked(product: Product) {
                        toDetailProduct(product)
                    }
                })
                showLoading(false)
            }
        }

    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) binding?.pbLoading?.visibility = View.VISIBLE
        else binding?.pbLoading?.visibility = View.GONE
    }

    private fun toDetailProduct(product: Product) {
        Intent(requireContext(), DetailProductActivity::class.java).apply {
            putExtra(EXTRA_ID_PRODUCT, product.id).also {
                startActivity(it)
            }
        }
    }

    override fun onDestroy() {
        _binding = null

        super.onDestroy()
    }
}