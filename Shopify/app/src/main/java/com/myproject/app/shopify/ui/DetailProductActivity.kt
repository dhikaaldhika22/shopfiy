package com.myproject.app.shopify.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.myproject.app.shopify.R
import com.myproject.app.shopify.data.response.Product
import com.myproject.app.shopify.data.response.ResultResponse
import com.myproject.app.shopify.databinding.ActivityDetailProductBinding
import com.myproject.app.shopify.viewmodel.DetailProductViewModel
import com.myproject.app.shopify.viewmodel.HomeViewModel
import com.myproject.app.shopify.viewmodel.ViewModelFactory
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DetailProductActivity : AppCompatActivity(), View.OnClickListener {
    private var _binding: ActivityDetailProductBinding? = null
    private val binding get() =_binding
    private var productDetail: Product? = null
    private var id: Int? = null
    private var isCheckout: Boolean? = null

    private val detailViewModel: DetailProductViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    private val detailProductViewModel: HomeViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailProductBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        id = intent.extras?.get(EXTRA_ID_PRODUCT) as Int
        setToolbar(getString(R.string.detail))

        binding?.fabCheckout?.setOnClickListener(this)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    detailProductViewModel.product.collect { result ->
                        onProductDetail(result)
                    }
                }

                launch {
                    detailViewModel.isCheckout(id!!).collect { state ->
                        isCheckout(state)
                        isCheckout = state
                    }
                }

                launch {
                    detailViewModel.isLoading.collect { loaded ->
                        if (!loaded) detailProductViewModel.getProductDetail(id!!)
                        else showLoading(true)
                    }
                }
            }
        }
    }

    private fun setToolbar(title: String) {
        setSupportActionBar(binding?.toolbar)
        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            this.title = title
        }
    }

    private fun isCheckout(checkout: Boolean) {
        if (checkout) {
            binding?.fabCheckout?.setImageResource(R.drawable.ic_baseline_favorite)
        } else {
            binding?.fabCheckout?.setImageResource(R.drawable.ic_baseline_favorite_border)
        }
    }

    private fun getDetailProduct(product: Product) {
        binding?.apply {
            ivProductImage.setImageGlide(this@DetailProductActivity, product.image)
            tvProductName.text = product.title
            tvProductPrice.text = "$" + product.price
            tvCategory.text = product.category
            tvDesc.text = product.description
        }
    }

    private fun onProductDetail(result: ResultResponse<Product>) {
        when (result) {
            is ResultResponse.Loading -> showLoading(true)
            is ResultResponse.Error -> {
                showLoading(false)
                Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
            }
            is ResultResponse.Success ->{
                result.data.let { product ->
                    getDetailProduct(product)

                    val productModel = Product(product.id, product.title, product.price, product.description, product.category, product.image, true)
                    productDetail = productModel
                }
                showLoading(false)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()

        return true
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fab_checkout -> {
                if (isCheckout == false) {
                    productDetail?.let {
                        detailViewModel.saveToCheckout(it)
                    }
                    isCheckout(true)
                    Toast.makeText(this@DetailProductActivity, "Success add to Checkout!", Toast.LENGTH_SHORT).show()
                } else {
                    productDetail?.let {
                        detailViewModel.deleteCheckout(it)
                    }
                    isCheckout(false)
                    Toast.makeText(this@DetailProductActivity, "Removed to your Checkout list!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding?.apply {
                pbLoading.visibility = View.VISIBLE
                fabCheckout.visibility = View.GONE
            }
        } else {
            binding?.apply {
                pbLoading.visibility = View.GONE
                fabCheckout.visibility = View.VISIBLE
            }
        }
    }

    companion object {
        const val EXTRA_ID_PRODUCT = "extra_id_product"
        fun ImageView.setImageGlide(context: Context, url: String) {
            Glide
                .with(context)
                .load(url)
                .placeholder(R.drawable.placeholder)
                .into(this)
        }
    }

}