package kanco;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.tags.TableRow;
import org.htmlparser.tags.TableTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

public class ParseShip {
    private static String ENCODE = "UTF-8";

    public static String openFile(File file) {
        try {
            BufferedReader bis = new BufferedReader(
                    new InputStreamReader(new FileInputStream(file), ENCODE));
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

    public static List <kanShip> parseShips(File file){
        String htmlText = openFile(file);
        List<kanShip> ships = new ArrayList<>();
        try {
            Parser parser = Parser.createParser(htmlText, ENCODE);

            NodeFilter classFilter = new NodeFilter() {

                /**
                 * 
                 */
                private static final long serialVersionUID = 2L;

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
            for (int i =0,size = tables.size();i<size;i++){
                TableTag tableTag = (TableTag) tables.elementAt(i);
                NodeList lists = tableTag.getChildren();
                for (int j =0, jSize = lists.size();j<jSize;j++){
                    Node node = lists.elementAt(j);
                    if (node instanceof TableRow){
                        TableRow tableRow = (TableRow) node;
                        int index = 1;
                        for (int k = 0,kSize = tableRow.getChildCount();k<kSize;k++){
                            Node node2 = tableRow.childAt(k);
                            if (node2 instanceof TableColumn){
                                TableColumn column = (TableColumn) node2;
                                if (column.getChildCount() > 3){
                                    Node linkNode = column.childAt(3);
                                    if (linkNode instanceof LinkTag){
                                        LinkTag linkTag = (LinkTag) linkNode;
                                        String str = linkTag.getChildrenHTML();
                                        Pattern pattern = Pattern.compile("No\\.(\\d*)\\s(\\S*)");
                                        Matcher m = pattern.matcher(str);
                                        kanShip ship = new kanShip();
                                        if (m.find()){
                                            String indexStr = m.group(1);
                                            ship.setShipIndex(Integer.valueOf(indexStr));
                                            String name = m.group(2);
                                            ship.setShipName(name);
                                        }
                                        if (i > 34){
                                            ship.setShipIndex(350 + index);
                                            index++;
                                            ship.setShipName(str);
                                        }
                                        ship.setShipUrl(linkTag.getLink());
//                                        System.out.println(ship);
                                        ships.add(ship);
                                    }
                                }
                            }
                        }
                    }
                    
                }
            }


            System.out.println("ship = " + ships);

        } catch (ParserException e) {
            e.printStackTrace();
        }

        return ships;
    }
    
    public static kanShip parseShipSound(File file) {
        String htmlText = openFile(file);
        kanShip ship = null;
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

            ship = new kanShip();
            SoundFileds lastFileds = null;
            if (tables != null && tables.size() > 1) {
                Node soundNode = tables.elementAt(1);
                for (int index = 0 ;index < tables.size();index++){
                    soundNode = tables.elementAt(index);
                    int count = soundNode.getChildren().size();
                    System.out.println("count" + count);
                    //TODO fix the function of find the sound node
                    if (soundNode.getText().contains("mp3")){
                        break;
                    }
                }

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
//                             System.out.println("slists = " + slists);
                            int linesIndex = 1;
                            int soundIndex = 3;
                            if (slists.size() == 6) {
                                Node filed = slists.elementAt(1);
                                if (filed instanceof TableColumn) {
                                    TableColumn column = (TableColumn) filed;
//                                     System.out.println("column = " +
//                                     column.getStringText());
                                    lastFileds = new SoundFileds(column.getStringText());
                                    ship.addSoundFileds(lastFileds);
                                }
                                linesIndex += 2;
                                soundIndex += 2;
                            }

                            Node linesRow = slists.elementAt(linesIndex);
                            ShipLines shipLines = null;
                            String lines = "";
                            if (linesRow instanceof TableColumn) {
                                String text = ((TableColumn) linesRow).getStringText();
                                lines = handleText(text);
                            }

                            Node soundRow = slists.elementAt(soundIndex);
                            if (soundRow instanceof TableColumn) {
                                TableColumn soundCol = ((TableColumn) soundRow);
                                if (soundCol.getChildCount() > 1 && soundCol.getChild(1) instanceof LinkTag) {
                                    LinkTag linkTag = (LinkTag) soundCol.getChild(1);
                                    String href = linkTag.getLink();
//                                     System.out.println("href = " + href);
                                    // TODO fix the name of sound
                                    Sound sound = new Sound(file.getName().replaceAll("\\.html", ""), href);
                                    shipLines = new ShipLines(lines, sound);
                                    if (lastFileds != null) {
                                        lastFileds.addShipLines(shipLines);
                                    }
                                }
                            }

                        }
                    }
                }
            }

//            System.out.println("ship = " + ship);

        } catch (ParserException e) {
            e.printStackTrace();
        }

        return ship;
    }

    private static String handleText(String text) {
        String s = text;
        if (text.contains("div")) {
            s = s.replaceAll("<([^>]*)>", "");
            s = s.replaceAll("\n+", "\n");
            // System.out.println("s = " + s);
        } else if (text.contains("span")) {
            s = text.replaceAll("<br />", "\n");
            s = s.replaceAll("<([^>]*)>", "");
            // System.out.println("s = " + s);
        }
        return s;
    }
    
    
    public static void main(String[] args) throws MalformedURLException {
//        parseShipSound(new File("Kongo.html"));
        ArrayList<kanShip> ships = (ArrayList<kanShip>) parseShips(new File("ships.html"));
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(20);
        
        
      
        for (int i = 0; i < ships.size(); i++) {
            kanShip ship = ships.get(i);
            final int shipIndex = ship.getShipIndex();
            String path = "ships\\" + shipIndex + ".html";
//            System.out.println(path);
            String url = ship.getShipUrl();
            //download html of all ships
//            downloadFile(path,url);
            System.out.println(shipIndex);
            kanShip tmpShip = parseShipSound(new File(path));
            ArrayList<SoundFileds> soundFileds = tmpShip.getSoundFileds();
            if (tmpShip != null ){
                ship.setSoundFileds(tmpShip.getSoundFileds());
            }
            if (soundFileds != null){
                for (int j =0 ,jSize = soundFileds.size();j < jSize;j++){
                    ArrayList<ShipLines> shipLines = soundFileds.get(j).getShipLines();
                    for (int k =0,kSize = shipLines.size();k< kSize;k++){
                        Sound sound = shipLines.get(k).getSound();
                        String line = shipLines.get(k).getLines();
                        String lineUrl = sound.getmUrl();
                        int lastIndex = lineUrl.lastIndexOf('/'); 
                        if (lastIndex > 0){
                            
                            String name = lineUrl.substring(lastIndex + 1, lineUrl.length());
                            String soundPath = "sound\\" + shipIndex + "\\" +  name;
//                            System.out.println(soundPath);
                            //download the sounds of the ship

                            fixedThreadPool.execute(new Runnable() {
                                
                                @Override
                                public void run() {
                                    try {
//                                      System.out.println(soundPath);
//                                      System.out.println(lineUrl);
                                        downloadFile(soundPath, lineUrl);
                                    } catch (MalformedURLException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                }
                            });
                  
                        }
//                        System.out.println(sound);
//                        System.out.println(line);
                    }
                }
            }
        }
        System.out.println("end");
        synchronized (urls) {
            System.out.println(urls);
        }
        while (urls.size() > 0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            synchronized (urls) {
                System.out.println(urls);
            }
        }
        fixedThreadPool.shutdown();
    }
    
    private static ArrayList<String> urls = new ArrayList<>();
    public static void downloadFile(String path,String netUrl) throws MalformedURLException{
        File file = new File(path);
        if (file.exists()) {
            return;
        }
        File parentFile = file.getParentFile();
        if (parentFile != null && !parentFile.exists()){
            parentFile.mkdirs();
        }
        URL url = new URL(netUrl);
        int byteSum = 0;
        int byteRead = 0;
        synchronized (urls) {
            urls.add(netUrl);
        }
        try {
            URLConnection conn = url.openConnection();
            InputStream inStream = conn.getInputStream();
            FileOutputStream fs = new FileOutputStream(path);
            
            byte[] buffer = new byte[4096];
            while ((byteRead = inStream.read(buffer)) != -1) {
                byteSum += byteRead;
//                System.out.println(byteSum);
                fs.write(buffer,0,byteRead);
            }
            fs.close();
            synchronized (urls) {
                urls.remove(netUrl);
            }
        } catch (IOException e) {
            System.out.println("error" + path);
            System.out.println("error" + url);
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
}
