package maunaloa.utils;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import oahu.financial.DownloaderJanitor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 2/16/13
 * Time: 2:16 PM
 */
public class NetfondsJanitor implements DownloaderJanitor {
    private String storePath;

    public NetfondsJanitor() {

    }



    @Override
    public void storeLoginPage(HtmlPage page) {

    }

    @Override
    public void storeLogoutPage(HtmlPage page) {

    }

    @Override
    public void storeDerivativePage(HtmlPage page, String ticker) {
        File out = new File(String.format("%s/%s.html", storePath, ticker));
        try (FileOutputStream fop = new FileOutputStream(out)) {

            // if file doesn't exists, then create it
            if (!out.exists()) {
                out.createNewFile();
            }

            // get the content in bytes

            byte[] contentInBytes = page.asXml().getBytes();

            fop.write(contentInBytes);
            fop.flush();
            fop.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void storeIndexPage(HtmlPage page, String ticker) {

    }

    public void setStorePath(String storePath) {
        this.storePath = storePath;
    }
}
