package bit.com.pathmapper.Models;

/**
 * Created by jacksct1 on 20/10/2016.
 */

public class Collection
{
    private int id;
    private int collectionName;

    public Collection(int id, int collectionName) {
        this.id = id;
        this.collectionName = collectionName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(int collectionName) {
        this.collectionName = collectionName;
    }
}



