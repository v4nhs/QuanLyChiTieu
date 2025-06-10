package com.example.quanlychitieu.fragment


import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TimePicker
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.quanlychitieu.R
import java.util.Calendar
import com.example.quanlychitieu.broadcast.ReminderReceiver


class ReminderTimeFragment : Fragment() {

    private lateinit var timePicker: TimePicker
    private lateinit var btnSave: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_reminder_time, container, false)

        timePicker = view.findViewById(R.id.timePicker)
        btnSave = view.findViewById(R.id.btnSaveTime)

        timePicker.setIs24HourView(true)

        // 🔸 Kiểm tra và xin quyền gửi thông báo nếu đang chạy Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    "android.permission.POST_NOTIFICATIONS" // ← Gõ chuỗi trực tiếp
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf("android.permission.POST_NOTIFICATIONS"), // ← Gõ chuỗi trực tiếp
                    100
                )
            }
        }

        btnSave.setOnClickListener {
            val hour = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                timePicker.hour
            } else {
                timePicker.currentHour
            }

            val minute = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                timePicker.minute
            } else {
                timePicker.currentMinute
            }

            saveReminderTime(hour, minute)
            setReminder(hour, minute)
            Toast.makeText(context, "Đã lưu giờ nhắc: $hour:$minute", Toast.LENGTH_SHORT).show()
        }

        return view
    }

    private fun saveReminderTime(hour: Int, minute: Int) {
        val prefs = requireContext().getSharedPreferences("reminder_settings", Context.MODE_PRIVATE)
        prefs.edit().putInt("hour", hour).putInt("minute", minute).apply()
    }

    private fun setReminder(hour: Int, minute: Int) {
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), ReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            if (before(Calendar.getInstance())) {
                add(Calendar.DATE, 1) // nếu thời gian đã qua, đặt cho ngày mai
            }
        }

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }
}
