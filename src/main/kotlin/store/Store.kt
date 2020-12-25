package store

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import models.LegalDocument
import org.bson.Document
import org.bson.conversions.Bson

interface Store {
    val gson: Gson get() = GsonBuilder().create()
    fun store(document: LegalDocument)
    fun store(documents: Iterable<LegalDocument>) = documents.forEach { d -> store(d) }
    fun all(): List<LegalDocument>
    fun byFilter(filter: Bson): List<LegalDocument>?
    fun toDocument(value: LegalDocument): Document = Document.parse(gson.toJson(value))
    fun fromDocument(value: Document): LegalDocument = gson.fromJson(value.toJson(), LegalDocument::class.java)
}