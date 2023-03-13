package com.epam.esm.dto;

import com.epam.esm.models.GiftCertificate;
import com.epam.esm.models.interfaces.TagInterface;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.List;

public class TagMainDto extends TagNestedDto {

    private List<GiftCertificateNestedDto> certificates;

    public TagMainDto() {
        super();
    }

    public TagMainDto(long id, String name) {
        super(id, name);
    }

    public TagMainDto(long id, String name, List<GiftCertificateNestedDto> certificates) {
        super(id, name);
        this.certificates = certificates;
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

        return new EqualsBuilder().append(super.getId(), that.getId()).append(super.getName(), that.getName()).append(certificates, that.certificates).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(super.getId()).append(super.getName()).append(certificates).toHashCode();
    }

    @Override
    public String toString() {
        return "Tag{" +
                "id=" + super.getId() +
                ", name='" + super.getName() + '\'' +
                ", certificates=" + certificates +
                '}';
    }
}
