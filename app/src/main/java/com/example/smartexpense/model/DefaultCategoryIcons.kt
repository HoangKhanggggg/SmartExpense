package com.example.smartexpense.model

/**
 * URL icon mặc định cho 8 danh mục mặc định (Cột mốc 5 - Glide tải ảnh từ mạng).
 * Ảnh: Twemoji (jdecked/twemoji, giấy phép CC-BY 4.0) host trên raw.githubusercontent.com.
 * Mất mạng/lỗi tải -> UiUtils.bindCategoryIcon tự dùng icon cục bộ theo iconKey.
 */
object DefaultCategoryIcons {

    private const val BASE = "https://raw.githubusercontent.com/jdecked/twemoji/main/assets/72x72/"

    /** iconKey (drawable cục bộ) -> URL ảnh */
    val urls: Map<String, String> = mapOf(
        "ic_food" to BASE + "1f35c.png",
        "ic_car" to BASE + "1f697.png",
        "ic_school" to BASE + "1f393.png",
        "ic_shopping" to BASE + "1f6d2.png",
        "ic_movie" to BASE + "1f3ac.png",
        "ic_health" to BASE + "1f48a.png",
        "ic_salary" to BASE + "1f4b0.png",
        "ic_gift" to BASE + "1f381.png"
    )

    fun urlFor(iconKey: String): String = urls[iconKey].orEmpty()
}
