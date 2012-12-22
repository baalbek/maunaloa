package maunaloa.dummy;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import oahu.financial.EtradeDownloader;
import org.apache.commons.lang.NotImplementedException;

import java.io.IOException;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 12/19/12
 * Time: 7:51 PM
 */
public class Downloader implements EtradeDownloader {
    @Override
    public HtmlPage downloadDerivatives(String ticker) throws IOException {
        URL url = this.getClass().getResource("/" + ticker + ".html");

        System.out.println("URL: " + url.toString());

        WebClient webClient = new WebClient();

        return webClient.getPage(url);
    }

    @Override
    public HtmlPage downloadIndex(String stockIndex) throws IOException {
        URL url = this.getClass().getResource("/" + stockIndex + ".html");
        WebClient webClient = new WebClient();

        return webClient.getPage(url);
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
}
