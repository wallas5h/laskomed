package https.github.com.wallas5h.LaskoMed.api.utils;

public class EnumsContainer {

  public enum MedicalPackage {
    PREMIUM,
    STANDARD
  }

  public enum AppointmentStatus {
    COMPLETED, MISSED, INTERRUPTED
  }
  public enum AppointmentType {
    TELECONSULTATION, FACILITY, ONLINE
  }
}
