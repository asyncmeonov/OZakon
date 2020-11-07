package models

/**
 * Article containing a set of sections
 *
 * Член, съдържащ алинеи
 */
data class Article(val id: Int, val sections: Set<Section>)