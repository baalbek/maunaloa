package maunaloa.dummy;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.Page;
import oahu.financial.html.EtradeDownloader;
import org.apache.commons.lang.NotImplementedException;

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
    public Page downloadPaperHistory(String ticker) throws IOException {
        return null;
    }

    @Override
    public void login() throws IOException {
        ////To change body of implemented methods use File | Settings | File Templates.
        throw new NotImplementedException();
    }

    @Override
    public void logout() throws IOException {
        ////To change body of implemented methods use File | Settings | File Templates.
        throw new NotImplementedException();
    }

    @Override
    public void closeAllWindows() {
        ////To change body of implemented methods use File | Settings | File Templates.
        throw new NotImplementedException();
    }

    @Override
    public HtmlPage getLoginPage() {
        //return null;//To change body of implemented methods use File | Settings | File Templates.
        throw new NotImplementedException();
    }

    @Override
    public HtmlPage getLogoutPage() {
        //return null;//To change body of implemented methods use File | Settings | File Templates.
        throw new NotImplementedException();
    }

    public String getHtmlPath() {
        return htmlPath;
    }

    public void setHtmlPath(String htmlPath) {
        this.htmlPath = htmlPath;
    }
}
