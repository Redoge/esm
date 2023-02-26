package com.epam.esm.util.mappers;

import com.epam.esm.dto.GiftCertificateMainDto;
import com.epam.esm.dto.GiftCertificateNestedDto;
import com.epam.esm.dto.TagMainDto;
import com.epam.esm.dto.TagNestedDto;
import com.epam.esm.models.Tag;
import com.epam.esm.models.interfaces.TagInterface;
import com.epam.esm.util.formatters.TimeFormatter;
import com.epam.esm.util.mappers.interfaces.ToListRowMapperInterface;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class GiftCertificateRowMapper implements RowMapper<GiftCertificateMainDto>, ToListRowMapperInterface<GiftCertificateMainDto>{
    private final TimeFormatter timeFormatter;

    public GiftCertificateRowMapper(TimeFormatter timeFormatter) {
        this.timeFormatter = timeFormatter;
    }

    @Override
    public GiftCertificateMainDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        GiftCertificateMainDto giftCertificate = new GiftCertificateMainDto();
        giftCertificate.setId(rs.getLong("gift_certificate_id"));
        giftCertificate.setName(rs.getString("gift_certificate_name"));
        giftCertificate.setDescription(rs.getString("gift_certificate_description"));
        giftCertificate.setPrice(rs.getBigDecimal("gift_certificate_price"));
        giftCertificate.setDuration(rs.getInt("gift_certificate_duration"));
        giftCertificate.setCreateDate(timeFormatter.timeToIso8601(rs.getObject("gift_certificate_create_date", LocalDateTime.class)));
        giftCertificate.setLastUpdateDate(timeFormatter.timeToIso8601(rs.getObject("gift_certificate_last_update_date", LocalDateTime.class)));
        List<TagInterface> tags = new ArrayList<>();
        do {
            if(rs.getLong("tag_id")!=0) {
                TagNestedDto tag = new TagNestedDto();
                tag.setId(rs.getLong("tag_id"));
                tag.setName(rs.getString("tag_name"));
                tags.add(tag);
            }
        } while (rs.next() && giftCertificate.getId() == rs.getLong("gift_certificate_id"));
        giftCertificate.setTags(tags);
        return giftCertificate;

    }

    @Override
    public List<GiftCertificateMainDto> mapRowToList(List<Map<String, Object>> rows) {
        if(rows == null || rows.size()==0) return List.of();
        rows.sort(Comparator.comparing(k->Long.parseLong(String.valueOf(k.get("gift_certificate_id")))));
        List<GiftCertificateMainDto> result = new ArrayList<>();
        Map<Long, List<TagInterface>> resultTagMap = new HashMap<>();
        GiftCertificateMainDto tmpCert = null;
        for(int i = 0; i < rows.size(); i++) {
            Map<String, Object> rowMap = rows.get(i);
            if(tmpCert == null){
                tmpCert = new GiftCertificateMainDto();
                tmpCert.setId(Long.parseLong(String.valueOf(rowMap.get("gift_certificate_id"))));
                tmpCert.setName(String.valueOf(rowMap.get("gift_certificate_name")));
                tmpCert.setDescription(String.valueOf(rowMap.get("gift_certificate_description")));
                tmpCert.setPrice(new BigDecimal(String.valueOf(rowMap.get("gift_certificate_price"))));
                tmpCert.setDuration(Integer.parseInt(String.valueOf(rowMap.get("gift_certificate_duration"))));
                tmpCert.setCreateDate(timeFormatter.timeToIso8601(LocalDateTime.parse(
                        String.valueOf(rowMap.get("gift_certificate_create_date")), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S"))));
                tmpCert.setLastUpdateDate(timeFormatter.timeToIso8601(LocalDateTime.parse(
                        String.valueOf(rowMap.get("gift_certificate_last_update_date")), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S"))));

            }
            if(tmpCert.getId() == Long.parseLong(String.valueOf(rowMap.get("gift_certificate_id")))){
                if(resultTagMap.get(tmpCert.getId())!=null){
                    resultTagMap.get(tmpCert.getId()).add(buildTagNestedDto(rowMap));
                }else{
                    var tag = buildTagNestedDto(rowMap);
                    if (tag != null) {
                        resultTagMap.put(tmpCert.getId(), new LinkedList<>(
                                List.of(tag)));
                    }
                }
            }else{
                result.add(tmpCert);
                tmpCert = null;
                i--;
            }
        }
        result.add(tmpCert);
        addTagNestedDtoToGiftCertificateMainDto(result, resultTagMap);
        return result;
    }

    private void addTagNestedDtoToGiftCertificateMainDto(
            List<GiftCertificateMainDto> result, Map<Long, List<TagInterface>> resultTagMap){
        for(var gc: result){
            if(resultTagMap.get(gc.getId())!=null) {
                gc.setTags(resultTagMap.get(gc.getId()));
            }else{
                gc.setTags(List.of());
            }
        }
    }

    private TagNestedDto buildTagNestedDto(Map<String, Object> rowMap){
        if(rowMap.get("tag_id")==null)return null;
        var result = new TagNestedDto();
        result.setId(Long.parseLong(String.valueOf(rowMap.get("tag_id"))));
        result.setName(String.valueOf(rowMap.get("tag_name")));
        return result;
    }
}