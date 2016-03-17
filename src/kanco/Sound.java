package kanco;

import java.io.Serializable;


public class Sound implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 3892325484913202881L;
    private String mShipName;
    private String mUrl ;
    private String mCachePath;
    
    public Sound(String shipName ,String url){
        mShipName = shipName;
        mUrl = url;
    }

    public String getmShipName() {
        return mShipName;
    }

    public void setmShipName(String mShipName) {
        this.mShipName = mShipName;
    }

    public String getmUrl() {
        return mUrl;
    }

    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public String getmCachePath() {
        return mCachePath;
    }

    @Override
    public String toString() {
        return "Sound [mShipName=" + mShipName + ", mUrl=" + mUrl
                + ", mCachePath=" + mCachePath + "]";
    }


    public void setmCachePath(String mCachePath) {
        this.mCachePath = mCachePath;
    }
    
}
