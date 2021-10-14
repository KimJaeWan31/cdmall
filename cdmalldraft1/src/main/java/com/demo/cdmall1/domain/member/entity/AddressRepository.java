package com.demo.cdmall1.domain.member.entity;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface AddressRepository extends CrudRepository<Address, AddressId>{
	List<Address> findByMember(Member member);

	Optional<Address> findByAdno(Integer adno);
}
