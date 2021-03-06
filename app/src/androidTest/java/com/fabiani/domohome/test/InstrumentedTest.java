package com.fabiani.domohome.test;

import android.graphics.Bitmap;
import android.support.test.runner.AndroidJUnit4;

import com.fabiani.domohome.SettingsFragment;
import com.fabiani.domohome.Command;
import com.fabiani.domohome.VideoFetchr;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class InstrumentedTest {
    private ArrayList<Command>commands;
    private Set<String> titles;
    @Before
    public void initialize() {
        // settings for camera testing
        SettingsFragment.sIp = "10.0.0.36";
        SettingsFragment.sPasswordOpen = 22071975;

        // settings for Hashset testing
        commands=new ArrayList<>();
        Command c = new Command();
        c.setTitle("pippo");
        c.setWhere(52);
        commands.add(c);
        titles = commands
                .stream()
                .map(Command::getTitle)
                .collect(Collectors.toSet());
    }
        @Test
        public void isUrlBitmapEqualToNull() throws IOException {
            VideoFetchr videoFetchr = new VideoFetchr();
            Bitmap b = videoFetchr.getUrlBitmap("https://" + SettingsFragment.sIp + "/telecamera.php");
            assertNotNull("Not null value is good! ",b);
        }

    @Test
    public void isSetNotNull(){
        assertNotNull("notnull",titles);
        Logger logger=Logger.getLogger("Logger");
        logger.info("Set   "+titles);
        logger.info("List   "+commands);
    }
    @Test
    public void isSearchPerformed(){
        String search="pippo";
        assertTrue("test for true",titles.contains(search));


    }
}
