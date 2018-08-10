package com.acquire.shopick.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.acquire.shopick.R;
import com.acquire.shopick.ShopickApplication;
import com.acquire.shopick.bus.BusProvider;
import com.acquire.shopick.event.CreateUserEvent;
import com.acquire.shopick.event.StartLogin;
import com.acquire.shopick.event.UserCreatedEvent;
import com.acquire.shopick.io.model.Intro;
import com.acquire.shopick.ui.widget.ProgressView;
import com.acquire.shopick.util.AccountUtils;
import com.acquire.shopick.util.AnalyticsManager;
import com.acquire.shopick.util.DispatchIntentUtils;
import com.acquire.shopick.util.SnackbarUtil;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.path.android.jobqueue.JobManager;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.acquire.shopick.util.LogUtils.LOGD;
import static com.acquire.shopick.util.LogUtils.makeLogTag;

/**
 * Created by gaurav on 9/24/15.
 */
public class LoginActivity extends CoreActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 9049;
    private static String TAG = makeLogTag(LoginActivity.class);

    private boolean opening;

    @Bind(R.id.sign_in_button) SignInButton sign_in_button;
    @Bind(R.id.intro_pager)   ViewPager mViewPager;
    @Bind(R.id.intro_screens) CirclePageIndicator mPageIndicator;
    @Bind(R.id.progress_login) ProgressView mProgressView;

    @Bind(R.id.login_button) LoginButton loginButton;

    @Bind(R.id.skip_button)
    TextView skipButton;

    private CallbackManager callbackManager;
    private IntroScreenAdapter introScreenAdapter;
    private GoogleApiClient mGoogleApiClient;
    @Inject
    JobManager jobManager;

    Bus mBus;

    public static String SHOPICK_LOGIN_OPENING = "com.acquire.shopick.android.login_opening_intent";
    /**
     * When creating, retrieve this instance's number from its arguments.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.login_screen_layout);
        super.onCreate(savedInstanceState);
        loginButton.setReadPermissions("email");


        // If using in a fragment
        // Other app specific specialization
        callbackManager = CallbackManager.Factory.create();

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                SnackbarUtil.showMessage(mViewPager, "Login Successful! try again.");

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.i("LoginActivity", response.toString());
                        // Get facebook data from login
                        startWaitDontClose();
                        Bundle bFacebookData = getFacebookData(object,  loginResult.getAccessToken().getToken());


                    }
            });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id, first_name, last_name, email, age_range,  gender, location"); // Parámetros que pedimos a facebook
            request.setParameters(parameters);
            request.executeAsync();
            }

            @Override
            public void onCancel() {
                // App codestart
                startWait();
                SnackbarUtil.showMessage(mViewPager, "Login Cancelled! try again.");

            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                startWait();
                SnackbarUtil.showMessage(mViewPager, "Login Failed! try again.");

            }
        });

        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(new Scope(Scopes.PLUS_LOGIN))
                .requestId()
                .requestIdToken(getString(R.string.server_client_id))
                .requestProfile()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addApi(Plus.API)
                .build();

        sign_in_button.setSize(SignInButton.SIZE_STANDARD);
        sign_in_button.setScopes(gso.getScopeArray());


        ArrayList<Intro> intro_list =  new ArrayList<Intro>();
        Intro item =  new Intro(1,"Shopick","Your shopping companion",R.drawable.placeholder);
        Intro item1 =  new Intro(2,"Find this product","Ask us to locate the product; size Availability, color availability or just about anything at your nearest store for brand!",R.drawable.offer_meta_posts);
        Intro item2 =  new Intro(3,"Post your purchase at shopick to earn picks ","Post your recent purchase and offer you spot at brands to earn Pick and reeedem them for real cash!",R.drawable.offer_meta_posts);
        Intro item3 =  new Intro(4,"Reedem your Picks "," You can reedem picks for real cash or shopping vouchers at your favourite brands ",R.drawable.offer_meta_posts);


        intro_list.add(item);
        intro_list.add(item1);
        intro_list.add(item2);
        intro_list.add(item3);


        introScreenAdapter =  new IntroScreenAdapter(getApplicationContext(), getFragmentManager());
        introScreenAdapter.mItems.addAll(intro_list);
        mViewPager.setAdapter(introScreenAdapter);
        mPageIndicator.setViewPager(mViewPager);
        AnalyticsManager.sendEvent(TAG, "Showing login screen" , "category", 0L);
    }




    private Bundle getFacebookData(JSONObject object, String authToken) {

        try {
            Bundle bundle = new Bundle();
            String id = object.getString("id");

            try {
                String profile_url = "https://graph.facebook.com/" + id + "/picture?width=200&height=150";
                URL profile_pic = new URL(profile_url);
                Log.i("profile_pic", profile_pic + "");
                bundle.putString("profile_pic", profile_pic.toString());
                AccountUtils.setPlusImageUrl(getApplicationContext(),
                        AccountUtils.getActiveAccountName(getApplicationContext()),
                        profile_url
                        );


            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }

            bundle.putString("idFacebook", id);

            if (object.has("email")) {
                AccountUtils.setActiveAccount(getApplicationContext(), object.getString("email"));
            }
            AccountUtils.setFacebookProfileId(getApplicationContext(), AccountUtils.getActiveAccountName(getApplicationContext()), id);
            AccountUtils.setAuthToken(getApplicationContext(),
                    AccountUtils.getActiveAccountName(getApplicationContext()),
                    authToken);

            if (object.has("first_name"))
                AccountUtils.setPlusName(getApplicationContext(),
                        AccountUtils.getActiveAccountName(getApplicationContext()),
                        object.getString("first_name") +" " +object.getString("last_name"));
            if (object.has("gender"))
                AccountUtils.setPlusGender(getApplicationContext(),
                        AccountUtils.getActiveAccountName(getApplicationContext()),
                        object.getString("gender").equals("male") ? 0 : 1
                        );
            if (object.has("birthday"))
                bundle.putString("birthday", object.getString("birthday"));
            if (object.has("location"))
                bundle.putString("location", object.getJSONObject("location").getString("name"));
            if (object.has("email")) {
                String email = object.getString("email");
                sendCreateuserEvent(email,"facebook");
            }
            startActivity(DispatchIntentUtils.dispatchLikedBrandntent(getApplicationContext()));
            return bundle;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void sendCreateuserEvent(String email, String loginType) {
        BusProvider.getInstance().post(new CreateUserEvent(AccountUtils.getAuthToken(getApplicationContext()),
                AccountUtils.getActiveAccountName(getApplicationContext()),
                AccountUtils.getPlusName(getApplicationContext()),
                AccountUtils.getPlusImageUrl(getApplicationContext()),
                AccountUtils.getPlusCoverUrl(getApplicationContext()),
                AccountUtils.getPlusGender(getApplicationContext(), email),
                AccountUtils.getPlusAgeMax(getApplicationContext(), email),
                AccountUtils.getPlusAgeMin(getApplicationContext(), email),
                AccountUtils.getFacebookProfileId(getApplicationContext()),
                        loginType)
                );
    }

    @OnClick(R.id.sign_in_button)
    public void onSignInClick(View pick) {
       // startLoginProcess();
        BusProvider.getInstance().post(new StartLogin());
       // jobManager.addJob(new GoogleLoginJob(this));
        AccountUtils.setLoginEnabled(getApplicationContext());
       // startWait();
       // finish();
        startWaitDontClose();
        signIn();
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }




    public void startWait() {
        sign_in_button.setVisibility(View.GONE);
        loginButton.setVisibility(View.GONE);
        mProgressView.resetAnimation();
        mProgressView.setVisibility(View.VISIBLE);
        Handler handler = new Handler();
        handler.postDelayed(runnable, 2000);
    }

    public void startWaitDontClose() {
        sign_in_button.setVisibility(View.GONE);
        loginButton.setVisibility(View.GONE);
        mProgressView.resetAnimation();
        mProgressView.setVisibility(View.VISIBLE);

    }

    Runnable runnable = new Runnable() {
        public void run () {
            // Do your stuff here
            finish();
        }
    };

    @Override
    public   void onResume() {
        super.onResume();
        if(AccountUtils.getLoginDone(getApplicationContext())) {
            startWait();
            SnackbarUtil.showMessage(mViewPager, "You have already loggedin!! Closing Activity");
        }
        mBus = BusProvider.getInstance();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }

    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess() + "  " + result.getStatus().getStatusMessage());

        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            AccountUtils.setActiveAccount(getApplicationContext(), acct.getEmail());
            String accountName = AccountUtils.getActiveAccountName(getApplicationContext());

            if (mGoogleApiClient.hasConnectedApi(Plus.API)) {
                Person person = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
                AccountUtils.setPlusGender(getApplicationContext(), accountName, person.getGender());
                if (person.getAgeRange() != null) {
                    AccountUtils.setPlusAgeMAX(getApplicationContext(), accountName, person.getAgeRange().getMax());
                    AccountUtils.setPlusAgeMIN(getApplicationContext(), accountName, person.getAgeRange().getMin());
                }
            }
            if (acct.getPhotoUrl() != null) {
                AccountUtils.setPlusImageUrl(getApplicationContext(), accountName, acct.getPhotoUrl().toString());
            }
            AccountUtils.setPlusName(getApplicationContext(), accountName, acct.getDisplayName());
            AccountUtils.setFacebookProfileId(getApplicationContext(), accountName, acct.getId());
            AccountUtils.setAuthToken(getApplicationContext(), accountName, acct.getIdToken());
            sendCreateuserEvent(accountName, "google");
            LOGD(TAG, "Login done!! google " + acct.getEmail());
        } else {
            // Signed out, show unauthenticated UI.
            startWait();
            LOGD(TAG, "Login done!! google unauthenticated " );
        }
    }

    @Subscribe
    public void onUserCreatedEvent(UserCreatedEvent event) {
        startActivity(DispatchIntentUtils.dispatchLikedBrandntent(getApplicationContext()));
        finish();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @OnClick(R.id.skip_button)
    public void OnClickSkip() {
        supportFinishAfterTransition();
    }
}
