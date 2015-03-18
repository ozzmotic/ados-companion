package goodwin.ados.adoscompanion;

import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Chronometer;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    Chronometer mChronometer;
    ArrayList<Pair> eventLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<Pair> eventLog = new ArrayList<Pair>();
        mChronometer = (Chronometer) findViewById(R.id.chronometer);

    }

    public void startTimer(View v) {
        mChronometer.setBase(SystemClock.elapsedRealtime());
        mChronometer.start();
    }

    public long getCurrentTime() {
        return SystemClock.elapsedRealtime() - mChronometer.getBase();
    }

    public void stopTimer(View v) {
        mChronometer.stop();

    }

    public void logEvent(View v) {
        String event = (String)v.getTag();
        long time = getCurrentTime();
        Pair p = Pair.create(time, event);
        eventLog.add(p);
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
