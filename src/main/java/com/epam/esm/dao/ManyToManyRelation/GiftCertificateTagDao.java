package com.epam.esm.dao.ManyToManyRelation;

import com.epam.esm.dao.interfaces.ManyToManyRelation.GiftCertificateTagInterface;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class GiftCertificateTagDao implements GiftCertificateTagInterface {

    private static final String DELETE_FROM_CERT_BY_TAG_ID_AND_CERTIFICATE_ID_QUERY = "DELETE FROM gift_certificate_tag " +
            "WHERE tag_id = ? AND gift_certificate_id = ?;";
    private static final String ADD_TO_CERT_BY_TAG_ID_AND_CERTIFICATE_ID_QUERY = "INSERT INTO gift_certificate_tag " +
            "(tag_id, gift_certificate_id) VALUE(?, ?);";
    private final JdbcTemplate jdbcTemplate;

    public GiftCertificateTagDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean removeFromGiftCertificateByTagIdAndCertId(long tagId, long certId) {
        return jdbcTemplate.update(DELETE_FROM_CERT_BY_TAG_ID_AND_CERTIFICATE_ID_QUERY, tagId, certId) != 0;
    }

    @Override
    public boolean addTagToCertificateByTagIdAndCertId(long tagId, long certId) {
        try {
            return jdbcTemplate.update(ADD_TO_CERT_BY_TAG_ID_AND_CERTIFICATE_ID_QUERY, tagId, certId) != 0;
        }catch (DataAccessException e){
            return false;
        }
    }

}
