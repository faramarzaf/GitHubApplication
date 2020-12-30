package app.android.githubservice.util;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import app.android.githubservice.R;

public class LanguageColorGenerator {

    public static String getColors(Context context, String key) {
        InputStream is = context.getResources().openRawResource(R.raw.colors);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];

        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String jsonstring = writer.toString();
        Map<String, String> map = new Gson().fromJson(jsonstring, new TypeToken<HashMap<String, String>>() {
        }.getType());


        return map.get(key);
    }


}
