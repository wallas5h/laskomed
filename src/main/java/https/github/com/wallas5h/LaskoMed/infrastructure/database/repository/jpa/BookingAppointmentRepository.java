package https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.jpa;

import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.BookingAppointmentEntity;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.DoctorAvailabilityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingAppointmentRepository extends JpaRepository<BookingAppointmentEntity, Long> {

  @Query("""
        SELECT ba FROM BookingAppointmentEntity ba
        WHERE ba.doctor.doctorId = :id
        AND ba.bookingStatus <> 'cancelled'
        ORDER BY CAST(ba.bookingDate AS date)
        """)
  List<BookingAppointmentEntity> findByDoctorId(final @Param("id") Long doctorId);
}
