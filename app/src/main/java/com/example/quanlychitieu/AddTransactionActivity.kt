package com.example.quanlychitieu

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

class AddTransactionActivity : AppCompatActivity() {

    private lateinit var tvTitle: TextView
    private lateinit var edtTransactionTitle: EditText
    private lateinit var edtTransactionAmount: EditText
    private lateinit var btnSave: Button
    private lateinit var viewModel: TransactionViewModel
    private var transactionType: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_transaction)

        tvTitle = findViewById(R.id.tvTitle)
        edtTransactionTitle = findViewById(R.id.edtTransactionTitle)
        edtTransactionAmount = findViewById(R.id.edtTransactionAmount)
        btnSave = findViewById(R.id.btnSaveTransaction)

        transactionType = intent.getStringExtra("transaction_type")
        when (transactionType) {
            "expense" -> tvTitle.text = "Nhập giao dịch Chi"
            "income" -> tvTitle.text = "Nhập giao dịch Thu"
            else -> tvTitle.text = "Nhập giao dịch"
        }

        viewModel = ViewModelProvider(this)[TransactionViewModel::class.java]

        btnSave.setOnClickListener {
            val title = edtTransactionTitle.text.toString().trim()
            val amountStr = edtTransactionAmount.text.toString().trim()
            if (title.isEmpty() || amountStr.isEmpty()) {
                edtTransactionTitle.error = "Không được để trống"
                return@setOnClickListener
            }
            val amount = amountStr.toDoubleOrNull() ?: 0.0
            val transaction = Transaction(
                title = title,
                amount = amount,
                type = transactionType ?: "unknown",
                date = System.currentTimeMillis()
            )
            viewModel.insert(transaction)
            finish()
        }
    }
}
