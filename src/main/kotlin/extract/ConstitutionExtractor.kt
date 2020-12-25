package extract

import models.Article
import models.Chapter
import models.Conclusion
import models.LegalDocument
import models.Section
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import java.io.IOException
import java.lang.StringBuilder
import java.net.URL

class ConstitutionExtractor(override val source: URL, private val backupSource: URL?) : Extractor {

    private val options: ChromeOptions = ChromeOptions().addArguments("--headless")

    override fun read(): List<String> {
        logger.info("Reading Constitution from ${source.toExternalForm()}")
        try {
            return readPdf(source)
        } catch (ex: IOException){
             if (backupSource != null) {
                 logger.warn("Failed reading from main source. Retrying with backup...")
                 return readWithSelenium(backupSource)
             }
            throw IOException("Could not read Constitution document from $source. ${ex.message}")
        }
    }

    override fun parse(raw: List<String>): LegalDocument {
        logger.info("Start parsing Constitution to LegalDocument object...")
        val flat = raw.joinToString(separator = "\n")
        val chapters = flat.parseChapters()
        val conclusion = flat.parseConclusion()
        logger.info("Finished parsing ${chapters.size} chapters")
        return LegalDocument(name = "Конституция", chapters = chapters, conclusion = conclusion)
    }

    private fun readPdf(source: URL): List<String>{
        //Try reading a PDF directly
        val input = source.openStream()
        input.use {
            val raw = PDDocument.load(it)
            return PDFTextStripper().getText(raw).lines()
        }
    }

    //Does not support conclusion section
    private fun readWithSelenium(source: URL): List<String> {
        val driver = ChromeDriver(options)
        try {
            driver.get(source.toExternalForm())
            val constitution = driver.findElement(By.className("articlebody"))
            val bodyBuilder = StringBuilder()
            bodyBuilder.append(
                constitution
                    .findElements(By.cssSelector("p"))
                    .map(WebElement::getText)
            )
            return bodyBuilder.lines()
        } finally {
            driver.close()
        }
    }

    private fun String.parseChapters(): Set<Chapter> {
        return this.findAllChapters()
            .map { chapter ->
                Chapter(
                    id = chapter.findChapterId(),
                    name = chapter.findChapterName(),
                    articles = chapter.findAllArticles()
                        .map { article ->
                            Article(
                                id = article.findArticleId(),
                                sections = article.findAllSections()
                                    .map { section ->
                                        Section(
                                            id = section.findSectionId(),
                                            raw = section.findSectionText()
                                        )
                                    }
                                    .toSet()
                            )
                        }.toSet()
                )
            }.toSet()
    }

    private fun String.parseConclusion(): Conclusion {
        val chapters = this.findCustom("(Преходни и )*Заключителни разпоредби", "(Преходни и )*Заключителни разпоредби")
            .map {
                    chapter ->
                    Chapter(
                        articles = chapter.findAllParagraphs()
                            .map { article ->
                                Article(
                                    id = article.findParagraphId(),
                                    sections = article.findAllSections()
                                        .map { section ->
                                            Section(
                                                id = section.findSectionId(),
                                                raw = section.findSectionText()
                                            )
                                        }.toSet()
                                )
                            }.toSet()
                    )
            }.toSet()
        return Conclusion(name = "ПРЕХОДНИ И ЗАКЛЮЧИТЕЛНИ РАЗПОРЕДБИ", sections = chapters)
    }
}