package com.epam.esm.dto;

import com.epam.esm.models.GiftCertificate;
import com.epam.esm.models.interfaces.TagInterface;

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
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", certificates=" + certificates +
                '}';
    }
}
