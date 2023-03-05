package com.epam.esm.util.mappers.rowMappers;

import com.epam.esm.dto.GiftCertificateMainDto;
import com.epam.esm.dto.TagNestedDto;
import com.epam.esm.models.GiftCertificate;
import com.epam.esm.models.interfaces.TagInterface;
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
public class GiftCertificateRowMapper implements RowMapper<GiftCertificate>, ToListRowMapperInterface<GiftCertificate>{


    @Override
    public GiftCertificate mapRow(ResultSet rs, int rowNum) throws SQLException {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(rs.getLong(GiftCertificateFieldEnum.ID.getName()));
        giftCertificate.setName(rs.getString(GiftCertificateFieldEnum.NAME.getName()));
        giftCertificate.setDescription(rs.getString(GiftCertificateFieldEnum.DESCRIPTION.getName()));
        giftCertificate.setPrice(rs.getBigDecimal(GiftCertificateFieldEnum.PRICE.getName()));
        giftCertificate.setDuration(rs.getInt(GiftCertificateFieldEnum.DURATION.getName()));
        giftCertificate.setCreateDate(rs.getObject(GiftCertificateFieldEnum.CREATE_DATE.getName(), LocalDateTime.class));
        giftCertificate.setLastUpdateDate(rs.getObject(GiftCertificateFieldEnum.LAST_UPDATE_DATE.getName(), LocalDateTime.class));
        List<TagInterface> tags = new ArrayList<>();
        do {
            if (rs.getLong(TagFieldEnum.ID.getName()) != 0) {
                TagNestedDto tag = new TagNestedDto();
                tag.setId(rs.getLong(TagFieldEnum.ID.getName()));
                tag.setName(rs.getString(TagFieldEnum.NAME.getName()));
                tags.add(tag);
            }
        } while (rs.next() && giftCertificate.getId() == rs.getLong(GiftCertificateFieldEnum.ID.getName()));
        giftCertificate.setTags(tags);
        return giftCertificate;

    }

    @Override
    public List<GiftCertificate> mapRowToList(List<Map<String, Object>> rows) {
        if(CollectionUtils.isEmpty(rows)) return List.of();
        rows.sort(Comparator.comparing(k -> Long.parseLong(String.valueOf(k.get(GiftCertificateFieldEnum.ID.getName())))));
        List<GiftCertificate> result = new ArrayList<>();
        Map<Long, List<TagInterface>> resultTagMap = new HashMap<>();
        GiftCertificate tmpCert = null;
        for(int i = 0; i < rows.size(); i++) {
            Map<String, Object> rowMap = rows.get(i);
            if(tmpCert == null) {
                tmpCert = new GiftCertificate();
                tmpCert.setId(Long.parseLong(String.valueOf(rowMap.get(GiftCertificateFieldEnum.ID.getName()))));
                tmpCert.setName(String.valueOf(rowMap.get(GiftCertificateFieldEnum.NAME.getName())));
                tmpCert.setDescription(String.valueOf(rowMap.get(GiftCertificateFieldEnum.DESCRIPTION.getName())));
                tmpCert.setPrice(new BigDecimal(String.valueOf(rowMap.get(GiftCertificateFieldEnum.PRICE.getName()))));
                tmpCert.setDuration(Integer.parseInt(String.valueOf(rowMap.get(GiftCertificateFieldEnum.DURATION.getName()))));
                tmpCert.setCreateDate(LocalDateTime.parse(
                        String.valueOf(rowMap.get(GiftCertificateFieldEnum.CREATE_DATE.getName())), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S")));
                tmpCert.setLastUpdateDate(LocalDateTime.parse(
                        String.valueOf(rowMap.get(GiftCertificateFieldEnum.LAST_UPDATE_DATE.getName())), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S")));

            }
            if (tmpCert.getId() == Long.parseLong(String.valueOf(rowMap.get(GiftCertificateFieldEnum.ID.getName())))) {
                if (resultTagMap.get(tmpCert.getId()) != null) {
                    resultTagMap.get(tmpCert.getId()).add(buildTagNestedDto(rowMap));
                } else {
                    var tag = buildTagNestedDto(rowMap);
                    if (tag != null) {
                        resultTagMap.put(tmpCert.getId(), new LinkedList<>(
                                List.of(tag)));
                    }
                }
            } else {
                result.add(tmpCert);
                tmpCert = null;
                i--;
            }
        }
        result.add(tmpCert);
        addTagNestedDtoToGiftCertificate(result, resultTagMap);
        return result;
    }

    private void addTagNestedDtoToGiftCertificate(
            List<GiftCertificate> result, Map<Long, List<TagInterface>> resultTagMap){
        for(var gc: result){
            if(resultTagMap.get(gc.getId())!=null) {
                gc.setTags(resultTagMap.get(gc.getId()));
            }else{
                gc.setTags(List.of());
            }
        }
    }

    private TagNestedDto buildTagNestedDto(Map<String, Object> rowMap) {
        if (rowMap.get(TagFieldEnum.ID.getName()) == null)
            return null;
        var result = new TagNestedDto();
        result.setId(Long.parseLong(String.valueOf(rowMap.get(TagFieldEnum.ID.getName()))));
        result.setName(String.valueOf(rowMap.get(TagFieldEnum.NAME.getName())));
        return result;
    }
}