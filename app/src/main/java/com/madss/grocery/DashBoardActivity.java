
package com.madss.grocery;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;

import nl.joery.animatedbottombar.AnimatedBottomBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashBoardActivity extends AppCompatActivity implements FragmentLogin.OnSessionCreatedListener,OnCartCountChangeListener{
    Context context;
    DrawerLayout drawer_layout_id;
    Toolbar toolbar_id;
    ActionBarDrawerToggle toggle;
    FrameLayout frame_drawer_id;
    AnimatedBottomBar bottom_navbar_id;
    NavigationView navigation_view_id;
    PojoAdd newlist;

    ImageView callIcon, whatsappIcon, emailIcon, whatsappheadIcon, sidebarImageView, addToCartId, fb_img_id, imageSearchId;
    TextView sidebarTextView, editRegistration, Cart_item_count;
    ProgressDialog progressDialog;
    Dialog customDialog;
    SessionManager sessionManager = null;
    RetrofitApiInterface retrofitApiInterface = null;


    String ImgUrl;

    DBhandler db;

    private static final int CALL_PHONE_PERMISSION_REQUEST = 123; // You can use any integer value


    private static final int DELAY_MILLIS = 5000; // 5 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dash_board_activity);

        initialization();
//        checkLoginStatus(); // Check login status on activity creation
        getWindow().setStatusBarColor(getResources().getColor(R.color.primary_clr));   //for changing color status bar
        updateLoggedInUserInfo();
        showPopAd(); //pop advertisement
        // Show the custom design as a dialog
        //handleCustomDialogForAd();
        // handle sidebar and header icon click
        handleIconClick();
        // navigationItem selected listener on NavigationView
        handleNavigationViewListener();
        //handleNavIconOnClick();
        // handle bottom tab selected listener
        handleBottomTabSelectListener(); // user defined method
        //checkAndRequestPhoneStatePermission();
        /*if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 101);
        }*/
        //to show no. of item over cart logo
        CartCount();
        UpdateCustomerInfo();

    }

    private void UpdateCustomerInfo() {
        editRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               FragmentUpdate fragment= new FragmentUpdate();
               getSupportFragmentManager().beginTransaction()
                       .replace(R.id.frame_drawer_id,fragment).commit();
               drawer_layout_id.close();
            }
        });
    }



    void CartCount() {
        int Cart_Count= db.getAllProducts().size();
        onCartCountChange(Cart_Count);
    }
    @Override
    public void onCartCountChange(int newCount) {
        Cart_item_count.setText(String.valueOf(newCount));

    }

    private void showPopAd() {

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.popup_ad);

      retrofitApiInterface.getPopUpAdd().enqueue(new Callback<PojoAdd>() {
          @Override
          public void onResponse(Call<PojoAdd> call, Response<PojoAdd> response) {
              if (response.isSuccessful()){
                  newlist=response.body();
                  ImgUrl = newlist.getImage_formate();


                      CustomDialogAd.showDialog(context, ImgUrl, newlist.getLink());


              }
              else{
                  Toast.makeText(context, "add is not available", Toast.LENGTH_SHORT).show();
              }

          }

          @Override
          public void onFailure(Call<PojoAdd> call, Throwable t) {
              Log.d("API_Response", "Response message: " + "ERROR OCCURRED");
              Toast.makeText(context, "ERROR OCCURRED IN FETCHING DATA", Toast.LENGTH_SHORT).show();
          }
      });

/*        // Set up the button inside the dialog
        ImageView image = dialog.findViewById(R.id.image_ad);
        ImageView cross_id= dialog.findViewById(R.id.cross_id);
        Glide.with(context)
                .load("https://madsssoftwaresolution.com/Online_Shopping_Website_Php/AdvertiseImage/1.jpg")
                .placeholder(R.drawable.myknwlogo)
                .error(R.drawable.no_img)         // Optional error image if loading fails
                .into(image);

        cross_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        // Show the dialog
        dialog.show();*/

    }

    public void initialization() {
        context = this;
        toolbar_id = findViewById(R.id.toolbar_id);
        drawer_layout_id = findViewById(R.id.drawer_layout_id);
        frame_drawer_id = findViewById(R.id.frame_drawer_id);
        bottom_navbar_id = findViewById(R.id.bottom_navbar_id);
        navigation_view_id = findViewById(R.id.navigation_menu_id);
        Cart_item_count=findViewById(R.id.Cart_item_count);
        callIcon = findViewById(R.id.call_img_id);
        whatsappIcon = findViewById(R.id.whatsapp_img_id);
        whatsappheadIcon = findViewById(R.id.whatsapp_head_img_id);
        emailIcon = findViewById(R.id.email_img_id);
        fb_img_id = findViewById(R.id.fb_img_id);
        sidebarTextView = findViewById(R.id.sidebarTextView);
        editRegistration = findViewById(R.id.editRegistration);
        sidebarImageView = findViewById(R.id.sidebarImageView);
        addToCartId = findViewById(R.id.addToCartId);
        imageSearchId = findViewById(R.id.search_id);
        sessionManager = new SessionManager(DashBoardActivity.this);
        db = new DBhandler(getApplicationContext());
        retrofitApiInterface = RetrofitApiClient.getApiClient(this).create(RetrofitApiInterface.class);


        // default fragment layout show on load
        getSupportFragmentManager().beginTransaction().replace(frame_drawer_id.getId(), new FragmentHome()).commit();
        setSupportActionBar(toolbar_id);

        // below line for creation of toggle
        toggle = new ActionBarDrawerToggle(this, drawer_layout_id, toolbar_id, R.string.Open, R.string.Close);
        toggle.getDrawerArrowDrawable().setColor(ContextCompat.getColor(this, android.R.color.black));

        //  add created toggle
        drawer_layout_id.addDrawerListener(toggle);
        toggle.syncState();

        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading Please Wait...");

    }

   /* private void handleCustomDialogForAd() {
        //progressDialog.show();
        retrofitApiInterface.getPopUpAddInfo().enqueue(new Callback<PojoPopUpAdd>() {
            @Override
            public void onResponse(Call<PojoPopUpAdd> call, Response<PojoPopUpAdd> response) {


                if (response.isSuccessful() && response.body() != null) {
                    //Log.d("API_Response", "Response Message: " + response.body());
                    //Toast.makeText( getApplicationContext(), "AD FETCHED", Toast.LENGTH_SHORT).show()
                    PojoPopUpAdd dto = response.body();
                    if (dto.getA_id() > 0) {
                        // Calling Add Dialog
                        String adImageUrl = RetrofitApiClient.getBaseUrl() + "AdvertiseImage/" + dto.getA_id() + "." + dto.getImage_formate();
                        //Toast.makeText( getApplicationContext(), "Image"+adImageUrl, Toast.LENGTH_SHORT).show();
                        //Toast.makeText( getApplicationContext(), "Ad"+dto.getLink(), Toast.LENGTH_SHORT).show();
                        if (ImageAvailabilityChecker.isImageAvailable(getApplicationContext(), adImageUrl)) {
                            CustomDialogAd.showDialog(context, adImageUrl, dto.getLink());
                        }

                    }

                }
                //progressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<PojoPopUpAdd> call, Throwable t) {
                Log.d("API_Response", "Response message: " + "ERROR OCCURRED");
                Toast.makeText(getApplicationContext(), "AD ERROR OCCURRED", Toast.LENGTH_SHORT).show();
                //progressDialog.dismiss();
            }
        });
    }*/

    private void handleIconClick() {
        addToCartId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace current fragment with YourFragment
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_drawer_id, new FragmentAddToCart())
                        .addToBackStack(null)
                        .commit();
            }
        });

        callIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform action for calling
                // For example:
                // Start a phone call intent
                /*Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:9826607332"));
                startActivity(intent);*/

                // Check if the app has the CALL_PHONE permission
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    // If the permission is granted, initiate the phone call
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:9826607332"));
                    startActivity(callIntent);
                } else {
                    // If the permission is not granted, request it from the user
                    ActivityCompat.requestPermissions(DashBoardActivity.this, new String[]{Manifest.permission.CALL_PHONE}, CALL_PHONE_PERMISSION_REQUEST);
                }
            }
        });
        fb_img_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String facebookPageUrl = "https://www.facebook.com/MyKhandwa"; // Replace with your Facebook Page URL
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(facebookPageUrl)));
            }
        });

        whatsappIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToWhatsappChat();
            }
        });


        whatsappheadIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToWhatsappChat();
            }
        });


        emailIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:madss@gmail.com"));
                intent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                intent.putExtra(Intent.EXTRA_TEXT, "Body");
                startActivity(intent);
            }
        });


        imageSearchId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment selectedFragment = new FragmentAllProductInfoBySearch();

                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frame_drawer_id);


                if (currentFragment.getClass() != selectedFragment.getClass()) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_drawer_id, selectedFragment).addToBackStack(null).commit();


                }
            }
        });




    }

   /* private void handleNavIconOnClick() {


        editRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawer_layout_id.close();

                Fragment selectedFragment = new FragmentEditRegistration();

                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frame_drawer_id);

                if (currentFragment.getClass() != selectedFragment.getClass()) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_drawer_id, selectedFragment).addToBackStack(null).commit();


                }
            }
        });
        imageSearchId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment selectedFragment = new FragmentAllRegistrationInfoBySearch();

                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frame_drawer_id);


                if (currentFragment.getClass() != selectedFragment.getClass()) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_drawer_id, selectedFragment).addToBackStack(null).commit();


                }
            }
        });
    }*/
/*    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.inflate(R.menu.top_navigation_menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Handle menu item clicks inside the PopupMenu
                // Handle settings action
                // Add more cases as needed
                return item.getItemId() == R.id.top_home_id;
            }
        });
        popupMenu.show();
    }*/

    public void goToWhatsappChat() {
        String phoneNumber = "9826607332"; // Replace this with the desired phone number
        Uri uri = Uri.parse("https://api.whatsapp.com/send?phone=" + phoneNumber);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }


    public void handleNavigationViewListener() {
        navigation_view_id.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                if (item.getItemId() == R.id.nav_home_id) {
                    selectedFragment = new FragmentHome();
                } else if (item.getItemId() == R.id.nav_about_id) {
                    selectedFragment = new FragmentAboutUs();
                } else if (item.getItemId() == R.id.nav_contact_id) {
                    selectedFragment = new FragmentContactUs();
                } else if (item.getItemId() == R.id.nav_feedback_id) {
                    selectedFragment = new FragmentFeedback();
                }else if (item.getItemId() == R.id.Pending_order_id){
                    selectedFragment = new FragmentPendingOrder();
                }else if (item.getItemId() == R.id.Complete_order_id){
                    selectedFragment = new FragmentCompleteOrder();
                }
                else if (item.getItemId() == R.id.nav_logout_id) {
                    sessionManager.logoutSession();
                    selectedFragment = new FragmentHome();
                } else if (item.getItemId() == R.id.nav_login_id) {
//                    sessionManager.logoutSession();
                    selectedFragment = new FragmentLogin();
                }


                drawer_layout_id.close();
                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(frame_drawer_id.getId(), selectedFragment).addToBackStack(null).commit();
                }
                return true;
            }
        });

    }

    private void handleBottomTabSelectListener() {
        bottom_navbar_id.setOnTabSelectListener(new AnimatedBottomBar.OnTabSelectListener() {
            @Override
            public void onTabSelected(int menuItemId, @Nullable AnimatedBottomBar.Tab tab, int previousMenuItemId, @NonNull AnimatedBottomBar.Tab previousTab) {
                Fragment selectedFragment = null;
                int selectedIndex = bottom_navbar_id.getSelectedIndex();
                Bundle bundle = new Bundle();

                // Replace R.id.menu_id with the actual ID of your menu item
                if (selectedIndex == 0) {
                    selectedFragment = new FragmentHome();
                } else if (selectedIndex == 1) {
                    selectedFragment = new FragmentShopping();
                } else if (selectedIndex == 2) {
                    selectedFragment = new FragmentOffers();
                }
                else if (selectedIndex == 3) {
                    selectedFragment = new FragmentAddToCart();
                }

                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frame_drawer_id);

                if (selectedFragment != null && currentFragment.getClass() != selectedFragment.getClass()) {
                    // Replace the current fragment with the new one
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_drawer_id, selectedFragment)  // R.id.frame_id is the container for your fragments
                            .addToBackStack(null)  // Add the transaction to the back stack
                            .commit();
                }
            }

            @Override
            public void onTabReselected(int menuItemId, @NonNull AnimatedBottomBar.Tab tab) {
                // Handle the event when a tab is reselected, if necessary
                //Toast.makeText(context, "Tab Reselected", Toast.LENGTH_SHORT).show();
                Fragment selectedFragment = null;
                int selectedIndex = bottom_navbar_id.getSelectedIndex();
                Bundle bundle = new Bundle();

                // Replace R.id.menu_id with the actual ID of your menu item
                if (selectedIndex == 0) {
                    selectedFragment = new FragmentHome();
                } else if (selectedIndex == 1) {
                    selectedFragment = new FragmentSubCategory();
                } else if (selectedIndex == 2) {
                    selectedFragment = new FragmentAddToCart();
                } else if (selectedIndex == 3) {
                    selectedFragment = new FragmentHome();
                }

                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frame_drawer_id);

                if (selectedFragment != null && currentFragment.getClass() != selectedFragment.getClass()) {
                    // Replace the current fragment with the new one
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_drawer_id, selectedFragment)  // R.id.frame_id is the container for your fragments
                            .addToBackStack(null)  // Add the transaction to the back stack
                            .commit();
                }
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        /* if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Toast.makeText(DashBoardActivity.this, "SERIAL NO : "+Build.SERIAL, Toast.LENGTH_SHORT).show();
        Log.d("Tag",Build.SERIAL);*/
        // Check for internet connectivity in a background thread

        new CheckInternetConnectivityTask().execute();
    }

    @Override
    public void onSessionCreated(PojoLogin user) {
        // update the session and UI when the session is created
        sessionManager.createSession(user);
        updateLoggedInUserInfo();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }




    public class CheckInternetConnectivityTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... voids) {
            return isNetworkConnected();
        }

        @Override
        protected void onPostExecute(Boolean isNetworkConnected) {
            if (!isNetworkConnected) {
                showNoInternetActivity();
            }
        }
    }

    private void showNoInternetActivity() {
        Toast.makeText(this, "NO INTERNET", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, NoInternetActivity.class);
        startActivity(i);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 101:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                } else {
                    //not granted
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

        if (requestCode == CALL_PHONE_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, initiate the phone call
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:9826607332"));
                startActivity(callIntent);
            } else {
                // Permission denied
                Toast.makeText(this, "Call permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void updateLoggedInUserInfo() {
        int regId = sessionManager.getRegId();
        LinearLayout editView = findViewById(R.id.editView);

        Menu navigationMenu = navigation_view_id.getMenu();
        MenuItem navLogin = navigationMenu.findItem(R.id.nav_login_id);
        MenuItem navLogout = navigationMenu.findItem(R.id.nav_logout_id);
        MenuItem navPending = navigationMenu.findItem(R.id.Pending_order_id);
        MenuItem navComplete = navigationMenu.findItem(R.id.Complete_order_id);


        if (regId == 0) {
            navLogout.setVisible(false);
            editView.setVisibility(View.GONE);
            navPending.setVisible(false);
            navComplete.setVisible(false);

            Glide.with(getApplicationContext()).load(R.drawable.profile).error(R.drawable.ic_profile_drawer).into(sidebarImageView);

            sidebarTextView.setText("My Khandwa");
        } else {
            navLogin.setVisible(false);
            navLogout.setVisible(true);
            navPending.setVisible(true);
            navComplete.setVisible(true);
            editView.setVisibility(View.VISIBLE);
            String imageUrl = RetrofitApiClient.getBaseUrl() + "RegistretionImage/" + regId + ".jpg";

            Glide.with(context).load(imageUrl).error(R.drawable.profile).into(sidebarImageView);

//            sidebarTextView.setText(sessionManager.getuserName());
            sidebarTextView.setText("Online Shopping");
        }


    }


}
