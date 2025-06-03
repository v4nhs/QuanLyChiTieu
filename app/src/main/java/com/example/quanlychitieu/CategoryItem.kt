package com.example.quanlychitieu.model // Hoặc package phù hợp

import androidx.annotation.DrawableRes

data class CategoryItem(
    val name: String,
    @DrawableRes val iconResId: Int,
    var isSelected: Boolean = false // Để quản lý trạng thái được chọn
)