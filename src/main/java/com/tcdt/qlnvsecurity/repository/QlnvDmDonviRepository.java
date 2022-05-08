package com.tcdt.qlnvsecurity.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


import com.tcdt.qlnvsecurity.table.QlnvDmDonvi;

@Repository
public interface QlnvDmDonviRepository extends CrudRepository<QlnvDmDonvi, Long> {
	QlnvDmDonvi findByMaDvi(String maDvi);
}
