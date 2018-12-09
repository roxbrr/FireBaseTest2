package app.calcounter.com.firebasetest2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static final String AUTHOR_KEY = "author";
    public static final String QUOTE_KEY = "quote";

    private static final int RC_SIGN_IN = 123;

    List<AuthUI.IdpConfig> providers;

    TextView myQuoteTextView;
    TextView textViewQuoteDisplay;

    private DocumentReference mDocRef = FirebaseFirestore.getInstance().collection("sampleData").document("inspiration");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewQuoteDisplay = (TextView) findViewById(R.id.textViewQuoteDisplay);

        providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        FirebaseAuth auth = FirebaseAuth.getInstance(); // this go into each intent?
        if(auth.getCurrentUser() != null)
        {
            // already signed in
        }
        else
        {
            // AuthUI is built in but does not exist yet?
            startActivityForResult(
                    // Get an instance of AuthUI based on the default app
                    AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers).build(),
                    // add these urls for terms of service etc
                    //.setTosAndPrivacyPolicyUrls(
                    //                        "https://example.com/terms.html",
                    //                        "https://example.com/privacy.html")
                    RC_SIGN_IN);
        }
    }

    @Override // all has to be on start
    protected  void onStart(){
        super.onStart();
        // this is the event listener and this will crash it if not detached?
        // this will grab the data when it updates
        // need to turn off this listener

        // this is a different from that if you pass the activity and the activity stops you can
        // stop this listener

        // this is for the metadata
        // this uses more data?

        //DocumentListenOptions verboseOptions = new DocumentListenOptions();
        //verboseOptions.includeMetadataChanges();

                                                    // add verbose options here if you want that included
        mDocRef.addSnapshotListener(this,new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent( DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                if(documentSnapshot.exists())
                {
                    String quoteText = documentSnapshot.getString(QUOTE_KEY);
                    String authorText = documentSnapshot.getString(AUTHOR_KEY);
                    textViewQuoteDisplay.setText(" " + quoteText + " " + " -- " + authorText);
                }else if(e != null)
                {
                    Log.w("document update issue", e);
                }
            }
        });

    }



    public void fetchQuote(View view)
    {
        mDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists())
                {
                    String quoteText = documentSnapshot.getString(QUOTE_KEY);
                    String authorText = documentSnapshot.getString(AUTHOR_KEY);
                    textViewQuoteDisplay.setText(" " + quoteText + " " + " -- " + authorText);

                    //Map<String,Object> myData = documentSnapshot.getData();

                    // money in the bank
                    InspiringQuote myQuote = documentSnapshot.toObject(InspiringQuote.class);
                }
            }
        });

    }

    public void onNext(View view)
    {
        Intent newIntent = new Intent(MainActivity.this, TestNextActivity.class);
        startActivity(newIntent);
        finish();
    }



    public void onClick(View view) {
        EditText quoteView = (EditText) findViewById(R.id.editTextQuote);
        EditText authorView = (EditText) findViewById(R.id.editText2);

        String quoteText = quoteView.getText().toString();
        String authorText = authorView.getText().toString();

        if(quoteText.isEmpty() || authorText.isEmpty()) {return;} // odd statement but works

        Map<String,Object> dataToSave = new HashMap<String,Object>();
        dataToSave.put(QUOTE_KEY,quoteText);
        dataToSave.put(AUTHOR_KEY, authorText);

        mDocRef.set((dataToSave)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("InspiringQuote", "Document has been saved!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("InspiringQuote", "didn't save document");
            }
        });

    }

}
