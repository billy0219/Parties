package com.now.parties;

import android.view.View;

/**
 * Created by billy on 2017-08-22.
 */

public class ViewItem {
    private String placeImage, placeName, placeDesc;

    public ViewItem () {

    }

    public ViewItem ( String placeImage, String placeName ){
        this.placeImage = placeImage;
        this.placeName = placeName;
        this.placeDesc = placeDesc;
    }

    public String getPlaceDesc() {
        return placeDesc;
    }

    public void setPlaceImage(String placeImage) {
        this.placeImage = placeImage;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getPlaceImage() {
        return placeImage;
    }

    public void setPlaceDesc(String placeDesc) {
        this.placeDesc = placeDesc;
    }
}
