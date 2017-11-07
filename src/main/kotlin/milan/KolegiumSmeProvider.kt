package milan

import com.mashape.unirest.http.Unirest
import org.apache.http.conn.ssl.SSLConnectionSocketFactory
import org.apache.http.impl.client.HttpClients
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import java.security.cert.X509Certificate
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class KolegiumSmeProvider : MenuProvider {

    init {
        initUnirest()
    }

    override fun getMenu(): Menu {

        val content = Unirest.get("https://restauracie.sme.sk/restauracia/apartmany-kolegium_5498-presov_2912/denne-menu").asString().body

        val document = Jsoup.parse(content)
        val rows = document.select("div.dnesne_menu>div.jedlo_polozka")

        if (rows.isNotEmpty()) {
            return createMenuFromRows(rows)
        }

        return createMenuNotFound()
    }

    private fun createMenuFromRows(elements: Elements): Menu {
        val items = elements.map {
            val title = it.select("div.left").text()
            val price = it.select("span.right>b").text()
            "${title.trim()}\t[${price.trim()}]"
        }

        return Menu(true, items)
    }

    private fun createMenuNotFound(): Menu {
        return Menu(false)
    }

    private fun initUnirest() {
        try {

            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                override fun getAcceptedIssuers(): Array<X509Certificate>? {
                    return null
                }

                override fun checkClientTrusted(certs: Array<X509Certificate>, authType: String) {}

                override fun checkServerTrusted(certs: Array<X509Certificate>, authType: String) {}

            })


            val sslcontext = SSLContext.getInstance("SSL")
            sslcontext.init(null, trustAllCerts, java.security.SecureRandom())
            HttpsURLConnection.setDefaultSSLSocketFactory(sslcontext.socketFactory)
            val sslsf = SSLConnectionSocketFactory(sslcontext)
            val httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build()
            Unirest.setHttpClient(httpclient)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}