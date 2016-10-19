package bit.com.pathmapper.Models;

/**
 * Created by jacksct1 on 20/10/2016.
 */

public class Collection
{
    private int id;
    private String collectionName;

    public Collection() {
    }

    public Collection(int id, String collectionName) {
        this.id = id;
        this.collectionName = collectionName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }
}



