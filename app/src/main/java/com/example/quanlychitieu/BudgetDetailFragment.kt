package com.example.quanlychitieu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider // QUAN TRỌNG: Import ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quanlychitieu.adapter.BudgetDetailAdapter
import com.example.quanlychitieu.databinding.FragmentBudgetDetailBinding
// BudgetItem đã được import trong Adapter nên không cần ở đây nếu không dùng trực tiếp
import com.example.quanlychitieu.viewmodel.BudgetViewModel // QUAN TRỌNG: Import BudgetViewModel
// java.util.Date không cần import ở đây nữa nếu bạn không tạo dữ liệu mẫu

class BudgetDetailFragment : Fragment() {

    private var _binding: FragmentBudgetDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var budgetAdapter: BudgetDetailAdapter
    private lateinit var budgetViewModel: BudgetViewModel // Khai báo ViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBudgetDetailBinding.inflate(inflater, container, false)
        // Khởi tạo ViewModel
        budgetViewModel = ViewModelProvider(this).get(BudgetViewModel::class.java)

        setupRecyclerView()
        observeViewModel() // Bỏ comment và sử dụng hàm này

        // Không cần loadBudgetData() với dữ liệu mẫu nữa
        // budgetViewModel.loadAllTransactionsAsBudgetItems() // ViewModel tự gọi khi init,
        // hoặc bạn có thể gọi ở onResume nếu muốn làm mới

        return binding.root
    }

    private fun setupRecyclerView() {
        budgetAdapter = BudgetDetailAdapter()
        binding.rvBudgetDetails.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = budgetAdapter
        }
    }

    // Bỏ comment và sử dụng hàm này để quan sát dữ liệu từ ViewModel
    private fun observeViewModel() {
        budgetViewModel.budgetItemsLiveData.observe(viewLifecycleOwner) { budgetItems ->
            // Kiểm tra xem danh sách có rỗng không để hiển thị thông báo nếu cần
            if (budgetItems.isNullOrEmpty()) {
                binding.rvBudgetDetails.visibility = View.GONE
                // binding.tvNoBudgetDataMessage.visibility = View.VISIBLE // Thêm một TextView cho thông báo này trong XML
            } else {
                binding.rvBudgetDetails.visibility = View.VISIBLE
                // binding.tvNoBudgetDataMessage.visibility = View.GONE
                budgetAdapter.submitList(budgetItems)
            }
        }
    }

    // Bạn có thể thêm hàm này để làm mới dữ liệu khi Fragment quay trở lại
    override fun onResume() {
        super.onResume()
        budgetViewModel.loadAllTransactionsAsBudgetItems() // Tải lại/làm mới dữ liệu
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}