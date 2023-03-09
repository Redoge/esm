package com.epam.esm.dao.interfaces.ManyToManyRelation;

public interface GiftCertificateTagInterface {
    boolean removeFromGiftCertificateByTagIdAndCertId(long tagId, long certId);

    boolean addTagToCertificateByTagIdAndCertId(long tagId, long certId);

}
