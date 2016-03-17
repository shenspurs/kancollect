package kanco;
import java.io.Serializable;

public class ShipLines implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -849471218170279633L;

    private String mLines;
    private Sound mSound;

    public ShipLines(String lines,Sound sound) {
        mLines = lines;
        mSound = sound;
    }

    public String getLines(){
        return mLines;
    }

    public Sound getSound(){
        return mSound;
    }

    @Override
    public String toString() {
        return "ShipLines [mLines=" + mLines + ", mSound=" + mSound + "]";
    }
    
}
