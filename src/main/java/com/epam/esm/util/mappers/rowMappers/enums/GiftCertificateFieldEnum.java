package com.epam.esm.util.mappers.rowMappers.enums;

public enum GiftCertificateFieldEnum {
    ID("gift_certificate_id"),
    NAME("gift_certificate_name"),
    DESCRIPTION("gift_certificate_description"),
    PRICE("gift_certificate_price"),
    DURATION("gift_certificate_duration"),
    CREATE_DATE("gift_certificate_create_date"),
    LAST_UPDATE_DATE("gift_certificate_last_update_date");
    private String name;

    GiftCertificateFieldEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
