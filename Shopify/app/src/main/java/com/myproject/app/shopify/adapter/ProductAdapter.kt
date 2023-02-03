package com.myproject.app.shopify.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.myproject.app.shopify.data.response.Product
import com.myproject.app.shopify.databinding.ListProductBinding

class ProductAdapter(private val listProduct: List<Product>) : RecyclerView.Adapter<ProductAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ListViewHolder(var binding: ListProductBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val binding = ListProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val product = listProduct[position]

        holder.binding.apply {
            Glide.with(ivThumbnailProduct.context)
                .load(product.image)
                .centerCrop()
                .into(ivThumbnailProduct)
            tvName.text = product.title
            tvPrice.text = "$" + product.price
            tvCategory.text = product.category
        }

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(product)
        }
    }

    override fun getItemCount(): Int = listProduct.size

    interface OnItemClickCallback {
        fun onItemClicked(product: Product)
    }
}

