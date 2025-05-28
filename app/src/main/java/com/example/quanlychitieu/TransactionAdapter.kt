package com.example.quanlychitieu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TransactionAdapter(
    private var transactions: List<Transaction>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(transaction: Transaction)
        fun onItemLongClick(transaction: Transaction): Boolean
    }

    inner class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvAmount: TextView = itemView.findViewById(R.id.tvAmount)
        val tvDate: TextView = itemView.findViewById(R.id.tvDate)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(transactions[adapterPosition])
            }
            itemView.setOnLongClickListener {
                listener.onItemLongClick(transactions[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_transaction, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactions[position]
        holder.tvTitle.text = transaction.title
        // Thêm dấu "+" hoặc "-" tùy theo loại giao dịch:
        holder.tvAmount.text = if (transaction.type == "income") {
            "+ ${transaction.amount}"
        } else {
            "- ${transaction.amount}"
        }
        // Định dạng ngày (sử dụng DateFormat)
        holder.tvDate.text = android.text.format.DateFormat.format("dd/MM/yyyy", transaction.date)
    }

    override fun getItemCount(): Int = transactions.size

    fun updateList(newList: List<Transaction>) {
        transactions = newList
        notifyDataSetChanged()
    }
}
