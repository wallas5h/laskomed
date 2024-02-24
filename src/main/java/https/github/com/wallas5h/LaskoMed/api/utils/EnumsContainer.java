package https.github.com.wallas5h.LaskoMed.api.utils;

public class EnumsContainer {

  public enum MedicalPackage {
    PREMIUM,
    STANDARD
  }

  public enum BookingStatus {
    PENDING, CONFIRMED, CANCELLED, COMPLETED, MISSED, INTERRUPTED
  }
  public enum AppointmentStatus {
    COMPLETED, MISSED, INTERRUPTED
  }
  public enum AppointmentType {
    TELECONSULTATION, FACILITY, ONLINE
  }

  public enum RoleNames{
    PATIENT, DOCTOR, ADMIN
  }
}
