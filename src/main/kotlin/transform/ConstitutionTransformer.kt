package transform

import models.LegalDocument

class ConstitutionTransformer: Transformer {
    override fun transform(document: LegalDocument): LegalDocument {
        //TODO think about how to do versioning here
        //Considerations :

        // Maintain the tree-like structure: evey subdocument has a version header. When a change is detected at any level
        // the modified date is set, version is incremented and propagated to the root. This may not be useful for checking historical documents

        // Maintain one versioning header to the top-most document which indicates changes. Might be more difficult reconstructing historical documents
        return document
    }
}
