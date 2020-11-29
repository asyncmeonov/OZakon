package store

import models.LegalDocument
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoClient

//TODO: @param conf should handle deployment/dev params. Consider using yaml/typesafe config files for this
class LegalDocumentStore(private val conf: Map<String, String>) : Store {
    private val mongoClient: MongoClient = MongoClients.create(conf["conString"])

    override fun store(document: LegalDocument) {
        val db = mongoClient.getDatabase("ozakondb")
        val col = db.getCollection(conf["collection"])
        col.insertOne(toDocument(document))
    }
}

