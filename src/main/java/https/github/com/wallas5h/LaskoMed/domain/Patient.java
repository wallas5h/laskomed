package https.github.com.wallas5h.LaskoMed.domain;

import lombok.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@With
@Value
@Builder
@EqualsAndHashCode(of = "patientId")
@ToString(of = {"patientId", "name", "surname", "pesel", "birthdate", "email", "appUser", "phone",
    "medicalPackage", "gender" })
public class Patient {

    Long patientId;
    String name;
    String surname;
    String pesel;
    String birthdate;
    String email;
    String phone;
    String medicalPackage;
    String gender;
    Address address;
    User appUser;
    Set<MedicalAppointment> appointments;
    Set<BookingAppointment> bookings;
    Set<Prescription> prescriptions;
    Set<Referral> referrals;
    Set<DiagnosedDisease> diagnosedDiseases;

    public Set<MedicalAppointment> getAppointments(){
        return Objects.isNull( appointments) ? new HashSet<>() : appointments;
    }
    public Set<BookingAppointment> getBookings(){
        return Objects.isNull( bookings) ? new HashSet<>() : bookings;
    }
    public Set<Prescription> getPrescriptions(){
        return Objects.isNull( prescriptions) ? new HashSet<>() : prescriptions;
    }
    public Set<Referral> getReferrals(){
        return Objects.isNull( referrals) ? new HashSet<>() : referrals;
    }
    public Set<DiagnosedDisease> getDiagnosedDiseases(){
        return Objects.isNull( diagnosedDiseases) ? new HashSet<>() : diagnosedDiseases;
    }

}
