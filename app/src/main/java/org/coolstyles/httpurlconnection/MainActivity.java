package org.coolstyles.httpurlconnection;


import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private Button buttonSendRequest;
    private TextView textResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Khởi tạo các view
        buttonSendRequest = findViewById(R.id.button_send_request);
        textResponse = findViewById(R.id.text_response);

        // Đặt sự kiện click cho nút gửi yêu cầu
        buttonSendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Gửi yêu cầu đến REST API
                new ApiTask().execute("https://jsonplaceholder.typicode.com/posts");
            }
        });
    }

    private class ApiTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                // Đọc dữ liệu từ InputStream
                InputStream inputStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                response = stringBuilder.toString();

                // Đóng các luồng
                bufferedReader.close();
                inputStream.close();
                connection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // Xử lý phản hồi JSON và hiển thị kết quả trên TextView
            try {
                JSONArray jsonArray = new JSONArray(result);
                StringBuilder responseBuilder = new StringBuilder();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    int id = jsonObject.getInt("id");
                    String title = jsonObject.getString("title");
                    String body = jsonObject.getString("body");
                    responseBuilder.append("ID: ").append(id).append(", Title: ").append(title).append(", Body: ").append(body).append("\n");
                }
                textResponse.setText(responseBuilder.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
