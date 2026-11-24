package com.example.smartexpense.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.smartexpense.R
import com.example.smartexpense.databinding.ItemTransactionRowBinding
import com.example.smartexpense.ui.common.TxRow
import com.example.smartexpense.utils.CurrencyUtils
import com.example.smartexpense.utils.UiUtils

/**
 * Dòng giao dịch dùng chung (Home - Giao dịch gần đây, List - giao dịch trong ngày):
 * badge tròn màu danh mục + icon danh mục, tiêu đề, phụ đề, số tiền +/- (mục 5.4).
 */
class TransactionRowAdapter(
    private val onItemClick: (TxRow) -> Unit
) : ListAdapter<TxRow, TransactionRowAdapter.Holder>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemTransactionRowBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val row = currentList[position]
        val binding = holder.binding
        val context = binding.root.context

        // Badge + icon danh mục: Glide tải từ iconUrl, dự phòng icon cục bộ theo iconKey
        UiUtils.bindCategoryIcon(binding.badgeCircle, binding.ivIcon, row.iconUrl, row.iconKey, row.colorHex)

        binding.tvTitle.text = row.title
        binding.tvSubtitle.text = row.subtitle
        val sign = if (row.income) "+" else "-"
        binding.tvAmount.text = sign + CurrencyUtils.formatVnd(row.amount)
        binding.tvAmount.setTextColor(
            context.getColor(if (row.income) R.color.green else R.color.text_primary)
        )

        binding.root.setOnClickListener { onItemClick(row) }
    }

    class Holder(val binding: ItemTransactionRowBinding) : RecyclerView.ViewHolder(binding.root)

    private companion object {
        val DIFF = object : DiffUtil.ItemCallback<TxRow>() {
            override fun areItemsTheSame(oldItem: TxRow, newItem: TxRow) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: TxRow, newItem: TxRow) =
                oldItem == newItem
        }
    }
}
