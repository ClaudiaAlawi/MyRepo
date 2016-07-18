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
/*
 * Copyright (C) 2009 University of Washington
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package it.fabaris.wfp.widget;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import org.javarosa.core.model.data.IAnswerData;
import org.javarosa.core.model.data.StringData;
import org.javarosa.form.api.FormEntryPrompt;

import java.io.File;
import java.text.Normalizer;

import it.fabaris.wfp.activities.FormEntryActivity;
import it.fabaris.wfp.activities.ImagePreviewActivity;
import it.fabaris.wfp.activities.R;
import it.fabaris.wfp.application.Collect;
import it.fabaris.wfp.utility.FileUtils;
import it.fabaris.wfp.view.ODKView;

/**
 * Widget that allows user to take pictures, sounds or video and add them to the form.
 */
public class ImageWidget extends QuestionWidget implements IBinaryWidget {
    private final static String t = "MediaWidget";



    private static Button mScoreButton;
    private static Button mZeroButton;
    private static ImageView mImageView;
    private static ImageView mImageView1;
    private static ImageView mImageView2;
    private static String mBinaryName;
    public static String imagesPath;

    private  String mInstanceFolder;
    private boolean mWaitingForData;
    public static String mFormName;

    private static TextView mErrorTextView;
    public static boolean prevView= false;
    public static int cropsPicturesIndex= -5;
    public static int picturesIndex= 0;
    private double currentScrore= 0;
    public static boolean imageScore = false;


    public ImageWidget(final Context context, final FormEntryPrompt prompt) {
        super(context, prompt);

        mWaitingForData = false;
        mInstanceFolder =
            FormEntryActivity.mInstancePath.substring(0,
                FormEntryActivity.mInstancePath.lastIndexOf("/") + 1);
        mFormName = FormEntryActivity.formName;
        final String mainPath =Environment.getExternalStorageDirectory().getPath() +"/GRASP/"+mFormName;

        setOrientation(LinearLayout.VERTICAL);

        mErrorTextView = new TextView(context);
        mErrorTextView.setText("Selected file is not a valid image");
        cropsPicturesIndex+= 2; // for crops walking
//        if (picturesIndex > 5)
//            picturesIndex = 1;
        Intent i = new Intent(context, ImagePreviewActivity.class);
        try
        {
            //   if (i.resolveActivity(context.getPackageManager()) != null) {
            ((Activity) getContext()).startActivityForResult(i, FormEntryActivity.IMAGE_PREVIEW);

            mWaitingForData = true;
            //   }

            //********************
            //mBinaryName =
            //********************

        } catch (ActivityNotFoundException e) {
            Toast.makeText(getContext(),
                    getContext().getString(R.string.activity_not_found, "image capture"),
                    Toast.LENGTH_SHORT);
        }

        mScoreButton = new Button(getContext());
        mScoreButton.setText(getContext().getString(R.string.scoreButton));
        mScoreButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, mAnswerFontsize);
        mScoreButton.setPadding(20, 20, 20, 20);
        mScoreButton.setEnabled(!prompt.isReadOnly());
        mScoreButton.setBackgroundColor(colorHelper.getMandatoryBackgroundColor());


        //-------------------setup for the zero button------------------------//
        mZeroButton = new Button(getContext());
        mZeroButton.setText(getContext().getString(R.string.zeroButton));
        mZeroButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, mAnswerFontsize);
        mZeroButton.setPadding(20, 20, 20, 20);
        mZeroButton.setEnabled(!prompt.isReadOnly());
        mZeroButton.setBackgroundColor(Color.LTGRAY);

        mZeroButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                currentScrore = 0;
                imageScore = true;

                setBinaryData(currentScrore);
                IAnswerData s = getAnswer();
                setAnswer(s);
            }
        });
        mScoreButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {

                mErrorTextView.setVisibility(View.GONE);

                // get the species:
                currentScrore = picturesIndex;
                imageScore = true;

                setBinaryData(currentScrore);
                IAnswerData s = getAnswer();
                setAnswer(s);

                mScoreButton.setBackgroundColor(Color.LTGRAY);

                //add the score to the arrayList

            }
        });


        imageScore =false;

//----------------------------------------------Get the Value of the Select---------------------------------------------------------------//

        final String species = SpinnerWidget.selectedAnswer.toString();
if (mFormName.contains("Walking")) {
    if (species != null)
        switch (species) {
            case "Camel":
                imagesPath = "Camels/" + picturesIndex;
                break;
            case "Cattle":
                imagesPath = "Cattles/" + picturesIndex;
                break;
            case "Goat":
                imagesPath = "Goats/" + picturesIndex;
                break;
            case "Long tailed sheep":
                imagesPath = "Lts/" + picturesIndex;
                break;
            case "Fat tailed sheep":
                imagesPath = "Fts/" + picturesIndex;
                break;
            case "Barley":
                imagesPath = "Barley/" + cropsPicturesIndex;
                break;
            case "Cassava":
                imagesPath = "Cassava/" + cropsPicturesIndex;
                break;
            case "Groundnut":
                imagesPath = "Groundnut/" + cropsPicturesIndex;
                break;
            case "Maize":
                imagesPath = "Maize/" + cropsPicturesIndex;
                break;
            case "Teff":
                imagesPath = "Teff/" + cropsPicturesIndex;
                break;
            case "Sunflowers":
                imagesPath = "Sunflowers/" + cropsPicturesIndex;
                break;
            case "Wheat Irrigated":
                imagesPath = "WheatI/" + cropsPicturesIndex;
                break;
            case "Wheat Rainfed":
                imagesPath = "WheatR/" + cropsPicturesIndex;
                break;
            case "Upland Rice":
                imagesPath = "Uplandrice/" + cropsPicturesIndex;
                break;
            case "Pearl Millet":
                imagesPath = "Pearlmillet/" + cropsPicturesIndex;
                break;
            case "Early Main Sorghum Rainfed":
                imagesPath = "Earlymaindrf/" + cropsPicturesIndex;
                break;
            case "Early Main Sorghum Irrigated":
                imagesPath = "EarlymainI/" + cropsPicturesIndex;
                break;
            case "Late Maturing Sorghum":
                imagesPath = "Latesorg/" + cropsPicturesIndex;
                break;
            case "Finger Millet":
                imagesPath = "Fingermillet/" + cropsPicturesIndex;
                break;
            default:
                break;

        }
}
        else{

    if(species != null)
        switch (species) {
            case "Camel":
                imagesPath = "Camels";
                break;
            case "Cattle":
                imagesPath = "Cattles";
                break;
            case "Goat":
                imagesPath = "Goats";
                break;
            case "Long tailed sheep":
                imagesPath = "Lts" ;
                break;
            case "Fat tailed sheep":
                imagesPath = "Fts" ;
                break;
            case"Barley":
                imagesPath ="Barley";
                break;
            case"Cassava":
                imagesPath ="Cassava";
                break;
            case"Groundnut":
                imagesPath ="Groundnut";
                break;
            case"Maize":
                imagesPath ="Maize";
                break;
            case"Teff":
                imagesPath ="Teff";
                break;
            case"Sunflowers":
                imagesPath ="Sunflowers";
                break;
            case"Wheat Irrigated":
                imagesPath ="WheatI";
                break;
            case"Wheat Rainfed":
                imagesPath ="WheatR";
                break;
            case"Upland Rice":
                imagesPath ="Uplandrice";
                break;
            case"Pearl Millet":
                imagesPath ="Pearlmillet";
                break;
            case"Early Main Sorghum Rainfed":
                imagesPath ="Earlymaindrf";
                break;
            case"Early Main Sorghum Irrigated":
                imagesPath ="EarlymainI";
                break;
            case"Late Maturing Sorghum":
                imagesPath ="Latesorg";
                break;
            case"Finger Millet":
                imagesPath ="Fingermillet";
                break;
            default:
                break;

        }
        }
//---------------------------------------------------------------------------------------End of getting selection --------------------------------------------------------------//
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------//
// ------------------------------------------------------------------------------------Setup buttons for crops walking -----------------------------------------------------------//


        final Button closeUpBtn = new Button(context);
        closeUpBtn.setText("Close Up");
        closeUpBtn.setTextSize(TypedValue.COMPLEX_UNIT_DIP, mAnswerFontsize);
        closeUpBtn.setPadding(20, 20, 20, 20);
        closeUpBtn.setEnabled(!prompt.isReadOnly());
        closeUpBtn.setBackgroundColor(Color.LTGRAY);
        closeUpBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                int closeupIndex = cropsPicturesIndex+ 1;
                String closeupPath = imagesPath.substring(0,imagesPath.lastIndexOf("/")) + "/"+closeupIndex;
                String imgCu = mainPath +"/"+closeupPath+ ".jpg";
                try {
                    Intent myIntent = new Intent(android.content.Intent.ACTION_VIEW);
                    File file = new File(imgCu);
                    String extension = android.webkit.MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(file).toString());
                    String mimetype = android.webkit.MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
                    myIntent.setDataAndType(Uri.fromFile(file),mimetype);
                    //   myIntent.setPackage("com.android.gallery");
                    context.startActivity(myIntent);
//                        Uri uri = Uri.parse("file:/"+FormEntryActivity.imagePath);
//                        context.startActivity(new Intent(Intent.ACTION_VIEW,uri));;
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(context,
                            context.getString(R.string.activity_not_found, "view image"),
                            Toast.LENGTH_SHORT);
                }
            }
        });;
        final Button scoreBtn = new Button(context);
        scoreBtn.setText("Score");
        scoreBtn.setTextSize(TypedValue.COMPLEX_UNIT_DIP, mAnswerFontsize);
        scoreBtn.setPadding(20, 20, 20, 20);
        scoreBtn.setEnabled(!prompt.isReadOnly());
        scoreBtn.setBackgroundColor(Color.GREEN);
        scoreBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                //check selection
               //get the index of the image from the image path
                // if it dosn't work out just put a checkbox
            }
        });;

//--------------------Setup buttons for livestock driving -----------//
        final Button CS1Button = new Button(getContext());
        CS1Button.setText("SCORE CS1");
        CS1Button.setTextSize(TypedValue.COMPLEX_UNIT_DIP, mAnswerFontsize);
        CS1Button.setPadding(20, 20, 20, 20);
        CS1Button.setEnabled(!prompt.isReadOnly());
        CS1Button.setBackgroundColor(getResources().getColor(R.color.wfp_azure1));


        final Button CS2Button = new Button(getContext());
        CS2Button.setText("SCORE CS2");
        CS2Button.setTextSize(TypedValue.COMPLEX_UNIT_DIP, mAnswerFontsize);
        CS2Button.setPadding(20, 20, 20, 20);
        CS2Button.setEnabled(!prompt.isReadOnly());
        CS2Button.setBackgroundColor(getResources().getColor(R.color.wfp_azure2));
        final Button CS3Button = new Button(getContext());
        CS3Button.setText("SCORE CS3");
        CS3Button.setTextSize(TypedValue.COMPLEX_UNIT_DIP, mAnswerFontsize);
        CS3Button.setPadding(20, 20, 20, 20);
        CS3Button.setEnabled(!prompt.isReadOnly());
        CS3Button.setBackgroundColor(Color.YELLOW);
        final Button CS4Button = new Button(getContext());
        CS4Button.setText("SCORE CS4");
        CS4Button.setTextSize(TypedValue.COMPLEX_UNIT_DIP, mAnswerFontsize);
        CS4Button.setPadding(20, 20, 20, 20);
        CS4Button.setEnabled(!prompt.isReadOnly());
        CS4Button.setBackgroundColor(getResources().getColor(R.color.light_red));
        final Button CS5Button = new Button(getContext());
        CS5Button.setText("SCORE CS5");
        CS5Button.setTextSize(TypedValue.COMPLEX_UNIT_DIP, mAnswerFontsize);
        CS5Button.setPadding(20, 20, 20, 20);
        CS5Button.setEnabled(!prompt.isReadOnly());
        CS5Button.setBackgroundColor(Color.RED);

        CS1Button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {

                mErrorTextView.setVisibility(View.GONE);

                // get the species:
                currentScrore = 1;
                imageScore = true;

                setBinaryData(currentScrore);
                IAnswerData s = getAnswer();
                setAnswer(s);

                CS1Button.setBackgroundColor(Color.LTGRAY);

                //add the score to the arrayList

            }
        });
        CS2Button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {

                mErrorTextView.setVisibility(View.GONE);

                // get the species:
                currentScrore = 2;
                imageScore = true;

                setBinaryData(currentScrore);
                IAnswerData s = getAnswer();
                setAnswer(s);

                CS2Button.setBackgroundColor(Color.LTGRAY);

                //add the score to the arrayList

            }
        });
        CS3Button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {

                mErrorTextView.setVisibility(View.GONE);

                // get the species:
                currentScrore = 3;
                imageScore = true;

                setBinaryData(currentScrore);
                IAnswerData s = getAnswer();
                setAnswer(s);

                CS3Button.setBackgroundColor(Color.LTGRAY);

                //add the score to the arrayList

            }
        });
        CS4Button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {

                mErrorTextView.setVisibility(View.GONE);

                // get the species:
                currentScrore = 4;
                imageScore = true;

                setBinaryData(currentScrore);
                IAnswerData s = getAnswer();
                setAnswer(s);

                CS4Button.setBackgroundColor(Color.LTGRAY);

                //add the score to the arrayList

            }
        });
        CS5Button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {

                mErrorTextView.setVisibility(View.GONE);

                // get the species:
                currentScrore = 5;
                imageScore = true;

                setBinaryData(currentScrore);
                IAnswerData s = getAnswer();
                setAnswer(s);

                CS5Button.setBackgroundColor(Color.LTGRAY);

                //add the score to the arrayList

            }
        });

        //--------------------Setup Buttons for the crops driving-----------------//
       final Button mRedButton = new Button(getContext());
       mRedButton.setText(getContext().getString(R.string.red));
       mRedButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, mAnswerFontsize);
        mRedButton.setPadding(20, 20, 20, 20);
        mRedButton.setEnabled(!prompt.isReadOnly());
        mRedButton.setBackgroundColor(Color.RED);
        //yellow
        final Button mYellowButton = new Button(getContext());
        mYellowButton.setText(getContext().getString(R.string.yellow));
        mYellowButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, mAnswerFontsize);
        mYellowButton.setPadding(20, 20, 20, 20);
        mYellowButton.setEnabled(!prompt.isReadOnly());
        mYellowButton.setBackgroundColor(Color.YELLOW);

        //blue
        final Button mBlueButton = new Button(getContext());
        mBlueButton.setText(getContext().getString(R.string.blue));
        mBlueButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, mAnswerFontsize);
        mBlueButton.setPadding(20, 20, 20, 20);
        mBlueButton.setEnabled(!prompt.isReadOnly());
        mBlueButton.setBackgroundColor(Color.BLUE);

        mRedButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                switch (species){
                    case"Barley":
                        currentScrore =3.25;
                        break;
                    case"Cassava":
                        currentScrore =37.60;
                        break;
                    case"Groundnuts":
                        currentScrore =3;
                        break;
                    case"Maize":
                        currentScrore =6;
                        break;
                    case"Teff":
                        currentScrore =2.40;
                        break;
                    case"Sunflowers":
                        currentScrore =2.66;
                        break;
                    case"Wheat Irrigated":
                        currentScrore =6.35;
                        break;
                    case"Wheat Rainfed":
                        currentScrore =5.50;
                        break;
                    case"Upland Rice":
                        currentScrore =4.75;
                        break;
                    case"Pearl Millet":
                        currentScrore =2.40;
                        break;
                    case"Early Main Sorghum Rainfed":
                        currentScrore =4.70;
                        break;
                    case"Early Main Sorghum Irrigated":
                        currentScrore =5.80;
                        break;
                    case"Late Maturing Sorghum":
                        currentScrore =2.64;
                        break;
                    case"Finger Millet":
                        currentScrore =2.99;
                        break;
                    default:
                        break;

                }

                setBinaryData(currentScrore);
                IAnswerData s = getAnswer();
                setAnswer(s);

                mRedButton.setBackgroundColor(Color.DKGRAY);
                mYellowButton.setBackgroundColor(Color.YELLOW);
                mBlueButton.setBackgroundColor(Color.BLUE);
                //add the score to the arrayList

            }
        });

        mBlueButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                switch (species){
                    case"Barley":
                        currentScrore =0.50;
                        break;
                    case"Cassava":
                        currentScrore =6.99;
                        break;
                    case"Groundnuts":
                        currentScrore =0.60;
                        break;
                    case"Maize":
                        currentScrore =0.60;
                        break;
                    case"Teff":
                        currentScrore =0.50;
                        break;
                    case"Sunflowers":
                        currentScrore =0.85;
                        break;
                    case"Wheat Irrigated":
                        currentScrore =1.50;
                        break;
                    case"Wheat Rainfed":
                        currentScrore =0.85;
                        break;
                    case"Upland Rice":
                        currentScrore =0.90;
                        break;
                    case"Pearl Millet":
                        currentScrore=0.59;
                        break;
                    case"Early Main Sorghum Rainfed":
                        currentScrore =0.70;
                        break;
                    case"Early Main Sorghum Irrigated":
                        currentScrore =1.43;
                        break;
                    case"Late Maturing Sorghum":
                        currentScrore =0.50;
                        break;
                    case"Finger Millet":
                        currentScrore =0.50;
                        break;
                    default:
                        break;

                }

                setBinaryData(currentScrore);
                IAnswerData s = getAnswer();
                setAnswer(s);

                mBlueButton.setBackgroundColor(Color.DKGRAY);
                mRedButton.setBackgroundColor(Color.RED);
                mYellowButton.setBackgroundColor(Color.YELLOW);

                //add the score to the arrayList

            }
        });
        mYellowButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                switch (species){
                    case"Barley":
                        currentScrore =1.85;
                        break;
                    case"Cassava":
                        currentScrore =20.15;
                        break;
                    case"Groundnuts":
                        currentScrore =2;
                        break;
                    case"Maize":
                        currentScrore =2.25;
                        break;
                    case"Teff":
                        currentScrore =1.25;
                        break;
                    case"Sunflowers":
                        currentScrore =1.80;
                        break;
                    case"Wheat Irrigated":
                        currentScrore =3.85;
                        break;
                    case"Wheat Rainfed":
                        currentScrore =2.50;
                        break;
                    case"Upland Rice":
                        currentScrore =2.20;
                        break;
                    case"Pearl Millet":
                        currentScrore=1.25;
                        break;
                    case"Early Main Sorghum Rainfed":
                        currentScrore =2;
                        break;
                    case"Early Main Sorghum Irrigated":
                        currentScrore =3.25;
                        break;
                    case"Late Maturing Sorghum":
                        currentScrore =1.70;
                        break;
                    case"Finger Millet":
                        currentScrore =1.51;
                        break;
                    default:
                        break;

                }

                setBinaryData(currentScrore);
                IAnswerData s = getAnswer();
                setAnswer(s);

                mYellowButton.setBackgroundColor(Color.DKGRAY);
                mRedButton.setBackgroundColor(Color.RED);
                mBlueButton.setBackgroundColor(Color.BLUE);

                //add the score to the arrayList

            }
        });







        //////////////////////////////////////////////////////////////////////

        mImageView = new ImageView(context);
        mImageView1 = new ImageView(context);
        mImageView2 = new ImageView(context);


      //  addView(mZeroButton);
        addView(mImageView);
        addView(mScoreButton);
        addView(mErrorTextView);




        //*********
    if (mFormName.contains("CropsDriving"))
    {
            removeView(mScoreButton);
            addView(mRedButton);
            addView(mYellowButton);
            addView(mBlueButton);
            addView(mZeroButton);
    }
        else if (mFormName.contains("LivestockWalking"))
    {

    }
        else if (mFormName.contains("LivestockDriving")){
            removeView(mScoreButton);
            addView(CS1Button);
            addView(CS2Button);
            addView(CS3Button);
            addView(CS4Button);
            addView(CS5Button);
            addView(mZeroButton);
        }
        else if (mFormName.contains("CropsWalking"))
    {
        //add see more buttons
        removeView(mImageView);
        removeView(mScoreButton);
       // addView(mImageView);
        //TODO add "Close Up" image and handle it
        addView(closeUpBtn);
        addView(scoreBtn);
        // TODO handle path for images 1 to 9 in one folder for the fad and handlethe close-ups for each one
        addView(mImageView1);
        addView(mImageView2);
        // add zero score based on condition
        //addView(mZeroButton);

    }
        //*********

        mErrorTextView.setVisibility(View.GONE);

        // retrieve answer from data model and update ui
        mBinaryName = prompt.getAnswerText();


    }

	/**
	 * get the file path and delete the file
	 */
    private void deleteMedia() {
        

        // There's only 1 in this case, but android 1.6 doesn't implement delete on
        // android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI only on
        // android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI + a #
        String[] projection = {
            Images.ImageColumns._ID
        };
        Cursor c =
            getContext().getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
                "_data='" + mInstanceFolder + mBinaryName + "'", null, null);
        int del = 0;
        if (c.getCount() > 0) {
            c.moveToFirst();
            String id = c.getString(c.getColumnIndex(Images.ImageColumns._ID));

            Log.i(
                t,
                "attempting to delete: "
                        + Uri.withAppendedPath(
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id));
            del =
                getContext().getContentResolver().delete(
                    Uri.withAppendedPath(
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id), null,
                    null);
        }
        c.close();

        // clean up variables
        mBinaryName = null;
        Log.i(t, "Deleted " + del + " rows from media content provider");
    }

    /**
     * reset the value of the answer Widget
     */
    public void clearAnswer() {
        // remove the file
        deleteMedia();
        mImageView.setImageBitmap(null);
        mErrorTextView.setVisibility(View.GONE);

        // reset buttons
//        mCaptureButton.setText(getContext().getString(R.string.capture_image));
    }


    /**
     * get the answer value from the widget
     */
    public IAnswerData getAnswer() {
        if (mBinaryName != null) {
//            mCaptureButton.setBackgroundColor(colorHelper.getReadOnlyBackgroundColor());
//            mChooseButton.setBackgroundColor(colorHelper.getReadOnlyBackgroundColor());
        	return new StringData(mBinaryName.toString());
        } else {
            return null;
        }
    }

    /**
     * get the put of the answer file
     * @param uri
     * @return
     */
    private String getPathFromUri(Uri uri) {
        if (uri.toString().startsWith("file")) {
            return uri.toString().substring(6);
        } else {
            // find entry in content provider
            Cursor c = getContext().getContentResolver().query(uri, null, null, null, null);
            c.moveToFirst();

            // get data path
            String colString = c.getString(c.getColumnIndex("_data"));
            c.close();
            return colString;
        }
    }


    public void setBinaryData(Object binaryuri) {
        // you are replacing an answer. delete the previous image using the
        // content provider.
//        if (mBinaryName != null) {
//
//        }
;
        mBinaryName = currentScrore + "";
        Log.i(t, "Setting current answer to " + currentScrore);

        mWaitingForData = false;
    }



    /**
     * Hide the soft keyboard if it's showing
     */
    public void setFocus(Context context) {
        // Hide the soft keyboard if it's showing.
        InputMethodManager inputManager =
            (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(this.getWindowToken(), 0);
    }


    public boolean isWaitingForBinaryData() {
        return mWaitingForData;
    }


    public void setOnLongClickListener(OnLongClickListener l) {
//        mCaptureButton.setOnLongClickListener(l);
//        mChooseButton.setOnLongClickListener(l);
        if (mImageView != null) {
            mImageView.setOnLongClickListener(l);
        }
    }


    public void cancelLongPress() {
        super.cancelLongPress();
//        mCaptureButton.cancelLongPress();
//        mChooseButton.cancelLongPress();
        if (mImageView != null) {
            mImageView.cancelLongPress();
        }
    }


	@Override
	public IAnswerData setAnswer(IAnswerData a) {
		// TODO Auto-generated method stub
		return null;
	}

	public static void previewPhoto(String imageUri, final Context context)
	{
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int screenWidth = display.getWidth();
        int screenHeight = display.getHeight();

        if(mFormName.contains("LivestockWalking")){
            final String imgCu1= imageUri.substring(0,imageUri.lastIndexOf(".")) +"cu1.jpg";
            final String imgCu2= imageUri.substring(0,imageUri.lastIndexOf(".")) +"cu2.jpg";
            File f1 =new File(imgCu1);
            File f2 = new File(imgCu2);
            if (f1.exists()) {
                Bitmap bmp1 = FileUtils.getBitmapScaledToDisplay(f1, screenHeight, screenWidth);
                if (bmp1 == null) {
                    mErrorTextView.setVisibility(View.VISIBLE);
                }
                mImageView1.setImageBitmap(bmp1);
            } else {
                mImageView1.setImageBitmap(null);
            }

            mImageView1.setPadding(10, 10, 10, 10);
            mImageView1.setAdjustViewBounds(true);

            //second


            mImageView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent("android.intent.action.VIEW");

                    try {
                        Intent myIntent = new Intent(android.content.Intent.ACTION_VIEW);
                        File file = new File(imgCu1);
                        String extension = android.webkit.MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(file).toString());
                        String mimetype = android.webkit.MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
                        myIntent.setDataAndType(Uri.fromFile(file),mimetype);
                        //   myIntent.setPackage("com.android.gallery");
                        context.startActivity(myIntent);
//                        Uri uri = Uri.parse("file:/"+FormEntryActivity.imagePath);
//                        context.startActivity(new Intent(Intent.ACTION_VIEW,uri));;
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(context,
                                context.getString(R.string.activity_not_found, "view image"),
                                Toast.LENGTH_SHORT);
                    }
//                }
//                c.close();
                }
            });
            // view close up2
            if (f2.exists()) {
                Bitmap bmp2 = FileUtils.getBitmapScaledToDisplay(f2, screenHeight, screenWidth);
                if (bmp2 == null) {
                    mErrorTextView.setVisibility(View.VISIBLE);
                }
                mImageView2.setImageBitmap(bmp2);
            } else {
                mImageView2.setImageBitmap(null);
            }

            mImageView2.setPadding(10, 10, 10, 10);
            mImageView2.setAdjustViewBounds(true);

            //second


            mImageView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent("android.intent.action.VIEW");

                    try {
                        Intent myIntent = new Intent(android.content.Intent.ACTION_VIEW);
                        File file = new File(imgCu2);
                        String extension = android.webkit.MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(file).toString());
                        String mimetype = android.webkit.MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
                        myIntent.setDataAndType(Uri.fromFile(file),mimetype);
                        //   myIntent.setPackage("com.android.gallery");
                        context.startActivity(myIntent);
//                        Uri uri = Uri.parse("file:/"+FormEntryActivity.imagePath);
//                        context.startActivity(new Intent(Intent.ACTION_VIEW,uri));;
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(context,
                                context.getString(R.string.activity_not_found, "view image"),
                                Toast.LENGTH_SHORT);
                    }
//                }
//                c.close();
                }
            });


        }


        File f = new File(imageUri);//(mInstanceFolder + "/" + mBinaryName);

        if (f.exists()) {
            Bitmap bmp = FileUtils.getBitmapScaledToDisplay(f, screenHeight, screenWidth);
            if (bmp == null) {
                mErrorTextView.setVisibility(View.VISIBLE);
            }
            mImageView.setImageBitmap(bmp);
        } else {
            mImageView.setImageBitmap(null);
        }

        mImageView.setPadding(10, 10, 10, 10);
        mImageView.setAdjustViewBounds(true);

        //second


        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent("android.intent.action.VIEW");

                    try {
                        Intent myIntent = new Intent(android.content.Intent.ACTION_VIEW);
                        File file = new File(FormEntryActivity.imagePath);
                        String extension = android.webkit.MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(file).toString());
                        String mimetype = android.webkit.MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
                        myIntent.setDataAndType(Uri.fromFile(file),mimetype);
                     //   myIntent.setPackage("com.android.gallery");
                        context.startActivity(myIntent);
//                        Uri uri = Uri.parse("file:/"+FormEntryActivity.imagePath);
//                        context.startActivity(new Intent(Intent.ACTION_VIEW,uri));;
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(context,
                            context.getString(R.string.activity_not_found, "view image"),
                            Toast.LENGTH_SHORT);
                    }
//                }
//                c.close();
            }
        });




	}



}