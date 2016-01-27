package teamatom.colormesh;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Result extends AppCompatActivity {

    DatabaseHelper myDb;
    int high_score,cscore,tout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        myDb = new DatabaseHelper(this);

        Intent intent = getIntent();

        Cursor res = myDb.getAllData();
        if (res.getCount() == 0) {
            high_score = 0;
        }
        else {
            StringBuilder buffer = new StringBuilder();
            while (res.moveToNext())
                buffer.append(res.getString(0));
            high_score = Integer.parseInt(buffer.toString());
        }

        cscore = Integer.parseInt(intent.getStringExtra(ColorMesh.score));
        tout = Integer.parseInt(intent.getStringExtra(ColorMesh.tout));

        TextView greet = (TextView) findViewById(R.id.greet);

        if (tout == 1) {
            if (cscore > high_score) {
                greet.setText(R.string.tout_high);
                myDb.updateData("0", String.valueOf(cscore));
            }
            else greet.setText(R.string.tout);
        }
        else if (cscore > high_score) {
            greet.setText(R.string.high_score_beat);
            myDb.updateData("0",String.valueOf(cscore));
        }
        else if (cscore < high_score) greet.setText(R.string.try_again);
        else greet.setText(R.string.high_score_eql);
        greet.append(" "+cscore);

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXIT", true);
        startActivity(intent);
        finish();
    }

}