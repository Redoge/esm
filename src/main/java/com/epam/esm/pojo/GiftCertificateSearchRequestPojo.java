package com.epam.esm.pojo;


public class GiftCertificateSearchRequestPojo {
    private String name;
    private String description;
    private String tagName;
    private String sortByName;
    private String sortByTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getSortByName() {
        return sortByName;
    }

    public void setSortByName(String sortByName) {
        this.sortByName = sortByName;
    }

    public String getSortByTime() {
        return sortByTime;
    }

    public void setSortByTime(String sortByTime) {
        this.sortByTime = sortByTime;
    }

    @Override
    public String toString() {
        return "GiftCertificateSearchRequestPojo{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", tagName='" + tagName + '\'' +
                ", sortByName='" + sortByName + '\'' +
                ", sortByTime='" + sortByTime + '\'' +
                '}';
    }

}
