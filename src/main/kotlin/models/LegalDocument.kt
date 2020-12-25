package models

data class LegalDocument(
    val header: VersionHeader = VersionHeader(),
    val name: String,
    val chapters: Set<Chapter>,
    val conclusion: Conclusion?
)