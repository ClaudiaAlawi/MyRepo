/*******************************************************************************
 * Copyright (c) 2012 Fabaris SRL.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * Contributors:
 *     Fabaris SRL - initial API and implementation
 ******************************************************************************/
package it.fabaris.wfp.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import it.fabaris.wfp.application.Collect;
import it.fabaris.wfp.provider.FormProvider.DatabaseHelper;
import it.fabaris.wfp.task.HttpCheckPostTask;
import it.fabaris.wfp.utility.ConstantUtility;
import utils.ApplicationExt;

/**
 * Class that manage the preferences of the app
 */
public class userPreferencesActivity extends PreferenceActivity  {


    public static String KEY_BUTTON_CHECK = "button_check_conn";
    public static String KEY_BUTTON_MANUALS="button_pdf_download";
    public static String KEY_BUTTON_CROPS_W= "button_crops_w_download";
    public static String KEY_BUTTON_CROPS_D="button_crops_d_download";
    public static String KEY_BUTTON_LIVESTOCK_W="button_livestock_w_download";
    public static String KEY_BUTTON_LIVESTOCK_D="button_livestock_d_download";


    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.user_settings);
        setTitle(getString(R.string.app_name) + " > " + getString(R.string.settings));


        Preference buttonCheckConn = (Preference) findPreference(KEY_BUTTON_CHECK);
        buttonCheckConn.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference pref) {
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                String httpServer = settings.getString(PreferencesActivity.KEY_SERVER_URL, getString(R.string.default_server_url));
                String numClient = settings.getString(PreferencesActivity.KEY_CLIENT_TELEPHONE, getString(R.string.default_client_telephone));

                //SCELTA CONNESSIONE A RICHIESTA
                String onRequest = settings.getString(PreferencesActivity.KEY_REQUEST_CHOISE, getString(R.string.on_request));

                String http = httpServer + "/test";
                String phone = numClient;
                String data = "test";
                HttpCheckPostTask asyncTask = new HttpCheckPostTask(userPreferencesActivity.this, http, phone, data);
                asyncTask.execute();
                return true;
            }

        });

        mProgressDialog = new ProgressDialog(userPreferencesActivity.this);
        mProgressDialog.setMessage("Download...");
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setMax(100);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        Preference buttonManualsDownload = (Preference) findPreference(KEY_BUTTON_MANUALS);
        buttonManualsDownload.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                String httpServer = settings.getString(PreferencesActivity.KEY_SERVER_URL, getString(R.string.default_server_url));
                DownloadFile downloadFile = new DownloadFile();
                DownloadFile downloadFile2 = new DownloadFile();
                DownloadFile downloadFile3 = new DownloadFile();
                DownloadFile downloadFile4 = new DownloadFile();
                DownloadFile downloadFile5 = new DownloadFile();
                DownloadFile downloadFile6 = new DownloadFile();
                DownloadFile downloadFile7 = new DownloadFile();
                DownloadFile downloadFile8 = new DownloadFile();
//                downloadFile.execute(getString(R.string.new_app_url));
                String URL1,URL2,URL3,URL4,URL5,URL6,URL7,URL8= new String();
//                InetAddress actualIP,actualIP1;
//                String address="";
//                String address1="";

                String ip = settings.getString(PreferencesActivity.KEY_IP,null);
//                apkURL =ip +":80/graspreporting/Public/GraspMobile.apk";


                if(Patterns.WEB_URL.matcher(ip).matches()){

                    //TODO add the paths to other pdfs
                    URL1 ="http://"+ ip +":80/graspreporting/Public/AboutPETCrops.pdf";
                    downloadFile.execute(URL1);
                    URL2 ="http://"+ ip +":80/graspreporting/Public/AboutPETLivestock.pdf";
                    downloadFile2.execute(URL2);
                    URL3 ="http://"+ ip +":80/graspreporting/Public/Guide_PETCrops_Part1.pdf";
                    downloadFile3.execute(URL3);
                    URL4 ="http://"+ ip +":80/graspreporting/Public/Guide_PETCrops_Part2.pdf";
                    downloadFile4.execute(URL4);
                    URL5 ="http://"+ ip +":80/graspreporting/Public/Guide_PETLivestock_Part1.pdf";
                    downloadFile5.execute(URL5);
                    URL6 ="http://"+ ip +":80/graspreporting/Public/Guide_PETLivestock_Part2.pdf";
                    downloadFile6.execute(URL6);
                    URL7 ="http://"+ ip +":80/graspreporting/Public/QuickGuideCrops.pdf";
                    downloadFile7.execute(URL7);
                    URL8 ="http://"+ ip +":80/graspreporting/Public/QuickGuideLivestock.pdf";
                    downloadFile8.execute(URL8);

                }
                else{
                    Toast.makeText(getBaseContext(),"Server Unavailable!", Toast.LENGTH_LONG).show();
                }


                return true;
            }
        });

Preference livestockDrivingPics = (Preference)findPreference(KEY_BUTTON_LIVESTOCK_D);
        livestockDrivingPics.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                String httpServer = settings.getString(PreferencesActivity.KEY_SERVER_URL, getString(R.string.default_server_url));
                DownloadFile downloadFile1 = new DownloadFile();

//                downloadFile.execute(getString(R.string.new_app_url));
                String URL= new String();
//                InetAddress actualIP,actualIP1;
//                String address="";
//                String address1="";

                String ip = settings.getString(PreferencesActivity.KEY_IP,null);
//                apkURL =ip +":80/graspreporting/Public/GraspMobile.apk";


                if(Patterns.WEB_URL.matcher(ip).matches()){

                    //TODO add the paths to other pdfs
                    URL ="http://"+ ip +":80/graspreporting/Public/LivestockDriving.zip";
                    downloadFile1.execute(URL);
                    File file = new File(Environment.getExternalStorageDirectory().getPath() +"/GRASP/LivestockDriving.zip");
//                    try {
                     // getApplicationContext().deleteFile(file.getName());  //file.getCanonicalFile().delete();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }

                }
                else{
                    Toast.makeText(getBaseContext(),"Server Unavailable!", Toast.LENGTH_LONG).show();
                }


                return true;
            }
        });

Preference downloadLivestockWlaking = (Preference)findPreference(KEY_BUTTON_LIVESTOCK_W);
        downloadLivestockWlaking.setOnPreferenceClickListener(new OnPreferenceClickListener(){
            public boolean onPreferenceClick(Preference preference) {
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                String httpServer = settings.getString(PreferencesActivity.KEY_SERVER_URL, getString(R.string.default_server_url));
                DownloadFile downloadFile1 = new DownloadFile();

//                downloadFile.execute(getString(R.string.new_app_url));
                String URL= new String();
//                InetAddress actualIP,actualIP1;
//                String address="";
//                String address1="";

                String ip = settings.getString(PreferencesActivity.KEY_IP,null);
//                apkURL =ip +":80/graspreporting/Public/GraspMobile.apk";


                if(Patterns.WEB_URL.matcher(ip).matches()){

                    //TODO add the paths to other pdfs
                    URL ="http://"+ ip +":80/graspreporting/Public/LivestockWalking.zip";
                    downloadFile1.execute(URL);
                    File file = new File(Environment.getExternalStorageDirectory().getPath() +"/GRASP/LivestockWalking.zip");
//                    try {
                    // getApplicationContext().deleteFile(file.getName());  //file.getCanonicalFile().delete();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }

                }
                else{
                    Toast.makeText(getBaseContext(),"Server Unavailable!", Toast.LENGTH_LONG).show();
                }


                return true;
            }
        });

        Preference downloadCropsWalking = (Preference)findPreference(KEY_BUTTON_CROPS_W);
        downloadCropsWalking.setOnPreferenceClickListener(new OnPreferenceClickListener(){
            public boolean onPreferenceClick(Preference preference) {
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                String httpServer = settings.getString(PreferencesActivity.KEY_SERVER_URL, getString(R.string.default_server_url));
                DownloadFile downloadFile1 = new DownloadFile();

//                downloadFile.execute(getString(R.string.new_app_url));
                String URL= new String();
//                InetAddress actualIP,actualIP1;
//                String address="";
//                String address1="";

                String ip = settings.getString(PreferencesActivity.KEY_IP,null);
//                apkURL =ip +":80/graspreporting/Public/GraspMobile.apk";


                if(Patterns.WEB_URL.matcher(ip).matches()){

                    //TODO add the paths to other pdfs
                    URL ="http://"+ ip +":80/graspreporting/Public/CropsWalking.zip";
                    downloadFile1.execute(URL);
                    File file = new File(Environment.getExternalStorageDirectory().getPath() +"/GRASP/CropsWalking.zip");
//                    try {
                    // getApplicationContext().deleteFile(file.getName());  //file.getCanonicalFile().delete();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }

                }
                else{
                    Toast.makeText(getBaseContext(),"Server Unavailable!", Toast.LENGTH_LONG).show();
                }


                return true;
            }
        });

        Preference downloadCropsDriving = (Preference)findPreference(KEY_BUTTON_CROPS_D);
        downloadCropsDriving.setOnPreferenceClickListener(new OnPreferenceClickListener(){
            public boolean onPreferenceClick(Preference preference) {
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                String httpServer = settings.getString(PreferencesActivity.KEY_SERVER_URL, getString(R.string.default_server_url));
                DownloadFile downloadFile1 = new DownloadFile();

//                downloadFile.execute(getString(R.string.new_app_url));
                String URL= new String();
//                InetAddress actualIP,actualIP1;
//                String address="";
//                String address1="";

                String ip = settings.getString(PreferencesActivity.KEY_IP,null);
//                apkURL =ip +":80/graspreporting/Public/GraspMobile.apk";


                if(Patterns.WEB_URL.matcher(ip).matches()){

                    //TODO add the paths to other pdfs
                    URL ="http://"+ ip +":80/graspreporting/Public/CropsDriving.zip";
                    downloadFile1.execute(URL);
                    File file = new File(Environment.getExternalStorageDirectory().getPath() +"/GRASP/CropsDriving.zip");
//                    try {
                    // getApplicationContext().deleteFile(file.getName());  //file.getCanonicalFile().delete();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }

                }
                else{
                    Toast.makeText(getBaseContext(),"Server Unavailable!", Toast.LENGTH_LONG).show();
                }


                return true;
            }
        });






    }


    /**
     * while leaving the activity, unregister the SharedPreferencesListener
     */
    @Override
    protected void onPause() {
        super.onPause();
        // getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    /**
     * while resuming the activity, register the SharedPreferencesListener
     * in order to manages possible changes in the preferences
     */
    @Override
    protected void onResume() {
        super.onResume();

        // getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

    }



    private class DownloadFile extends AsyncTask<String, Integer, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... sUrl) {
            try {
                URL url = new URL(sUrl[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                /**
                 *  download the file
                 */
                InputStream input = new BufferedInputStream(url.openStream());

                /**
                 * CREATE A TEMPORARY FOLDER
                 */


                String fileName =url.toString();
                String file=fileName.substring(fileName.lastIndexOf("/"));
                File output = new File(Environment.getExternalStorageDirectory().getPath() ,"/GRASP/"+file );
                //File output = new File(Environment.getExternalStorageDirectory().getPath() + "/GRASP/grasp.apk");
                //File output = new File(PreferencesActivity.this.getCacheDir() + "/GRASP/grasp.apk");

                FileOutputStream fileOutput = new FileOutputStream(output);

                /**
                 *  this will be useful so that you can show a typical 0-100% progress bar
                 */
                int fileLength = connection.getContentLength();

                byte data[] = new byte[1024];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    /**
                     *  publishing the progress....
                     */
                    publishProgress((int) (total * 100 / fileLength));
                    Log.i("downloaded", "scaricati " + total);
                    fileOutput.write(data, 0, count);
                }
                fileOutput.flush();
                fileOutput.close();
                input.close();
                mProgressDialog.dismiss();

                if (fileName.contains("zip")) {
                    System.gc();
                    String unzipLocation =Environment.getExternalStorageDirectory().getPath() +"/GRASP/";
                    String zipped =Environment.getExternalStorageDirectory().getPath() +"/GRASP/"+file ;
                    //Decompress d = new Decompress(zipped, unzipLocation);
                    unzip(zipped, unzipLocation);

                }




            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            // if we get here, length is known, now set indeterminate to false
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setMax(100);
            mProgressDialog.setProgress(progress[0]);
        }
        protected void onPostExecute(String sResponse) {
            File file = new File(Environment.getExternalStorageDirectory().getPath() +"/GRASP/LivestockDriving.zip");
            try {
                file.getCanonicalFile().delete();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mProgressDialog.dismiss();
            System.gc();

            ;
//            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(userPreferencesActivity.this);
//            alertDialog.setTitle("DownLoad Completed");
//            alertDialog.setMessage("PET Manuals Downloaded Successfully!");
//
//            alertDialog.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int id) {
//                    dialog.dismiss();
//                }
//            });
//            final AlertDialog alert = alertDialog.create();
//            alert.show();
//        }



        }
    }
//    public class Decompress {
//        private String _zipFile;
//        private String _location;
//
//        public Decompress(String zipFile, String location) {
//            _zipFile = zipFile;
//            _location = location;
//
//            _dirChecker("");
//        }

        public void unzip(String zipFile, String location) throws IOException {
            int size;
            byte[] buffer = new byte[1024];

            try {
                if ( !location.endsWith("/") ) {
                    location += "/";
                }
                File f = new File(location);
                if(!f.isDirectory()) {
                    f.mkdirs();
                }
                ZipInputStream zin = new ZipInputStream(new BufferedInputStream(new FileInputStream(zipFile), 1024));
                try {
                    ZipEntry ze = null;
                    while ((ze = zin.getNextEntry()) != null) {
                        String path = location + ze.getName();
                        File unzipFile = new File(path);

                        if (ze.isDirectory()) {
                            if(!unzipFile.isDirectory()) {
                                unzipFile.mkdirs();
                            }
                        } else {
                            // check for and create parent directories if they don't exist
                            File parentDir = unzipFile.getParentFile();
                            if ( null != parentDir ) {
                                if ( !parentDir.isDirectory() ) {
                                    parentDir.mkdirs();
                                }
                            }

                            // unzip the file
                            FileOutputStream out = new FileOutputStream(unzipFile, false);
                            BufferedOutputStream fout = new BufferedOutputStream(out, 1024);
                            try {
                                while ( (size = zin.read(buffer, 0, 1024)) != -1 ) {
                                    fout.write(buffer, 0, size);
                                }

                                zin.closeEntry();
                            }
                            finally {
                                fout.flush();
                                fout.close();
                            }
                        }
                    }
                }
                finally {
                    zin.close();
                }
            }
            catch (Exception e) {
                Log.e("", "Unzip exception", e);
            }
        }

//        private void _dirChecker(String dir) {
//            File f = new File(_location + dir);
//
//            if(!f.isDirectory()) {
//                f.mkdirs();
//            }
//        }
//    }

}

