package com.lim.assemble.todayassemble.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "accounts")
@Getter @Builder
@AllArgsConstructor @NoArgsConstructor
public class Accounts extends BaseEntity {



}
