package com.example.quanlychitieu

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var btnTienChi: Button
    private lateinit var btnTienThu: Button
    private lateinit var edtNgay: EditText
    private lateinit var edtGhiChu: EditText
    private lateinit var edtThuNhap: EditText
    private lateinit var recyclerViewCategories: RecyclerView
    private lateinit var btnNhapKhoanChi: Button

    private var isIncomeSelected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        setupCategoryRecyclerView()
        setupClickListeners()
    }

    private fun initViews() {
        btnTienChi = findViewById(R.id.btn_tien_chi)
        btnTienThu = findViewById(R.id.btn_tien_thu)
        edtNgay = findViewById(R.id.edt_ngay)
        edtGhiChu = findViewById(R.id.edt_ghi_chu)
        edtThuNhap = findViewById(R.id.edt_thu_nhap)
        recyclerViewCategories = findViewById(R.id.recycler_categories)
        btnNhapKhoanChi = findViewById(R.id.btn_nhap_khoan_chi)

        // Set initial state
        updateButtonStates()
    }

    private fun setupCategoryRecyclerView() {
        val categories = listOf(
            Category("Ăn uống", R.drawable.ic_food, "#FF9800"),
            Category("Hàng ngày", R.drawable.ic_daily, "#4CAF50"),
            Category("Quần áo", R.drawable.ic_clothes, "#F44336"),
            Category("Mỹ phẩm", R.drawable.ic_beauty, "#3F51B5"),
            Category("Phí giao lưu", R.drawable.ic_social, "#FF5722"),
            Category("Y tế", R.drawable.ic_medical, "#009688")
        )

        recyclerViewCategories.layoutManager = GridLayoutManager(this, 3)
        recyclerViewCategories.adapter = CategoryAdapter(categories) { category ->
            // Handle category selection
            onCategorySelected(category)
        }
    }

    private fun setupClickListeners() {
        btnTienChi.setOnClickListener {
            isIncomeSelected = false
            updateButtonStates()
        }

        btnTienThu.setOnClickListener {
            isIncomeSelected = true
            updateButtonStates()
        }

        btnNhapKhoanChi.setOnClickListener {
            // Handle submit action
            submitExpense()
        }
    }

    private fun updateButtonStates() {
        if (isIncomeSelected) {
            btnTienThu.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_200))
            btnTienChi.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_light))
        } else {
            btnTienChi.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_200))
            btnTienThu.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_light))
        }
    }

    private fun onCategorySelected(category: Category) {
        // Handle category selection logic
        // You can store selected category or update UI accordingly
    }

    private fun submitExpense() {
        val date = edtNgay.text.toString()
        val note = edtGhiChu.text.toString()
        val amount = edtThuNhap.text.toString()

        // Validate inputs
        if (date.isEmpty() || note.isEmpty() || amount.isEmpty()) {
            // Show error message
            return
        }

        // Process the expense/income entry
        // Save to database or perform other actions
    }
}

data class Category(
    val name: String,
    val iconResId: Int,
    val color: String
)

class CategoryAdapter(
    private val categories: List<Category>,
    private val onCategoryClick: (Category) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    private var selectedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.bind(category, position == selectedPosition)

        holder.itemView.setOnClickListener {
            val previousPosition = selectedPosition
            selectedPosition = position
            notifyItemChanged(previousPosition)
            notifyItemChanged(selectedPosition)
            onCategoryClick(category)
        }
    }

    override fun getItemCount() = categories.size

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtCategoryName: TextView = itemView.findViewById(R.id.txt_category_name)
        private val imgCategory: View = itemView.findViewById(R.id.img_category)

        fun bind(category: Category, isSelected: Boolean) {
            txtCategoryName.text = category.name

            // Set background color based on selection
            if (isSelected) {
                itemView.setBackgroundColor(
                    ContextCompat.getColor(itemView.context, R.color.teal_200)
                )
            } else {
                itemView.setBackgroundColor(
                    ContextCompat.getColor(itemView.context, android.R.color.white)
                )
            }
        }
    }
}
