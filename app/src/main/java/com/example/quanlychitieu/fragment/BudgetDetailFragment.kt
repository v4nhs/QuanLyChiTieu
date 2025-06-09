package com.example.quanlychitieu.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quanlychitieu.R
import com.example.quanlychitieu.adapter.BudgetDetailAdapter
import com.example.quanlychitieu.connectdb.AppDatabase
import com.example.quanlychitieu.databinding.FragmentBudgetDetailBinding
import com.example.quanlychitieu.entity.BudgetItem
import com.example.quanlychitieu.viewmodel.BudgetViewModelFactory
import com.example.quanlychitieu.viewmodel.BudgetViewModel
import com.example.quanlychitieu.repository.TransactionRepository

class BudgetDetailFragment : Fragment() {

    private var _binding: FragmentBudgetDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var budgetViewModel: BudgetViewModel
    private lateinit var budgetAdapter: BudgetDetailAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentBudgetDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        setupRecyclerView()
        observeViewModel()
    }

    private fun setupViewModel() {
        val repository = TransactionRepository(AppDatabase.getDatabase(requireContext()).transactionDao())
        val factory = BudgetViewModelFactory(repository)
        budgetViewModel = ViewModelProvider(this, factory).get(BudgetViewModel::class.java)
    }

    private fun setupRecyclerView() {
        // Khởi tạo adapter với lambda function xử lý sự kiện xóa
        budgetAdapter = BudgetDetailAdapter { budgetItem ->
            showDeleteConfirmationDialog(budgetItem)
        }
        binding.rvBudgetDetails.adapter = budgetAdapter
        binding.rvBudgetDetails.layoutManager = LinearLayoutManager(requireContext())
    }

    // Hàm hiển thị hộp thoại xác nhận xóa
    private fun showDeleteConfirmationDialog(budgetItem: BudgetItem) {
        AlertDialog.Builder(requireContext())
            .setTitle("Xác nhận xóa")
            .setMessage("Bạn có chắc chắn muốn xóa giao dịch này không?")
            .setIcon(R.drawable.ic_delete)
            .setPositiveButton("Xóa") { _, _ ->
                budgetViewModel.deleteTransaction(budgetItem)
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    private fun observeViewModel() {
        budgetViewModel.budgetItems.observe(viewLifecycleOwner) { budgetItems ->
            val isEmpty = budgetItems.isNullOrEmpty()
            binding.rvBudgetDetails.visibility = if (isEmpty) View.GONE else View.VISIBLE
            binding.tvNoBudgetDataMessage.visibility = if (isEmpty) View.VISIBLE else View.GONE
            budgetAdapter.submitList(budgetItems)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}