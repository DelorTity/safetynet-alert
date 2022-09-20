package com.softwify.safetynetAlert.dao;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softwify.safetynetAlert.model.FireStation;
import com.softwify.safetynetAlert.model.MedicalRecord;
import com.softwify.safetynetAlert.model.Person;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

@Component
public class DataStoreManager implements CommandLineRunner {
	private DataStore dataStore;

	@Override
	public void run(String... args) throws Exception {
		ClassPathResource resource = new ClassPathResource("data.json");
		InputStream inputStream = resource.getInputStream();

		dataStore = new ObjectMapper().readValue(inputStream, DataStore.class);
	}

	public List<Person> getPersons() {
		return dataStore.getPersons();
	}

	public List<FireStation> getFireStation() {
		return dataStore.getFireStations();
	}

	public List<MedicalRecord> getMedicalRecords() {
		return dataStore.getMedicalRecords();
	}


	private static class DataStore {
		private List<Person> persons;

		@JsonProperty("firestations")
		private List<FireStation> fireStations;

		@JsonProperty("medicalrecords")
		private List<MedicalRecord> medicalRecords;

		public List<Person> getPersons() {
			return persons;
		}

		public void setPersons(List<Person> persons) {
			this.persons = persons;
		}

		public List<FireStation> getFireStations() {
			return fireStations;
		}

		public void setFireStations(List<FireStation> fireStations) {
			this.fireStations = fireStations;
		}

		public List<MedicalRecord> getMedicalRecords() {
			return medicalRecords;
		}

		public void setMedicalRecords(List<MedicalRecord> medicalRecords) {
			this.medicalRecords = medicalRecords;
		}
	}
}