
# Weather Data Microservice

This is a Spring Boot microservice that fetches weather information for a given **pincode** and **date** using the OpenWeather API.
It stores the data in a local database to avoid making repeated API calls for the same request.

---

## API Endpoint

```
GET /weather/{pincode}/{date}
```

### Path Parameters:

* `pincode` – The postal code (e.g., 560001)
* `date` – The date in `yyyy-MM-dd` format (e.g., 2025-08-20)

---

## How It Works

1. Gets the latitude and longitude for the given pincode using OpenWeather's Geocoding API.
2. Fetches weather data for that location and date using the OpenWeather API.
3. Saves the result in the database to avoid redundant external API calls.

---

## Technologies Used

* Java
* Spring Boot
* OpenWeather API
* JPA
* H2


---

