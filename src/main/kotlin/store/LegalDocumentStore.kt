package store

import models.LegalDocument
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoClient
import org.bson.conversions.Bson

//TODO: @param conf should handle deployment/dev params. Consider using yaml/typesafe config files for this
class LegalDocumentStore(conf: Map<String, String>) : Store {
    private val mongoClient: MongoClient = MongoClients.create(conf["conString"])
    private val db = mongoClient.getDatabase("ozakondb")
    private val col = db.getCollection(conf["collection"])


    override fun store(document: LegalDocument) {
        col.insertOne(toDocument(document))
    }

    override fun all(): List<LegalDocument> = col.find().map { fromDocument(it) }.toList()

    override fun byFilter(filter: Bson): List<LegalDocument> = col.find(filter).map { fromDocument(it) }.toList()
}

