package com.firstroject.weather;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class WeatherInfoController {
	@Autowired
	private WeatherInfoService weatherInfoService;
	
	@GetMapping("/weather/{pincode}/{date}")
	public ResponseEntity<WeatherInfo> getWeatherInfo(@PathVariable String pincode, @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
		WeatherInfo weatherInfo =weatherInfoService.getWeatherInfo(pincode, date);
		if (weatherInfo != null) {
			return new ResponseEntity<>(weatherInfo, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
}
