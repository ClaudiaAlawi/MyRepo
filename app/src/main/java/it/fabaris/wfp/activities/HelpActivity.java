package it.fabaris.wfp.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Class that defines the help area.
 *
 */

public class HelpActivity extends Activity {
    private static final String AUTHORITY = "com.commonsware.android.cp.v4file";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);
        //setTitle(getString(R.string.app_name) + " > " + getString(R.string.help));
       setTitle("HELP > PET Manuals");
        final Context context = HelpActivity.this;
        final Button aboutCropsBtn = (Button) findViewById(R.id.about_crops);
        final Button aboutLivestockBtn = (Button) findViewById(R.id.about_livestock);
        final Button quickGuideCrops = (Button) findViewById(R.id.quickG_crops);
        final Button quickGuideLivestock = (Button) findViewById(R.id.quickG_livestock);
        final Button cropsGuide1 = (Button) findViewById(R.id.crops_guide1);
        final Button cropsGuide2 = (Button) findViewById(R.id.crops_guide2);
        final Button livestockGuide1 = (Button) findViewById(R.id.livestock_guide1);
        final Button livestockGuide2 = (Button) findViewById(R.id.livestock_guide2);


        aboutCropsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                File pdfFile = new File(Environment.getExternalStorageDirectory(), "/GRASP/AboutPETCrops.pdf");//File path
                if (pdfFile.exists()) //Checking for the file is exist or not
                {
                    Uri path = Uri.fromFile(pdfFile);
                    Intent objIntent = new Intent(Intent.ACTION_VIEW);
                    objIntent.setDataAndType(path, "application/pdf");
                    objIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(objIntent);//Staring the pdf viewer
                } else {

                    Toast.makeText(HelpActivity.this, "The file not exists! ", Toast.LENGTH_SHORT).show();

                }
            }
        });

        aboutLivestockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                File pdfFile = new File(Environment.getExternalStorageDirectory(), "/GRASP/AboutPETLivestock.pdf");//File path
                if (pdfFile.exists()) //Checking for the file is exist or not
                {
                    Uri path = Uri.fromFile(pdfFile);
                    Intent objIntent = new Intent(Intent.ACTION_VIEW);
                    objIntent.setDataAndType(path, "application/pdf");
                    objIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(objIntent);//Staring the pdf viewer
                } else {

                    Toast.makeText(HelpActivity.this, "The file not exists! ", Toast.LENGTH_SHORT).show();

                }
            }
        });

        quickGuideCrops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                File pdfFile = new File(Environment.getExternalStorageDirectory(), "/GRASP/QuickGuideCrops.pdf");//File path
                if (pdfFile.exists()) //Checking for the file is exist or not
                {
                    Uri path = Uri.fromFile(pdfFile);
                    Intent objIntent = new Intent(Intent.ACTION_VIEW);
                    objIntent.setDataAndType(path, "application/pdf");
                    objIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(objIntent);//Staring the pdf viewer
                } else {

                    Toast.makeText(HelpActivity.this, "The file not exists! ", Toast.LENGTH_SHORT).show();

                }
            }
        });

        quickGuideLivestock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                File pdfFile = new File(Environment.getExternalStorageDirectory(), "/GRASP/QuickGuideLivestock.pdf");//File path
                if (pdfFile.exists()) //Checking for the file is exist or not
                {
                    Uri path = Uri.fromFile(pdfFile);
                    Intent objIntent = new Intent(Intent.ACTION_VIEW);
                    objIntent.setDataAndType(path, "application/pdf");
                    objIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(objIntent);//Staring the pdf viewer
                } else {

                    Toast.makeText(HelpActivity.this, "The file not exists! ", Toast.LENGTH_SHORT).show();

                }
            }
        });

        cropsGuide1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                File pdfFile = new File(Environment.getExternalStorageDirectory(), "/GRASP/Guide_PETCrops_Part1.pdf");//File path
                if (pdfFile.exists()) //Checking for the file is exist or not
                {
                    Uri path = Uri.fromFile(pdfFile);
                    Intent objIntent = new Intent(Intent.ACTION_VIEW);
                    objIntent.setDataAndType(path, "application/pdf");
                    objIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(objIntent);//Staring the pdf viewer
                } else {

                    Toast.makeText(HelpActivity.this, "The file not exists! ", Toast.LENGTH_SHORT).show();

                }
            }
        });

        cropsGuide2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                File pdfFile = new File(Environment.getExternalStorageDirectory(), "/GRASP/Guide_PETCrops_Part2.pdf");//File path
                if (pdfFile.exists()) //Checking for the file is exist or not
                {
                    Uri path = Uri.fromFile(pdfFile);
                    Intent objIntent = new Intent(Intent.ACTION_VIEW);
                    objIntent.setDataAndType(path, "application/pdf");
                    objIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(objIntent);//Staring the pdf viewer
                } else {

                    Toast.makeText(HelpActivity.this, "The file not exists! ", Toast.LENGTH_SHORT).show();

                }
            }
        });


        livestockGuide1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                File pdfFile = new File(Environment.getExternalStorageDirectory(), "/GRASP/Guide_PETLivestock_Part1.pdf");//File path
                if (pdfFile.exists()) //Checking for the file is exist or not
                {
                    Uri path = Uri.fromFile(pdfFile);
                    Intent objIntent = new Intent(Intent.ACTION_VIEW);
                    objIntent.setDataAndType(path, "application/pdf");
                    objIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(objIntent);//Staring the pdf viewer
                } else {

                    Toast.makeText(HelpActivity.this, "The file not exists! ", Toast.LENGTH_SHORT).show();

                }
            }
        });

        livestockGuide2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                File pdfFile = new File(Environment.getExternalStorageDirectory(), "/GRASP/Guide_PETLivestock_Part2.pdf");//File path
                if (pdfFile.exists()) //Checking for the file is exist or not
                {
                    Uri path = Uri.fromFile(pdfFile);
                    Intent objIntent = new Intent(Intent.ACTION_VIEW);
                    objIntent.setDataAndType(path, "application/pdf");
                    objIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(objIntent);//Staring the pdf viewer
                } else {

                    Toast.makeText(HelpActivity.this, "The file not exists! ", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }
}