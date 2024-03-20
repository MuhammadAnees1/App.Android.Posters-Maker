package com.example.postersmaker;
public class Font {
    private int id;
    private String name;
    private String filePath;

    public int getId() {
        return id;
    }
   public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    public String getFilePath() {
        return filePath;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
