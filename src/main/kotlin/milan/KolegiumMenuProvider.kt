package milan

import com.mashape.unirest.http.Unirest
import org.apache.http.impl.client.HttpClients
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.conn.ssl.SSLConnectionSocketFactory
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.security.cert.X509Certificate
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


class KolegiumMenuProvider : MenuProvider {

    init {
        initUnirest()
    }

    override fun getMenu(): Menu {

        val content = Unirest.get("https://menucka.sk/denne-menu/presov/apartmany-kolegium").asString().body
        val document = Jsoup.parse(content)
        val rows = document.select("div#restaurant>div.row")

        for (row in rows) {
            val title = row.select("div>div.day-title")
            if (title.size == 1) {
                return createMenuFromRow(row.allElements.first())
            }

        }

        return createMenuNotFound()
    }

    private fun createMenuFromRow(element: Element): Menu {
        val menuElements = element.select("div.col-xs-10.col-sm-10")

        val items = menuElements.map {
            it.text().trim()
        }.map {
            it.trim()
        }.filter {
            !it.isEmpty()
        }

        return Menu(true, items)
    }

    private fun createMenuNotFound(): Menu {
        return Menu(false)
    }

    private fun initUnirest() {
        try {

            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate>? {
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