package extract

import enums.ChapterType
import models.LegalDocument
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.net.URL

interface Extractor {
    val logger: Logger get() = LoggerFactory.getLogger(javaClass)
    val source : URL
    fun read() : List<String>
    fun parse(raw: List<String>): LegalDocument

    fun String.findAllChapters(): Set<String> {
        val regex = """Глава ((?:(?!Глава )[\s\S])*)""".toRegex()
        return regex.findAll(this).map(MatchResult::value).toSet()
    }

    fun String.findChapterId(): Int {
        val regex = """(?<=Глава\s)(.+)(?=\.)""".toRegex()
        return ChapterType.valueOf(regex.find(this)!!.value.trim()).id
    }

    fun String.findChapterName(): String {
        val regex = """(?<=\n).*""".toRegex()
        return regex.find(this)!!.value
    }

    fun String.findAllArticles(): Set<String> {
        //Any string that starts with Чл. followed by a space and ending with newline and a Чл. again
        val regex = """Чл\.((?:(?!Чл\.)[\s\S])*)""".toRegex() // Чл\.\s.*((\r\n).*)*^.*(?=(\Чл\.\s))
        return regex.findAll(this).map(MatchResult::value).toSet()
    }

    /*
    Given A string containing a single valid article, return which article number it is
    groupValues always contains two elements. The first one is the full match of (Чл. X) and the second one is the id X
     */
    fun String.findArticleId(): Int {
        // Given a string containing a single article (and it's sections)
        val regex = """Чл\.\s([\d]+)""".toRegex()
        return regex.find(this)!!.groupValues[1].toInt()
    }

    fun String.findAllSections(): Set<String> {
        //Finds any sections and should be applied on an Article string. Will be empty if the Article is singular
        val regex = """\(\d\).*""".toRegex()
        regex.findAll(this).forEach { v -> println("[" + v.value + "]") }
        val values = regex.findAll(this).map(MatchResult::value).toSet()
        return if (values.isNullOrEmpty()) {
            setOf(this)
        } else
            values
    }

    fun String.findSectionId(): Int {
        val regex = """\(([\d]+)\)""".toRegex()
        val value = regex.find(this)
        return if (value == null) {
            1
        } else {
            value.groupValues[1].toInt()
        }
    }

    fun String.findSectionText(): String {
        val regex = """(?<=\(\d\)).*""".toRegex()
        return when {
            regex.matches(this) -> regex.find(this)!!.value
            else -> this
        }
    }

    fun String.findAllParagraphs(): Set<String>{
        val regex = """§\s([\d]+)\.((?:(?!§\s([\d]+)\.)[\s\S])*)""".toRegex() // Чл\.\s.*((\r\n).*)*^.*(?=(\Чл\.\s))
        return regex.findAll(this).map(MatchResult::value).toSet()
    }

    fun String.findParagraphId(): Int{
        val regex = """§\s([\d]+)\.""".toRegex()
        val value = regex.find(this)
        return if (value == null) {
            1
        } else {
            value.groupValues[1].toInt()
        }
    }


    /**
     * @param start start string (inclusive)
     * @param end end string (exclusive)
     */
    fun String.findCustom(start: String, end: String): Set<String>{
        val regex = """$start((?:(?!$end)[\s\S])*)""".toRegex()
        return regex.findAll(this).map(MatchResult::value).toSet()
    }
}