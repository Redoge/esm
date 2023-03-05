//package com.epam.esm.services;
//
//import com.epam.esm.DAO.TagDao;
//import com.epam.esm.dto.GiftCertificateMainDto;
//import com.epam.esm.dto.TagMainDto;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.when;
//@ExtendWith(MockitoExtension.class)
//class TagServiceTest {
//    @Mock
//    private TagDao tagDao;
//    @InjectMocks
//    private TagService service;
//    private final List<TagMainDto> tagList = createTagListForTest();
//    @Test
//    void getAllTest() {
//        when(tagDao.findAll()).thenReturn(tagList);
//        assertEquals(service.getAll(), tagList);
//    }
//    @Test
//    void getByIdTest() {
//        when(tagDao.findById(1L)).thenReturn(Optional.ofNullable(tagList.get(0)));
//        assertEquals(service.getById(1L).get(), tagList.get(0));
//    }
//    @Test
//    void getByNameTest() {
//        when(tagDao.findByName("name2")).thenReturn(Optional.ofNullable(tagList.get(1)));
//        assertEquals(service.getByName("name2").get(), tagList.get(1));
//    }
//
//    @Test
//    void deleteByIdTest() {
//        when(tagDao.deleteById(3L)).thenReturn(true);
//        assertTrue(service.deleteById(3L));
//    }
//
//    @Test
//    void saveTest() {
//        when(tagDao.save("name56")).thenReturn(true);
//        assertTrue(service.save("name56"));
//    }
//
//    private List<TagMainDto> createTagListForTest(){
//    return List.of(
//            new TagMainDto(1L, "name1"),
//            new TagMainDto(2L, "name2"),
//            new TagMainDto(3L, "name3"),
//            new TagMainDto(4L, "name4"),
//            new TagMainDto(5L, "name5")
//    );
//    }
//}