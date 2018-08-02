package com.ositelgroup.xls2json.model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ExcelFileRepository extends JpaRepository<ExcelFile, Long> {

    ExcelFile findByFileName(String fileName);
}
