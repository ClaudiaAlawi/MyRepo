package it.fabaris.wfp.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

public class ImagePreviewActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);
        Intent intent = new Intent();
        intent.setData(Uri.parse("1"));
        setResult(RESULT_OK,intent);
        finish();
    }

}
