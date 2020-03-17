package com.fabiani.domohome.model;

import android.content.Context;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;

class JSONSerializer {
    private static final String TAG = JSONSerializer.class.getSimpleName();
    private Context mContext;
    private String mSerializeFileName;

     public JSONSerializer(Context c, String f) {
        mContext = c;
        mSerializeFileName = f;
    }

    void saveCommands(ArrayList<Command> commands)  {
        try (OutputStream out = mContext.openFileOutput(mSerializeFileName, Context.MODE_PRIVATE);
             Writer writer = new OutputStreamWriter(out)) {
             String jsonString=new Gson().toJson(commands);
             writer.write(jsonString);
        } catch (IOException e) {
            Log.i(TAG, "Unable to write file");
        }
    }

    ArrayList<Command> loadCommands() {
        ArrayList<Command> commands = new ArrayList<>();
        Type type=new TypeToken<ArrayList<Command>>(){}.getType();
        try (InputStream in = mContext.openFileInput(mSerializeFileName);
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in))) {
             commands=new Gson().fromJson(bufferedReader,type);
        } catch (IOException e) {
            Log.i(TAG, "Unable to read file");
        }
        return commands;
    }
}



