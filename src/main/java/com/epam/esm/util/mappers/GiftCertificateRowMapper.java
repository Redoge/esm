package com.epam.esm.util.mappers;

import com.epam.esm.models.GiftCertificate;
import com.epam.esm.models.Tag;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GiftCertificateRowMapper implements RowMapper<GiftCertificate> {

    @Override
    public GiftCertificate mapRow(ResultSet rs, int rowNum) throws SQLException {
        GiftCertificate giftCertificate = new GiftCertificate();

        giftCertificate.setId(rs.getLong("id"));
        giftCertificate.setName(rs.getString("name"));
        giftCertificate.setDescription(rs.getString("description"));
        giftCertificate.setPrice(rs.getBigDecimal("price"));
        giftCertificate.setDuration(rs.getInt("duration"));
        giftCertificate.setCreateDate(rs.getTimestamp("create_date").toLocalDateTime());
        giftCertificate.setLastUpdateDate(rs.getTimestamp("last_update_date").toLocalDateTime());

        // Map tags
        List<Tag> tags = new ArrayList<>();
        do {
            Tag tag = new Tag();
            tag.setId(rs.getLong("tag_id"));
            tag.setName(rs.getString("tag_name"));
            tags.add(tag);
        } while (rs.next());

        giftCertificate.setTags(tags);

        return giftCertificate;
    }
}

