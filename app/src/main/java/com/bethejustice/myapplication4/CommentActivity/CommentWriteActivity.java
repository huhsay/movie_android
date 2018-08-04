package com.bethejustice.myapplication4.CommentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bethejustice.myapplication4.R;

public class CommentWriteActivity extends AppCompatActivity {

    RatingBar ratingBar;
    EditText commentView;
    int movieId;
    String title;
    float User_rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_comment);

        ratingBar =(RatingBar) findViewById(R.id.ratingBar);
        commentView = (EditText) findViewById(R.id.comment);
        Button cancelButton = (Button) findViewById(R.id.cancelButton);
        Button saveButton = (Button) findViewById(R.id.saveButton);
        TextView title = findViewById(R.id.title);


        Intent receivedIntent = getIntent();
        title.setText(receivedIntent.getStringExtra("title"));


        cancelButton.setOnClickListener(new View.OnClickListener() {
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
