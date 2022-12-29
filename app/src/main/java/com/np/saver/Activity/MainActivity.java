package com.np.saver.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fxn.stash.Stash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.np.saver.Adapter.CustomAdapter;
import com.np.saver.Adapter.MainDataAdapter;
import com.np.saver.Adapter.UserCatDataAdapter;
import com.np.saver.Model.CatImage;
import com.np.saver.Model.CategoryModel;
import com.np.saver.Model.CategoryRowModel;
import com.np.saver.Model.UserCat;
import com.np.saver.R;
import com.np.saver.Utils.Adsload;
import com.np.saver.Utils.Constants;
import com.np.saver.Utils.StoredPreferencesValue;
import com.np.saver.database.DatabaseHelper;
import com.saadahmedsoft.popupdialog.PopupDialog;
import com.saadahmedsoft.popupdialog.Styles;
import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView rvMainlist, rvuserlist;
    ImageView ivSetting, addnew, refresh;
    TextView AppName;
    ArrayList<CategoryRowModel> catList;
    DatabaseHelper dbhelper;
    MainDataAdapter mainDataAdapter;
    boolean isVisable = true;
    Adsload adsload;
    List<CatImage> catImagesList = new ArrayList<>();
    ArrayList<UserCat> userCatList = new ArrayList<>();

    CustomAdapter customAdapter;
    UserCatDataAdapter userCatDataAdapter;
    CatImage catImages;
    FrameLayout frameBanner, frameBanner1;
    private static int viewClick = 0;
    public static ImageView selectedImage;

    boolean doubleBackToExitPressedOnce = false;
    ArrayList<CategoryModel> categoryModelArrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        popUp();
        init();
        setUpData();
        userCatList.addAll(this.dbhelper.getcategory1());
        userCatDataAdapter = new UserCatDataAdapter(MainActivity.this, userCatList);
        rvuserlist.setAdapter(userCatDataAdapter);
        rvuserlist.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        ivSetting.setOnClickListener(view -> {

            startActivity(new Intent(MainActivity.this, SettingActivity.class));
        });
        refresh.setOnClickListener(view -> {
            Toast.makeText(this, "Refreshed!", Toast.LENGTH_SHORT).show();
//            startActivity(new Intent(MainActivity.this, MainActivity.class));
        });
        AppName.setOnClickListener(view -> {
            Toast.makeText(this, "Refreshed!", Toast.LENGTH_SHORT).show();

//            startActivity(new Intent(MainActivity.this, MainActivity.class));
        });
        addnew.setOnClickListener(view -> {
            final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogBox);
            LayoutInflater inflater = getLayoutInflater();
            View convertView = inflater.inflate(R.layout.dialogue_category, null);
            RecyclerView recylcer = convertView.findViewById(R.id.recylcer);
            EditText input = convertView.findViewById(R.id.editname);
            LinearLayout itemspinner = (LinearLayout) convertView.findViewById(R.id.itemspinner);
            selectedImage = (ImageView) convertView.findViewById(R.id.selectedImage);
            TextView no = convertView.findViewById(R.id.no);
            TextView save = convertView.findViewById(R.id.yes);
            recylcer.setVisibility(View.GONE);
            itemspinner.setVisibility(View.VISIBLE);
            itemspinner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recylcer.setVisibility(View.VISIBLE);
                    itemspinner.setVisibility(View.VISIBLE);

                }
            });
            recylcer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recylcer.setVisibility(View.VISIBLE);
                    itemspinner.setVisibility(View.VISIBLE);
                }
            });

            if (viewClick == 0) {
                viewClick = 1;
                catImages = new CatImage();
                catImages.setImageType(R.drawable.p1);
                catImagesList.add(catImages);

//                catImages = new CatImage();
//                catImages.setImageType(R.drawable.p2);
//                catImagesList.add(catImages);
                catImages = new CatImage();
                catImages.setImageType(R.drawable.p3);
                catImagesList.add(catImages);

                catImages = new CatImage();
                catImages.setImageType(R.drawable.a7);
                catImagesList.add(catImages);

                catImages = new CatImage();
                catImages.setImageType(R.drawable.a8);
                catImagesList.add(catImages);

                catImages = new CatImage();
                catImages.setImageType(R.drawable.p4);
                catImagesList.add(catImages);

                catImages = new CatImage();
                catImages.setImageType(R.drawable.p5);
                catImagesList.add(catImages);

                catImages = new CatImage();
                catImages.setImageType(R.drawable.p6);
                catImagesList.add(catImages);

                catImages = new CatImage();
                catImages.setImageType(R.drawable.a7);
                catImagesList.add(catImages);

                catImages = new CatImage();
                catImages.setImageType(R.drawable.a8);
                catImagesList.add(catImages);

                catImages = new CatImage();
                catImages.setImageType(R.drawable.a9);
                catImagesList.add(catImages);

                catImages = new CatImage();
                catImages.setImageType(R.drawable.a10);
                catImagesList.add(catImages);

                catImages = new CatImage();
                catImages.setImageType(R.drawable.a11);
                catImagesList.add(catImages);

                catImages = new CatImage();
                catImages.setImageType(R.drawable.a12);
                catImagesList.add(catImages);

                catImages = new CatImage();
                catImages.setImageType(R.drawable.p2);
                catImagesList.add(catImages);

            }

            customAdapter = new CustomAdapter(MainActivity.this, catImagesList);
            recylcer.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
            recylcer.setHasFixedSize(true);
            recylcer.setAdapter(customAdapter);
            mBuilder.setView(convertView); // setView
            AlertDialog dialog = mBuilder.create();
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (input.getText().toString().equals("")) {
                        Toast.makeText(MainActivity.this, "Please Enter Category Name", Toast.LENGTH_SHORT).show();

                    } else {
                        /*Bitmap bitmap;
                        bitmap = ((BitmapDrawable) selectedImage.getDrawable()).getBitmap();
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        byte[] bitMapData = stream.toByteArray();
                        dbhelper = new DatabaseHelper(MainActivity.this);
                        dbhelper.addcategory1(input.getText().toString(), bitMapData);*/

                        String name = input.getText().toString();
                        int image = catImagesList.get(Stash.getInt(Constants.CURRENT_POSITION, 0)).getImageType();

                        CategoryModel categoryModel = new CategoryModel();
                        categoryModel.name = name;
                        categoryModel.image = image;
                        categoryModel.total_count = 0;
                        categoryModel.push_key = Constants.databaseReference().push().getKey();

                        Constants.databaseReference().child(Constants.CATEGORIES)
                                .child(categoryModel.push_key)
                                .setValue(categoryModel);

                        dialog.dismiss();
                        Toast.makeText(MainActivity.this, "Category saved!", Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(MainActivity.this, MainActivity.class));
//                        finish();
                    }
                }
            });
            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        });

        Constants.databaseReference().child(Constants.CATEGORIES)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {

                            categoryModelArrayList.clear();

                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                categoryModelArrayList.add(dataSnapshot.getValue(CategoryModel.class));
                            }

                            mainDataAdapter = new MainDataAdapter(MainActivity.this, categoryModelArrayList);
                            rvMainlist.setAdapter(mainDataAdapter);
                            rvMainlist.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));

                        } else {
                            addDefaultCategories();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    private void addDefaultCategories() {
        CategoryModel categoryModel1 = new CategoryModel();
        categoryModel1.name = "Aadhar\nCard";
        categoryModel1.image = R.drawable.icon_aadhar;
        categoryModel1.total_count = 0;
        categoryModel1.push_key = Constants.databaseReference().push().getKey();

        CategoryModel categoryModel2 = new CategoryModel();
        categoryModel2.name = "Pan\nCard";
        categoryModel2.image = R.drawable.icon_pan_card;
        categoryModel2.total_count = 0;
        categoryModel2.push_key = Constants.databaseReference().push().getKey();

        CategoryModel categoryModel3 = new CategoryModel();
        categoryModel3.name = "Voting\nCard";
        categoryModel3.image = R.drawable.icon_voting_card;
        categoryModel3.total_count = 0;
        categoryModel3.push_key = Constants.databaseReference().push().getKey();

        CategoryModel categoryModel4 = new CategoryModel();
        categoryModel4.name = "Driving\nLicense";
        categoryModel4.image = R.drawable.icon_driving_lice;
        categoryModel4.total_count = 0;
        categoryModel4.push_key = Constants.databaseReference().push().getKey();

        CategoryModel categoryModel5 = new CategoryModel();
        categoryModel5.name = "Passport";
        categoryModel5.image = R.drawable.icon_passport;
        categoryModel5.total_count = 0;
        categoryModel5.push_key = Constants.databaseReference().push().getKey();

        CategoryModel categoryModel6 = new CategoryModel();
        categoryModel6.name = "Business\nCard";
        categoryModel6.image = R.drawable.icon_bu_card;
        categoryModel6.total_count = 0;
        categoryModel6.push_key = Constants.databaseReference().push().getKey();

        CategoryModel categoryModel7 = new CategoryModel();
        categoryModel7.name = "RC\nBook";
        categoryModel7.image = R.drawable.icon_rc_book;
        categoryModel7.total_count = 0;
        categoryModel7.push_key = Constants.databaseReference().push().getKey();

        CategoryModel categoryModel8 = new CategoryModel();
        categoryModel8.name = "Office ID\ncard";
        categoryModel8.image = R.drawable.icon_office_id;
        categoryModel8.total_count = 0;
        categoryModel8.push_key = Constants.databaseReference().push().getKey();

        CategoryModel categoryModel9 = new CategoryModel();
        categoryModel9.name = "Debit\nCard";
        categoryModel9.image = R.drawable.icon_debit_card;
        categoryModel9.total_count = 0;
        categoryModel9.push_key = Constants.databaseReference().push().getKey();

        CategoryModel categoryModel10 = new CategoryModel();
        categoryModel10.name = "Credit\nCard";
        categoryModel10.image = R.drawable.icon_credit_card;
        categoryModel10.total_count = 0;
        categoryModel10.push_key = Constants.databaseReference().push().getKey();

        CategoryModel categoryModel11 = new CategoryModel();
        categoryModel11.name = "Transport\nCard";
        categoryModel11.image = R.drawable.icon_transport_card;
        categoryModel11.total_count = 0;
        categoryModel11.push_key = Constants.databaseReference().push().getKey();


        CategoryModel categoryModel12 = new CategoryModel();
        categoryModel12.name = "Insurance\nCard";
        categoryModel12.image = R.drawable.icon_insurance_card;
        categoryModel12.total_count = 0;
        categoryModel12.push_key = Constants.databaseReference().push().getKey();

        CategoryModel categoryModel13 = new CategoryModel();
        categoryModel13.name = "Shopping\nCards";
        categoryModel13.image = R.drawable.icon_shopping_card;
        categoryModel13.total_count = 0;
        categoryModel13.push_key = Constants.databaseReference().push().getKey();

        CategoryModel categoryModel14 = new CategoryModel();
        categoryModel14.name = "My\nCards";
        categoryModel14.image = R.drawable.ic_baseline_how_to_vote_24;
        categoryModel14.total_count = 0;
        categoryModel14.push_key = Constants.databaseReference().push().getKey();

        CategoryModel categoryModel15 = new CategoryModel();
        categoryModel15.name = "Other\nCards";
        categoryModel15.image = R.drawable.icon_other_card;
        categoryModel15.total_count = 0;
        categoryModel15.push_key = Constants.databaseReference().push().getKey();

        Constants.databaseReference().child(Constants.CATEGORIES)
                .child(categoryModel1.push_key)
                .setValue(categoryModel1);

        Constants.databaseReference().child(Constants.CATEGORIES)
                .child(categoryModel2.push_key)
                .setValue(categoryModel2);

        Constants.databaseReference().child(Constants.CATEGORIES)
                .child(categoryModel3.push_key)
                .setValue(categoryModel3);

        Constants.databaseReference().child(Constants.CATEGORIES)
                .child(categoryModel4.push_key)
                .setValue(categoryModel4);

        Constants.databaseReference().child(Constants.CATEGORIES)
                .child(categoryModel5.push_key)
                .setValue(categoryModel5);
        Constants.databaseReference().child(Constants.CATEGORIES)
                .child(categoryModel6.push_key)
                .setValue(categoryModel6);
        Constants.databaseReference().child(Constants.CATEGORIES)
                .child(categoryModel7.push_key)
                .setValue(categoryModel7);
        Constants.databaseReference().child(Constants.CATEGORIES)
                .child(categoryModel8.push_key)
                .setValue(categoryModel8);
        Constants.databaseReference().child(Constants.CATEGORIES)
                .child(categoryModel9.push_key)
                .setValue(categoryModel9);

        Constants.databaseReference().child(Constants.CATEGORIES)
                .child(categoryModel10.push_key)
                .setValue(categoryModel10);

        Constants.databaseReference().child(Constants.CATEGORIES)
                .child(categoryModel11.push_key)
                .setValue(categoryModel11);

        Constants.databaseReference().child(Constants.CATEGORIES)
                .child(categoryModel12.push_key)
                .setValue(categoryModel12);

        Constants.databaseReference().child(Constants.CATEGORIES)
                .child(categoryModel13.push_key)
                .setValue(categoryModel13);

        Constants.databaseReference().child(Constants.CATEGORIES)
                .child(categoryModel14.push_key)
                .setValue(categoryModel14);

        Constants.databaseReference().child(Constants.CATEGORIES)
                .child(categoryModel15.push_key)
                .setValue(categoryModel15);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    private void setUpData() {
        this.dbhelper = new DatabaseHelper(this);
        if (StoredPreferencesValue.isFirstLaunch(this)) {
            firstdefaultinsert();
        }

        catList = new ArrayList();
        catList.addAll(this.dbhelper.getcategory());
        /*mainDataAdapter = new MainDataAdapter(MainActivity.this, catList);
        rvMainlist.setAdapter(mainDataAdapter);
        rvMainlist.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));*/
    }

    public void firstdefaultinsert() {
        String[] strArr = new String[]{"Aadhar\nCard", "Pan\nCard", "Voting\nCard",
                "Driving\nLicense", "Passport", "Business\nCard", "RC\nBook",
                "Office ID\ncard", "Debit\nCard", "Credit\nCard", "Transport\nCard",
                "Insurance\nCard", "Shopping\nCards", "My\nCards", "Other\nCards"};
        int[] iArr = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
        if (this.dbhelper != null) {
            for (int i = 0; i < strArr.length; i++) {
                this.dbhelper.addcategory(strArr[i], iArr[i]);
            }
        }
        StoredPreferencesValue.setFirstLaunch(this, false);
    }

    public void init() {
        rvMainlist = findViewById(R.id.rvMainlist);
        rvuserlist = findViewById(R.id.rvuserlist);
        ivSetting = findViewById(R.id.ivSetting);
        frameBanner = findViewById(R.id.frameBanner);
        frameBanner1 = findViewById(R.id.frameBanner1);
        addnew = findViewById(R.id.addnew);
        refresh = findViewById(R.id.refresh);
        AppName = findViewById(R.id.tvAppNAme);
        adsload = new Adsload(MainActivity.this);
        adsload.loadBanner(MainActivity.this, frameBanner);
        adsload.loadBanner1(MainActivity.this, frameBanner1);
    }

    public void popUp() {

        PopupDialog.getInstance(this)
                .setStyle(Styles.ALERT)
                .setHeading("Update Alert!!")
                .setDescription("We are updating the application" +
                        " Backup your data as soon as possible")
                .setCancelable(false)
                .showDialog(new OnDialogButtonClickListener() {
                    @Override
                    public void onPositiveClicked(Dialog dialog) {
                        super.onPositiveClicked(dialog);
                    }

                    @Override
                    public void onNegativeClicked(Dialog dialog) {
                        super.onNegativeClicked(dialog);
                    }

                    @Override
                    public void onDismissClicked(Dialog dialog) {
                        super.onDismissClicked(dialog);
                    }
                });
    }
}