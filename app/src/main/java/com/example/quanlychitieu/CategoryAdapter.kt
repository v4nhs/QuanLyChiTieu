package com.example.quanlychitieu.adapter // Hoặc package phù hợp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.quanlychitieu.R
import com.example.quanlychitieu.databinding.ItemCategoryBinding // ViewBinding cho item_category.xml
import com.example.quanlychitieu.model.CategoryItem

class CategoryAdapter(
    private val categories: List<CategoryItem>,
    private val onItemClick: (CategoryItem) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    private var selectedPosition = RecyclerView.NO_POSITION

    inner class CategoryViewHolder(val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(categoryItem: CategoryItem, position: Int) {
            binding.ivCategoryIcon.setImageResource(categoryItem.iconResId)
            binding.tvCategoryName.text = categoryItem.name

            // Thay đổi background hoặc hiệu ứng khác nếu item được chọn
            if (position == selectedPosition) {
                binding.root.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.category_selected_background)) // Tạo màu này trong colors.xml
            } else {
//                binding.root.setBackgroundResource(R.drawable.selectable_item_background) // Hoặc drawable mặc định
                val outValue = android.util.TypedValue()
                binding.root.context.theme.resolveAttribute(android.R.attr.selectableItemBackground, outValue, true)
                binding.root.setBackgroundResource(outValue.resourceId)
            }

            binding.root.setOnClickListener {
                if (selectedPosition != position) {
                    val previouslySelectedPosition = selectedPosition
                    selectedPosition = position
                    categoryItem.isSelected = true // Bạn có thể dùng biến isSelected trong CategoryItem nếu muốn quản lý phức tạp hơn

                    // Cập nhật UI cho item mới được chọn và item cũ (nếu có)
                    notifyItemChanged(selectedPosition)
                    if (previouslySelectedPosition != RecyclerView.NO_POSITION) {
                        categories[previouslySelectedPosition].isSelected = false
                        notifyItemChanged(previouslySelectedPosition)
                    }
                    onItemClick(categoryItem)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(categories[position], position)
    }

    override fun getItemCount(): Int = categories.size

    fun clearSelection() {
        if (selectedPosition != RecyclerView.NO_POSITION) {
            val previouslySelectedPosition = selectedPosition
            categories[previouslySelectedPosition].isSelected = false
            selectedPosition = RecyclerView.NO_POSITION
            notifyItemChanged(previouslySelectedPosition)
        }
    }
}