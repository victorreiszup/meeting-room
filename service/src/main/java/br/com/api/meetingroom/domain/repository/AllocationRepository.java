package br.com.api.meetingroom.domain.repository;

import br.com.api.meetingroom.domain.entity.Allocation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface AllocationRepository extends JpaRepository<Allocation, Long> {

    @Query("SELECT a " +
            "FROM Allocation a " +
            "WHERE a.room.id = :roomId AND a.startAt >= :startAt AND a.endAt <= :endAt")
    List<Allocation> findAllWithFilter(
            @Param("roomId") Long roomId,
            @Param("startAt") OffsetDateTime startAt,
            @Param("endAt") OffsetDateTime endAt
    );

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Allocation a "
            + "SET a.subject = :subject,"
            + " a.startAt = :startAt,"
            + " a.endAt = :endAt,"
            + " a.updatedAt = :updatedAt "
            + "WHERE a.id = :allocationId"
    )
    void updateAllocation(
            @Param("allocationId") Long allocationId,
            @Param("subject") String subject,
            @Param("startAt") OffsetDateTime startAt,
            @Param("endAt") OffsetDateTime endAt,
            @Param("updatedAt") OffsetDateTime updateAt
    );

    @Query(
            "SELECT a "
                    + "FROM Allocation a"
                    + " WHERE"
                    + " (:employeeEmail IS NULL OR a.employee.email = :employeeEmail) AND"
                    + " (:roomId IS NULL OR a.room.id = :roomId) AND"
                    + " (:startAt IS NULL OR a.startAt >= :startAt) AND"
                    + " (:endAt IS NULL OR a.endAt <= :endAt)"
    )
    List<Allocation> findAllWithFilter(
            @Param("employeeEmail") String employeeEmail,
            @Param("roomId") Long roomId,
            @Param("startAt") OffsetDateTime startAt,
            @Param("endAt") OffsetDateTime endAt,
            Pageable pageable
    );
}
