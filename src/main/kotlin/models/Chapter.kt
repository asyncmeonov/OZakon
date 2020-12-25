package models

data class Chapter(
    val header: VersionHeader = VersionHeader(),
    val id: Int? = null,
    val name: String? = null,
    val articles: Set<Article>
)