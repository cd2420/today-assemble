package com.lim.assemble.todayassemble.repository;

import com.lim.assemble.todayassemble.places.entity.Places;
import com.lim.assemble.todayassemble.entity.EntityFactory;
import com.lim.assemble.todayassemble.entity.Entity_Type;
import com.lim.assemble.todayassemble.places.repository.PlacesRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PlacesRepositoryTest {

    @Autowired
    private PlacesRepository placesRepository;

    @Test
    @DisplayName("기본 장소 저장")
    @Transactional
    public void givenBasicData_whenSaveRepository_thenReturnsBasicData(){
        // given
        Places places = (Places) EntityFactory.createEntity(Entity_Type.PLACES);

        // when
        placesRepository.save(places);

        // then
        List<Places> placesList = placesRepository.findAll();
        assertEquals(places.getName(), placesList.get(0).getName());
    }

}