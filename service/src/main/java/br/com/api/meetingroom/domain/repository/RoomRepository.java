package br.com.api.meetingroom.domain.repository;

import br.com.api.meetingroom.domain.entity.Room;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    Optional<Room> findByIdAndActive(Long idRoom, boolean active);

    Optional<Room> findByName(String name);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Room r " +
            "SET r.active = false " +
            "WHERE r.id = :idRoom")
    void deactivate(
            @Param("idRoom") Long idRoom
    );

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Room r " +
            "SET r.active = true " +
            "WHERE r.id = :idRoom")
    void activate(
            @Param("idRoom") Long idRoom
    );


    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Room r " +
            "SET r.name = :name, r.seats = :seats " +
            "WHERE r.id = :idRoom")
    void updateRoom(
            @Param("idRoom") Long idRoom,
            @Param("name") String name,
            @Param("seats") Integer seats
    );

    @Query(
            "SELECT r " +
                    "FROM Room r " +
                    "WHERE (:name IS NULL OR r.name = :name) AND " +
                    "(:active IS NULL OR r.active = :active)"
    )
    List<Room> findAllWinthFilter(
            @Param("name") String name,
            @Param("active") Boolean active,
            Pageable pageable
    );


}
