package com.pro.empdep.account_screens;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.pro.empdep.R;
import com.pro.empdep.firebase.Credentials;
import com.pro.empdep.firebase.RandomPhotoUrlGenerator;
import com.pro.empdep.model.User;
import com.pro.empdep.screens.UserPreference;
import com.pro.empdep.viewmodel.UserViewModel;

import java.util.concurrent.TimeUnit;


public class LoginActivity extends AppCompatActivity {

    EditText phone_editText, otp_editText;
    Button login_btn, verify_otp;
    private String phone_number;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    LinearLayout authentication_layout, otp_layout;
    private String mVerificationId;
    PhoneAuthProvider.ForceResendingToken mResendToken;
    public static String TAG = "LOGIN-ACTIVITY";
    ProgressBar progressBar, verify_progressBar;
    RelativeLayout main_layout;
    TextView reSendOtp, otpSentTo;
    LottieAnimationView otp_animation, number_animation;
    UserViewModel userViewModel;
    String device = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        phone_editText = findViewById(R.id.phone_edit_text);
        authentication_layout = findViewById(R.id.login_input_number);
        otp_layout = findViewById(R.id.login_input_otp);
        login_btn = findViewById(R.id.auth_login);
        otp_editText = findViewById(R.id.phone_edit_text_auto);
        verify_otp = findViewById(R.id.auth_login_auto);
        progressBar = findViewById(R.id.progress_circular);
        main_layout = findViewById(R.id.main_layout);
        reSendOtp = findViewById(R.id.resend_code);
        otpSentTo = findViewById(R.id.otp_sent_number);
        verify_progressBar = findViewById(R.id.progress_circular_verify);
        otp_animation = findViewById(R.id.login_image_auto);
        number_animation = findViewById(R.id.login_image);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        progressBar.setVisibility(View.GONE);
        verify_progressBar.setVisibility(View.GONE);


        authentication_layout.setVisibility(View.VISIBLE);
        otp_layout.setVisibility(View.GONE);


        login_btn.setOnClickListener(view -> {
            hideKeyboard(this);
            number_animation.playAnimation();
            if (phone_editText.getText().toString().isEmpty()) {
                Snackbar snackbar = Snackbar
                        .make(main_layout, "Phone Number can't be empty", Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(ContextCompat.getColor(getApplicationContext(), R.color.red));
                snackbar.show();
                return;
            } else {
                progressBar.setVisibility(View.VISIBLE);
                login_btn.setVisibility(View.GONE);
                phone_number = "+91" + phone_editText.getText().toString();
                otpSentTo.setText(phone_number);
                verifyPhone(phone_number);
            }

        });

        reSendOtp.setOnClickListener(view -> {
            resendVerificationCode(phone_number, mResendToken);
        });

        verify_otp.setOnClickListener(view -> {
            hideKeyboard(LoginActivity.this);
            verify_progressBar.setVisibility(View.VISIBLE);
            verify_otp.setVisibility(View.GONE);
            if (otp_editText.getText().toString().isEmpty()) {
                Snackbar snackbar = Snackbar
                        .make(main_layout, "OTP can't be empty", Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(getResources().getColor(R.color.red));
                snackbar.show();
                verify_progressBar.setVisibility(View.GONE);
                verify_otp.setVisibility(View.VISIBLE);
                return;
            } else {
                String code = otp_editText.getText().toString();
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

                //sign-in the user manually
                signInWithPhoneAuthCredential(credential);
            }

        });


        //call backs for OTP event
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {

                //signInWithPhoneAuthCredential(credential);


            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Snackbar snackbar = Snackbar
                        .make(main_layout, "Something went wrong.", Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(ContextCompat.getColor(getApplicationContext(), R.color.red));
                snackbar.show();


                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    snackbar = Snackbar
                            .make(main_layout, "Something went wrong check the number", Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(ContextCompat.getColor(getApplicationContext(), R.color.red));
                    snackbar.show();

                } else if (e instanceof FirebaseTooManyRequestsException) {

                    snackbar = Snackbar
                            .make(main_layout, "Too many request from this number", Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(ContextCompat.getColor(getApplicationContext(), R.color.red));
                    snackbar.show();

                }

                authentication_layout.setVisibility(View.VISIBLE);
                otp_layout.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                login_btn.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                otp_editText.requestFocus();
                Snackbar snackbar = Snackbar
                        .make(main_layout, "OTP sent", Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(getResources().getColor(R.color.primary));
                snackbar.setTextColor(getResources().getColor(R.color.white));
                otp_animation.playAnimation();

                snackbar.show();
                authentication_layout.setVisibility(View.GONE);
                otp_layout.setVisibility(View.VISIBLE);


                // Saving verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
                hideKeyboard(LoginActivity.this);

            }
        };


    }

    private void verifyPhone(String phone_number) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(phone_number)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void resendVerificationCode(String phone_Number, PhoneAuthProvider.ForceResendingToken token) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(phone_Number)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .setForceResendingToken(token)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential: success");
                        boolean isNew = task.getResult().getAdditionalUserInfo().isNewUser();

                        Intent homeIntent = new Intent(LoginActivity.this, HomeActivity.class);
                        if (isNew) {
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            String photoUrl = "https://avatars.dicebear.com/api/bottts/" + (new RandomPhotoUrlGenerator().randomUsernameExtension()) + ".png?colors=teal";
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName("")
                                    .setPhotoUri(Uri.parse(photoUrl))
                                    .build();

                            firebaseUser.updateProfile(profileUpdates).addOnCompleteListener(taskUpdate -> {
                                if (taskUpdate.isSuccessful()) {
                                    Log.d(TAG, "User profile updated.");
                                }
                            });

                        } else {

                            FirebaseMessaging.getInstance().getToken().addOnCompleteListener(deviceTask -> {
                                if (deviceTask.isSuccessful()) {
                                    device = deviceTask.getResult();
                                } else {
                                    device = "";
                                }
                                Log.d("DEVICE-TOKEN", "onCreateView: " + device);
                            }).addOnCompleteListener(task1 -> {
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                db.collection(Credentials.USER).document(FirebaseAuth.getInstance().getCurrentUser().getUid()).update("device", device);
                            });
                        }

                        UserPreference userPreference = new UserPreference(this,homeIntent);
                        userPreference.showUserPref();


                    } else {
                        // Sign in failed, display a message and update the UI
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid
                            Snackbar snackbar = Snackbar
                                    .make(main_layout, "Invalid OTP.", Snackbar.LENGTH_SHORT);
                            snackbar.setBackgroundTint(getResources().getColor(R.color.red));
                            snackbar.show();
                            verify_progressBar.setVisibility(View.GONE);
                            verify_otp.setVisibility(View.VISIBLE);
                            otp_editText.setText("");
                            authentication_layout.setVisibility(View.VISIBLE);
                            otp_layout.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                            login_btn.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);


    }

}



