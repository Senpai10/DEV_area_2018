package com.example.area;

/**
 * The type Add area item.
 */
public class AddAreaItem {
    private int mImageResource;
    private String mName;
    private String mDescription;

    /**
     * Instantiates a new Add area item.
     *
     * @param icon        the icon
     * @param name        the name
     * @param description the description
     */
    public AddAreaItem(int icon, String name, String description) {
        mImageResource = icon;
        mName = name;
        mDescription = description;
    }

    /**
     * Gets image ressource.
     *
     * @return the image ressource
     */
    public int getImageRessource() { return mImageResource; }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() { return mName; }

    /**
     * Gets description.
     *
     * @return the description
     */
    public String getDescription() { return mDescription; }
}
