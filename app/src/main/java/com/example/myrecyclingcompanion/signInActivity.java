package com.example.myrecyclingcompanion;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import java.util.concurrent.TimeUnit;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

public class signInActivity extends AppCompatActivity {

    int phone_number;
    ConstraintLayout verify_grp, sign_in_grp;
    EditText phone_number_text, code_text;
    ImageButton verify_btn, sign_in_btn;
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        Window w = getWindow();
        w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        w.setStatusBarColor(Color.rgb(77,160,97));
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        verify_grp = findViewById(R.id.verify_grp);
        sign_in_grp = findViewById(R.id.sign_in_grp);
        phone_number_text = findViewById(R.id.editTextPhone);
        code_text = findViewById(R.id.editTextCode);
        verify_btn = findViewById(R.id.verify_btn);
        sign_in_btn = findViewById(R.id.Sign_In_btn);

        verify_grp.setVisibility(View.VISIBLE);
        sign_in_grp.setVisibility(View.INVISIBLE);

        verify_btn.setOnClickListener(view -> {
            Log.d("tag", "clicked");
            if(phone_number_text.getText().toString().length() == 8){
                verify_code();
                if (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this)
                        == ConnectionResult.SUCCESS) {
                    // The SafetyNet Attestation API is available.
                    byte[] nonce = hexStringToByteArray("e84fd020ea3a6910a2d808002b30309d");
                    SafetyNet.getClient(this).attest(nonce, "AIzaSyA-8KG4hP-XqQkQ1cD1hOU-DRzmpcCP8eI")
                            .addOnSuccessListener(this,
                                    new OnSuccessListener<SafetyNetApi.AttestationResponse>() {
                                        @Override
                                        public void onSuccess(SafetyNetApi.AttestationResponse response) {
                                            // Indicates communication with the service was successful.
                                            // Use response.getJwsResult() to get the result data.
                                            mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                                                @Override
                                                public void onVerificationCompleted(PhoneAuthCredential credential) {
                                                    // This callback will be invoked in two situations:
                                                    // 1 - Instant verification. In some cases the phone number can be instantly
                                                    //     verified without needing to send or enter a verification code.
                                                    // 2 - Auto-retrieval. On some devices Google Play services can automatically
                                                    //     detect the incoming verification SMS and perform verification without
                                                    //     user action.
                                                    Log.d("tag", "onVerificationCompleted:" + credential);
                                                    go_home();
                                                    //signInWithPhoneAuthCredential(credential);
                                                }

                                                @Override
                                                public void onVerificationFailed(FirebaseException e) {
                                                    // This callback is invoked in an invalid request for verification is made,
                                                    // for instance if the the phone number format is not valid.
                                                    Log.w("tag", "onVerificationFailed", e);

                                                    if (e instanceof FirebaseAuthInvalidCredentialsException) {
                                                        // Invalid request
                                                    } else if (e instanceof FirebaseTooManyRequestsException) {
                                                        // The SMS quota for the project has been exceeded
                                                    }

                                                    // Show a message and update the UI
                                                }

                                                @Override
                                                public void onCodeSent(@NonNull String verificationId,
                                                                       @NonNull PhoneAuthProvider.ForceResendingToken token) {
                                                    // The SMS verification code has been sent to the provided phone number, we
                                                    // now need to ask the user to enter the code and then construct a credential
                                                    // by combining the code with a verification ID.
                                                    Log.d("tag", "onCodeSent:" + verificationId);

                                                    // Save verification ID and resending token so we can use them later
                                                    mVerificationId = verificationId;
                                                    mResendToken = token;

                                                }
                                            };
                                            PhoneAuthOptions options =
                                                    PhoneAuthOptions.newBuilder(mAuth)
                                                            .setPhoneNumber("+216"+phone_number_text.getText().toString())       // Phone number to verify
                                                            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                                            .setActivity(signInActivity.this)                 // Activity (for callback binding)
                                                            .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                                                            .build();
                                            PhoneAuthProvider.verifyPhoneNumber(options);
                                            mAuth.setLanguageCode("fr");


                                        }
                                    })
                            .addOnFailureListener(this, new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // An error occurred while communicating with the service.
                                    if (e instanceof ApiException) {
                                        // An error with the Google Play services API contains some
                                        // additional details.
                                        ApiException apiException = (ApiException) e;
                                        // You can retrieve the status code using the
                                        // apiException.getStatusCode() method.
                                    } else {
                                        // A different, unknown type of error occurred.
                                        Log.d("tag", "Error: " + e.getMessage());
                                    }
                                }
                            });
                } else {
                    // Prompt user to update Google Play services.
                    Log.d("tag","update");
                }
                //startActivity(new Intent(signInActivity.this, HomeActivity.class));
            }
        });

        sign_in_btn.setOnClickListener(view -> {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code_text.getText().toString());
            signInWithPhoneAuthCredential(credential);
        });


    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("tag", "signInWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();
                            go_home();
                            // Update UI
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w("tag", "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(signInActivity.this, "Check the Code", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    public void verify_code(){
        verify_grp.setVisibility(View.INVISIBLE);
        sign_in_grp.setVisibility(View.VISIBLE);
    }

    public void go_home(){
        startActivity(new Intent(signInActivity.this, HomeActivity.class));
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
}
