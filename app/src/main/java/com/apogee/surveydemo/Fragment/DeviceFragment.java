package com.apogee.surveydemo.Fragment;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;


import com.apogee.surveydemo.GridAdapter;
import com.apogee.surveydemo.Loglist;
import com.apogee.surveydemo.R;
import com.apogee.surveydemo.RoverConfigsNewActivity;
import com.apogee.surveydemo.utility.DeviceScanActivity;
import static com.apogee.surveydemo.TopoMap.baseinfpstring;

/**
 * A simple {@link Fragment} subclass.
 */
public class DeviceFragment extends Fragment {

    GridView grid;
    String[] web = {
            "Connection"/*,"Generic"*/, "Work Mode", /*"Static Setting", */"Terminal", "Device Info",/* "System Config", "About",*/

    } ;
    int[] imageId = {R.drawable.image_connection1,/*R.drawable.genericpng,*/ R.drawable.suitcase,/* R.drawable.image_staticsetting3,*/ R.drawable.cwindow, R.drawable.deviceapogee,/* R.drawable.image_systemconfig6,
            R.drawable.image_about7*/
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_device, container, false);
        GridAdapter adapter = new GridAdapter(getActivity().getApplicationContext(), web, imageId);
        grid = view.findViewById(R.id.grid);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if(position==0){
                    Toast.makeText(getActivity().getApplicationContext(), getString(R.string.you_clicked_at)+" " +web[+ position], Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity().getApplicationContext(), DeviceScanActivity.class);
                    startActivity(intent);
                }
                else if(position==1){
                    Intent intent = new Intent(getActivity().getApplicationContext(), RoverConfigsNewActivity.class);
                    startActivity(intent);

                }else if(position==2){
                    Intent intent = new Intent(getActivity().getApplicationContext(), Loglist.class);
                    startActivity(intent);
                }else if(position==3){
                    if(baseinfpstring!=null){
                        dialogbase();
                    }else{
                        Toast.makeText(getContext(), getText(R.string.oops_unable_to_find_data), Toast.LENGTH_SHORT).show();
                    }

                  /*  Intent intent = new Intent(getActivity().getApplicationContext(), MapsActivity.class);
                    startActivity(intent);*/
                }
            }
        });
        return view;
    }
    public void dialogbase(){
        final AlertDialog dialogBuilder = new AlertDialog.Builder(getContext()).create();
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.basepositiondialog, null);
        final TextView t1 = dialogView.findViewById(R.id.BName);
        final TextView t2 = dialogView.findViewById(R.id.BEasting);
        final TextView t3 = dialogView.findViewById(R.id.BNorthing);
        final TextView t4 = dialogView.findViewById(R.id.Bheighttt);
        final TextView t5 = dialogView.findViewById(R.id.BDist);
        final TextView t6 = dialogView.findViewById(R.id.eastname);
        final TextView t7 = dialogView.findViewById(R.id.northname);
        final TextView t8 = dialogView.findViewById(R.id.heightname);
        final RadioGroup radioGroup = dialogView.findViewById(R.id.rd1);
        RadioButton b = dialogView.findViewById(R.id.crtsn);
        b.setChecked(true);
        final RadioButton[] radioButton = new RadioButton[1];
        final Button button = dialogView.findViewById(R.id.llhbtn);
        String baseinfo = baseinfpstring;
        final String[] radiovalue = new String[1];
        final String bEastingg = baseinfo.split(",")[0];
        final String bNorthingg = baseinfo.split(",")[1];
        String bDistance = baseinfo.split(",")[2];
        String bHeight = baseinfo.split(",")[3];
        final String blattii = baseinfo.split(",")[4];
        final String blongii = baseinfo.split(",")[5];

        t2.setText(bEastingg+" m");
        t3.setText(bNorthingg+" m");
        t6.setText(getString(R.string.base_easting));
        t7.setText(getString(R.string.base_northing));
        t4.setText(bHeight);
        t5.setText(bDistance+" m");

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find the radioButton by returned id
                radioButton[0] = dialogView.findViewById(checkedId);
                // radioButton text
                radiovalue[0] = radioButton[0].getText().toString();
                if(radiovalue[0].equalsIgnoreCase(getString(R.string.cartesian))){
                    t2.setText(bEastingg+" m");
                    t3.setText(bNorthingg+" m");
                    t6.setText(getString(R.string.base_easting));
                    t7.setText(getString(R.string.base_northing));
                }else if(radiovalue[0].equalsIgnoreCase(getString(R.string.geographical))){
                    /*For degree indication use Alt+2-4-8*/
                    t2.setText(blattii+"°");
                    t3.setText(blongii+"°");
                    t6.setText(getString(R.string.base_latitude));
                    t7.setText(getString(R.string.base_longitude));
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
