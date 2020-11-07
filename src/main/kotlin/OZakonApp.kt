import extract.ConstitutionExtractor
import java.net.URL

fun main(args: Array<String>){
    val conURL = URL("https://www.parliament.bg/bg/const")
    val conExtractor = ConstitutionExtractor(conURL)
    val raw = conExtractor.read()
    val parsed = conExtractor.parse(raw)
    //TODO: Pass parsed to a transformer
    //TODO: Pass transformer to a loader
}