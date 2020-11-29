package transform

import models.LegalDocument

interface Transformer {
    fun transform(documents: Iterable<LegalDocument>): Iterable<LegalDocument> =  documents.map{d -> transform(d)}
    fun transform(document : LegalDocument): LegalDocument
}