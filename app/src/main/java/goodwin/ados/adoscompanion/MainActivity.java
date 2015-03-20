package goodwin.ados.adoscompanion;

import android.os.Environment;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Chronometer;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.opencsv.CSVWriter;

public class MainActivity extends ActionBarActivity {

    Chronometer mChronometer;
    List<String[]> eventLog;
    private static String TAG = "ADOS_companion";
    private long strt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        eventLog = new ArrayList<>();
        mChronometer = (Chronometer) findViewById(R.id.chronometer);
    }

    public void startTimer(View v) {
        strt = SystemClock.elapsedRealtime();
        mChronometer.setBase(strt);
        mChronometer.start();
    }

    public long getCurrentTime() {
        return SystemClock.elapsedRealtime() - mChronometer.getBase();
    }

    public void stopTimer(View v) {
        mChronometer.stop();

        // Save log
        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS);
        File file = new File(path, "ADOS_log_" + strt + ".csv");

        try {
            CSVWriter writer = new CSVWriter(new FileWriter(file), ',');
            writer.writeAll(eventLog);
            writer.close();
        } catch (IOException e) {
            Log.e(TAG,"Exception: invalid file");
        }

        System.exit(0);
    }

    public void logEvent(View v) {
        String event = (String)v.getTag();
        String time = String.valueOf(getCurrentTime());
        eventLog.add(new String[] {event, time});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
