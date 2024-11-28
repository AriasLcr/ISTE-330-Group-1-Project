package model;

public class Abstract {
    private int abstractID;
    private String title;
    private String abstractFile;

    public Abstract(int abstractID, String title, String abstractFile) {
        this.abstractID = abstractID;
        this.title = title;
        this.abstractFile = abstractFile;
    }

    public int getAbstractID() {
        return abstractID;
    }

    public void setAbstractID(int abstractID) {
        this.abstractID = abstractID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAbstractFile() {
        return abstractFile;
    }

    public void setAbstractFile(String abstractFile) {
        this.abstractFile = abstractFile;
    }
}
