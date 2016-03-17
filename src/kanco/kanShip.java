package kanco;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class kanShip implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 9170497488725429472L;
    private ArrayList<SoundFileds> mSoundFileds = new ArrayList<>();

    public void addSoundFileds(SoundFileds fileds) {
        mSoundFileds.add(fileds);
    }

    public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {
        kanShip ship = new kanShip();
        
        Sound sound1 = new Sound("长门", "nagato/1.mp3");
        Sound sound2 = new Sound("长门", "nagato/2.mp3");
        Sound sound3 = new Sound("长门", "nagato/3.mp3");
        Sound sound4 = new Sound("长门", "nagato/4.mp3");
        ShipLines lines1 = new ShipLines("lines1", sound1);
        ShipLines lines2 = new ShipLines("lines2", sound2);
        ShipLines lines3 = new ShipLines("lines3", sound3);
        ShipLines lines4 = new ShipLines("lines4", sound4);
        
        SoundFileds fileds1 = new SoundFileds("");
        fileds1.addShipLines(lines1);
        SoundFileds fileds2 = new SoundFileds("");
        fileds2.addShipLines(lines2);
        fileds2.addShipLines(lines3);
        SoundFileds fileds3 = new SoundFileds("");
        fileds3.addShipLines(lines4);
        
        ship.addSoundFileds(fileds1);
        ship.addSoundFileds(fileds2);
        ship.addSoundFileds(fileds3);
        
        File file = new File("test.obj");
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
        out.writeObject(ship);
        out.close();

        ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
        Object newship = in.readObject();
        in.close();
        System.out.print(newship);
    }

    @Override
    public String toString() {

        return "kanShip [mSoundFileds=" + Arrays.toString(mSoundFileds.toArray()) + "]";
    }
}
