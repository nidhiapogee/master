package com.apogee.surveydemo.Fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.apogee.surveydemo.CRS_sattelite;
import com.apogee.surveydemo.Export;
import com.apogee.surveydemo.GridAdapter;
import com.apogee.surveydemo.Import;
import com.apogee.surveydemo.NotificationCenter;
import com.apogee.surveydemo.ProjectList;
import com.apogee.surveydemo.R;
import com.apogee.surveydemo.Stakeselection;
import com.apogee.surveydemo.tasklist;

import java.text.DecimalFormat;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProjectFragment extends Fragment {
    SharedPreferences sharedPreferences;
    public static final String antennapref = "antenapref";
    GridView grid;
    String[] web = {
            "Project","Task", "SkyView", "Import", "Export", /*"Reports",*/ "Antenna", "Points","RoverConfig", "Config"/*, "Lines", "Features", "Cloud", "CodeList"*/,
    };
    int[] imageId = {R.drawable.image_project1,R.drawable.ab, R.drawable.satellite, R.drawable.importicon, R.drawable.export,/* R.drawable.image_reports5, */R.drawable.tripod,
            R.drawable.image_points7,R.drawable.image_connection1,R.drawable.deviceapogee/*, R.drawable.image_lines8, R.drawable.image_features9, R.drawable.image_cloud10, R.drawable.image_codelist11*/
    };

    public ProjectFragment() {
        // Required empty public constructor
    }

    private onClick listener;

    public void setListener(onClick listener) {
        this.listener = listener;
    }

  public   interface onClick{
        void onSuccess();
        void onConfig();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_device, container, false);
        GridAdapter adapter = new GridAdapter(getActivity().getApplicationContext(), web, imageId);
        grid = view.findViewById(R.id.grid);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if(position==0) {
                    Intent intent = new Intent(getActivity().getApplicationContext(), ProjectList.class);
                    startActivity(intent);
                }
                else if(position==1)
                {
                    Intent intent = new Intent(getActivity().getApplicationContext(), tasklist.class);
                    startActivity(intent);

                }else if(position==2){
                    Intent intent = new Intent(getActivity().getApplicationContext(), CRS_sattelite.class);
                    startActivity(intent);
                }else if(position==4){
                    Intent intent = new Intent(getActivity().getApplicationContext(), Export.class);
                    startActivity(intent);
                }else if(position==3){
                    Intent intent = new Intent(getActivity().getApplicationContext(), Import.class);
                    startActivity(intent);
                }else if(position==6){
                    Intent intent = new Intent(getActivity().getApplicationContext(), Stakeselection.class);
                    startActivity(intent);
                }else if(position==5){
                    dialogantenna();
                }else if(position == 7){
                   if(listener != null){
                       listener.onSuccess();
                   }

                }else if(position == 8){
                    if(listener != null){
                        listener.onConfig();
                    }
                }
            }
        });
        return view;
    }


    /*Alertdialog for antenna height*/

    public void dialogantenna(){
        final AlertDialog dialogBuilder = new AlertDialog.Builder(getContext()).create();
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialogantenna, null);
        final String[] antennatype = new String[1];
        final EditText editText = dialogView.findViewById(R.id.nm);
        final EditText editText1 = dialogView.findViewById(R.id.rds);
        final ImageView imgvw = dialogView.findViewById(R.id.imgv);
        final Spinner spinner = dialogView.findViewById(R.id.antennatype);
        Button button1 = dialogView.findViewById(R.id.antnabtn);
        sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(getContext());
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        String antenna = sharedPreferences.getString(antennapref, "default value");
        if(!antenna.equalsIgnoreCase("default value")) {
            String pname = antenna.split("_")[0];
            String height = antenna.split("_")[1];
            editText.setText(pname);
            editText1.setText(height);
        }

        final ArrayList<String> typelist = new ArrayList<>();
        typelist.add(getString(R.string.vertical));
        typelist.add(getString(R.string.slope));
        final ArrayAdapter<String> model_typeAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, typelist);
        model_typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(model_typeAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               antennatype[0] = parent.getItemAtPosition(position).toString();
                if(antennatype[0].equalsIgnoreCase(getString(R.string.vertical))){
                    imgvw.setImageResource(R.drawable.vertical);
                }else if(antennatype[0].equalsIgnoreCase(getString(R.string.slope))){
                    imgvw.setImageResource(R.drawable.slope);
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // DO SOMETHINGS
                String pointname = editText.getText().toString().trim();
                String height = editText1.getText().toString().trim();
                if(!pointname.equalsIgnoreCase("") && !height.equalsIgnoreCase("")){
                    if(antennatype[0].equalsIgnoreCase(getString(R.string.vertical))){
                        editor.putString(antennapref, pointname+"_"+height+"_"+getString(R.string.vertical));
                        editor.commit();
                    }else if(antennatype[0].equalsIgnoreCase(getString(R.string.slope))){
                        editor.putString(antennapref, pointname+"_"+height+"_"+getString(R.string.slope));
                        DecimalFormat df=new DecimalFormat();
                    }

                }else{
                    Toast.makeText(getContext(), getText(R.string.empty_field), Toast.LENGTH_SHORT).show();
                }
                dialogBuilder.dismiss();
            }
        });

        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
        Window window = dialogBuilder.getWindow();
        window.getAttributes().windowAnimations = R.style.DialogAnimation;
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }



}
