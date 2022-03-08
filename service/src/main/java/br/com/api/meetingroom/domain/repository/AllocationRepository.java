package br.com.api.meetingroom.domain.repository;

import br.com.api.meetingroom.domain.entity.Allocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface AllocationRepository extends JpaRepository<Allocation, Long> {

    @Query("SELECT a FROM Allocation a WHERE" +
            "a.room.id = :roomId AND a.startAt >= :startAt AND a.endAT <= :endAt")
    List<Allocation> findAllWithFilter(@Param("roomId") Long roomId, @Param("startAt") OffsetDateTime startAt, @Param("endAt") OffsetDateTime endAt);
}
