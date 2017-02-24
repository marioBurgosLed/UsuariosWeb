package sigequip.usuariosweb;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button Consultar;
    Button Insertar;
    Button Actualizar;
    Button Borrar;
    EditText eID;
    EditText eNombre;
    EditText ePass;
    EditText Resultado;

    String IP="http://sigequip.esy.es";
    String GET= IP + "/obtenerUsuarios.php";
    String UPDATE= IP +"/actualizarUsuario.php";
    String DELETE= IP +"borrarUsuario.php";
    String INSERT= IP+"insertarUsuario.php";

    ObtenerWebService hiloconexion;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar =(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Consultar=(Button)findViewById(R.id.Consultar);
        Insertar=(Button)findViewById(R.id.Insertar);
        Actualizar=(Button)findViewById(R.id.Actualizar);
        Borrar=(Button)findViewById(R.id.Borrar);
        eID=(EditText)findViewById(R.id.eID);
        eNombre=(EditText)findViewById(R.id.eNombre);
        ePass=(EditText)findViewById(R.id.ePass);

        //Listener de los botones

        Consultar.setOnClickListener(this);
        Insertar.setOnClickListener(this);
        Actualizar.setOnClickListener(this);
        Borrar.setOnClickListener(this);

        /*FloatingActionButton fab=(FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
            Snackbar.make(view,"replace with your own action",Snackbar.LENGTH_LONG)
                    .setAction("Action",null).show();
        */



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.Consultar:
                hiloconexion=new ObtenerWebService();
                hiloconexion.execute(GET,"1");
                break;

            case R.id.Insertar:
                hiloconexion=new ObtenerWebService();
                hiloconexion.execute(INSERT,"2");
                break;

            case R.id.Actualizar:
                hiloconexion=new ObtenerWebService();
                hiloconexion.execute(UPDATE,"3");
                break;

            case R.id.Borrar:
                hiloconexion=new ObtenerWebService();
                hiloconexion.execute(DELETE,"4");
                break;

            default:
                break;

        }

    }

    public class ObtenerWebService extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... params) {
            String cadena=params[0];
            String devuelve="";
            URL url=null;

            if(params[1]=="1") {
                try {
                    url=new URL(cadena);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestProperty("User-Agent","Mozilla/5.0" +
                    "(Linux;Android 1.5; es-ES) Ejemplo HTTP");

                    int respuesta =connection.getResponseCode();
                    StringBuilder result= new StringBuilder();

                    if(respuesta==HttpURLConnection.HTTP_OK) {
                        InputStream in = new BufferedInputStream(connection.getInputStream());
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                        String line;
                        while ((line = reader.readLine()) != null) {
                            result.append(line);
                        }

                        JSONObject respuestaJSON = new JSONObject(result.toString());
                        String resultJSON = respuestaJSON.getString("estado");

                        // String direccion ="SIN DATOS";
                        if (resultJSON == "1") {
                            JSONArray UsuariosJSON = respuestaJSON.getJSONArray("Usuarios");
                            for (int i = 0; i < UsuariosJSON.length(); i++) {
                                devuelve = devuelve + UsuariosJSON.getJSONObject(i).getString("idUsuario") + " " +
                                        UsuariosJSON.getJSONObject(i).getString("usuNombre") + " " +
                                        UsuariosJSON.getJSONObject(i).getString("usuPass") + "\n";
                            }
                        } else if (resultJSON == "2") {
                            devuelve = "No hay Usuarios";
                        }
                    }

            }
            catch (MalformedURLException e){
            e.printStackTrace();}
                catch (IOException e){
                    e.printStackTrace();}
                catch (JSONException e){
                    e.printStackTrace();
                }
                }
            return devuelve;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            Resultado.setText(s);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }


        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }
}
