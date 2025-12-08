package com.example.quanlytoanhanhom4.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Properties;

/**
 * Service để lấy thông tin thời tiết từ WeatherAPI.com
 */
public final class WeatherService {
    
    private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);
    private static final String WEATHER_API_URL = "https://api.weatherapi.com/v1/current.json";
    private static String cachedCity = null; // Cache vị trí để tránh gọi API nhiều lần
    private static String apiKey = null;
    
    private WeatherService() {
        // Utility class
    }
    
    /**
     * Lấy API key từ application.properties
     */
    private static String getApiKey() {
        if (apiKey != null) {
            return apiKey;
        }
        
        try (InputStream input = WeatherService.class
                .getResourceAsStream("/application.properties")) {
            if (input != null) {
                Properties props = new Properties();
                props.load(input);
                String key = props.getProperty("weather.api.key", "");
                if (key != null && !key.trim().isEmpty() && !key.equals("YOUR_API_KEY_HERE")) {
                    apiKey = key.trim();
                    logger.debug("Đã load API key từ cấu hình");
                    return apiKey;
                }
            }
        } catch (Exception e) {
            logger.debug("Không thể đọc weather API key từ application.properties", e);
        }
        
        logger.warn("Chưa cấu hình WeatherAPI.com API key!");
        return null;
    }
    
    /**
     * Lấy tên thành phố từ application.properties (nếu có) hoặc tự động phát hiện
     */
    private static String getCity() {
        // Kiểm tra xem có cấu hình tĩnh không
        try (InputStream input = WeatherService.class
                .getResourceAsStream("/application.properties")) {
            if (input != null) {
                Properties props = new Properties();
                props.load(input);
                String city = props.getProperty("weather.city", "");
                if (city != null && !city.trim().isEmpty() && !city.equals("auto")) {
                    // Nếu có cấu hình và không phải "auto", dùng cấu hình
                    logger.debug("Sử dụng thành phố từ cấu hình: {}", city);
                    return city.trim();
                }
            }
        } catch (Exception e) {
            logger.debug("Không thể đọc weather city từ application.properties", e);
        }
        
        // Mặc định dùng Hanoi
        logger.debug("Sử dụng thành phố mặc định: Hanoi");
        return "Hanoi";
    }
    
    /**
     * Lấy tên thành phố chính xác từ IP geolocation API
     * Chỉ thử 1 API nhanh nhất để tải nhanh hơn
     * @return Tên thành phố hoặc null nếu không lấy được
     */
    private static String getCityFromGeolocation() {
        if (cachedCity != null) {
            return cachedCity;
        }
        
        // Chỉ thử 1 API nhanh nhất: ipapi.co (nhanh và chính xác)
        String city = tryGetCityFromAPI("https://ipapi.co/json/", "city");
        if (city != null) {
            cachedCity = city;
            logger.info("Đã lấy tên thành phố từ ipapi.co: {}", cachedCity);
            return cachedCity;
        }
        
        logger.debug("Không thể lấy tên thành phố từ geolocation API");
        return null;
    }
    
    /**
     * Thử lấy tên thành phố từ một API cụ thể
     */
    private static String tryGetCityFromAPI(String apiUrl, String cityFieldName) {
        try {
            URL url = new URI(apiUrl).toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(1500); // Giảm timeout để tải nhanh hơn
            connection.setReadTimeout(2000);
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36");
            
            int responseCode = connection.getResponseCode();
            
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (InputStream responseStream = connection.getInputStream()) {
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode rootNode = mapper.readTree(responseStream);
                    
                    // Lấy từ field chính
                    String city = normalizeCityName(rootNode.path(cityFieldName).asText(null));
                    
                    // Nếu không có, thử các field khác
                    if (city == null) {
                        city = normalizeCityName(rootNode.path("city_name").asText(null));
                    }
                    if (city == null) {
                        city = normalizeCityName(rootNode.path("region").asText(null));
                    }
                    if (city == null) {
                        city = normalizeCityName(rootNode.path("regionName").asText(null));
                    }
                    if (city == null) {
                        city = normalizeCityName(rootNode.path("district").asText(null));
                    }
                    
                    // Đối với ip-api.com, có thể cần parse từ query
                    if (city == null && rootNode.has("query")) {
                        // Bỏ qua, chỉ lấy city
                    }
                    
                    return city;
                }
            }
        } catch (Exception e) {
            logger.debug("Không thể lấy từ {}: {}", apiUrl, e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Lấy thông tin thời tiết hiện tại từ WeatherAPI.com
     * 
     * @return WeatherInfo object chứa thông tin thời tiết
     */
    public static WeatherInfo getCurrentWeather() {
        String apiKey = getApiKey();
        if (apiKey == null) {
            logger.warn("Chưa cấu hình API key, trả về thông tin mặc định");
            return createDefaultWeatherInfo();
        }
        
        String location = getCity();
        try {
            // WeatherAPI.com format: ?key=YOUR_KEY&q=Hanoi&aqi=no
            String urlString = String.format("%s?key=%s&q=%s&aqi=no", 
                WEATHER_API_URL, apiKey, location);
            
            logger.debug("Đang lấy thời tiết từ: {}", urlString);
            
            URL url = new URI(urlString).toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(3000);
            connection.setReadTimeout(5000);
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            
            int responseCode = connection.getResponseCode();
            
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (InputStream responseStream = connection.getInputStream()) {
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode rootNode = mapper.readTree(responseStream);
                    
                    // Parse thông tin thời tiết từ WeatherAPI.com format
                    JsonNode current = rootNode.path("current");
                    JsonNode locationNode = rootNode.path("location");
                    
                    if (!current.isMissingNode()) {
                        double temperature = current.path("temp_c").asDouble(28.0);
                        double feelsLike = current.path("feelslike_c").asDouble(30.0);
                        int humidity = current.path("humidity").asInt(75);
                        
                        // Lấy icon URL từ condition.icon
                        String iconUrl = current.path("condition").path("icon").asText("");
                        if (iconUrl != null && !iconUrl.isEmpty() && !iconUrl.startsWith("http")) {
                            iconUrl = "https:" + iconUrl;
                        }
                        
                        // Lấy mô tả thời tiết
                        String description = current.path("condition").path("text").asText("Sunny");
                        if (description != null && !description.trim().isEmpty()) {
                            description = translateToVietnamese(description.trim());
                        } else {
                            description = "Nắng";
                        }
                        
                        // Lấy tên thành phố
                        String cityName = locationNode.path("name").asText(location);
                        if (cityName == null || cityName.isEmpty()) {
                            cityName = location;
                        }
                        
                        logger.debug("Đã lấy thời tiết thành công cho: {}", cityName);
                        
                        return new WeatherInfo(
                            cityName,
                            temperature,
                            feelsLike,
                            humidity,
                            description,
                            iconUrl
                        );
                    }
                }
                logger.warn("Không thể parse dữ liệu thời tiết từ API");
                return createDefaultWeatherInfo();
            } else {
                logger.warn("Lỗi khi lấy thời tiết. Response code: {}", responseCode);
                return createDefaultWeatherInfo();
            }
        } catch (Exception e) {
            logger.error("Lỗi khi lấy thời tiết từ WeatherAPI.com", e);
            return createDefaultWeatherInfo();
        }
    }
    
    /**
     * Tạo thông tin thời tiết mặc định khi không thể lấy từ API
     */
    private static WeatherInfo createDefaultWeatherInfo() {
        String city = null;
        
        // Thử lấy từ geolocation API
        try {
            city = getCityFromGeolocation();
        } catch (Exception e) {
            logger.debug("Không thể lấy thành phố từ geolocation khi tạo default", e);
        }
        
        // Nếu không có, thử lấy từ cấu hình
        if (city == null || city.isEmpty()) {
            try {
                String location = getCity();
                if (location != null && !location.isEmpty() && !location.equals("auto:ip")) {
                    city = location.replace("+", " ");
                }
            } catch (Exception e) {
                // Ignore
            }
        }
        
        // Nếu vẫn không có, dùng mặc định
        if (city == null || city.isEmpty()) {
            city = "Vị trí hiện tại";
        }
        
        return new WeatherInfo(
            city,
            28.0,
            30.0,
            75,
            "Nắng",
            "https://cdn.weatherapi.com/weather/64x64/day/113.png"
        );
    }
    
    /**
     * Reset cache vị trí (dùng khi muốn lấy lại vị trí mới)
     */
    public static void resetLocationCache() {
        cachedCity = null;
        logger.debug("Đã reset cache vị trí");
    }
    
    /**
     * Dịch mô tả thời tiết từ tiếng Anh sang tiếng Việt
     * Public method để có thể gọi từ controller
     */
    public static String translateToVietnamese(String englishDesc) {
        if (englishDesc == null || englishDesc.isEmpty()) {
            return "Nắng";
        }
        
        // Nếu đã là tiếng Việt (chứa ký tự đặc biệt của tiếng Việt), trả về luôn
        String trimmed = englishDesc.trim();
        if (containsVietnamese(trimmed)) {
            return trimmed;
        }
        
        String lowerDesc = trimmed.toLowerCase();
        
        // Mapping các mô tả thời tiết phổ biến - kiểm tra từ dài đến ngắn
        // Thời tiết cực đoan
        if (lowerDesc.contains("heavy rain") || lowerDesc.contains("heavy rainfall") || 
            lowerDesc.contains("torrential")) {
            return "Mưa to";
        } else if (lowerDesc.contains("light rain") || lowerDesc.contains("light rainfall") ||
                   lowerDesc.contains("light drizzle")) {
            return "Mưa nhỏ";
        } else if (lowerDesc.contains("moderate rain")) {
            return "Mưa vừa";
        } else if (lowerDesc.contains("thunderstorm") || lowerDesc.contains("thunder")) {
            return "Dông";
        } else if (lowerDesc.contains("tropical storm") || lowerDesc.contains("typhoon")) {
            return "Bão";
        } else if (lowerDesc.contains("hurricane")) {
            return "Bão lớn";
        }
        
        // Mây và trời trong
        if (lowerDesc.contains("partly cloudy")) {
            return "Ít mây";
        } else if (lowerDesc.contains("mostly cloudy") || lowerDesc.contains("generally cloudy")) {
            return "Nhiều mây";
        } else if (lowerDesc.contains("overcast")) {
            return "U ám";
        } else if (lowerDesc.contains("clear sky") || lowerDesc.contains("clear")) {
            return "Trời quang";
        } else if (lowerDesc.contains("sunny")) {
            return "Nắng";
        } else if (lowerDesc.contains("cloudy")) {
            return "Nhiều mây";
        } else if (lowerDesc.contains("partly sunny") || lowerDesc.contains("partly clear")) {
            return "Nắng ít mây";
        }
        
        // Mưa
        if (lowerDesc.contains("rain") || lowerDesc.contains("shower") || 
            lowerDesc.contains("precipitation")) {
            return "Mưa";
        } else if (lowerDesc.contains("drizzle")) {
            return "Mưa phùn";
        }
        
        // Tuyết và băng
        if (lowerDesc.contains("sleet")) {
            return "Mưa tuyết";
        } else if (lowerDesc.contains("snow") || lowerDesc.contains("blizzard")) {
            return "Tuyết";
        } else if (lowerDesc.contains("freezing")) {
            return "Đóng băng";
        }
        
        // Sương và mù
        if (lowerDesc.contains("fog") || lowerDesc.contains("mist") || lowerDesc.contains("ground fog")) {
            return "Sương mù";
        } else if (lowerDesc.contains("haze") || lowerDesc.contains("smoke")) {
            return "Mù mịt";
        }
        
        // Gió và bụi
        if (lowerDesc.contains("windy") || lowerDesc.contains("strong wind")) {
            return "Có gió";
        } else if (lowerDesc.contains("dust") || lowerDesc.contains("sandstorm")) {
            return "Bụi";
        }
        
        // Nếu không match, kiểm tra một số từ khóa chung
        if (lowerDesc.contains("fair") || lowerDesc.contains("fine")) {
            return "Trời đẹp";
        } else if (lowerDesc.contains("unstable") || lowerDesc.contains("variable")) {
            return "Thay đổi";
        }
        
        // Nếu không match, trả về "Nắng" mặc định
        logger.debug("Không tìm thấy bản dịch cho: {}, dùng mặc định", englishDesc);
        return "Nắng";
    }
    
    /**
     * Kiểm tra xem chuỗi có chứa ký tự tiếng Việt không
     */
    private static boolean containsVietnamese(String text) {
        if (text == null || text.isEmpty()) {
            return false;
        }
        // Kiểm tra các ký tự đặc trưng của tiếng Việt
        return text.matches(".*[àáạảãâầấậẩẫăằắặẳẵèéẹẻẽêềếệểễìíịỉĩòóọỏõôồốộổỗơờớợởỡùúụủũưừứựửữỳýỵỷỹđĐ].*");
    }
    
    /**
     * Chuẩn hóa tên thành phố - loại bỏ ký tự không hợp lệ, kiểm tra xem có phải tọa độ không
     */
    private static String normalizeCityName(String cityName) {
        if (cityName == null || cityName.trim().isEmpty()) {
            return null;
        }
        
        String normalized = cityName.trim();
        
        // Loại bỏ các ký tự đặc biệt không mong muốn ở đầu/cuối
        normalized = normalized.replaceAll("^[\\s\\-_.,;:]+|[\\s\\-_.,;:]+$", "");
        
        // Loại bỏ các ký tự đặc biệt không hợp lệ
        normalized = normalized.replaceAll("[<>\"'`]", "");
        
        // Kiểm tra xem có phải là tọa độ không (chứa số và dấu phẩy/dấu chấm)
        if (normalized.matches(".*\\d+[.,]\\s*\\d+.*") || normalized.matches("^-?\\d+\\.?\\d*[NS]?\\s*-?\\d+\\.?\\d*[EW]?$")) {
            logger.debug("Tên thành phố có vẻ là tọa độ: {}", normalized);
            return null;
        }
        
        // Kiểm tra xem có phải là IP address không
        if (normalized.matches("^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}$")) {
            logger.debug("Tên thành phố có vẻ là IP address: {}", normalized);
            return null;
        }
        
        // Kiểm tra xem có quá ngắn không (ít hơn 2 ký tự)
        if (normalized.length() < 2) {
            return null;
        }
        
        // Loại bỏ các từ không mong muốn
        String lower = normalized.toLowerCase();
        if (lower.equals("auto") || 
            lower.equals("auto:ip") ||
            lower.contains("vị trí hiện tại") ||
            lower.contains("current location") ||
            lower.contains("location") && normalized.length() < 15) {
            return null;
        }
        
        // Viết hoa chữ cái đầu của mỗi từ (title case)
        if (normalized.length() > 0 && !containsVietnamese(normalized)) {
            String[] words = normalized.split("\\s+");
            StringBuilder titleCase = new StringBuilder();
            for (String word : words) {
                if (titleCase.length() > 0) {
                    titleCase.append(" ");
                }
                if (word.length() > 0) {
                    titleCase.append(word.substring(0, 1).toUpperCase());
                    if (word.length() > 1) {
                        titleCase.append(word.substring(1).toLowerCase());
                    }
                }
            }
            normalized = titleCase.toString();
        }
        
        return normalized;
    }
    
    /**
     * Class để lưu thông tin thời tiết
     */
    public static class WeatherInfo {
        private final String city;
        private final double temperature;
        private final double feelsLike;
        private final int humidity;
        private final String description;
        private final String icon;
        
        public WeatherInfo(String city, double temperature, double feelsLike, 
                         int humidity, String description, String icon) {
            this.city = city;
            this.temperature = temperature;
            this.feelsLike = feelsLike;
            this.humidity = humidity;
            this.description = description;
            this.icon = icon;
        }
        
        public String getCity() {
            return city;
        }
        
        public double getTemperature() {
            return temperature;
        }
        
        public double getFeelsLike() {
            return feelsLike;
        }
        
        public int getHumidity() {
            return humidity;
        }
        
        public String getDescription() {
            return description;
        }
        
        public String getIcon() {
            return icon;
        }
        
        /**
         * Lấy URL icon thời tiết từ WeatherAPI.com
         * Icon đã có sẵn URL đầy đủ từ API
         */
        public String getIconUrl() {
            if (icon == null || icon.isEmpty()) {
                return "https://cdn.weatherapi.com/weather/64x64/day/113.png"; // Default sunny icon
            }
            
            // Icon từ API đã có format: //cdn.weatherapi.com/weather/64x64/day/116.png
            // Cần thêm "https:" nếu chưa có
            if (icon.startsWith("//")) {
                return "https:" + icon;
            } else if (icon.startsWith("http")) {
                return icon;
            } else {
                return "https://cdn.weatherapi.com/weather/64x64/day/113.png";
            }
        }
        
        /**
         * Format nhiệt độ với đơn vị
         */
        public String getFormattedTemperature() {
            return String.format("%.0f°C", temperature);
        }
        
        /**
         * Format mô tả với chữ cái đầu viết hoa
         */
        public String getFormattedDescription() {
            if (description == null || description.isEmpty()) {
                return "Không có dữ liệu";
            }
            return description.substring(0, 1).toUpperCase() + 
                   description.substring(1);
        }
    }
}

