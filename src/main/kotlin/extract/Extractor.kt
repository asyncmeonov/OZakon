package extract

import models.LegalDocument
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.net.URL

interface Extractor {
    val logger: Logger get() = LoggerFactory.getLogger(javaClass)
    val source : URL
    fun read() : List<String>
    fun parse(raw: List<String>): LegalDocument
}