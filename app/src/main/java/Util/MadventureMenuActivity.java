package Util;


import com.madventures.sawmalie.madventures.AboutActivity;
import com.madventures.sawmalie.madventures.Settings;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

/**
 * Custom Activity that extends Activity. All activities in our application
 * inherit from this custom Activity. The goal of this is to have all activities
 * with the same overwritten onOptionsItemSelected (and accompanying methods).
 * This is to standardize and centralize our options menu across all our
 * activities.
 */
public abstract class MadventureMenuActivity extends Activity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        }
        return false;
    }

    /**
     * Helper method that creates a new Intent and launches a web browser with
     * the supplied URL.
     *
     * @param url
     *            URL to be opened
     */
    public void launchWebsite(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    /**
     * Helper method that creates a new Intent and launches the About Activity.
     *
     * @param view
     *            View from where it originated
     */
    public void launchAbout(View view) {
        Intent about = new Intent(this, AboutActivity.class);
        startActivity(about);
    }

    /**
     * Helper method that creates a new Intent and launches the Settings
     * Activity.
     *
     * @param view
     *            View from where it originated
     */
    public void launchSettings(View view) {
        Intent s = new Intent(this, Settings.class);
        startActivity(s);
    }
}
