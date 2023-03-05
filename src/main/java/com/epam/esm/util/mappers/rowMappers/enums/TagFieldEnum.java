package com.epam.esm.util.mappers.rowMappers.enums;

public enum TagFieldEnum {
    ID("tag_id"), NAME("tag_name");
    private String name;

    TagFieldEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
