package models

data class Conclusion(
    val header: VersionHeader = VersionHeader(),
    val name: String,
    val sections: Set<Chapter>
)
