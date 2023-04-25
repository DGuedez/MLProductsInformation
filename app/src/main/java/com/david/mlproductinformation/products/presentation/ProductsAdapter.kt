package com.david.mlproductinformation.products.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.david.mlproductinformation.R
import com.david.mlproductinformation.common.utils.setImage
import com.david.mlproductinformation.databinding.RecyclerViewProductsResultBinding
import com.david.mlproductinformation.products.presentation.model.UIProduct
import javax.inject.Inject

class ProductsAdapter @Inject constructor(val listenerCallback: (String) -> Unit) :
    ListAdapter<UIProduct, ProductsAdapter.FoundProductsVieWHolder>(ITEM_COMPARATOR) {

    companion object {
        const val EMPTY_VALUE = ""
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoundProductsVieWHolder {
        val binding = RecyclerViewProductsResultBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FoundProductsVieWHolder(binding)
    }

    override fun onBindViewHolder(holder: FoundProductsVieWHolder, position: Int) {
        val item: UIProduct = getItem(position)
        val context = holder.itemView.context
        holder.bind(item, context)
        holder.itemView.setOnClickListener { listenerCallback(item.detailUrlLink) }
    }

    inner class FoundProductsVieWHolder(private val binding: RecyclerViewProductsResultBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: UIProduct, context: Context) {
            with(binding) {
                productResultsTitle.text = item.title
                productResultsPrice.text = generatePriceText(item, context)
                productResultsInstallments.text = generateInstallmentText(item, context)
                productResultsArrival.text = generateShippingText(item, context)
                productResultsThumbnail.setImage(item.thumbnail)
            }

        }

        private fun generatePriceText(item: UIProduct, context: Context): CharSequence? {
            return "${context.getString(R.string.currency_symbol)} ${item.price}"
        }

        private fun generateShippingText(item: UIProduct, context: Context): CharSequence {
            return StringBuilder().apply {
                append(context.getString(R.string.shipping_arrives))
                append(context.getString(R.string.empty_space))
                if (item.productFreeShipping) {
                    append(context.getString(R.string.shipping_free))
                    append(context.getString(R.string.empty_space))
                }
                append(context.getString(R.string.shipping_arrives_tomorrow))
            }

        }

        private fun generateInstallmentText(
            installment: UIProduct,
            context: Context
        ): CharSequence {
            return if (installment.InstallmentAmount.isNotEmpty() && installment.installmentQuantity.isNotEmpty()) {
                "${context.getString(R.string.installment_in)} " +
                        "${installment.installmentQuantity} " +
                        "${context.getString(R.string.installment_multiplication)} " +
                        "${installment.InstallmentAmount} " +
                        context.getString(R.string.installment_without_fee)
            } else {
                EMPTY_VALUE
            }
        }
    }
}

private val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<UIProduct>() {
    override fun areItemsTheSame(oldItem: UIProduct, newItem: UIProduct): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: UIProduct, newItem: UIProduct): Boolean {
        return oldItem == newItem
    }
}