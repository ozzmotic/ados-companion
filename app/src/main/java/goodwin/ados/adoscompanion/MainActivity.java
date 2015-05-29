package goodwin.ados.adoscompanion;

import android.os.Environment;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.HapticFeedbackConstants;
import android.view.View.OnTouchListener;
import android.view.MotionEvent;
import android.widget.ToggleButton;

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
    private boolean isSessionActive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeButton( R.id.start_session, HapticFeedbackConstants.KEYBOARD_TAP );
        initializeButton( R.id.end_session, HapticFeedbackConstants.KEYBOARD_TAP );

        //Event list
        int[] buttonIDs = new int[] {R.id.freeplay,R.id.name,R.id.rabbitcar,R.id.bubbles,
            R.id.balloon,R.id.social_smile,R.id.peekaboo,R.id.imitation,R.id.birthday,R.id.snack,
            R.id.ec_examiner,R.id.ec_parent,R.id.rss,R.id.directX,R.id.gesture,R.id.requesting,
            R.id.init_JA,R.id.eye_gaze,R.id.language,R.id.sensory,R.id.rep,R.id.anx};

        for (int id : buttonIDs) {
            initializeButton( id, HapticFeedbackConstants.KEYBOARD_TAP );
        }

        eventLog = new ArrayList<>();
        mChronometer = (Chronometer) findViewById(R.id.chronometer);
    }

    public void startTimer(View v) {
        strt = SystemClock.elapsedRealtime();
        mChronometer.setBase(strt);
        mChronometer.start();
        isSessionActive = true;
    }

    public long getCurrentTime() {
        return SystemClock.elapsedRealtime() - mChronometer.getBase();
    }

    //TODO: Pause button

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

        isSessionActive = false;
        System.exit(0);
    }

    public void logEvent(View v) {
        String event = (String)v.getTag();
        String time = String.valueOf(getCurrentTime());
        boolean on = ((ToggleButton) v).isChecked();

        if (on) {
            eventLog.add(new String[] {event + "_started", time});
        } else {
            eventLog.add(new String[] {event + "_ended", time});
        }

    }

    public void logBehavior(View v) {
        String event = (String)v.getTag();
        String time = String.valueOf(getCurrentTime());
        eventLog.add(new String[] {event, time});
    }

    private void initializeButton(int btnId, int hapticId) {
        Button btn = ( Button ) findViewById( btnId );
        btn.setOnTouchListener( new HapticTouchListener(hapticId) );
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

    private class HapticTouchListener implements OnTouchListener {

        private final int feedbackType;

        public HapticTouchListener( int type ) { feedbackType = type; }

        public int feedbackType() { return feedbackType; }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // only perform feedback when the user touches the view, as opposed
            // to lifting a finger off the view
            if( event.getAction() == MotionEvent.ACTION_DOWN ){
                // perform the feedback
                v.performHapticFeedback( feedbackType() );
            }
            return false;
        }
    }
}
