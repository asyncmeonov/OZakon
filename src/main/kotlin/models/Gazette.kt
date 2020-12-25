package models

import java.net.URL

/**
 * Помощен обект, моделиращ издания на Държавен вестник
 */
data class Gazette(val issue: Int, val year: Int, val url: URL)
