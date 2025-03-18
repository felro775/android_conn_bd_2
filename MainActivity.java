package com.decodexs.conexion03;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    Spinner spinnerA;
    Spinner spinnerB;
    Spinner spinnerC;
    TextView txtLista;
    EditText passClubSelec;
    ArrayList<String> listaEquipos1;
    ArrayList<String> listaEquipos2;
    ArrayList<String> listaEquipos3;

    // VARIABLES DE LA BASE DE DATOS
    ConnectionClass connectionClass;
    Connection con;
    ResultSet rs;
    String name, str;

    String nombreClubSelec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        spinnerA = (Spinner)findViewById(R.id.spinner1);
        spinnerB = (Spinner)findViewById(R.id.spinner2);
        spinnerC = (Spinner)findViewById(R.id.spinner3);
        passClubSelec = (EditText)findViewById(R.id.editTextTextPassword1);
        txtLista = (TextView)findViewById(R.id.textView1);
        connectionClass = new ConnectionClass();
        conectar();
        consultarEquipo1();

    }

    private void consultarEquipo1() {
        listaEquipos1 = new ArrayList<String>();
        listaEquipos1.add("SELECCIONE CATEGORIA :");

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try{
                con = connectionClass.CONN();
                //String consulta = "SELECT * FROM clubes WHERE id_club = 3";
                String consulta = "SELECT distinct categoria_club FROM clubes";
                PreparedStatement stmt = con.prepareStatement(consulta);
                ResultSet rs1 = stmt.executeQuery();
                while (rs1.next()){
                    listaEquipos1.add(rs1.getString(1));
                }
                //rs1.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        ArrayAdapter<CharSequence> adaptador1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listaEquipos1);
        spinnerA.setAdapter(adaptador1);
        spinnerA.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    String texto2r = listaEquipos1.get(position).toString();
                    Toast.makeText(getApplicationContext(), texto2r, Toast.LENGTH_LONG).show();
                    consultarEquipo2(texto2r);

                    //VACIAR SPINNER C
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void consultarEquipo2(String texto2m) {
        listaEquipos2 = new ArrayList<String>();
        listaEquipos2.add("SELECCIONE GRUPO :");

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try{
                con = connectionClass.CONN();
                //String consulta = "SELECT * FROM clubes WHERE id_club = 3";
                String consulta2 = "SELECT distinct serie_club FROM clubes WHERE categoria_club='"+texto2m+"'";
                PreparedStatement stmt2 = con.prepareStatement(consulta2);
                ResultSet rs2 = stmt2.executeQuery();
                while (rs2.next()){
                    listaEquipos2.add(rs2.getString(1));
                }
                //rs1.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        ArrayAdapter<CharSequence> adaptador2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listaEquipos2);
        spinnerB.setAdapter(adaptador2);
        spinnerB.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position2, long id) {
                if (position2 != 0) {
                    String texto3r = listaEquipos2.get(position2).toString();
                    Toast.makeText(getApplicationContext(), texto3r, Toast.LENGTH_LONG).show();
                    consultarEquipo3(texto2m, texto3r);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void consultarEquipo3(String texto2m, String texto3m) {
        listaEquipos3 = new ArrayList<String>();
        listaEquipos3.add("SELECCIONE EQUIPO :");

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try{
                con = connectionClass.CONN();
                //String consulta = "SELECT * FROM clubes WHERE id_club = 3";
                String consulta3 = "SELECT nombre_club FROM clubes WHERE serie_club='"+texto3m+"' AND categoria_club='"+texto2m+"'";
                PreparedStatement stmt3 = con.prepareStatement(consulta3);
                ResultSet rs3 = stmt3.executeQuery();
                while (rs3.next()){
                    listaEquipos3.add(rs3.getString(1));
                }
                //rs1.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        ArrayAdapter<CharSequence> adaptador3 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listaEquipos3);
        spinnerC.setAdapter(adaptador3);
        spinnerC.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position3, long id) {
                if (position3 != 0) {
                    String texto4r = listaEquipos3.get(position3).toString();
                    nombreClubSelec = texto4r;
                    Toast.makeText(getApplicationContext(), texto4r, Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }





    public void conectar() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                con = connectionClass.CONN();

                if (con == null) {
                    str = "Error en la Coneccion con Servidor MySQL";
                } else {
                    str = "CONECTADO con Servidor MySQL";
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            runOnUiThread(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
            });
        });
    }



    public void btnClick(View view) {
        //Toast.makeText(getApplicationContext(), nombreClubSelec, Toast.LENGTH_LONG).show();
        //String passVerificar = passClubSelec.getText().toString();
        //Toast.makeText(getApplicationContext(), passVerificar, Toast.LENGTH_LONG).show();

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try{
                con = connectionClass.CONN();
                String passVerificar = passClubSelec.getText().toString();
                String consulta4 = "SELECT id_club FROM clubes WHERE nombre_club='"+nombreClubSelec+"' AND delegado_club='"+passVerificar+"'";
                //String consulta4 = "SELECT nombre_club FROM clubes WHERE id_club = 3";
                //String consulta = "SELECT * FROM clubes";
                PreparedStatement stmt4 = con.prepareStatement(consulta4);
                ResultSet rs4 = stmt4.executeQuery();
                //StringBuilder bStr = new StringBuilder("Lista de Equipos\n");
                while (rs4.next()){
                    //bStr.append(rs.getString("nombre_club")).append("\n");
                    //listaEquipos3.add(rs3.getString(1));
                    //Toast.makeText(getApplicationContext(), rs4.getString(1), Toast.LENGTH_LONG).show();

                    txtLista.setText(rs4.getString(1));
                }
                //name = bStr.toString();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            runOnUiThread(() ->{
                try {
                    Thread.sleep(1000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                //TextView txtLista = findViewById(R.id.textView1);
                //txtLista.setText(passVerificar);
            });
        });
    }

}
