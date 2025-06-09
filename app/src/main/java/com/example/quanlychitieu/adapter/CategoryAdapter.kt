package com.example.quanlychitieu.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.quanlychitieu.R
import com.example.quanlychitieu.databinding.ItemCategoryBinding // ViewBinding cho item_category.xml
import com.example.quanlychitieu.entity.CategoryItem

class CategoryAdapter(
    private var categories: List<CategoryItem>,
    private val onItemClick: (CategoryItem) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    private var selectedPosition = RecyclerView.NO_POSITION

    inner class CategoryViewHolder(val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(categoryItem: CategoryItem, position: Int) {
            binding.ivCategoryIcon.setImageResource(categoryItem.iconResId)
            binding.tvCategoryName.text = categoryItem.name
            if (position == selectedPosition) {
                binding.root.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.category_selected_background))
            } else {
                val outValue = android.util.TypedValue()
                binding.root.context.theme.resolveAttribute(android.R.attr.selectableItemBackground, outValue, true)
                binding.root.setBackgroundResource(outValue.resourceId)
            }

            binding.root.setOnClickListener {
                if (selectedPosition != position) {
                    val previouslySelectedPosition = selectedPosition
                    selectedPosition = position
                    categoryItem.isSelected = true
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

    fun updateCategories(newCategories: List<CategoryItem>) {
        this.categories = newCategories
        clearSelection() // Xóa lựa chọn cũ
        notifyDataSetChanged() // Báo cho Adapter vẽ lại toàn bộ danh sách
    }
    fun clearSelection() {
        if (selectedPosition != RecyclerView.NO_POSITION) {
            val previouslySelectedPosition = selectedPosition
            categories[previouslySelectedPosition].isSelected = false
            selectedPosition = RecyclerView.NO_POSITION
            notifyItemChanged(previouslySelectedPosition)
        }
    }
}