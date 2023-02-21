package com.epam.esm.util.mappers;

import com.epam.esm.models.GiftCertificate;
import com.epam.esm.models.Tag;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Component
public class GiftCertificateRowMapper implements RowMapper<GiftCertificate> {
    @Override
    public GiftCertificate mapRow(ResultSet rs, int rowNum) throws SQLException {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(rs.getLong("gift_certificate_id"));
        giftCertificate.setName(rs.getString("gift_certificate_name"));
        giftCertificate.setDescription(rs.getString("gift_certificate_description"));
        giftCertificate.setPrice(rs.getBigDecimal("gift_certificate_price"));
        giftCertificate.setDuration(rs.getInt("gift_certificate_duration"));
        giftCertificate.setCreateDate(rs.getObject("gift_certificate_create_date", LocalDateTime.class));
        giftCertificate.setLastUpdateDate(rs.getObject("gift_certificate_last_update_date", LocalDateTime.class));
        List<Tag> tags = new ArrayList<>();
        do {
            Tag tag = new Tag();
            tag.setId(rs.getLong("tag_id"));
            tag.setName(rs.getString("tag_name"));
            tags.add(tag);
        } while (rs.next() && giftCertificate.getId() == rs.getLong("gift_certificate_id"));
        giftCertificate.setTags(tags);
        return giftCertificate;

    }
}