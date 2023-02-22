package com.epam.esm.dto;

import com.epam.esm.models.Tag;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class GiftCertificateMainDto {
    private long id;
    private String name;
    private String description;
    private BigDecimal price;
    private int duration;
    private String createDate;
    private String lastUpdateDate;
    private List<TagNestedDto> tags;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(String lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public List<TagNestedDto> getTags() {
        return tags;
    }

    public void setTags(List<TagNestedDto> tags) {
        this.tags = tags;
    }

    public void addTag(TagNestedDto tag) {
        this.tags.add(tag);
    }
    @Override
    public String toString() {
        return "GiftCertificate{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", duration=" + duration +
                ", createDate=" + createDate +
                ", lastUpdateDate=" + lastUpdateDate +
                ", tags=" + tags +
                '}';
    }
}
