package com.example.test_git;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    GridView gridView;
    SimpleAdapter adapter;
    List<Map<String,String>> data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText id = findViewById(R.id.edtid);
        EditText name = findViewById(R.id.edtname);
        EditText address = findViewById(R.id.edtaddress);
        TextView a = findViewById(R.id.result);
        Button insert = findViewById(R.id.add);
        Button update = findViewById(R.id.update);
        Button delete = findViewById(R.id.delete);
        Button get = findViewById(R.id.get);
        gridView = findViewById(R.id.gridView);
        loadData();

        insert.setOnClickListener(v -> {
            Connection connection = connectSQL();
            try {
                if (connection != null){
                    String insertSQL = "INSERT INTO UserInfo VALUES ('"+name.getText().toString()+"','"+address.getText().toString()+"')";
                    Statement statement = connection.createStatement();
                    boolean resultSet = statement.execute(insertSQL);
                    if (resultSet){
                        ResultSet rs = statement.getResultSet();
                        Log.e("RS",rs+"");
                    }
                    a.setText(""+resultSet);
                }
            }catch (Exception e){
                Log.e("Insert Error",e.getMessage());
            }
        });

        update.setOnClickListener(v -> {
            Connection connection = connectSQL();
            try {
                if (connection != null){
                    String updateSQL = "UPDATE UserInfo SET Name = '"+name.getText().toString()+"',Address= '"+address.getText().toString()+"'WHERE ID = '"+id.getText().toString()+"'";
                    Statement statement = connection.createStatement();
                    boolean resultSet = statement.execute(updateSQL);
                    if (resultSet){
                        ResultSet rs = statement.getResultSet();
                        Log.e("RS",rs+"");
                    }
                    a.setText(""+resultSet);
                }
            }catch (Exception e){
                Log.e("Update Error",e.getMessage());
            }
        });

        delete.setOnClickListener(v -> {
            Connection connection = connectSQL();
            try {
                if (connection != null){
                    String deleteSQL = "DELETE UserInfo WHERE ID = '"+id.getText().toString()+"'";
                    Statement statement = connection.createStatement();
                    boolean resultSet = statement.execute(deleteSQL);
                    if (resultSet){
                        ResultSet rs = statement.getResultSet();
                        Log.e("RS",rs+"");
                    }
                    a.setText(""+resultSet);
                }
            }catch (Exception e){
                Log.e("Delete Error",e.getMessage());
            }
        });

        get.setOnClickListener(v -> {
            Connection connection = connectSQL();
            try {
                if (connection != null){
                    String deleteSQL = "SELECT * FROM UserInfo WHERE ID = '"+id.getText().toString()+"'";
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(deleteSQL);
                    while (resultSet.next()){
                        name.setText(resultSet.getString(2));
                        address.setText(resultSet.getString(3));
                    }
                    connection.close();
                }
            }catch (Exception e){
                Log.e("Get Error",e.getMessage());
            }
        });


    }
    @SuppressLint("NewApi")
    public Connection connectSQL(){
        Connection connection = null;
        String ip = "192.168.1.11",port = "1433",username="sa",password="thangpoly123",databasename="CRUDAndroidDB";
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            String connectionUrl = "jdbc:jtds:sqlserver://" + ip + ":" + port + ";" + "databasename=" + databasename + ";user=" + username + ";" + "password=" + password + ";";
            connection = DriverManager.getConnection(connectionUrl);
        }catch (Exception e){
            Log.e("Error",e.getMessage());
        }
        return connection;
    }

    public void loadData(){
        data = new ArrayList<>();
        Connection connection = connectSQL();
        if (connection != null) {
            try{
                String getAll = "SELECT * FROM UserInfo";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(getAll);
                while (resultSet.next()){
                    Map<String,String> tab = new HashMap<>();
                    tab.put("id",resultSet.getString("ID"));
                    tab.put("name",resultSet.getString("Name"));
                    tab.put("address",resultSet.getString("Address"));
                    data.add(tab);
                }
                connection.close();
            }catch (Exception e){
                Log.e("Get Data Error",e.getMessage());
            }
            String [] from = {"id","name","address"};
            int [] to = {R.id.userId,R.id.userName,R.id.userAddress};
            adapter = new SimpleAdapter(MainActivity.this,data,R.layout.view_holder,from,to);
            gridView.setAdapter(adapter);
            refresh();
        }
    }

    private void refresh(){
        final Handler handler = new Handler();
        final Runnable runnable = () -> loadData();

        handler.postDelayed(runnable,1000);
    }

    public void startPH25350(View view) {
        startActivity(new Intent(MainActivity.this, Tungnsph25350.class));
    }
}