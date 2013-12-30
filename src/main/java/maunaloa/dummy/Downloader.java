package maunaloa.dummy;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.Page;
import oahu.exceptions.NotImplementedException;
import oahu.financial.html.EtradeDownloader;
//import org.apache.commons.lang.NotImplementedException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 12/19/12
 * Time: 7:51 PM
 */
public class Downloader implements EtradeDownloader {

    private String htmlPath;

    @Override
    public HtmlPage downloadDerivatives(String ticker) throws IOException {
        URL url = getUrl(ticker);

        System.out.println("URL: " + url.toString());

        WebClient webClient = new WebClient();

        return webClient.getPage(url);
    }

    private URL getUrl(String ticker) throws MalformedURLException {
        return new URL(String.format("%s/%s.html", getHtmlPath(),ticker));
    }

    @Override
    public HtmlPage downloadIndex(String stockIndex) throws IOException {
        WebClient webClient = new WebClient();

        return webClient.getPage(getUrl(stockIndex));
    }

    @Override
    public Page downloadPaperHistory(String s) throws IOException {
        throw new oahu.exceptions.NotImplementedException();
    }

    @Override
    public void login() throws IOException {
        throw new NotImplementedException();
    }

    @Override
    public void logout() throws IOException {
        throw new NotImplementedException();
    }

    @Override
    public void closeAllWindows() {
        throw new NotImplementedException();
    }

    @Override
    public HtmlPage getLoginPage() {
        throw new NotImplementedException();
    }

    @Override
    public HtmlPage getLogoutPage() {
        throw new NotImplementedException();
    }

    @Override
    public WebClient getWebClient() {
        throw new oahu.exceptions.NotImplementedException();
    }

    public String getHtmlPath() {
        return htmlPath;
    }

    public void setHtmlPath(String htmlPath) {
        this.htmlPath = htmlPath;
    }
}
