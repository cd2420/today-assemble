package com.lim.assemble.todayassemble.repository;

import com.lim.assemble.todayassemble.entity.EntityFactory;
import com.lim.assemble.todayassemble.entity.Entity_Type;
import com.lim.assemble.todayassemble.places.entity.Places;
import com.lim.assemble.todayassemble.places.entity.PlacesImages;
import com.lim.assemble.todayassemble.places.repository.PlacesImagesRepository;
import com.lim.assemble.todayassemble.places.repository.PlacesRepository;
import com.lim.assemble.todayassemble.common.type.ImagesType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class PlacesImagesRepositoryTest {

    @Autowired
    private PlacesRepository placesRepository;

    @Autowired
    private PlacesImagesRepository placesImagesRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("기본 장소 이미지 > 기본 장소 이미지 생성")
    @Transactional
    void givenBasicData_whenSaveRepository_thenReturnsBasicData() {
        // given
        PlacesImages placesImages = (PlacesImages) EntityFactory.createEntity(Entity_Type.PLACES_IMAGES);
        Places places = placesImages.getPlaces();

        List<PlacesImages> givenPlacesImagesList = new ArrayList<>();
        givenPlacesImagesList.add(placesImages);
        givenPlacesImagesList.add(
                PlacesImages.builder()
                        .imagesType(ImagesType.SUB)
                        .image("SUB IMAGE ~~~~~~")
                        .places(places)
                        .build()
        );

        // when
        placesRepository.save(places);
        placesImagesRepository.saveAll(givenPlacesImagesList);

        entityManager.clear();

        // then
        List<Places> placesList = placesRepository.findAll();
        List<PlacesImages> placesImagesList = placesImagesRepository.findAll();

        assertEquals(placesImagesList.size(), 2);

        Iterator<PlacesImages> itPlaces = placesList.get(0).getPlacesImagesSet().iterator();
        while(itPlaces.hasNext()) {
            PlacesImages e = itPlaces.next();
            log.info(e.getImage());
        }
    }

}