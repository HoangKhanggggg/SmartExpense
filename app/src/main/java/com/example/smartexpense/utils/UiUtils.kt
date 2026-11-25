package com.example.smartexpense.utils

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.drawable.DrawableCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.smartexpense.R

/** Hỗ trợ icon và màu danh mục */
object UiUtils {

    /** Tìm drawable theo tên iconKey của danh mục; không có thì dùng icon chung */
    fun categoryIconId(context: Context, iconKey: String?): Int {
        if (iconKey.isNullOrBlank()) return R.drawable.ic_category_generic
        val id = context.resources.getIdentifier(iconKey, "drawable", context.packageName)
        return if (id != 0) id else R.drawable.ic_category_generic
    }

    /** Đọc mã màu danh mục; giá trị không hợp lệ -> xanh chủ đạo */
    fun parseColor(hex: String?): Int {
        if (hex.isNullOrBlank()) return Color.parseColor("#2563EB")
        return runCatching { Color.parseColor(hex) }.getOrDefault(Color.parseColor("#2563EB"))
    }

    /** Độ đậm của badge khi hiển thị ảnh màu tải từ mạng (0x33 = 20%) */
    private const val BADGE_ALPHA_REMOTE = 0x33

    private fun tintBadge(badge: View, color: Int) {
        badge.background?.let { drawable ->
            DrawableCompat.setTint(DrawableCompat.wrap(drawable.mutate()), color)
        }
    }

    /**
     * Hiển thị icon danh mục (Cột mốc 5 - Glide):
     * - iconUrl có giá trị: Glide tải ảnh bất đồng bộ và cache đĩa. Trong lúc chờ, hoặc khi
     *   lỗi/mất mạng, vẫn hiện icon cục bộ theo iconKey (badge đậm màu + icon trắng như cũ).
     * - iconUrl rỗng: dùng icon cục bộ.
     * Ảnh tải về là ảnh màu nên bỏ tint trắng và làm nhạt badge để icon nổi rõ.
     * Dùng được trong RecyclerView: Glide tự hủy yêu cầu cũ khi ViewHolder được tái sử dụng.
     */
    fun bindCategoryIcon(badge: View, icon: ImageView, iconUrl: String?, iconKey: String?, colorHex: String?) {
        val color = parseColor(colorHex)
        val localRes = categoryIconId(icon.context, iconKey)

        fun showLocal() {
            tintBadge(badge, color)
            icon.imageTintList = ColorStateList.valueOf(Color.WHITE)
            icon.setImageResource(localRes)
        }

        if (iconUrl.isNullOrBlank()) {
            Glide.with(icon).clear(icon)
            showLocal()
            return
        }

        showLocal()
        Glide.with(icon)
            .load(iconUrl.trim())
            .placeholder(localRes)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>, isFirstResource: Boolean): Boolean {
                    showLocal()
                    return true
                }

                override fun onResourceReady(resource: Drawable, model: Any, target: Target<Drawable>?, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                    tintBadge(badge, ColorUtils.setAlphaComponent(color, BADGE_ALPHA_REMOTE))
                    icon.imageTintList = null
                    return false
                }
            })
            .into(icon)
    }

    /** Chữ cái đầu làm avatar (ký tự đầu của tên sau khi bỏ khoảng trắng) */
    fun initial(name: String?): String {
        val trimmed = name?.trim().orEmpty()
        return if (trimmed.isEmpty()) "?" else trimmed.substring(0, 1).uppercase()
    }
}
