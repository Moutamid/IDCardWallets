package com.np.saver.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;


import com.np.saver.Model.Cardgetset;
import com.np.saver.Model.CategoryRowModel;
import com.np.saver.Model.UserCat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String CREATE_TABLE_CARD;
    private static final String CREATE_TABLE_CARD1;
    private static final String CREATE_TABLE_CATEGORY;
    private static final String CREATE_TABLE_CATEGORY1;
    private static final String DATABASE_NAME = "db_cardwallet";
    private static final String DB_TABLE = "table_card";
    private static final String DB_TABLE1 = "table_card1";
    private static final String DB_TABLE_CATEGORY = "table_category";
    private static final String DB_TABLE_CATEGORY1 = "table_category1";
    private static final String KEY_CARD = "card";
    private static String KEY_ID = "id";
    private static String KEY_ID_CAT = "id";
    private static String KEY_ID_CAT_TYPE = "type";
    private static final String KEY_IMAGE = "image_front";
    private static final String KEY_IMAGE1 = "image_back";
    private static final String KEY_NAME = "book_name";
    private static String KEY_NAME_CAT = "name";
    Context con;

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
    }

    static {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CREATE TABLE table_card(");
        stringBuilder.append(KEY_ID);
        stringBuilder.append(" INTEGER PRIMARY KEY AUTOINCREMENT,");
        stringBuilder.append(KEY_NAME);
        stringBuilder.append(" TEXT,");
        stringBuilder.append(KEY_IMAGE);
        stringBuilder.append(" BLOB,");
        stringBuilder.append(KEY_IMAGE1);
        stringBuilder.append(" BLOB,");
        stringBuilder.append(KEY_CARD);
        stringBuilder.append(" TEXT);");
        CREATE_TABLE_CARD = stringBuilder.toString();
         stringBuilder = new StringBuilder();
        stringBuilder.append("CREATE TABLE table_card1(");
        stringBuilder.append(KEY_ID);
        stringBuilder.append(" INTEGER PRIMARY KEY AUTOINCREMENT,");
        stringBuilder.append(KEY_NAME);
        stringBuilder.append(" TEXT,");
        stringBuilder.append(KEY_IMAGE);
        stringBuilder.append(" BLOB,");
        stringBuilder.append(KEY_IMAGE1);
        stringBuilder.append(" BLOB,");
        stringBuilder.append(KEY_CARD);
        stringBuilder.append(" TEXT);");
        CREATE_TABLE_CARD1 = stringBuilder.toString();
        stringBuilder = new StringBuilder();
        stringBuilder.append("CREATE TABLE table_category(");
        stringBuilder.append(KEY_ID_CAT);
        stringBuilder.append(" INTEGER PRIMARY KEY AUTOINCREMENT,");
        stringBuilder.append(KEY_NAME_CAT);
        stringBuilder.append(" TEXT,");
        stringBuilder.append(KEY_ID_CAT_TYPE);
        stringBuilder.append(" INTEGER);");
        CREATE_TABLE_CATEGORY = stringBuilder.toString();
        stringBuilder = new StringBuilder();
        stringBuilder.append("CREATE TABLE table_category1(");
        stringBuilder.append(KEY_ID_CAT);
        stringBuilder.append(" INTEGER PRIMARY KEY AUTOINCREMENT,");
        stringBuilder.append(KEY_NAME_CAT);
        stringBuilder.append(" TEXT,");
        stringBuilder.append(KEY_ID_CAT_TYPE);
        stringBuilder.append(" INTEGER);");
        CREATE_TABLE_CATEGORY1 = stringBuilder.toString();
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.con = context;
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL(CREATE_TABLE_CARD);
        sQLiteDatabase.execSQL(CREATE_TABLE_CARD1);
        sQLiteDatabase.execSQL(CREATE_TABLE_CATEGORY);
        sQLiteDatabase.execSQL(CREATE_TABLE_CATEGORY1);
    }

    public int addcategory(String str, int i) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME_CAT, str);
        contentValues.put(KEY_ID_CAT_TYPE, i);
        writableDatabase.insert(DB_TABLE_CATEGORY, String.valueOf(0), contentValues);
        writableDatabase.close();
        return i;
    }
    public void addcategory1(String str, byte[] i) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME_CAT, str);
        contentValues.put(KEY_ID_CAT_TYPE, i);
        writableDatabase.insert(DB_TABLE_CATEGORY1, String.valueOf(0), contentValues);
        writableDatabase.close();
//        return i;
    }

    public void addEntry(String str, byte[] bArr, byte[] bArr2, String str2) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME, str);
        contentValues.put(KEY_IMAGE, bArr);
        contentValues.put(KEY_IMAGE1, bArr2);
        contentValues.put(KEY_CARD, str2);
        writableDatabase.insert(DB_TABLE, null, contentValues);
        writableDatabase.close();

    }
    public void addEntry1(String str, byte[] bArr, byte[] bArr2, String str2) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME, str);
        contentValues.put(KEY_IMAGE, bArr);
        contentValues.put(KEY_IMAGE1, bArr2);
        contentValues.put(KEY_CARD, str2);
        writableDatabase.insert(DB_TABLE1, null, contentValues);
        writableDatabase.close();

    }

    public void deletecard(String str) {
        try {
            SQLiteDatabase writableDatabase = getWritableDatabase();
            String str2 = DB_TABLE;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(KEY_ID);
            stringBuilder.append("=?");
            writableDatabase.delete(str2, stringBuilder.toString(), new String[]{str});
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("deletecard: ");
            stringBuilder2.append(writableDatabase);
            Log.i("lllll", stringBuilder2.toString());
            writableDatabase.close();
        } catch (Exception str3) {
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append("");
            stringBuilder3.append(str3);
            Log.e("Exception", stringBuilder3.toString());
        }
    }
    public void deletecard1(String str) {
        try {
            SQLiteDatabase writableDatabase = getWritableDatabase();
            String str2 = DB_TABLE1;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(KEY_ID);
            stringBuilder.append("=?");
            writableDatabase.delete(str2, stringBuilder.toString(), new String[]{str});
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("deletecard: ");
            stringBuilder2.append(writableDatabase);
            Log.i("lllll", stringBuilder2.toString());
            writableDatabase.close();
        } catch (Exception str3) {
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append("");
            stringBuilder3.append(str3);
            Log.e("Exception", stringBuilder3.toString());
        }
    }

    public void deletecategory(int i) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("id: ");
        stringBuilder.append(i);
        Log.i("Result", stringBuilder.toString());
        String str = DB_TABLE_CATEGORY1;
        stringBuilder = new StringBuilder();
        stringBuilder.append(KEY_ID_CAT);
        stringBuilder.append(" =?");
        int delete = writableDatabase.delete(str, stringBuilder.toString(), new String[]{String.valueOf(i)});
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("deletelist_item: ");
        stringBuilder2.append(delete);
        Log.i("Result", stringBuilder2.toString());
//        if (delete == -1) {
//            return 0;
//        }
        StringBuilder stringBuilder3 = new StringBuilder();
        stringBuilder3.append("DELETE FROM table_category1 WHERE ");
        stringBuilder3.append(KEY_ID_CAT);
        stringBuilder3.append("=");
        stringBuilder3.append(i);
        writableDatabase.execSQL(stringBuilder3.toString());
//        return 0;
    }

    public long updatecard(String str, String str2, byte[] bArr, byte[] bArr2) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME, str2);
        contentValues.put(KEY_IMAGE, bArr);
        contentValues.put(KEY_IMAGE1, bArr2);
        str2 = DB_TABLE;
        StringBuilder bArre = new StringBuilder();
        bArre.append(KEY_ID);
        bArre.append("= ?");
        writableDatabase.update(str2, contentValues, bArre.toString(), new String[]{str});
        writableDatabase.close();
        return Long.parseLong(str);
    }
    public long updatecard1(String str, String str2, byte[] bArr, byte[] bArr2) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME, str2);
        contentValues.put(KEY_IMAGE, bArr);
        contentValues.put(KEY_IMAGE1, bArr2);
        str2 = DB_TABLE1;
        StringBuilder bArre = new StringBuilder();
        bArre.append(KEY_ID);
        bArre.append("= ?");
        writableDatabase.update(str2, contentValues, bArre.toString(), new String[]{str});
        writableDatabase.close();
        return Long.parseLong(str);
    }

    public ArrayList<Cardgetset> getcard(String str) {
        ArrayList<Cardgetset> arrayList = new ArrayList();
        SQLiteDatabase readableDatabase = getReadableDatabase();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT * FROM table_card where card = '");
        stringBuilder.append(str);
        stringBuilder.append("'");
        Log.e("getcard: ", "" + stringBuilder);
        Cursor cursor = readableDatabase.rawQuery(stringBuilder.toString(), null);
        while (cursor.moveToNext()) {
            try {
                int i = cursor.getInt(cursor.getColumnIndex(KEY_ID));
                String string = cursor.getString(cursor.getColumnIndex(KEY_NAME));
                byte[] blob = cursor.getBlob(cursor.getColumnIndex(KEY_IMAGE));
                byte[] blob2 = cursor.getBlob(cursor.getColumnIndex(KEY_IMAGE1));
                String string2 = cursor.getString(cursor.getColumnIndex(KEY_CARD));
                Cardgetset cardgetset = new Cardgetset();
                cardgetset.setId(i);
                cardgetset.setName(string);
                cardgetset.setImage_front(blob);
                cardgetset.setImage_back(blob2);
                cardgetset.setCard(string2);
                arrayList.add(cardgetset);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append(cursor.getCount());
        stringBuilder.append("");
        if (cursor != null) {
            cursor.close();
        }
        readableDatabase.close();
        return arrayList;
    }
    public ArrayList<Cardgetset> getcard1(String str) {
        ArrayList<Cardgetset> arrayList = new ArrayList();
        SQLiteDatabase readableDatabase = getReadableDatabase();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT * FROM table_card1 where card = '");
        stringBuilder.append(str);
        stringBuilder.append("'");
        Log.e("getcard: ", "" + stringBuilder);
        Cursor cursor = readableDatabase.rawQuery(stringBuilder.toString(), null);
        while (cursor.moveToNext()) {
            try {
                int i = cursor.getInt(cursor.getColumnIndex(KEY_ID));
                String string = cursor.getString(cursor.getColumnIndex(KEY_NAME));
                byte[] blob = cursor.getBlob(cursor.getColumnIndex(KEY_IMAGE));
                byte[] blob2 = cursor.getBlob(cursor.getColumnIndex(KEY_IMAGE1));
                String string2 = cursor.getString(cursor.getColumnIndex(KEY_CARD));
                Cardgetset cardgetset = new Cardgetset();
                cardgetset.setId(i);
                cardgetset.setName(string);
                cardgetset.setImage_front(blob);
                cardgetset.setImage_back(blob2);
                cardgetset.setCard(string2);
                arrayList.add(cardgetset);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append(cursor.getCount());
        stringBuilder.append("");
        if (cursor != null) {
            cursor.close();
        }
        readableDatabase.close();
        return arrayList;
    }

    public ArrayList<Cardgetset> getForUpdate(String str, String str2) {
        ArrayList<Cardgetset> arrayList = new ArrayList();
        SQLiteDatabase readableDatabase = getReadableDatabase();
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("SELECT * FROM table_card where id = ");
        stringBuilder2.append(str);
        stringBuilder2.append(" and card=");
        stringBuilder2.append(str2);
        stringBuilder.append(stringBuilder2.toString());
        Log.e("getforupdate: ", "" + stringBuilder2);
        Cursor cursor = readableDatabase.rawQuery(stringBuilder.toString(), null);
        while (cursor.moveToNext()) {
            try {
                str2 = String.valueOf(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                String string = cursor.getString(cursor.getColumnIndex(KEY_NAME));
                byte[] blob = cursor.getBlob(cursor.getColumnIndex(KEY_IMAGE));
                byte[] blob2 = cursor.getBlob(cursor.getColumnIndex(KEY_IMAGE1));
                String string2 = cursor.getString(cursor.getColumnIndex(KEY_CARD));
                Cardgetset cardgetset = new Cardgetset();
                cardgetset.setId(Integer.parseInt(str2));
                cardgetset.setName(string);
                cardgetset.setImage_front(blob);
                cardgetset.setImage_back(blob2);
                cardgetset.setCard(string2);
                arrayList.add(cardgetset);
            } catch (Exception str22) {
                str22.printStackTrace();
            }
        }
        StringBuilder str22 = new StringBuilder();
        str22.append(cursor.getCount());
        str22.append("");
        cursor.close();
        readableDatabase.close();
        return arrayList;
    }
    public ArrayList<Cardgetset> getForUpdate1(String str, String str2) {
        ArrayList<Cardgetset> arrayList = new ArrayList();
        SQLiteDatabase readableDatabase = getReadableDatabase();
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("SELECT * FROM table_card1 where id = ");
        stringBuilder2.append(str);
        stringBuilder2.append(" and card=");
        stringBuilder2.append(str2);
        stringBuilder.append(stringBuilder2.toString());
        Log.e("getforupdate: ", "" + stringBuilder2);
        Cursor cursor = readableDatabase.rawQuery(stringBuilder.toString(), null);
        while (cursor.moveToNext()) {
            try {
                str2 = String.valueOf(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                String string = cursor.getString(cursor.getColumnIndex(KEY_NAME));
                byte[] blob = cursor.getBlob(cursor.getColumnIndex(KEY_IMAGE));
                byte[] blob2 = cursor.getBlob(cursor.getColumnIndex(KEY_IMAGE1));
                String string2 = cursor.getString(cursor.getColumnIndex(KEY_CARD));
                Cardgetset cardgetset = new Cardgetset();
                cardgetset.setId(Integer.parseInt(str2));
                cardgetset.setName(string);
                cardgetset.setImage_front(blob);
                cardgetset.setImage_back(blob2);
                cardgetset.setCard(string2);
                arrayList.add(cardgetset);
            } catch (Exception str22) {
                str22.printStackTrace();
            }
        }
        StringBuilder str22 = new StringBuilder();
        str22.append(cursor.getCount());
        str22.append("");
        cursor.close();
        readableDatabase.close();
        return arrayList;
    }

    public boolean totalcardisAvailable(String str) {
        SQLiteDatabase readableDatabase = getReadableDatabase();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT * FROM table_card where card = '");
        stringBuilder.append(str);
        stringBuilder.append("'");
        return readableDatabase.rawQuery(stringBuilder.toString(), null).moveToFirst() ? true : false;
    }
    public boolean totalcardisAvailable1(String str) {
        SQLiteDatabase readableDatabase = getReadableDatabase();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT * FROM table_card1 where card = '");
        stringBuilder.append(str);
        stringBuilder.append("'");
        return readableDatabase.rawQuery(stringBuilder.toString(), null).moveToFirst() ? true : false;
    }

    public ArrayList<CategoryRowModel> getcategory() {
        ArrayList<CategoryRowModel> arrayList = new ArrayList();
        SQLiteDatabase readableDatabase = getReadableDatabase();
        Cursor query = readableDatabase.query(DB_TABLE_CATEGORY, null, null, null, null, null, null);
        while (query.moveToNext()) {
            try {
                arrayList.add(new CategoryRowModel(query.getInt(query.getColumnIndex(KEY_ID_CAT)), query.getInt(query.getColumnIndex(KEY_ID_CAT_TYPE)), query.getString(query.getColumnIndex(KEY_NAME_CAT))));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(query.getCount());
        stringBuilder.append("");
        query.close();
        readableDatabase.close();
        return arrayList;
    }
    public ArrayList<UserCat> getcategory1() {
        ArrayList<UserCat> arrayList = new ArrayList();
        SQLiteDatabase readableDatabase = getReadableDatabase();
        Cursor query = readableDatabase.query(DB_TABLE_CATEGORY1, null, null, null, null, null, null);
        while (query.moveToNext()) {
            try {
                arrayList.add(new UserCat(query.getInt(query.getColumnIndex(KEY_ID_CAT)), query.getBlob(query.getColumnIndex(KEY_ID_CAT_TYPE)), query.getString(query.getColumnIndex(KEY_NAME_CAT))));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(query.getCount());
        stringBuilder.append("");
        query.close();
        readableDatabase.close();
        return arrayList;
    }

    public ArrayList<CategoryRowModel> getcategory_image(int i) {
        ArrayList<CategoryRowModel> arrayList = new ArrayList();
        Cursor query = getReadableDatabase().query(DB_TABLE_CATEGORY, null, null, null, null, null, null);
        String str = KEY_CARD;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("addEntry: ");
        stringBuilder.append(i);
        Log.i(str, stringBuilder.toString());
        arrayList.add(new CategoryRowModel(query.getInt(query.getColumnIndex(KEY_ID_CAT_TYPE))));
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("addEntry: ");
        stringBuilder2.append(arrayList);
        Log.i("id", stringBuilder2.toString());
        query.close();
        return arrayList;
    }
    public void importDB(String inFileName) {

        final String outFileName = con.getDatabasePath(DATABASE_NAME).toString();

        try {

            File dbFile = new File(inFileName);
            FileInputStream fis = new FileInputStream(dbFile);

            // Open the empty db as the output stream
            OutputStream output = new FileOutputStream(outFileName);

            // Transfer bytes from the input file to the output file
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }

            // Close the streams
            output.flush();
            output.close();
            fis.close();

            Toast.makeText(con, "Restore Completed", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(con, "Unable to Restore database. Retry", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    public void backup(String outFileName) {

        //database path
        final String inFileName = con.getDatabasePath(DATABASE_NAME).toString();

        try {

            File dbFile = new File(inFileName);
            FileInputStream fis = new FileInputStream(dbFile);

            // Open the empty db as the output stream
            OutputStream output = new FileOutputStream(outFileName);

            // Transfer bytes from the input file to the output file
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }

            // Close the streams
            output.flush();
            output.close();
            fis.close();

            Toast.makeText(con, "Backup Completed", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(con, "Unable to backup database. Retry", Toast.LENGTH_SHORT).show();
            Log.d("error1",e.getMessage().toString());
            e.printStackTrace();
        }
    }


}
