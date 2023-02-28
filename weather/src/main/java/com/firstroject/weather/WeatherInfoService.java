package com.firstroject.weather;

import java.time.LocalDate;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherInfoService {

	@Autowired
	private WeatherInfoRepository weatherInfoRepository;
    @Value("${openweathermap.api.key}")
	private String apiKey;
	
	@Autowired
	private RestTemplate restTemplate;
	
	public WeatherInfo getWeatherInfo(String pincode, LocalDate date) {
		// check if weather information already exists in the database
		WeatherInfo weatherInfo = weatherInfoRepository.findByPincodeAndDate(pincode, date).orElse(null);
		if (weatherInfo != null) {
			return weatherInfo;
		}
		
		// if not, fetch latitude and longitude from geocoding API
		// String geocodingUrl = String.format("http://api.openweathermap.org/geo/1.0/zip?zip=%s,IN&appid=%s", pincode, geocodingApiKey);
		// ResponseEntity<String> geocodingResponse = restTemplate.getForEntity(geocodingUrl, String.class);
		// JSONObject geocodingJson = new JSONObject(geocodingResponse.getBody());
		
		// if (!geocodingJson.getString("status").equals("OK")) {
		// 	throw new RuntimeException("Failed to fetch geocoding data for pincode: " + pincode);
		// }
		
		// JSONObject location = geocodingJson.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location");
		// Double latitude = location.getDouble("lat");
		// Double longitude = location.getDouble("lng");
		// if not, fetch latitude and longitude from OpenWeather Geocoding API
		String geocodingUrl = String.format("https://api.openweathermap.org/geo/1.0/zip?zip=%s,IN&appid=%s", pincode, apiKey);
		ResponseEntity<String> geocodingResponse = restTemplate.getForEntity(geocodingUrl, String.class);
		JSONObject geocodingJson = new JSONObject(geocodingResponse.getBody());

		if (geocodingJson.isNull("lat") || geocodingJson.isNull("lon")) {
			throw new RuntimeException("Failed to fetch geocoding data for pincode: " + pincode);
		}
		Double latitude = geocodingJson.getDouble("lat");
		Double longitude = geocodingJson.getDouble("lon");

		
		// fetch weather information from OpenWeatherMap API
		String weatherUrl = String.format("https://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&appid=%s", latitude, longitude, apiKey);
		ResponseEntity<String> weatherResponse = restTemplate.getForEntity(weatherUrl, String.class);
		JSONObject weatherJson = new JSONObject(weatherResponse.getBody());
		
		if (weatherJson.getInt("cod") != 200) {
			throw new RuntimeException("Failed to fetch weather data for pincode: " + pincode);
		}
		
		Double temperature = weatherJson.getJSONObject("main").getDouble("temp");
		Double humidity = weatherJson.getJSONObject("main").getDouble("humidity");
		
		// save weather information to the database
		weatherInfo = new WeatherInfo();
		weatherInfo.setPincode(pincode);
		weatherInfo.setDate(date);
		weatherInfo.setLatitude(latitude);
		weatherInfo.setLongitude(longitude);
		weatherInfo.setTemperature(temperature);
		weatherInfo.setHumidity(humidity);
		weatherInfo = weatherInfoRepository.save(weatherInfo);
		
		return weatherInfo;
	}
	
}
