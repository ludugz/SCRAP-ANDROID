package eplus.scrap.model;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by nals-anhdv on 11/20/17.
 */

public class MyTicket {
    static String fileName_unuse = "myTicketUnuse.json";
    static String fileName_used = "myTicketUsed.json";

    public static void save_unuse(Context context, String mJsonResponse) {
        try {
            FileWriter file = new FileWriter(context.getFilesDir().getPath() + "/" + fileName_unuse);
            file.write(mJsonResponse);
            file.flush();
            file.close();
        } catch (IOException e) {
            //Log.e("TAG", "Error in Writing: " + e.getLocalizedMessage());
        }
    }

    public static String getData_unuse(Context context) {
        try {
            File f = new File(context.getFilesDir().getPath() + "/" + fileName_unuse);
            //check whether file exists
            FileInputStream is = new FileInputStream(f);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer);
        } catch (IOException e) {
            //Log.e("TAG", "Error in Reading: " + e.getLocalizedMessage());
            return null;
        }
    }
    public static void save_used(Context context, String mJsonResponse) {
        try {
            FileWriter file = new FileWriter(context.getFilesDir().getPath() + "/" + fileName_used);
            file.write(mJsonResponse);
            file.flush();
            file.close();
        } catch (IOException e) {
            //Log.e("TAG", "Error in Writing: " + e.getLocalizedMessage());
        }
    }

    public static String getData_used(Context context) {
        try {
            File f = new File(context.getFilesDir().getPath() + "/" + fileName_used);
            //check whether file exists
            FileInputStream is = new FileInputStream(f);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer);
        } catch (IOException e) {
            //Log.e("TAG", "Error in Reading: " + e.getLocalizedMessage());
            return null;
        }
    }
}
