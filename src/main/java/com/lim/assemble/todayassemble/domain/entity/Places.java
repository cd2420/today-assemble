package com.lim.assemble.todayassemble.domain.entity;

import com.lim.assemble.todayassemble.domain.entity.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "places")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Places extends BaseEntity {

    private String name;

    private String address;

    private String latitude;

    private String longtitude;

}
