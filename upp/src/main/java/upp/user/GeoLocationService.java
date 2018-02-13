package upp.user;

import org.springframework.stereotype.Component;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;

@Component
public class GeoLocationService {
	public MockUser defineGeoLocation(MockUser obj) {
		GeoApiContext context = new GeoApiContext();
		context.setApiKey("AIzaSyDT9LFTvIEcj1GhGxjZkp8_CO2gFjkyBPY");
		GeocodingResult[] results;
		try {
			results = GeocodingApi.geocode(context, obj.getCity()).await();
			obj.setLongitude((long) results[0].geometry.location.lng);
			obj.setLatitude((long) results[0].geometry.location.lat);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}
}
