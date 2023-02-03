package com.myproject.app.shopify.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.myproject.app.shopify.adapter.ProductAdapter
import com.myproject.app.shopify.data.response.Product
import com.myproject.app.shopify.databinding.FragmentCheckoutBinding
import com.myproject.app.shopify.viewmodel.CheckoutViewModel
import com.myproject.app.shopify.viewmodel.ViewModelFactory
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CheckoutFragment : Fragment() {
    private var _binding: FragmentCheckoutBinding? = null
    private val binding get() =_binding

    private val checkoutViewModel: CheckoutViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCheckoutBinding.inflate(layoutInflater, container, false)

        lifecycleScope.launchWhenStarted {
            launch {
                checkoutViewModel.checkout.collect {
                    if (it.isNotEmpty()) getCheckoutData(it)
                    else showInfo()
                }
            }
        }

        return binding?.root
    }

    private fun showInfo() {
        binding?.noData?.visibility = View.VISIBLE
        binding?.rvCheckout?.visibility = View.GONE
    }

    private fun getCheckoutData(product: List<Product>) {
        val list = ArrayList<Product>()

        product.forEach { products ->
            val data = Product(
                products.id,
                products.title,
                products.price,
                products.description,
                products.category,
                products.image,
                false
            )
            list.add(data)
        }

        val productAdapter = ProductAdapter(list)

        binding?.rvCheckout?.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = productAdapter
            visibility = View.VISIBLE
            setHasFixedSize(true)
        }

        binding?.noData?.visibility = View.GONE

        productAdapter.setOnItemClickCallback(object: ProductAdapter.OnItemClickCallback {
            override fun onItemClicked(product: Product) {
                detailCheckout(product)
            }
        })
    }
    private fun detailCheckout(product: Product) {
        Intent(requireContext(), DetailProductActivity::class.java).apply {
            putExtra(DetailProductActivity.EXTRA_ID_PRODUCT, product.id)
        }.also {
            startActivity(it)
        }
    }
}