package com.fabiani.domohome;

import android.app.Application;
import android.content.Context;

import com.fabiani.domohome.model.Command;
import com.fabiani.domohome.model.Dashboard;

import org.junit.Test;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.fabiani.domohome.model.Dashboard.get;
import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }


}