import extract.ConstitutionExtractor
import store.LegalDocumentStore
import transform.ConstitutionTransformer
import java.net.URL

val defaultDbConf = mapOf(
    "conString" to "mongodb://localhost:27017",
    "collection" to "constitution"
)

fun main(args: Array<String>) {
    val conURL = URL("https://www.parliament.bg/bg/const")
    val conExtractor = ConstitutionExtractor(conURL)
    val raw = conExtractor.read()
    val parsed = conExtractor.parse(raw)

    val conTransformer = ConstitutionTransformer()
    val transformed = conTransformer.transform(parsed)

    val conStore = LegalDocumentStore(defaultDbConf)
    conStore.store(transformed)
}