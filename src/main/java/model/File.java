package model;

public class File {
    private String name;
    private String url;
    private int size;
    private String image;

    public File(String name, String url, int size, String image) {
        this.name = name;
        this.url = url;
        this.size = size;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public int getSize() {
        return size;
    }

    public String getImage() {
        return image;
    }
}
