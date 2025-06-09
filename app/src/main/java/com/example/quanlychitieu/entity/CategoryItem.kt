package com.example.quanlychitieu.entity

import androidx.annotation.DrawableRes

data class CategoryItem(
    val name: String,
    @DrawableRes val iconResId: Int,
    var isSelected: Boolean = false
)