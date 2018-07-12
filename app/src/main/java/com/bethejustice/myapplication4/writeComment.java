package com.bethejustice.myapplication4;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

public class writeComment extends AppCompatActivity {

    RatingBar ratingBar;
    EditText commentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_comment);

        ratingBar =(RatingBar) findViewById(R.id.ratingBar);
        commentView = (EditText) findViewById(R.id.comment);
        Button cancleButton = (Button) findViewById(R.id.cancelButton);
        Button saveButton = (Button) findViewById(R.id.saveButton);

        Intent receivedIntent = getIntent();
        float rating = receivedIntent.getFloatExtra("rating",0.0f);
        ratingBar.setStepSize(rating);

        cancleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                String comment = commentView.getText().toString();
                Log.d("intent", comment);
                intent.putExtra("commnet", comment);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }
}
