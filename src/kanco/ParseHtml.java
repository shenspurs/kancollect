package kanco;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.tags.TableTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.visitors.TextExtractingVisitor;;

public class ParseHtml {

    private static String ENCODE = "UTF-8";

    public static String openFile(String szFileName) {
        try {
            BufferedReader bis = new BufferedReader(
                    new InputStreamReader(new FileInputStream(new File(szFileName)), ENCODE));
            String szContent = "";
            String szTemp;

            while ((szTemp = bis.readLine()) != null) {
                szContent += szTemp + "\n";
            }
            bis.close();
            return szContent;
        } catch (Exception e) {
            return "";
        }
    }

    private static void message(String szMsg) {
        try {
            System.out.println(new String(szMsg.getBytes(ENCODE), System.getProperty("file.encoding")));
        } catch (Exception e) {
        }
    }

    public static void main(String[] args) {

        String htmlText = openFile("Kongo.html");
        System.out.println("2");
        try {
            Parser parser = Parser.createParser(htmlText, ENCODE);

            NodeFilter classFilter = new NodeFilter() {

                /**
                 * 
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public boolean accept(Node node) {
                    if (node.getText().contains("class=\"wikitable\"")) {
                        return true;
                    } else {
                        return false;
                    }
                }
            };

            NodeFilter filter = new NodeClassFilter(TableTag.class);

            AndFilter tableFilter = new AndFilter(filter, classFilter);

            NodeList tables = parser.extractAllNodesThatMatch(tableFilter);

            for (int i = 0; i < tables.size(); i++) {
                Node node = tables.elementAt(0);

                // node.ge
                // System.out.println(parentText);
            }
            //
            // TextExtractingVisitor visitor = new TextExtractingVisitor();
            //
            // parser.visitAllNodesWith(visitor);
            // String textInPage = visitor.getExtractedText();
            // message(textInPage);

            System.out.println("");
            ;
        } catch (ParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
