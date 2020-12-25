import extract.ConstitutionExtractor
import models.LegalDocument
import store.LegalDocumentStore
import transform.ConstitutionTransformer
import java.net.URL

val defaultDbConf = mapOf(
    "conString" to "mongodb://localhost:27017",
    "collection" to "laws"
)

//TODO build a conf file (yaml?)
fun main(args: Array<String>) {
    val backupConURL = URL("https://www.parliament.bg/bg/const")
    val conURL = URL("https://www.cpdp.bg/userfiles/file/Documents_2018/Constitution_Republic%20of%20Bulgaria_Bg.pdf")
    val conExtractor = ConstitutionExtractor(conURL,backupConURL)
    val conStore = LegalDocumentStore(defaultDbConf)
    val conTransformer = ConstitutionTransformer(conStore)


    //Read from URL
    val raw: List<String> = conExtractor.read()

    //Parse
    val parsed: LegalDocument = conExtractor.parse(raw)

    //Transform
    val transformed: LegalDocument = conTransformer.transform(parsed)

    //Store
    conStore.store(transformed)
}