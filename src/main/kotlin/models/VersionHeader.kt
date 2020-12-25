package models

import java.util.Calendar
import java.util.Date

/**
 * @param version уникален индикатор за версията на дадена част от правен документ
 * @param modifiedDate датата на внесена промяна
 * @param effectiveDate датата от която промяната е в сила
 * @param raw текста назоваващ изменението. Пример: "Нова - ДВ, бр. 18 от 2005 г" поставено преди нов член
 * @param modifiedIssue брой на държавен вестник, където е промяната.
 */
data class VersionHeader(
    val version: Int = 0,
    val modifiedDate: Date = Calendar.getInstance().time,
    val effectiveDate: Date = modifiedDate,
    val modifiedIssue: Gazette? = null,
    val raw: String? = null
)