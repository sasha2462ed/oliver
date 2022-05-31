package com.example.siteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.siteapp.databinding.ActivityMainBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding v1;
    Context ct;
    CheckBox sesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        v1 = ActivityMainBinding.inflate(getLayoutInflater());
        View view = v1.getRoot();

        sesion=v1.checkBox;

        setContentView(view);

        ct=view.getContext();

        SharedPreferences admin=getApplicationContext().getSharedPreferences("x",MODE_PRIVATE);
        boolean login=admin.getBoolean("estado",false);
        String tip_usuario=admin.getString("tip_usuario","");

        if(login){
            iniSesion(tip_usuario);
        }


        v1.btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (v1.txp1.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Campo nombre vacio", Toast.LENGTH_SHORT).show();
                } else {
                    if (v1.txp2.getText().toString().isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Campo cedula vacio", Toast.LENGTH_SHORT).show();
                    } else {
                        validarUsuario("http://192.168.101.5/conexion_php/validar_usuario.php");
                    }
                }

            }

                //if ( validarUsuario("http://192.168.1.4/usuarios_bd/validar_usuario.php" )) {
                //} else if (v1.txp1.equals("222") && v1.txp2.equals("444")); {

                //Toast.makeText(MainActivity.this, "Ingreso Tecnico", Toast.LENGTH_SHORT).show();

                //Intent intent = new Intent(getApplicationContext(), niveltecnico.class);
                //startActivity(intent);
                //}


        });
    }

    private void validarUsuario(String URL){




        StringRequest stringRequest = new StringRequest(Request.Method.POST,URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.isEmpty()) {
                    try {
                        JSONObject objUser= new JSONObject(response);


                        Toast.makeText( MainActivity.this, "Bienvenido "+objUser.getString("nombre"), Toast.LENGTH_SHORT).show();
                        Intent activity=null;

                        SharedPreferences admin=ct.getSharedPreferences("x",ct.MODE_PRIVATE);
                        SharedPreferences.Editor data=admin.edit();

                        if(sesion.isChecked())
                        {
                            data.putBoolean("estado",true);
                        }

                        data.putString("nombre",objUser.getString("nombre"));
                        data.putString("cedula",objUser.getString("cedula"));
                        data.putString("tip_usuario",objUser.getString("tip_usuario"));
                        data.putString("id",objUser.getString("id"));
                        data.putString("ap",objUser.getString("ap"));

                        data.apply();



                       /* if(objUser.getString("tipo").equals("T")){

                            activity = new Intent(getBaseContext(), interfaz_tecnico.class);
                            *//*activity.putExtra("nomusu",objUser.getString("nombre"));
                            activity.putExtra("cedula1",objUser.getString("cedula"));*//*

                            //intento del tec
                        }else if (objUser.getString("tipo").equals("C")){
                            activity= new Intent(getBaseContext(), interfaz_usuario.class);
*//*                            activity.putExtra("cedula1",objUser.getString("cedula"));
                            activity.putExtra("nomusu",objUser.getString("nombre"));*//*


                        } else if (objUser.getString("tipo").equals("D")){
                            activity= new Intent(getBaseContext(), interfaz_dependiente.class);
                            *//*activity.putExtra("nomusu",objUser.getString("nombre"));
                            activity.putExtra("cedula1",objUser.getString("cedula"));*//*

                        }

                        startActivity(activity);*/

                        iniSesion(objUser.getString("tip_usuario"));

                    } catch (JSONException e) {
                        Log.i("Error",e.getMessage());
                    }


                }else{
                    Toast.makeText(MainActivity.this, "usuario/contrasena incorrecto", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,error.toString(), Toast.LENGTH_SHORT).show();

            }

        }){
            @Override
            protected Map<String, String> getParams () throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String, String>();
                parametros.put("cedula",v1.txp1.getText().toString());
                parametros.put("contrasena",v1.txp2.getText().toString());
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
        //return false;
    }


    void iniSesion(String type){
        Intent activity=null;

        if(type.equals("T")){
            activity = new Intent(getBaseContext(), interfaz_tecnico.class);

        }else if (type.equals("C")){
            activity= new Intent(getBaseContext(), interfaz_usuario.class);

        } else if (type.equals("D")){
            activity= new Intent(getBaseContext(), interfaz_dependiente.class);
        }

        startActivity(activity);
    }


    @Override
    protected void onStop(){
        super.onStop();

    }

}
