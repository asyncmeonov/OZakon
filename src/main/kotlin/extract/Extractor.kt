package extract

import models.LegalDocument
import java.net.URL

interface Extractor {
    val source : URL
    fun read() : List<String>
    fun parse(raw: List<String>): LegalDocument
}