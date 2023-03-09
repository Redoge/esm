package com.epam.esm.dto;


import com.epam.esm.models.interfaces.GiftCertificateInterface;
import com.epam.esm.models.interfaces.TagInterface;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.math.BigDecimal;
import java.util.List;

public class GiftCertificateMainDto implements GiftCertificateInterface {
    private long id;
    private String name;
    private String description;
    private BigDecimal price;
    private int duration;
    private String createDate;
    private String lastUpdateDate;
    private List<TagInterface> tags;

    public GiftCertificateMainDto() {
    }

    public GiftCertificateMainDto(long id, String name, String description, BigDecimal price, int duration, String createDate, String lastUpdateDate, List<TagInterface> tags) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
        this.tags = tags;
    }

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

    public List<TagInterface> getTags() {
        return tags;
    }

    public void setTags(List<TagInterface> tags) {
        this.tags = tags;
    }

    public void addTag(TagNestedDto tag) {
        this.tags.add(tag);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        GiftCertificateMainDto that = (GiftCertificateMainDto) o;

        return new EqualsBuilder().append(id, that.id).append(duration, that.duration).append(name, that.name).append(description, that.description).append(price, that.price).append(createDate, that.createDate).append(lastUpdateDate, that.lastUpdateDate).append(tags, that.tags).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(name).append(description).append(price).append(duration).append(createDate).append(lastUpdateDate).append(tags).toHashCode();
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
