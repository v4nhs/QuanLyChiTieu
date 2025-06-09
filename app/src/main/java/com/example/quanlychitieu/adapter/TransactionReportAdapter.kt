// File: adapter/TransactionReportAdapter.kt
package com.example.quanlychitieu.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.quanlychitieu.R
import com.example.quanlychitieu.entity.TransactionEntity
import com.example.quanlychitieu.databinding.ItemTransactionReportBinding // Cần tạo file layout này
import java.text.NumberFormat
import java.util.*

class TransactionReportAdapter : ListAdapter<TransactionEntity, TransactionReportAdapter.TransactionViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val binding = ItemTransactionReportBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransactionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class TransactionViewHolder(private val binding: ItemTransactionReportBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(transaction: TransactionEntity) {
            binding.tvCategoryName.text = transaction.category
            binding.tvTransactionNote.text = transaction.note

            val formattedAmount = NumberFormat.getCurrencyInstance(Locale("vi", "VN")).format(transaction.amount)
            if (transaction.type == "CHI") {
                binding.tvTransactionAmount.text = "-$formattedAmount"
                binding.tvTransactionAmount.setTextColor(ContextCompat.getColor(binding.root.context, R.color.expense_red))
            } else {
                binding.tvTransactionAmount.text = "+$formattedAmount"
                binding.tvTransactionAmount.setTextColor(ContextCompat.getColor(binding.root.context, R.color.income_green))
            }
            // TODO: Thêm logic để hiển thị icon cho danh mục nếu cần
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<TransactionEntity>() {
        override fun areItemsTheSame(oldItem: TransactionEntity, newItem: TransactionEntity): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: TransactionEntity, newItem: TransactionEntity): Boolean {
            return oldItem == newItem
        }
    }
}