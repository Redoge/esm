package com.epam.esm.util.mappers.rowMappers;

import com.epam.esm.dto.TagNestedDto;
import com.epam.esm.models.GiftCertificate;
import com.epam.esm.models.interfaces.TagInterface;
import com.epam.esm.util.mappers.interfaces.ToListRowMapperInterface;
import com.epam.esm.util.mappers.rowMappers.fieldNames.TagField;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.epam.esm.util.mappers.rowMappers.fieldNames.GiftCertificateField.*;
import static java.lang.Long.parseLong;
import static java.lang.String.valueOf;
import static java.util.Comparator.comparing;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;

@Component
public class GiftCertificateRowMapper implements RowMapper<GiftCertificate>, ToListRowMapperInterface<GiftCertificate>{
    private static final String TIME_PATTERN = "yyyy-MM-dd HH:mm:ss.S";

    @Override
    public GiftCertificate mapRow(ResultSet rs, int rowNum) throws SQLException {
        var giftCertificate = mapResultSetToGiftCertificate(rs);
        List<TagInterface> tags = new ArrayList<>();
        do {
            if (rs.getLong(TagField.ID) != 0) {
                TagNestedDto tag = new TagNestedDto();
                tag.setId(rs.getLong(TagField.ID));
                tag.setName(rs.getString(TagField.NAME));
                tags.add(tag);
            }
        } while (rs.next() && giftCertificate.getId() == rs.getLong(ID));
        giftCertificate.setTags(tags);
        return giftCertificate;

    }

    @Override
    public List<GiftCertificate> mapRowToList(List<Map<String, Object>> rows) {
        if (isEmpty(rows))
            return List.of();
        rows.sort(comparing(row -> parseLong(valueOf(row.get(ID)))));

        List<GiftCertificate> gCert = new ArrayList<>();
        Map<Long, List<TagInterface>> resultTagMap = new HashMap<>();
        GiftCertificate certificate = null;
        Map<String, Object> rowMap;
        TagNestedDto tag;

        for (int i = 0; i < rows.size(); i++) {
            rowMap = rows.get(i);
            if (certificate == null) {
                certificate = mapRowMapToGiftCertificate(rowMap);
            }
            if (certificate.getId() == parseLong(valueOf(rowMap.get(ID)))) {
                tag = buildTagNestedDto(rowMap);
                if (tag != null) {
                    resultTagMap.computeIfAbsent(certificate.getId(), k -> new ArrayList<>()).add(tag);
                }
            } else {
                gCert.add(certificate);
                certificate = null;
                i--;
            }
        }
        gCert.add(certificate);
        addTagNestedDtoToGiftCertificate(gCert, resultTagMap);
        return gCert;
    }

    private void addTagNestedDtoToGiftCertificate(
        List<GiftCertificate> gCerts, Map<Long, List<TagInterface>> resultTagMap){
        for(var gCert: gCerts){
            if(resultTagMap.get(gCert.getId())!=null) {
                gCert.setTags(resultTagMap.get(gCert.getId()));
            }else{
                gCert.setTags(List.of());
            }
        }
    }

    private TagNestedDto buildTagNestedDto(Map<String, Object> rowMap) {
        if (rowMap.get(TagField.ID) == null)
            return null;
        var tag = new TagNestedDto();
        tag.setId(parseLong(valueOf(rowMap.get(TagField.ID))));
        tag.setName(valueOf(rowMap.get(TagField.NAME)));
        return tag;
    }

    protected GiftCertificate mapResultSetToGiftCertificate(ResultSet resultSet) throws SQLException {
        var gCert = new GiftCertificate();
        gCert.setId(resultSet.getLong(ID));
        gCert.setName(resultSet.getString(NAME));
        gCert.setDescription(resultSet.getString(DESCRIPTION));
        gCert.setPrice(resultSet.getBigDecimal(PRICE));
        gCert.setDuration(resultSet.getInt(DURATION));
        gCert.setCreateDate(resultSet.getObject(CREATE_DATE, LocalDateTime.class));
        gCert.setLastUpdateDate(resultSet.getObject(LAST_UPDATE_DATE, LocalDateTime.class));
        return gCert;
    }

    protected GiftCertificate mapRowMapToGiftCertificate(Map<String, Object> rowMap) {
        var gCert = new GiftCertificate();
        gCert.setId(parseLong(valueOf(rowMap.get(ID))));
        gCert.setName(valueOf(rowMap.get(NAME)));
        gCert.setDescription(valueOf(rowMap.get(DESCRIPTION)));
        gCert.setPrice(new BigDecimal(valueOf(rowMap.get(PRICE))));
        gCert.setDuration(Integer.parseInt(valueOf(rowMap.get(DURATION))));
        gCert.setCreateDate(LocalDateTime.parse(
                valueOf(rowMap.get(CREATE_DATE)), DateTimeFormatter.ofPattern(TIME_PATTERN)));
        gCert.setLastUpdateDate(LocalDateTime.parse(
                valueOf(rowMap.get(LAST_UPDATE_DATE)), DateTimeFormatter.ofPattern(TIME_PATTERN)));
        return gCert;
    }
}