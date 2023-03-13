package com.epam.esm.pojo;


import com.epam.esm.util.sorters.enums.SortingOrder;
import com.epam.esm.util.sorters.enums.SortingType;

public class GiftCertificateSearchRequestPojo {
    private String name;
    private String description;
    private String tagName;
    private SortingType sortingType;
    private SortingOrder sortingOrder;

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

    public SortingType getSortingType() {
        return sortingType;
    }

    public void setSortingType(SortingType sortingType) {
        this.sortingType = sortingType;
    }

    public SortingOrder getSortingOrder() {
        return sortingOrder;
    }

    public void setSortingOrder(SortingOrder sortingOrder) {
        this.sortingOrder = sortingOrder;
    }

    @Override
    public String toString() {
        return "GiftCertificateSearchRequestPojo{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", tagName='" + tagName + '\'' +
                ", sortingType=" + sortingType +
                ", sortingOrder=" + sortingOrder +
                '}';
    }
}
