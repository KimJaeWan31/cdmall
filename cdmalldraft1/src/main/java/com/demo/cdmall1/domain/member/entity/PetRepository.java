package com.demo.cdmall1.domain.member.entity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, PetId> {
	List<Pet> findByMember(Member member);

	//Optional<Pet> findByPetno(Integer petno);
}
