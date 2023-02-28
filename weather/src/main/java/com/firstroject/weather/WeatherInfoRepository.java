package com.firstroject.weather;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface WeatherInfoRepository extends JpaRepository<WeatherInfo, Long> {

	Optional<WeatherInfo> findByPincodeAndDate(String pincode, LocalDate date);
	
}