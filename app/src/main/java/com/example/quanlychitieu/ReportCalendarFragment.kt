package com.example.quanlychitieu // Hoặc package name của bạn

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
// Quan trọng: Import class ViewBinding cho fragment_report_calendar.xml
import com.example.quanlychitieu.databinding.FragmentReportCalendarBinding

class ReportCalendarFragment : Fragment() {

    private var _binding: FragmentReportCalendarBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReportCalendarBinding.inflate(inflater, container, false)
        val view = binding.root
        // TODO: Thêm code xử lý cho các View trong fragment_report_calendar.xml tại đây
        // Ví dụ: binding.tvMonthYearSelector.setOnClickListener { ... }
        //        binding.calendarView.setOnDateChangeListener { ... }
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Tránh memory leak
    }
}