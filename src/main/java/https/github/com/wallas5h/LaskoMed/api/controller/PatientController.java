package https.github.com.wallas5h.LaskoMed.api.controller;

import https.github.com.wallas5h.LaskoMed.api.dto.PatientsDTO;
import https.github.com.wallas5h.LaskoMed.api.mapper.PatientMapper;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.jpa.AddressRepository;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.jpa.PatientRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(PatientController.PATIENTS)
@AllArgsConstructor
public class PatientController {
  public static final String PATIENTS = "/patients";

  private PatientRepository patientRepository;
  private AddressRepository addressRepository;
  private PatientMapper patientMapper;

  @GetMapping
 public PatientsDTO patientsList(){
    return PatientsDTO.of(patientRepository.findAll().stream()
        .map(a-> patientMapper.mapFromEntityToDto(a))
        .toList()
    );
  }
}
