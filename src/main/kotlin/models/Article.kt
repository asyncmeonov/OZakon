package models

/**
 * Article containing a set of sections
 *
 * Член, съдържащ алинеи
 */
data class Article(val header: VersionHeader = VersionHeader(), val id: Int, val sections: Set<Section>)