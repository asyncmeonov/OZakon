package models

data class Chapter(
    val header: VersionHeader = VersionHeader(),
    val id: Int,
    val name: String,
    val articles: Set<Article>
)