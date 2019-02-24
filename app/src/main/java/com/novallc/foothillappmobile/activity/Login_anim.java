package com.novallc.foothillappmobile.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.security.KeyPairGeneratorSpec;
import android.security.keystore.KeyNotYetValidException;
import android.security.keystore.KeyProperties;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.novallc.foothillappmobile.R;
import com.novallc.foothillappmobile.activity.ListViewAssets.ExpandableLayout;
import com.novallc.foothillappmobile.activity.WebViewAssets.RequestHTTPSTask;
import com.novallc.foothillappmobile.activity.util.AesCbcWithIntegrity;
import com.novallc.foothillappmobile.activity.util.ConnectivityClass;
import com.novallc.foothillappmobile.activity.util.ConnectivityReceiver;
import com.novallc.foothillappmobile.fragments.WebViewFragment;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.ECField;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.Calendar;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.security.auth.x500.X500Principal;

import static com.novallc.foothillappmobile.activity.util.AesCbcWithIntegrity.generateKey;
import static com.novallc.foothillappmobile.activity.util.AesCbcWithIntegrity.generateKeyFromPassword;
import static com.novallc.foothillappmobile.activity.util.AesCbcWithIntegrity.generateSalt;
import static com.novallc.foothillappmobile.activity.util.AesCbcWithIntegrity.keyString;
import static com.novallc.foothillappmobile.activity.util.AesCbcWithIntegrity.keys;
import static com.novallc.foothillappmobile.activity.util.AesCbcWithIntegrity.saltString;

public class Login_anim extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{
    private final int curApiVersion = Build.VERSION.SDK_INT;
    public static final String TAG = "FHS";

    private static final String AndroidKeyStore = "AndroidKeyStore";
    private static final String KEY_ALIAS = "foothillMobile";
    private static final String RSA_MODE =  "RSA/ECB/PKCS1Padding";
    private static final String AES_MODE = "AES/ECB/PKCS7Padding";
    private static final String SHARED_PREFERENCE_NAME = "loginPrefs";
    private static final String ENCRYPTED_KEY = "KEY_ENC";
    private static final String SALT_STORE = "SALT_ENC";

    private static boolean PASSWORD_BASED_KEY = true;
    private static String SALT_PASSWORD = "8B600EFBF4B543852A5277E21 passphrase";

    private LayoutInflater inflater;
    private RelativeLayout mRLoginLayout;
    private LinearLayout linearLayout;
    private TextView lLTextView_create;
    private CheckBox lLCheckBox;
    private Button button;
    private EditText input_email;
    private EditText input_password;
    private Snackbar mSnackbar;
    private Boolean transAnimComplete_ema = false; private Boolean tAC_ema_complete = true;
    private Boolean transAnimComplete_pass = false; private Boolean tAC_pass_complete = true;
    private String str_input_username; private String str_input_password;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;
    private String mKey;
    private KeyStore keyStore;
    private AesCbcWithIntegrity.SecretKeys generatedKey;
    private AesCbcWithIntegrity.SecretKeys key;
    private String keyStr;
    private boolean mAsyncTaskCompleted = false;

    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.login_anim);
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        lLCheckBox = (CheckBox)findViewById(R.id.saveLoginCheckBox);
            lLCheckBox.setEnabled(false);
        input_email = (EditText)findViewById(R.id.input_email);
        input_password = (EditText)findViewById(R.id.input_password);

        input_email.setEnabled(false);
        input_password.setEnabled(false);

        input_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!input_email.getText().toString().isEmpty()&&!input_password.getText().toString().isEmpty()){
                    lLCheckBox.setEnabled(true);
                }else lLCheckBox.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
                //Eliminate spaces in edit text
                String result = s.toString().replaceAll(" ", "");
                if (!s.toString().equals(result)) {
                    input_email.setText(result);
                    input_email.setSelection(result.length());
                }
            }
        });

        input_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!input_email.getText().toString().isEmpty()&&!input_password.getText().toString().isEmpty()){
                    lLCheckBox.setEnabled(true);
                }else lLCheckBox.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
                //Eliminate spaces in edit text
                String result = s.toString().replaceAll(" ", "");
                if (!s.toString().equals(result)) {
                    input_password.setText(result);
                    input_password.setSelection(result.length());
                }
            }
        });

        LoginAsyncTask newTask = new LoginAsyncTask();
        newTask.execute();

        /*AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    handleLogin_bckTask();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });*/
    }

    private class LoginAsyncTask extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... params) {
            try {
                handleLogin_bckTask();
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                handleLogin();
            }catch (Exception e){
                e.printStackTrace();
            }
            super.onPostExecute(s);
        }
    }

    @Override
    public void onEnterAnimationComplete() {
        super.onEnterAnimationComplete();
        mRLoginLayout = (RelativeLayout) findViewById(R.id.rLoginLayout);
        linearLayout = (LinearLayout) findViewById(R.id.LoginBox);
        lLTextView_create = (TextView)findViewById(R.id.link_signup);
        lLCheckBox = (CheckBox)findViewById(R.id.saveLoginCheckBox);
        button = (Button) findViewById(R.id.submit);

        /*
            bool_connectionTimeout: -1 = null, 0 = loginauthfailed, 1 = connectiontimeout
        */
        switch (WebViewActivity.bool_connectionTimeout){
            case 0:
                displaySnack(findViewById(R.id.rLoginLayout), getString(R.string.login_auth_failed));
                WebViewActivity.bool_connectionTimeout = -1;
                break;
            case 1:
                displaySnack(findViewById(R.id.rLoginLayout), getString(R.string.connection_timeout));
                WebViewActivity.bool_connectionTimeout = -1;
                break;
            case -1: break;
            default: break;
        }

        if(!(button.getVisibility() == View.VISIBLE) && !(linearLayout.getVisibility() == View.VISIBLE) && !(lLTextView_create.getVisibility() == View.VISIBLE) && !(lLCheckBox.getVisibility() == View.VISIBLE)) {
            linearLayout.setVisibility(View.GONE);
            lLTextView_create.setVisibility(View.GONE);
            lLCheckBox.setVisibility(View.GONE);
            button.setVisibility(View.GONE);
            final RelativeLayout mRelativeLayout = (RelativeLayout) findViewById(R.id.connectionView_include);
            mRelativeLayout.setVisibility(View.GONE);
            ImageView imageView = (ImageView) findViewById(R.id.logo);
            Animation loadAnimation = AnimationUtils.loadAnimation(this, R.anim.translate_y);

            if (checkConnection()) {
                loadAnimation.setAnimationListener(new AnimationListener_login(this, linearLayout, lLTextView_create, lLCheckBox, button));
                imageView.startAnimation(loadAnimation);
            } else {
                imageView.startAnimation(loadAnimation);
                loadAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        mRelativeLayout.setVisibility(View.VISIBLE);
                        Animation mFadeAnimation_scnd = AnimationUtils.loadAnimation(Login_anim.this, R.anim.fade);
                        mRelativeLayout.startAnimation(mFadeAnimation_scnd);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        }
        else return;

        if(curApiVersion >= Build.VERSION_CODES.LOLLIPOP){
            this.getWindow().setStatusBarColor(getResources().getColor(R.color.maps_status_bar));}

    }

    public void retryConnectivity(View view){
        RelativeLayout mRelativeLayout = (RelativeLayout)findViewById(R.id.includeViewGroup_connectivity);
        Animation mFadeAnimation_scnd = AnimationUtils.loadAnimation(Login_anim.this, R.anim.fade);
        if(checkConnection()) {
            mRelativeLayout.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
            lLTextView_create.setVisibility(View.VISIBLE);
            button.setVisibility(View.VISIBLE);
            lLCheckBox.setVisibility(View.VISIBLE);
            linearLayout.startAnimation(mFadeAnimation_scnd);
            lLTextView_create.startAnimation(mFadeAnimation_scnd);
            button.startAnimation(mFadeAnimation_scnd);
            lLCheckBox.setAnimation(mFadeAnimation_scnd);
        }else
            return;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.lga_in_left,R.anim.lga_out_right);
    }

    public void submitRequest(View view) {
//        Snackbar.make(view, (CharSequence) "Submitting...", Snackbar.LENGTH_LONG).show();
        input_email = (EditText)findViewById(R.id.input_email);
        input_password = (EditText)findViewById(R.id.input_password);

        Animation anim_transEma = AnimationUtils.loadAnimation(Login_anim.this, R.anim.translate_x_edittext_for);
        Animation anim_transPass = AnimationUtils.loadAnimation(Login_anim.this, R.anim.translate_x_edittext_for);
        Animation anim_transBack_ema = AnimationUtils.loadAnimation(Login_anim.this, R.anim.translate_x_edittext_bck);
        Animation anim_transBack_pass = AnimationUtils.loadAnimation(Login_anim.this, R.anim.translate_x_edittext_bck);

        anim_transBack_ema.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                tAC_ema_complete=true;
                transAnimComplete_ema = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        anim_transBack_pass.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                tAC_pass_complete=true;
                transAnimComplete_pass = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        anim_transEma.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                findViewById(R.id.img_required_field_email).setVisibility(View.VISIBLE);
                transAnimComplete_ema = true;
                tAC_ema_complete=false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        anim_transPass.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                findViewById(R.id.img_required_field_password).setVisibility(View.VISIBLE);
                transAnimComplete_pass = true;
                tAC_pass_complete=false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        if(input_email.getText().toString().matches("")){
            if(!transAnimComplete_ema) {
                input_email.startAnimation(anim_transEma);
            }
            displaySnack(view, getResources().getString(R.string.snackbar_fields_required));
        }else{
            findViewById(R.id.img_required_field_email).setVisibility(View.INVISIBLE);
            if(!tAC_ema_complete){
                input_email.startAnimation(anim_transBack_ema);
            }
        }

        if(input_password.getText().toString().matches("")){
            if(!transAnimComplete_pass) {
                input_password.startAnimation(anim_transPass);
            }
            displaySnack(view, getResources().getString(R.string.snackbar_fields_required));


        }else{
            findViewById(R.id.img_required_field_password).setVisibility(View.INVISIBLE);
            if(!tAC_pass_complete){
                input_password.startAnimation(anim_transBack_pass);
            }
        }

        if(!input_email.getText().toString().matches("")&&!input_password.getText().toString().matches("")){
//            Snackbar.make(view, (CharSequence) "Redirecting...", Snackbar.LENGTH_LONG).show();
            transAnimComplete_ema = false; tAC_ema_complete = true;
            transAnimComplete_pass = false; tAC_pass_complete = true;
            if(!checkConnection()){
                displaySnack(view, getResources().getString(R.string.no_connection_title));
                return;
            }else{
                //cannot use str_input values as they area stored in memory and can be retrieved in app cache, direct text inputs ONLY
                Intent WebViewClientIntent = new Intent(getApplicationContext(), WebViewActivity.class);
                Log.d("PASSANDUSER", str_input_username + " " +  str_input_password);
                WebViewClientIntent.putExtra("mUsername", input_email.getText().toString());
                WebViewClientIntent.putExtra("mPassword", input_password.getText().toString());
                WebViewClientIntent.putExtra("mURL", getString(R.string.uri));
                startActivity(WebViewClientIntent);
            }

        }
    }

    public void handleLogin_bckTask() throws Exception {
        //AES retrieval conversion, etc.
        //use some arbitrary filename and passkey for keystore validation

        /*
            saveLoginCheckBox handler, with preference query
            All inputs are encrypted with AES and CBC algs and 128bit padding
        */

        /*final String encryptedKey64 = loginPreferences.getString(ENCRYPTED_KEY, null);
        if (encryptedKey64 == null) {
            generatedKey = generateKey();
            ///String str_mKey = AesCbcWithIntegrity.keyString(generatedKey);
            loginPrefsEditor.putString(ENCRYPTED_KEY, AesCbcWithIntegrity.keyString(generatedKey));
            loginPrefsEditor.commit();
        }*/
        loginPreferences = getSharedPreferences(SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        saveLogin = loginPreferences.getBoolean("saveLogin", false);

        /*
            Generate and store AES Key
         */

        try {
            final String saltEncryption = loginPreferences.getString(SALT_STORE, null);
            if (saltEncryption == null) {//example for password based keys
                String salt = saltString(generateSalt());
                loginPrefsEditor.putString(SALT_STORE, salt);
                loginPrefsEditor.commit();

                Log.d("CHECK", "Salt: " + salt);
            }

            key = generateKeyFromPassword(SALT_PASSWORD, loginPreferences.getString(SALT_STORE, null));
            Log.d("CHECK", "salt is" + loginPreferences.getString(SALT_STORE, null));
            keyStr = keyString(key);
            Log.d("CHECK", "keystr = " + keyStr);
            key = null; //Recycle to demonstrate converting it from str

        } catch (GeneralSecurityException e) {
            Log.e(TAG, "GeneralSecurityException", e);
        }
    }

    public void handleLogin() throws Exception {
        Button submitBtn = (Button) findViewById(R.id.submit);

        lLCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromInputMethod(input_email.getWindowToken(), 0);
                    imm.hideSoftInputFromInputMethod(input_password.getWindowToken(), 0);

                    if (lLCheckBox.isChecked() && lLCheckBox.isEnabled()) {
                        //Encryption and storage
                        try {

                            key = keys(keyStr);
                            AesCbcWithIntegrity.CipherTextIvMac mCipherTxtUsername = AesCbcWithIntegrity.encrypt(input_email.getText().toString(), /*keys(loginPreferences.getString(ENCRYPTED_KEY, null))*/key);
                            AesCbcWithIntegrity.CipherTextIvMac mCipherTxtPassword = AesCbcWithIntegrity.encrypt(input_password.getText().toString(), /*keys(loginPreferences.getString(ENCRYPTED_KEY, null))*/key);

                            str_input_username = mCipherTxtUsername.toString();
                            str_input_password = mCipherTxtPassword.toString();
                            //Log.d("CHECK", "encrypted = " + str_input_username);
                            key = null;
                        } catch (GeneralSecurityException e) {
                            Log.e(TAG, "GeneralSecurityException", e);
                        } catch (UnsupportedEncodingException e) {
                            Log.e(TAG, "UnsupportedEncodingException", e);
                        }

                        loginPrefsEditor.putBoolean("saveLogin", true);
                        loginPrefsEditor.putString("username", str_input_username);
                        loginPrefsEditor.putString("password", str_input_password);
                        loginPrefsEditor.commit();
                    } else {
                        loginPrefsEditor.clear();
                        loginPrefsEditor.commit();
                    }
                }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    submitRequest(v);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromInputMethod(input_email.getWindowToken(), 0);
                    imm.hideSoftInputFromInputMethod(input_password.getWindowToken(), 0);

                    if (lLCheckBox.isChecked() && lLCheckBox.isEnabled()) {
                        try {

                            key = keys(keyStr);

                            AesCbcWithIntegrity.CipherTextIvMac mCipherTxtUsername = AesCbcWithIntegrity.encrypt(input_email.getText().toString(), /*keys(loginPreferences.getString(ENCRYPTED_KEY, null))*/key);
                            AesCbcWithIntegrity.CipherTextIvMac mCipherTxtPassword = AesCbcWithIntegrity.encrypt(input_password.getText().toString(), /*keys(loginPreferences.getString(ENCRYPTED_KEY, null))*/key);

                            str_input_username = mCipherTxtUsername.toString();
                            str_input_password = mCipherTxtPassword.toString();

                            key = null;
                        } catch (GeneralSecurityException e) {
                            Log.e(TAG, "GeneralSecurityException", e);
                        } catch (UnsupportedEncodingException e) {
                            Log.e(TAG, "UnsupportedEncodingException", e);
                        }

                        loginPrefsEditor.putBoolean("saveLogin", true);
                        loginPrefsEditor.putString("username", str_input_username);
                        loginPrefsEditor.putString("password", str_input_password);
                        loginPrefsEditor.commit();
                    } else {
                    }
                }
        });

        if (saveLogin) {
            try {
                //decryption and retrieval
//            Log.d("CHECK", "KEY = " + encryptedKey64);
                AesCbcWithIntegrity.CipherTextIvMac cipherTextIvMac_user = new AesCbcWithIntegrity.CipherTextIvMac(loginPreferences.getString("username", ""));
                AesCbcWithIntegrity.CipherTextIvMac cipherTextIvMac_pass = new AesCbcWithIntegrity.CipherTextIvMac(loginPreferences.getString("password", ""));

                Log.d("CHECK", "keyStrSaveLogin = " + keyStr);
                key = keys(keyStr);

                String _username = AesCbcWithIntegrity.decryptString(cipherTextIvMac_user, /*keys(loginPreferences.getString(ENCRYPTED_KEY, null))*/key);
                String _password = AesCbcWithIntegrity.decryptString(cipherTextIvMac_pass, /*keys(loginPreferences.getString(ENCRYPTED_KEY, null))*/key);

                key = null;
                if (!_username.isEmpty() || !_password.isEmpty()) {
                    input_email.setText(_username);
                    input_password.setText(_password);
                    lLCheckBox.setChecked(true);
                    lLCheckBox.setEnabled(true);
                    input_email.setBackgroundColor(getResources().getColor(R.color.edittext_remember_background));
                    input_password.setBackgroundColor(getResources().getColor(R.color.edittext_remember_background));
                Log.d("CHECK", "decrypted = " + input_email.getText().toString() );
                }
            }catch (GeneralSecurityException e) {
                Log.e(TAG, "GeneralSecurityException", e);
            } catch (UnsupportedEncodingException e) {
                Log.e(TAG, "UnsupportedEncodingException", e);
            } catch (NullPointerException e) {
                Log.e(TAG, "NullPointerException", e);
            }
        }

        //Re-enable after async and checks
        input_email.setEnabled(true);
        input_password.setEnabled(true);
    }

    private byte[] rsaEncrypt(byte[] secret) throws Exception{
        KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(KEY_ALIAS, null);
        // Encrypt the text
        Cipher inputCipher = Cipher.getInstance(RSA_MODE, "AndroidOpenSSL");
        inputCipher.init(Cipher.ENCRYPT_MODE, privateKeyEntry.getCertificate().getPublicKey());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, inputCipher);
        cipherOutputStream.write(secret);
        cipherOutputStream.close();

        byte[] vals = outputStream.toByteArray();
        return vals;
    }

    private  byte[]  rsaDecrypt(byte[] encrypted) throws Exception {
        KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry)keyStore.getEntry(KEY_ALIAS, null);
        Cipher output = Cipher.getInstance(RSA_MODE, "AndroidOpenSSL");
        output.init(Cipher.DECRYPT_MODE, privateKeyEntry.getPrivateKey());
        CipherInputStream cipherInputStream = new CipherInputStream(
                new ByteArrayInputStream(encrypted), output);
        ArrayList<Byte> values = new ArrayList<>();
        int nextByte;
        while ((nextByte = cipherInputStream.read()) != -1) {
            values.add((byte)nextByte);
        }

        byte[] bytes = new byte[values.size()];
        for(int i = 0; i < bytes.length; i++) {
            bytes[i] = values.get(i).byteValue();
        }
        return bytes;
    }

    private Key getSecretKey(Context context) throws Exception{
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        String enryptedKeyB64 = pref.getString(ENCRYPTED_KEY, null);
        // need to check null, omitted here
        byte[] encryptedKey = Base64.decode(enryptedKeyB64, Base64.DEFAULT);
        byte[] key = rsaDecrypt(encryptedKey);
        return new SecretKeySpec(key, "AES");
    }

    public String encrypt(Context context, byte[] input) throws Exception{
        Cipher c = Cipher.getInstance(AES_MODE, "BC");
        c.init(Cipher.ENCRYPT_MODE, getSecretKey(context));
        byte[] encodedBytes = c.doFinal(input);
        String encryptedBase64Encoded =  Base64.encodeToString(encodedBytes, Base64.DEFAULT);
        return encryptedBase64Encoded;
    }


    public byte[] decrypt(Context context, byte[] encrypted) throws Exception{
        Cipher c = Cipher.getInstance(AES_MODE, "BC");
        c.init(Cipher.DECRYPT_MODE, getSecretKey(context));
        byte[] decodedBytes = c.doFinal(encrypted);
        return decodedBytes;
    }



    public void createNewAccount(View view){
        str_input_username = null;
        str_input_password = null;
        Intent WebViewClientIntent = new Intent(getApplicationContext(), WebViewActivity.class);
        WebViewClientIntent.putExtra("mUsername", str_input_username);
        WebViewClientIntent.putExtra("mPassword", str_input_password);
        WebViewClientIntent.putExtra("mURL", getString(R.string.url_create_new_account));
        startActivity(WebViewClientIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ConnectivityClass.getInstance().setConnectivityListener(this);
    }

    //Connectivity status method
    private boolean checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        return isConnected;
    }

    private void displaySnack(View view, String _str){
        mSnackbar = Snackbar.make(view, _str, Snackbar.LENGTH_LONG);
        View sbView = mSnackbar.getView();
        TextView sbTextView = (TextView)sbView.findViewById(android.support.design.R.id.snackbar_text);
        sbTextView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.submit_request_background));
        sbTextView.setGravity(Gravity.CENTER_HORIZONTAL);
        mSnackbar.show();
    }

    /**
     * Callback trigger on network change
     */
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        checkConnection();
    }
}