package com.example.quanlychitieu.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.quanlychitieu.R

class LimitSettingFragment : Fragment() {

    private lateinit var timeSpinner: Spinner
    private lateinit var limitEditText: EditText
    private lateinit var notifySwitch: Switch
    private lateinit var saveButton: Button
    private lateinit var deleteButton: Button
    private lateinit var exitButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_limit_setting, container, false)

        // Ánh xạ View
        timeSpinner = view.findViewById(R.id.spinner_time)
        limitEditText = view.findViewById(R.id.edit_limit)
        notifySwitch = view.findViewById(R.id.switch_notify)
        saveButton = view.findViewById(R.id.btn_save)
        deleteButton = view.findViewById(R.id.btn_delete)
        exitButton = view.findViewById(R.id.btn_exit)

        // Thiết lập spinner
        val timeOptions = listOf("Ngày", "Tuần", "Tháng")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, timeOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        timeSpinner.adapter = adapter

        // Xử lý sự kiện nút Lưu
        saveButton.setOnClickListener {
            val limitText = limitEditText.text.toString()
            if (limitText.isBlank()) {
                Toast.makeText(context, "Vui lòng nhập hạn mức", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Đã lưu hạn mức!", Toast.LENGTH_SHORT).show()
                // TODO: Lưu hạn mức vào storage hoặc database
            }
        }

        // Xử lý sự kiện nút Xóa
        deleteButton.setOnClickListener {
            limitEditText.text.clear()
            notifySwitch.isChecked = false
            Toast.makeText(context, "Đã xóa hạn mức!", Toast.LENGTH_SHORT).show()
            // TODO: Xóa hạn mức trong storage hoặc database
        }

        // Xử lý sự kiện nút Thoát
        exitButton.setOnClickListener {
            requireActivity().onBackPressed()
        }

        return view
    }
}
