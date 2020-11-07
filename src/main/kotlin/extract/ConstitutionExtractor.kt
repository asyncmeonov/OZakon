package extract

import enums.ChapterType
import models.Article
import models.Chapter
import models.LegalDocument
import models.Section
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import java.net.URL


class ConstitutionExtractor(override val source: URL) : Extractor {

    private val options: ChromeOptions = ChromeOptions().addArguments("--headless")

    override fun read(): List<String> {
        val driver = ChromeDriver(options)
        try {
            driver.get(source.toExternalForm())
            return driver
                .findElementByClassName("articlebody")
                .findElements(By.cssSelector("p"))
                .map(WebElement::getText)
        } finally {
            driver.close()
        }
    }

    override fun parse(raw: List<String>): LegalDocument {
        val flat = raw.joinToString(separator = "\n")
        val chapters = flat.findAllChapters()
            .map { chapter ->
                Chapter(
                    chapter.findChapterId(),
                    chapter.findChapterName(),
                    chapter.findAllArticles()
                        .map { article ->
                            Article(
                                article.findArticleId(),
                                article.findAllSections()
                                    .map { section -> Section(section.findSectionId(), section.findSectionText()) }
                                    .toSet()
                            )
                        }.toSet()
                )
            }.toSet()

        println(chapters)
        return LegalDocument("Конституция", chapters)
    }

    private fun String.findAllChapters(): Set<String> {
        val regex = """Глава ((?:(?!Глава )[\s\S])*)""".toRegex()
        return regex.findAll(this).map(MatchResult::value).toSet()
    }

    private fun String.findChapterId(): Int {
        val regex = """(?<=Глава\s)(.+)(?=\n)""".toRegex()
        return ChapterType.valueOf(regex.find(this)!!.value).id
    }

    private fun String.findChapterName(): String {
        val regex = """(?<=\n).*""".toRegex()
        return regex.find(this)!!.value
    }

    private fun String.findAllArticles(): Set<String> {
        //Any string that starts with Чл. followed by a space and ending with newline and a Чл. again
        val regex = """Чл\.((?:(?!Чл\.)[\s\S])*)""".toRegex() // Чл\.\s.*((\r\n).*)*^.*(?=(\Чл\.\s))
        return regex.findAll(this).map(MatchResult::value).toSet()
    }

    /*
    Given A string containing a single valid article, return which article number it is
    groupValues always contains two elements. The first one is the full match of (Чл. X) and the second one is the id X
     */
    private fun String.findArticleId(): Int {
        // Given a string containing a single article (and it's sections)
        val regex = """Чл\.\s([\d]+)""".toRegex()
        return regex.find(this)!!.groupValues[1].toInt()
    }

    private fun String.findAllSections(): Set<String> {
        //Finds any sections and should be applied on an Article string. Will be empty if the Article is singular
        val regex = """\(\d\).*""".toRegex()
        regex.findAll(this).forEach { v -> println("[" + v.value + "]") }
        val values = regex.findAll(this).map(MatchResult::value).toSet()
        return if (values.isNullOrEmpty()) {
            setOf(this)
        } else
            values
    }

    private fun String.findSectionId(): Int {
        val regex = """\(([\d]+)\)""".toRegex()
        val value = regex.find(this)
        return if (value == null) {
            1
        } else {
            value.groupValues[1].toInt()
        }
    }

    private fun String.findSectionText(): String {
        val regex = """(?<=\(\d\)).*""".toRegex()
        return when {
            regex.matches(this) -> regex.find(this)!!.value
            else -> this
        }
    }
}
