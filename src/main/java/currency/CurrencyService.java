package currency;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class CurrencyService {

    public static ArrayList<Currency> getList() {

        try {
            URL url = new URL("https://cbu.uz/uz/arkhiv-kursov-valyut/json/");
            URLConnection urlConnection = url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            ObjectMapper objectMapper = new ObjectMapper();
            ArrayList<Currency> currencies = objectMapper.readValue(inputStream, new TypeReference<>(){});



            return currencies;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @SneakyThrows
    public  static  Currency getCurrency(Integer index ){
        URL url = new URL("https://cbu.uz/uz/arkhiv-kursov-valyut/json/");
        URLConnection urlConnection = url.openConnection();
        InputStream inputStream = urlConnection.getInputStream();
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayList<Currency> currencies = objectMapper.readValue(inputStream, new TypeReference<>(){});
        return currencies.get(index -1);


    }

    public static void main(String[] args) {
        System.out.println(getCurrency((int)1));
    }

}
