package com.epam.esm.util.mappers.interfaces;

import java.util.List;
import java.util.Map;

public interface ToListRowMapperInterface<T> {
    List<T> mapRowToList(List<Map<String, Object>> row);
}
