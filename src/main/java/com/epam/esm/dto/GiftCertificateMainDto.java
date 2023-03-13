package com.epam.esm.dto;


import com.epam.esm.models.interfaces.TagInterface;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.math.BigDecimal;
import java.util.List;

public class GiftCertificateMainDto extends GiftCertificateNestedDto {
    private List<TagInterface> tags;

    public GiftCertificateMainDto() {
        super();
    }

    public GiftCertificateMainDto(long id, String name, String description, BigDecimal price, int duration, String createDate, String lastUpdateDate) {
        super(id, name, description, price, duration, createDate, lastUpdateDate);
    }

    public GiftCertificateMainDto(long id, String name, String description, BigDecimal price, int duration, String createDate, String lastUpdateDate, List<TagInterface> tags) {
        super(id, name, description, price, duration, createDate, lastUpdateDate);
        this.tags = tags;
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

        return new EqualsBuilder().append(super.getId(), that.getId())
                .append(super.getDuration(), that.getDuration())
                .append(super.getName(), that.getName())
                .append(super.getDescription(), that.getDescription())
                .append(super.getPrice(), that.getPrice())
                .append(super.getCreateDate(), that.getCreateDate())
                .append(super.getLastUpdateDate(), that.getLastUpdateDate())
                .append(tags, that.tags)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(super.hashCode()).append(tags).toHashCode();
    }

    @Override
    public String toString() {
        return "GiftCertificate{" +
                "id=" + super.getId() +
                ", name='" + super.getName() + '\'' +
                ", description='" + super.getDescription() + '\'' +
                ", price=" + super.getPrice() +
                ", duration=" + super.getDuration() +
                ", createDate=" + super.getCreateDate() +
                ", lastUpdateDate=" + super.getLastUpdateDate() +
                ", tags=" + tags +
                '}';
    }
}
