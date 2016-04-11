package kanco;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;


public class SoundFileds implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -3479625120818421918L;
    private String mFiledsName;
    private ArrayList<ShipLines> mShipLines = new ArrayList<>();

    public void addShipLines(ShipLines lines){
        mShipLines.add(lines);
    }

    public SoundFileds(String name){
        mFiledsName = name;
    }

    public String getFiledName(){
        return mFiledsName;
    }
    public ArrayList<ShipLines> getShipLines(){
        return mShipLines;
    }

    @Override
    public String toString() {
        return "SoundFileds [mShipLines=" + Arrays.toString(mShipLines.toArray()) + "]";
    }
    
}
