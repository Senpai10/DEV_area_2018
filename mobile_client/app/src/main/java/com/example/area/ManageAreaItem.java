package com.example.area;

/**
 * The type Manage area item.
 */
public class ManageAreaItem {
    private int mImageResource;
    private String mText1;
    private String mText2;

    /**
     * Instantiates a new Manage area item.
     *
     * @param imageResource the image resource
     * @param text1         the text 1
     * @param text2         the text 2
     */
    public ManageAreaItem(int imageResource, String text1, String text2) {
        mImageResource = imageResource;
        mText1 = text1;
        mText2 = text2;
    }

    /**
     * Change text 1.
     *
     * @param text the text
     */
    public void changeText1(String text) {
        mText1 = text;
    }

    /**
     * Gets image resource.
     *
     * @return the image resource
     */
    public int getImageResource() {
        return mImageResource;
    }

    /**
     * Gets text 1.
     *
     * @return the text 1
     */
    public String getText1() {
        return mText1;
    }

    /**
     * Gets text 2.
     *
     * @return the text 2
     */
    public String getText2() {
        return mText2;
    }
}
