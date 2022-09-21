package com.example.meetishah.carmina;


        import android.content.Intent;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.util.Log;
        import android.util.Patterns;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.Toast;

        import com.basgeekball.awesomevalidation.AwesomeValidation;
        import com.basgeekball.awesomevalidation.ValidationStyle;

        import org.json.JSONObject;

        import java.io.BufferedReader;
        import java.io.BufferedWriter;
        import java.io.InputStreamReader;
        import java.io.OutputStream;
        import java.io.OutputStreamWriter;
        import java.net.HttpURLConnection;
        import java.net.URL;
        import java.net.URLEncoder;
        import java.util.Iterator;

        import javax.net.ssl.HttpsURLConnection;


public class Signup extends AppCompatActivity implements View.OnClickListener {
    //defining AwesomeValidation object
    private AwesomeValidation awesomeValidation;
    Button Sign;
    EditText uid,email,pwd,conp;
    String u,e,p,cp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        Sign=(Button)findViewById(R.id.button3);
        uid=(EditText)findViewById(R.id.uid);
        pwd=(EditText)findViewById(R.id.pwd);
        conp=(EditText)findViewById(R.id.conp);
        email=(EditText)findViewById(R.id.email);

        Sign.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {



        u=uid.getText().toString().trim();
        e=email.getText().toString().trim();
        p=pwd.getText().toString().trim();
        cp=conp.getText().toString().trim();
        String regexPassword = ".{8,}";
        awesomeValidation.addValidation(this, R.id.uid, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.nameerror);
        awesomeValidation.addValidation(this, R.id.email, Patterns.EMAIL_ADDRESS, R.string.emailerror);

        awesomeValidation.addValidation(this, R.id.pwd, regexPassword, R.string.invalid_password);
        awesomeValidation.addValidation(this, R.id.conp, regexPassword, R.string.invalid_confirm_password);



        if (awesomeValidation.validate()) {
            new SendPostequest().execute();
        }else{
            Toast.makeText(this, "invalid detail", Toast.LENGTH_SHORT).show();
        }

    }
    public class SendPostequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute(){
            // Toast.makeText(Signup_Activity.this, "on pre execute", Toast.LENGTH_SHORT).show();

        }

        protected String doInBackground(String... arg0) {
            //    Toast.makeText(Signup_Activity.this, "do in background", Toast.LENGTH_SHORT).show();
            try {

                URL url = new URL("http://172.20.10.3/test/signup1.php"); // here is your URL path

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("userid",u);
                postDataParams.put("email",e);
                postDataParams.put("password",p);
                postDataParams.put("confirmpassword",cp);
                Log.e("params",postDataParams.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode=conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in=new BufferedReader(new
                            InputStreamReader(
                            conn.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line="";

                    while((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                }
                else {
                    return new String("false : "+responseCode);
                }
            }
            catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(String result) {
            if(result.contains("Mail has been sent")){
            Toast.makeText(Signup.this,result, Toast.LENGTH_LONG).show();
            Intent i=new Intent(getApplicationContext(),Login.class);
            startActivity(i);
            } else{
                Toast.makeText(Signup.this,result, Toast.LENGTH_LONG).show();
            }
        }
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }
}