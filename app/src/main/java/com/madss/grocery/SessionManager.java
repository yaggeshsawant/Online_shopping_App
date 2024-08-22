package com.madss.grocery;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class SessionManager {
        Context context;
        SharedPreferences sharedPreferences;
        SharedPreferences.Editor editor;
        private final String PREF_FILE_NAME = "reg_info";
        private final int PRIVATE_MODE = 0;

        private final String KEY_REG_ID = "key_reg_id";
        private final String KEY_USER_NAME = "key_user_name";
        private final String KEY_IF_LOGGED_IN = "key_session_if_logged_in";

        public void createSession(PojoLogin dto) {

            editor = sharedPreferences.edit();
            editor.putInt(KEY_REG_ID, dto.getId());
            editor.putString(KEY_USER_NAME, dto.getName());
            editor.putBoolean(KEY_IF_LOGGED_IN, true);
            editor.apply();
        }

        public SessionManager(Context context) {
            this.context = context;
            sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, PRIVATE_MODE);
            editor = sharedPreferences.edit();
        }


    public boolean checkSession() {
        int sessionId = sharedPreferences.getInt(KEY_REG_ID, 0);
        return sessionId > 0;
    }

   /* public PojoLogin getStudentDetails() {
        PojoLogin dto = new PojoLogin();
            dto.setRegistration_id(sharedPreferences.getInt(KEY_REG_ID, 0));
            dto.setCompany_name(sharedPreferences.getString(KEY_COMP_NAME, null));

        return dto;
    }*/



    public int getRegId() {
        return sharedPreferences.getInt(KEY_REG_ID, 0);
    }
    public String getuserName() {
        return sharedPreferences.getString(KEY_USER_NAME, "");
    }



    public void logoutSession() {
        editor.clear();
        editor.apply();

        Intent intent = new Intent(context, DashBoardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);



        // Optional: If you want to finish the current activity
        if (context instanceof Activity) {
            ((Activity) context).finish();
        }
    }



}
