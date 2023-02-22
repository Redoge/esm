package com.epam.esm.util.mappers;

import com.epam.esm.dto.GiftCertificateNestedDto;
import com.epam.esm.dto.TagMainDto;
import com.epam.esm.util.formatters.TimeFormatter;
import com.epam.esm.util.mappers.interfaces.ToListRowMapperInterface;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class TagRowMapper implements RowMapper<TagMainDto>, ToListRowMapperInterface<TagMainDto> {
    private final TimeFormatter timeFormatter;

    public TagRowMapper(TimeFormatter timeFormatter) {
        this.timeFormatter = timeFormatter;
    }
    @Override
    public TagMainDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        TagMainDto tag = new TagMainDto();
        tag.setId(rs.getLong("tag_id"));
        tag.setName(rs.getString("tag_name"));
        List<GiftCertificateNestedDto> certificates = new ArrayList<>();
        do {
            GiftCertificateNestedDto giftCertificate = new GiftCertificateNestedDto();
            giftCertificate.setId(rs.getLong("gift_certificate_id"));
            giftCertificate.setName(rs.getString("gift_certificate_name"));
            giftCertificate.setDescription(rs.getString("gift_certificate_description"));
            giftCertificate.setPrice(rs.getBigDecimal("gift_certificate_price"));
            giftCertificate.setDuration(rs.getInt("gift_certificate_duration"));
            giftCertificate.setCreateDate(timeFormatter.timeToIso8601(rs.getObject("gift_certificate_create_date", LocalDateTime.class)));
            giftCertificate.setLastUpdateDate(timeFormatter.timeToIso8601(rs.getObject("gift_certificate_last_update_date", LocalDateTime.class)));
            certificates.add(giftCertificate);
        } while (rs.next() && tag.getId() == rs.getLong("tag_id"));
        tag.setCertificates(certificates);
        return tag;
    }

    @Override
    public List<TagMainDto> mapRowToList(List<Map<String, Object>> rows) {
        if(rows == null || rows.size()==0) return List.of();
        rows.sort(Comparator.comparing(k->Long.parseLong(String.valueOf(k.get("tag_id")))));
        List<TagMainDto> result = new ArrayList<>();
        Map<Long, List<GiftCertificateNestedDto>> resultCertificatesMap = new HashMap<>();
        TagMainDto tmpTag = null;
        for(int i = 0; i < rows.size(); i++) {
            Map<String, Object> rowMap = rows.get(i);
            if(tmpTag == null){
                tmpTag = new TagMainDto();
                tmpTag.setId(Long.parseLong(String.valueOf(rowMap.get("tag_id"))));
                tmpTag.setName(String.valueOf(rowMap.get("tag_name")));
            }
            if(tmpTag.getId() == Long.parseLong(String.valueOf(rowMap.get("tag_id")))){
                if(resultCertificatesMap.get(tmpTag.getId())!=null){
                    resultCertificatesMap.get(tmpTag.getId()).add(buildGiftCertificateNestedDto(rowMap));
                }else{
                    resultCertificatesMap.put(tmpTag.getId(), new LinkedList<>(
                            List.of(buildGiftCertificateNestedDto(rowMap))));
                }
            }else{
                result.add(tmpTag);
                tmpTag = null;
                i--;
            }
        }
        result.add(tmpTag);
        addGiftCertificateNestedDtoToTagMainDto(result, resultCertificatesMap);
        return result;
    }

    private void addGiftCertificateNestedDtoToTagMainDto(
            List<TagMainDto> result, Map<Long, List<GiftCertificateNestedDto>> resultCertificatesMap){
        for(var tag: result){
            tag.setCertificates(resultCertificatesMap.get(tag.getId()));
        }
    }

    private GiftCertificateNestedDto buildGiftCertificateNestedDto(Map<String, Object> rowMap){
        var result = new GiftCertificateNestedDto();
        result.setId(Long.parseLong(String.valueOf(rowMap.get("gift_certificate_id"))));
        result.setName(String.valueOf(rowMap.get("gift_certificate_name")));
        result.setDescription(String.valueOf(rowMap.get("gift_certificate_description")));
        result.setPrice(new BigDecimal(String.valueOf(rowMap.get("gift_certificate_price"))));
        result.setDuration(Integer.parseInt(String.valueOf(rowMap.get("gift_certificate_duration"))));
        result.setCreateDate(timeFormatter.timeToIso8601(LocalDateTime.parse(
                String.valueOf(rowMap.get("gift_certificate_create_date")), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S"))));
        result.setLastUpdateDate(timeFormatter.timeToIso8601(LocalDateTime.parse(
                String.valueOf(rowMap.get("gift_certificate_last_update_date")), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S"))));
        return result;
    }
}