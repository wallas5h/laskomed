package https.github.com.wallas5h.LaskoMed.business.services;

import https.github.com.wallas5h.LaskoMed.api.dto.PatientsDTO;
import https.github.com.wallas5h.LaskoMed.api.mapper.PatientMapper;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.jpa.AddressRepository;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.jpa.PatientRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class PatientService {
  private PatientRepository patientRepository;
  private AddressRepository addressRepository;
  private PatientMapper patientMapper;

  public PatientsDTO getPatientsList(){
    return PatientsDTO.of(patientRepository.findAll().stream()
        .map(a-> patientMapper.mapFromEntityToDto(a))
        .toList()
    );
  }
}
