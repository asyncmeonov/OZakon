package store

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import models.LegalDocument
import org.bson.Document

interface Store {
    val gson: Gson get() = GsonBuilder().create()
    fun store(document: LegalDocument)
    fun store(documents: Iterable<LegalDocument>) = documents.forEach { d -> store(d) }
    fun <T> toDocument(value: T): Document = Document.parse(gson.toJson(value))
}