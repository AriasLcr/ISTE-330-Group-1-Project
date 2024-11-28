package model;

public class AbstractInfo {
    private String title;
    private String authors;
    private String truncatedContent;

    public AbstractInfo(String title, String authors, String truncatedContent) {
        this.title = title;
        this.authors = authors;
        this.truncatedContent = truncatedContent;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getTruncatedContent() {
        return truncatedContent;
    }

    public void setTruncatedContent(String truncatedContent) {
        this.truncatedContent = truncatedContent;
    }
}
