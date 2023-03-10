package com.epam.esm.dto;

import com.epam.esm.models.GiftCertificate;
import com.epam.esm.models.interfaces.TagInterface;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.List;

public class TagMainDto implements TagInterface {
    private long id;
    private String name;
    private List<GiftCertificateNestedDto> certificates;

    public TagMainDto() {
    }

    public TagMainDto(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public TagMainDto(long id, String name, List<GiftCertificateNestedDto> certificates) {
        this.id = id;
        this.name = name;
        this.certificates = certificates;
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

    public List<GiftCertificateNestedDto> getCertificates() {
        return certificates;
    }

    public void setCertificates(List<GiftCertificateNestedDto> certificates) {
        this.certificates = certificates;
    }
    public void addCertificate(GiftCertificateNestedDto certificate) {
        this.certificates.add(certificate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        TagMainDto that = (TagMainDto) o;

        return new EqualsBuilder().append(id, that.id).append(name, that.name).append(certificates, that.certificates).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(name).append(certificates).toHashCode();
    }

    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", certificates=" + certificates +
                '}';
    }
}
