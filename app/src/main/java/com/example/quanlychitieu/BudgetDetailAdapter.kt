package com.example.quanlychitieu.adapter // HOẶC PACKAGE BẠN ĐẶT CÁC ADAPTER

import android.graphics.Color // Import Color nếu bạn dùng Color.parseColor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.quanlychitieu.R // Import R của project
import com.example.quanlychitieu.databinding.ItemBudgetDetailBinding // ViewBinding cho item
import com.example.quanlychitieu.model.BudgetItem // QUAN TRỌNG: Import BudgetItem
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

class BudgetDetailAdapter : ListAdapter<BudgetItem, BudgetDetailAdapter.BudgetViewHolder>(BudgetDiffCallback()) {

    // Định dạng số tiền không có ký hiệu tiền tệ, chỉ có dấu phẩy ngăn cách
    private val numberFormat = NumberFormat.getNumberInstance(Locale("vi", "VN"))
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    inner class BudgetViewHolder(val binding: ItemBudgetDetailBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(budgetItem: BudgetItem) {
            binding.tvBudgetNote.text = budgetItem.note ?: "Không có ghi chú"
            binding.tvBudgetType.text = budgetItem.type
            // Hiển thị số tiền, thêm "đ" vào cuối
            binding.tvBudgetAmount.text = "Số tiền: ${numberFormat.format(budgetItem.amount)}đ"
            binding.tvBudgetDate.text = "Ngày ${dateFormat.format(budgetItem.date)}"

            // Thiết lập icon (bạn cần một hàm để lấy icon dựa trên type hoặc note)
            // Ví dụ đơn giản:
            // binding.ivCalendarIcon.setImageResource(getIconForBudgetType(budgetItem.type))


            if (budgetItem.isExpense) {
                binding.tvBudgetAmount.setTextColor(ContextCompat.getColor(binding.root.context, R.color.expense_red))
            } else {
                binding.tvBudgetAmount.setTextColor(ContextCompat.getColor(binding.root.context, R.color.income_green))
            }
        }

        // Ví dụ hàm lấy icon (bạn cần tạo các drawable tương ứng)
        // private fun getIconForBudgetType(type: String): Int {
        //     return when (type.lowercase(Locale.getDefault())) {
        //         "loại phí y tế" -> R.drawable.ic_medical_budget // Ví dụ
        //         "loại phí đồ ăn" -> R.drawable.ic_food_budget   // Ví dụ
        //         // Thêm các case khác
        //         else -> R.drawable.ic_calendar // Icon mặc định
        //     }
        // }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BudgetViewHolder {
        val binding = ItemBudgetDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BudgetViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BudgetViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class BudgetDiffCallback : DiffUtil.ItemCallback<BudgetItem>() { // Khai báo kiểu BudgetItem ở đây
    override fun areItemsTheSame(oldItem: BudgetItem, newItem: BudgetItem): Boolean {
        return oldItem.id == newItem.id // So sánh bằng ID
    }

    override fun areContentsTheSame(oldItem: BudgetItem, newItem: BudgetItem): Boolean {
        return oldItem == newItem // Data class tự động có hàm equals() phù hợp
    }
}