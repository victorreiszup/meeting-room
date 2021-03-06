package br.com.api.meetingroom.domain.repository;

import br.com.api.meetingroom.domain.entity.Allocation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AllocationRepository extends JpaRepository<Allocation, Long> {

    @Query("SELECT a " +
            "FROM Allocation a " +
            "WHERE a.room.id = :roomId AND a.startAt >= :startAt AND a.endAt <= :endAt")
    List<Allocation> findAllWithFilter(
            @Param("roomId") Long roomId,
            @Param("startAt") LocalDateTime startAt,
            @Param("endAt") LocalDateTime endAt
    );

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Allocation a "
            + "SET a.startAt = :startAt,"
            + " a.endAt = :endAt,"
            + " a.updatedAt = :updatedAt "
            + "WHERE a.id = :allocationId"
    )
    void updateAllocation(
            @Param("allocationId") Long allocationId,
            @Param("startAt") LocalDateTime startAt,
            @Param("endAt") LocalDateTime endAt,
            @Param("updatedAt") LocalDateTime updateAt
    );

    @Query(
            "SELECT a FROM Allocation a WHERE " +
                    "(:employeeEmail IS NULL OR a.employee.email = :employeeEmail) AND " +
                    "(:roomId IS NULL OR a.room.id = :roomId) AND " +
                    "(cast(:startAt as timestamp) IS NULL OR a.startAt >= :startAt) AND " +
                    "(cast(:endAt as timestamp) IS NULL OR a.endAt <= :endAt)"
    )
    List<Allocation> findAllWithFilter(
            @Param("employeeEmail") String employeeEmail,
            @Param("roomId") Long roomId,
            @Param("startAt") LocalDateTime startAt,
            @Param("endAt") LocalDateTime endAt,
            Pageable pageable);

    boolean existsById(Long id);
}
