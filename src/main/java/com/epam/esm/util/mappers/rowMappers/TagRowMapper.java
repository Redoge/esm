package com.epam.esm.util.mappers.rowMappers;

import com.epam.esm.models.GiftCertificate;
import com.epam.esm.models.Tag;
import com.epam.esm.util.mappers.interfaces.ToListRowMapperInterface;
import com.epam.esm.util.mappers.rowMappers.fieldNames.TagField;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static com.epam.esm.util.mappers.rowMappers.fieldNames.GiftCertificateField.ID;

@Component
public class TagRowMapper implements RowMapper<Tag>, ToListRowMapperInterface<Tag> {
    private final GiftCertificateRowMapper giftCertificateRowMapper;

    public TagRowMapper(GiftCertificateRowMapper giftCertificateRowMapper) {
        this.giftCertificateRowMapper = giftCertificateRowMapper;
    }

    @Override
    public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {
        Tag tag = new Tag();
        tag.setId(rs.getLong(TagField.ID));
        tag.setName(rs.getString(TagField.NAME));
        List<GiftCertificate> certificates = new ArrayList<>();
        do {
            if (rs.getLong(ID) != 0) {
                var giftCertificate = giftCertificateRowMapper.mapResultSetToGiftCertificate(rs);
                certificates.add(giftCertificate);
            }
        } while (rs.next() && tag.getId() == rs.getLong(TagField.ID));
        tag.setCertificates(certificates);
        return tag;
    }

    @Override
    public List<Tag> mapRowToList(List<Map<String, Object>> rows) {
        if (CollectionUtils.isEmpty(rows))
            return List.of();
        rows.sort(Comparator.comparing(k -> Long.parseLong(String.valueOf(k.get(TagField.ID)))));

        List<Tag> result = new ArrayList<>();
        Map<Long, List<GiftCertificate>> resultCertificatesMap = new HashMap<>();
        Tag tag = null;
        Map<String, Object> rowMap;
        GiftCertificate gCert;

        for (int i = 0; i < rows.size(); i++) {
            rowMap = rows.get(i);
            if (tag == null) {
                tag = mapRowMapToTag(rowMap);
            }
            if (tag.getId() == Long.parseLong(String.valueOf(rowMap.get(TagField.ID)))) {
                gCert = buildGiftCertificate(rowMap);
                if (gCert != null) {
                    resultCertificatesMap.computeIfAbsent(tag.getId(), k -> new ArrayList<>()).add(gCert);
                }
            } else {
                result.add(tag);
                tag = null;
                i--;
            }
        }
        result.add(tag);
        addGiftCertificateToTag(result, resultCertificatesMap);
        return result;
    }

    private void addGiftCertificateToTag(
            List<Tag> result, Map<Long, List<GiftCertificate>> resultCertificatesMap){
        for(var tag: result){
            if(resultCertificatesMap.get(tag.getId())!=null) {
                tag.setCertificates(resultCertificatesMap.get(tag.getId()));
            } else {
                tag.setCertificates(List.of());
            }
        }
    }

    private GiftCertificate buildGiftCertificate(Map<String, Object> rowMap) {
        if (rowMap.get(ID) == null) return null;
        return giftCertificateRowMapper.mapRowMapToGiftCertificate(rowMap);
    }

    private Tag mapRowMapToTag(Map<String, Object> rowMap) {
        var id = Long.parseLong(String.valueOf(rowMap.get(TagField.ID)));
        var name = String.valueOf(rowMap.get(TagField.NAME));
        return new Tag(id, name);
    }
}