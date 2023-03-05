package com.epam.esm.util.mappers.rowMappers;

;
import com.epam.esm.models.GiftCertificate;
import com.epam.esm.models.Tag;
import com.epam.esm.util.formatters.TimeFormatter;
import com.epam.esm.util.mappers.interfaces.ToListRowMapperInterface;
import com.epam.esm.util.mappers.rowMappers.enums.GiftCertificateFieldEnum;
import com.epam.esm.util.mappers.rowMappers.enums.TagFieldEnum;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class TagRowMapper implements RowMapper<Tag>, ToListRowMapperInterface<Tag> {
    private final TimeFormatter timeFormatter;

    public TagRowMapper(TimeFormatter timeFormatter) {
        this.timeFormatter = timeFormatter;
    }
    @Override
    public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {
        Tag tag = new Tag();
        tag.setId(rs.getLong(TagFieldEnum.ID.getName()));
        tag.setName(rs.getString(TagFieldEnum.NAME.getName()));
        List<GiftCertificate> certificates = new ArrayList<>();
        do {
            if (rs.getLong(GiftCertificateFieldEnum.ID.getName()) != 0) {
                GiftCertificate giftCertificate = new GiftCertificate();
                giftCertificate.setId(rs.getLong(GiftCertificateFieldEnum.ID.getName()));
                giftCertificate.setName(rs.getString(GiftCertificateFieldEnum.NAME.getName()));
                giftCertificate.setDescription(rs.getString(GiftCertificateFieldEnum.DESCRIPTION.getName()));
                giftCertificate.setPrice(rs.getBigDecimal(GiftCertificateFieldEnum.PRICE.getName()));
                giftCertificate.setDuration(rs.getInt(GiftCertificateFieldEnum.DURATION.getName()));
                giftCertificate.setCreateDate(rs.getObject(GiftCertificateFieldEnum.CREATE_DATE.getName(), LocalDateTime.class));
                giftCertificate.setLastUpdateDate(rs.getObject(GiftCertificateFieldEnum.LAST_UPDATE_DATE.getName(), LocalDateTime.class));
                certificates.add(giftCertificate);
            }
        } while (rs.next() && tag.getId() == rs.getLong(TagFieldEnum.ID.getName()));
        tag.setCertificates(certificates);
        return tag;
    }

    @Override
    public List<Tag> mapRowToList(List<Map<String, Object>> rows) {
        if (CollectionUtils.isEmpty(rows))
            return List.of();
        rows.sort(Comparator.comparing(k -> Long.parseLong(String.valueOf(k.get(TagFieldEnum.ID.getName())))));
        List<Tag> result = new ArrayList<>();
        Map<Long, List<GiftCertificate>> resultCertificatesMap = new HashMap<>();
        Tag tmpTag = null;
        for (int i = 0; i < rows.size(); i++) {
            Map<String, Object> rowMap = rows.get(i);
            if (tmpTag == null) {
                tmpTag = new Tag();
                tmpTag.setId(Long.parseLong(String.valueOf(rowMap.get(TagFieldEnum.ID.getName()))));
                tmpTag.setName(String.valueOf(rowMap.get(TagFieldEnum.ID.getName())));
            }
            if (tmpTag.getId() == Long.parseLong(String.valueOf(rowMap.get(TagFieldEnum.ID.getName())))) {
                if (resultCertificatesMap.get(tmpTag.getId()) != null) {
                    resultCertificatesMap.get(tmpTag.getId()).add(buildGiftCertificate(rowMap));
                } else {
                    var gc = buildGiftCertificate(rowMap);
                    if (gc != null) {
                        resultCertificatesMap.put(tmpTag.getId(), new LinkedList<>(
                                List.of(gc)));
                    }
                }
            } else {
                result.add(tmpTag);
                tmpTag = null;
                i--;
            }
        }
            result.add(tmpTag);
            addGiftCertificateToTag(result, resultCertificatesMap);

        return result;
    }

    private void addGiftCertificateToTag(
            List<Tag> result, Map<Long, List<GiftCertificate>> resultCertificatesMap){
        for(var tag: result){
            if(resultCertificatesMap.get(tag.getId())!=null) {
                tag.setCertificates(resultCertificatesMap.get(tag.getId()));
            }else{
                tag.setCertificates(List.of());
            }
        }
    }

    private GiftCertificate buildGiftCertificate(Map<String, Object> rowMap) {
        if (rowMap.get(GiftCertificateFieldEnum.ID.getName()) == null) return null;
        var result = new GiftCertificate();
        result.setId(Long.parseLong(String.valueOf(rowMap.get(GiftCertificateFieldEnum.ID.getName()))));
        result.setName(String.valueOf(rowMap.get(GiftCertificateFieldEnum.NAME.getName())));
        result.setDescription(String.valueOf(rowMap.get(GiftCertificateFieldEnum.DESCRIPTION.getName())));
        result.setPrice(new BigDecimal(String.valueOf(rowMap.get(GiftCertificateFieldEnum.PRICE.getName()))));
        result.setDuration(Integer.parseInt(String.valueOf(rowMap.get(GiftCertificateFieldEnum.DURATION.getName()))));
        result.setCreateDate(LocalDateTime.parse(
                String.valueOf(rowMap.get(GiftCertificateFieldEnum.CREATE_DATE.getName())), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S")));
        result.setLastUpdateDate(LocalDateTime.parse(
                String.valueOf(rowMap.get(GiftCertificateFieldEnum.LAST_UPDATE_DATE.getName())), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S")));
        return result;
    }
}