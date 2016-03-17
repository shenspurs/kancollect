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
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.tags.TableTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;;

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

            kanShip ship = new kanShip();
            SoundFileds lastFileds = null;
            if (tables != null && tables.size() > 1) {
                Node soundNode = tables.elementAt(1);

                if (soundNode.getChildren() instanceof NodeList) {
                    NodeList lists = (NodeList) soundNode.getChildren();
                    int size = lists.size();
                    for (int i = 1; i < size; i = i + 2) {
                        if (i == 1) {
                            continue;
                        }
                        Node node = lists.elementAt(i);
                        if (node.getChildren() instanceof NodeList) {
                            NodeList slists = (NodeList) node.getChildren();
                            // System.out.println("slists = " + slists);
                            int linesIndex = 1;
                            int soundIndex = 3;
                            if (slists.size() == 6) {
                                Node filed = slists.elementAt(1);
                                if (filed instanceof TableColumn){
                                    TableColumn column = (TableColumn)filed;
//                                    System.out.println("column = " + column.getStringText());
                                    lastFileds = new SoundFileds(column.getStringText());
                                    ship.addSoundFileds(lastFileds);
                                }
                                linesIndex += 2;
                                soundIndex += 2;
                            }

                            Node linesRow = slists.elementAt(linesIndex);
                            ShipLines shipLines = null;
                            String lines ="";
                            if (linesRow instanceof TableColumn){
                                String text = ((TableColumn)linesRow).getStringText();
                                lines = handleText(text);
                            }

                            Node soundRow = slists.elementAt(soundIndex);
                            if (soundRow instanceof TableColumn){
                                TableColumn soundCol = ((TableColumn)soundRow);
                                if (soundCol.getChildCount() >1 && soundCol.getChild(1) instanceof LinkTag){
                                    LinkTag linkTag = (LinkTag) soundCol.getChild(1);
                                    String href = linkTag.getLink();
//                                    System.out.println("href = " + href);
                                    //TODO fix the name of sound
                                    Sound sound = new Sound("kougo", href);
                                    shipLines = new ShipLines(lines, sound);
                                    if (lastFileds != null){
                                        lastFileds.addShipLines(shipLines);
                                    }
                                }
                            }

                        }
                    }
                }
            }

            System.out.println("ship = " + ship);

        } catch (ParserException e) {
            e.printStackTrace();
        }

    }

    private static String handleText(String text) {
        String s = text;
        if (text.contains("div")){
            s = s.replaceAll("<([^>]*)>", "");
            s = s.replaceAll("\n+", "\n");
//            System.out.println("s = " + s);
        } else if (text.contains("span")){
            s = text.replaceAll("<br />", "\n");
            s = s.replaceAll("<([^>]*)>", "");
//            System.out.println("s = " + s);
        }
        return s;
    }

}
