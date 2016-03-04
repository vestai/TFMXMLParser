package ru.devsaider.tfmxml;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Ruslan Devsaider <me@devsaider.ru>
 */

public class XMLParser {
    private final static Logger logger = LoggerFactory.getLogger(XMLParser.class);
    private final static String CRYPT_KEY = "59A[XG^znsqsq8v{`Xhp3P9G";

    public static void main(String[] args) throws IOException {
    }

    public static String fetchSingle(int mapCode) throws IOException {
        Document doc = Jsoup.connect("http://api.micetigri.fr/maps/@" + String.valueOf(mapCode))
                .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.75 Safari/537.36")
                .get();
        String cryptedXML;
        Pattern p = Pattern.compile("(?is)map:\"(.*?)\",");
        Matcher m = p.matcher(doc.html());

        if (m.find() && !(cryptedXML = URLDecoder.decode(m.group(1), "UTF-8")).equals("")) {
            String finalXML = XMLParser.decrypt(cryptedXML, CRYPT_KEY);
            File file = new File("cache/" + String.valueOf(mapCode) + ".xml");

            // if file doesn't exists, then create it
            if (!file.exists()) {
                file.getParentFile().mkdir();
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(finalXML);
            bw.close();
            return finalXML;
        }

        return null;
    }

    public static String decrypt(String cryptedXML, String key) throws IOException {
        int c;
        String finalXML = "";
        byte[] str = Base64.getDecoder().decode(cryptedXML);
        for (int i = 0; i < str.length; i++) {
            c = str[i];
            c -= key.charAt((i + 1) % key.length());
            finalXML += (char) (c & 255);
        }

        return finalXML;
    }
}
