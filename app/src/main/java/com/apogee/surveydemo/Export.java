package com.apogee.surveydemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import com.apogee.surveydemo.Database.DatabaseOperation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import lib.folderpicker.FolderPicker;

import static com.apogee.surveydemo.Generic.taskGeneric.Name;
/*CreatedBy - Abhijeet
* This page related to all the saved point final export in csv/txt format*/
public class Export extends AppCompatActivity {
    EditText filname;
    TextView directorypath;
    Spinner datatypespnr;
    String IMAGE_DIRECTORY_NAME="/CSVEXport";
    private static final int FOLDERPICKER_CODE = 101;
    String datatypoe;
    DatabaseOperation dbTask = new DatabaseOperation(Export.this);
    String filename;
    int tskid;
    File dir =    new File(Environment.getExternalStorageDirectory(), IMAGE_DIRECTORY_NAME);
    Toolbar toolbar;
    String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(Export.this);
        String taskname = sharedPreferences.getString(Name, "default value");
        dbTask.open();
        tskid = dbTask.gettaskid(taskname);

        filname=findViewById(R.id.fname);
        directorypath=findViewById(R.id.dir);
        datatypespnr=findViewById(R.id.spndatatype);

        toolbar=findViewById(R.id.tool);
        toolbar.setTitle(getString(R.string.export));
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });



        ArrayList<String> typelist = new ArrayList<>();
        typelist.add(getString(R.string.csv));
        typelist.add(getString(R.string.txt));

        final ArrayAdapter<String> model_typeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, typelist);
        model_typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        datatypespnr.setAdapter(model_typeAdapter);

        datatypespnr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                datatypoe = parent.getItemAtPosition(position).toString();

            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        /*Creating Directory*/
        try{
            if(dir.mkdir()) {
                System.out.println("Directory created");
            } else {
                System.out.println("Directory is not created");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
         path = dir.getPath();
         directorypath.setText(path);

    }

    public void folderopen(View view){
        Intent intent = new Intent(Export.this, FolderPicker.class);
        startActivityForResult(intent, FOLDERPICKER_CODE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == FOLDERPICKER_CODE && resultCode == Activity.RESULT_OK) {

            path = intent.getExtras().getString("data");
            directorypath.setText(path);

        }
    }

    public void export(View view){
       exportalert();
    }

    /*Export locally that means in your folder*/
    public void onlyexport(){
        //generate data
        StringBuilder data = new StringBuilder();
        DatabaseOperation dbTask = new DatabaseOperation(Export.this);
        dbTask.open();
        ArrayList<String> surveydatalist = new ArrayList<>();
        surveydatalist = dbTask.getsurveydata(tskid);
        dbTask.close();

        /*p1,28.62648,077.37775,145.0,GPS fix,2020-02-13 11:28:58*/
        data.append("Point_name,Easting,Northing,Elevation,Point Code,Horizontal Precision,Vertical Precision,Date,Time,Antenna Height,Record Type,Precision Type");
        for(int i = 0; i<surveydatalist.size(); i++){
            String val = surveydatalist.get(i);
            String Point_name = val.split(",")[0];
            String easting = val.split(",")[1];
            String northing = val.split(",")[2];
            String elevation = val.split(",")[3];
            String point_code = val.split(",")[4];
            String horizontal_accuracy = val.split(",")[5];
            String vertical_accuracy = val.split(",")[6];
            String time = val.split(",")[7];
            String date = val.split(",")[8];
            String antennaheight = val.split(",")[9];
            String recordtype = val.split(",")[10];
            String precisiontype = val.split(",")[11];
            data.append("\n"+Point_name+","+easting+","+northing+","+elevation+","+point_code+","+horizontal_accuracy+","+vertical_accuracy+","+date+","+time+","+antennaheight+","+recordtype+","+precisiontype);
        }

        try{
            if(datatypoe.equalsIgnoreCase(getString(R.string.csv))){
                filename=filname.getText().toString()+".csv";
            }else if(datatypoe.equalsIgnoreCase(getString(R.string.txt))){
                filename=filname.getText().toString()+".txt";
            }

            //saving the file into device
            OutputStream out = new FileOutputStream(path+"/"+filename);
            out.write((data.toString()).getBytes());
            out.close();

            Toast.makeText(this,getString(R.string.data_exported),Toast.LENGTH_LONG).show();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }


    /*Share your data over the internet or message like=Gmail,Drive,Whatsapp and messages
    * You can make your own mail id according to your preference*/
    public void sendEmailWithAttachment() {
        //generate data
        StringBuilder data = new StringBuilder();
        DatabaseOperation dbTask = new DatabaseOperation(Export.this);
        dbTask.open();
        ArrayList<String> surveydatalist = new ArrayList<>();
        surveydatalist = dbTask.getsurveydata(tskid);
        dbTask.close();

        /*p1,28.62648,077.37775,145.0,GPS fix,2020-02-13 11:28:58*/

        /*p1,28.62648,077.37775,145.0,GPS fix,2020-02-13 11:28:58*/
        data.append("Point_name,Easting,Northing,Elevation,Point Code,Horizontal Precision,Vertical Precision,Date,Time,Antenna Height,Record Type,Precision Type");
        for(int i = 0; i<surveydatalist.size(); i++){
            String val = surveydatalist.get(i);
            String Point_name = val.split(",")[0];
            String easting = val.split(",")[1];
            String northing = val.split(",")[2];
            String elevation = val.split(",")[3];
            String point_code = val.split(",")[4];
            String horizontal_accuracy = val.split(",")[5];
            String vertical_accuracy = val.split(",")[6];
            String time = val.split(",")[7];
            String date = val.split(",")[8];
            String antennaheight = val.split(",")[9];
            String recordtype = val.split(",")[10];
            String precisiontype = val.split(",")[11];
            data.append("\n"+Point_name+","+easting+","+northing+","+elevation+","+point_code+","+horizontal_accuracy+","+vertical_accuracy+","+date+","+time+","+antennaheight+","+recordtype+","+precisiontype);
        }

        try{
            if(datatypoe.equalsIgnoreCase(getString(R.string.csv))){
                filename=filname.getText().toString()+".csv";
            }else if(datatypoe.equalsIgnoreCase(getString(R.string.txt))){
                filename=filname.getText().toString()+".txt";
            }

            //saving the file into device
            OutputStream out = new FileOutputStream(path+"/"+filename);
            out.write((data.toString()).getBytes());
            out.close();

            Toast.makeText(this,getString(R.string.data_exported),Toast.LENGTH_LONG).show();
            //exporting

            Context context = getApplicationContext();
            File filelocation = new File(path, filename);
            Uri contentUri = FileProvider.getUriForFile(context, "com.apogee.surveydemo.provider", filelocation);

            try {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent .setType("vnd.android.cursor.dir/email");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"apogeeabhijeet01@gmail.com"});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Survey App test Data");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "CSV Test");
                emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                emailIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
                startActivity(Intent.createChooser(emailIntent, "Send mail"));
            }catch (Exception e){
                e.printStackTrace();
            }


        } catch(Exception e) {
           e.printStackTrace();
        }
    }

    /*Alert for Export options*/
    public  void  exportalert(){
        final androidx.appcompat.app.AlertDialog dialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(this).create();
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.alertyesorno, null);
        Button button1 = dialogView.findViewById(R.id.positive);
        Button button2 = dialogView.findViewById(R.id.negativebutton);
        TextView textView1 = dialogView.findViewById(R.id.header);
        TextView textView2 = dialogView.findViewById(R.id.messaggg);

        textView1.setText(getString(R.string.export_new));
        textView2.setText(getString(R.string.choose_your_preferred_option));
        button1.setText(getString(R.string.export));
        button2.setText(getString(R.string.share));
        button2.setPadding(5,0,0,0);
        Drawable icon=this.getResources(). getDrawable( R.drawable.ic_baseline_share_24);
        button2.setCompoundDrawablesWithIntrinsicBounds(icon,null,null,null);

        dialogBuilder.setCancelable(true);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmailWithAttachment();
                dialogBuilder.dismiss();
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onlyexport();
                dialogBuilder.dismiss();

            }
        });

        dialogBuilder.setView(dialogView);
        Window window = dialogBuilder.getWindow();
        dialogBuilder.show();
        if(window != null){ // After the window is created, get the SoftInputMode
            window.getAttributes().windowAnimations = R.style.DialogAnimation;
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }
}
