package com.example.quanlychitieu.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.quanlychitieu.R
import com.example.quanlychitieu.databinding.ItemBudgetDetailBinding
import com.example.quanlychitieu.entity.BudgetItem
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

class BudgetDetailAdapter(
    private val onDeleteClick: (BudgetItem) -> Unit
) : ListAdapter<BudgetItem, BudgetDetailAdapter.BudgetViewHolder>(BudgetDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BudgetViewHolder {
        // Giả sử tên file binding là ItemBudgetDetailBinding
        val binding = ItemBudgetDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BudgetViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BudgetViewHolder, position: Int) {
        holder.bind(getItem(position), onDeleteClick)
    }

    class BudgetViewHolder(private val binding: ItemBudgetDetailBinding) : RecyclerView.ViewHolder(binding.root) {
        companion object {
            private val currencyFormat = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
            private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        }

        fun bind(budgetItem: BudgetItem, onDeleteClick: (BudgetItem) -> Unit) {
            // SỬ DỤNG ĐÚNG CÁC ID TỪ FILE XML
            binding.tvBudgetType.text = budgetItem.type
            binding.tvBudgetNote.text = budgetItem.note?.takeIf { it.isNotBlank() } ?: ""
            binding.tvBudgetDate.text = dateFormat.format(budgetItem.date)

            val formattedAmount = currencyFormat.format(budgetItem.amount)
            if (budgetItem.isExpense) {
                binding.tvBudgetAmount.text = "-$formattedAmount"
                binding.tvBudgetAmount.setTextColor(ContextCompat.getColor(binding.root.context, R.color.expense_red))
            } else {
                binding.tvBudgetAmount.text = "+$formattedAmount"
                binding.tvBudgetAmount.setTextColor(ContextCompat.getColor(binding.root.context, R.color.income_green))
            }

            binding.ivDelete.setOnClickListener {
                onDeleteClick(budgetItem)
            }
        }
    }
}

class BudgetDiffCallback : DiffUtil.ItemCallback<BudgetItem>() {
    override fun areItemsTheSame(oldItem: BudgetItem, newItem: BudgetItem): Boolean {
        return oldItem.id == newItem.id
    }
    override fun areContentsTheSame(oldItem: BudgetItem, newItem: BudgetItem): Boolean {
        return oldItem == newItem
    }
}