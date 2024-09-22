package com.example.technicshoplab4.Repository;

import com.example.technicshoplab4.Models.Warehouse;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {

    @Query("SELECT i FROM Warehouse i WHERE " +
            "(:type IS NULL OR i.typee = :type) AND " +
            "(:group IS NULL OR LOWER(i.groupp) LIKE LOWER(CONCAT('%', :group, '%'))) AND " +
            "(:importDate IS NULL OR i.importDate = :importDate) AND " +
            "(:exportDate IS NULL OR i.exportDate = :exportDate)")
    List<Warehouse> findByParams(
            @Param("type") String type,
            @Param("group") String group,
            @Param("importDate") LocalDate importDate,
            @Param("exportDate") LocalDate exportDate,
            Sort sort);


    @Query("SELECT DATE(f.importDate), COUNT(f) FROM Warehouse f GROUP BY DATE(f.importDate)")
    List<Object[]> findItemIssueStats();
}
