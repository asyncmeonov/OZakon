package models

data class Chapter(val id: Int, val name: String, val articles: Set<Article>)