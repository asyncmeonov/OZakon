package models

/*
Smallest legal entry

Алинея, най-малкато правна единица
 */
data class Section(val header: VersionHeader = VersionHeader(), val id: Int, val raw: String)