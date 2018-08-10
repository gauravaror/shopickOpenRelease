package com.acquire.shopick.ui;

import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.acquire.shopick.R;
import com.acquire.shopick.bus.BusProvider;
import com.acquire.shopick.event.CreatePostEvent;
import com.acquire.shopick.event.NewPostAddedEvent;
import com.acquire.shopick.io.model.SearchResult;
import com.acquire.shopick.io.network.ShopickApi;
import com.acquire.shopick.job.ShopickPostJob;
import com.acquire.shopick.util.AccountUtils;
import com.acquire.shopick.util.AnalyticsEvent;
import com.acquire.shopick.util.AnalyticsManager;
import com.acquire.shopick.util.DispatchIntentUtils;
import com.acquire.shopick.util.FileUtils;
import com.acquire.shopick.util.ImageUtil;
import com.acquire.shopick.util.SnackbarUtil;
import com.lyft.android.scissors.CropView;
import com.path.android.jobqueue.JobManager;
import com.squareup.otto.Subscribe;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

import static com.acquire.shopick.util.LogUtils.LOGD;
import static com.acquire.shopick.util.LogUtils.makeLogTag;

/**
 * Created by gaurav on 10/5/15.
 */
public class PostFeedItem extends CoreActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = makeLogTag(PostFeedItem.class);
    private static final String SCREEN_LABEL = "PostFeedItem";

    private String category;
    private Long categoryId = -1L;
    private int post_type = -1;

    private boolean cropView = true;

    private String imageUrl;
    private Bitmap imageFeed;
    private boolean isGalleryPick;
    private Uri galleryPick;

    @Bind(R.id.switch_mode)
    public Button switchMode;

    private Long placeId = -1L;
    private String placeIndex ;

    private String postCollectionTitle = "";
    private Long postCollectionId = -1L;


    @Bind(R.id.photo_feed_view)
    public ImageView mImageView;


    @Bind(R.id.post_feed_gallery)
    public Button postFeedGallery;

    @Bind(R.id.post_feed_camera)
    public Button postFeedCamera;

    @Bind(R.id.crop_feed_view)
    public CropView mCropView;

   // @Bind(R.id.post_feed_edit_text)
    //public EditText mEditText;

    @Bind(R.id.post_feed_layout_screen1)
    LinearLayout screen1;

    @Bind(R.id.post_feed_layout_screen2)
    RelativeLayout screen2;

    @Bind(R.id.post_feed_layout_screen3)
    LinearLayout screen3;

    @Bind(R.id.post_feed_item)
    public Button postButton;

    @Bind(R.id.feed_item_next)
    public Button nextButton;

    @Bind(R.id.post_feed_edit_text_location)
    public AutoCompleteTextView locationAutoComplete;

    @Bind(R.id.post_feed_edit_text_category)
    public AutoCompleteTextView typeAutoComplete;

    @Bind(R.id.post_feed_edit_text_discount)
    public AutoCompleteTextView postCollectionAutoComplete;

    @Bind(R.id.offer_or_not)
    public CheckBox offer_or_not;

    @Bind(R.id.discount_pricerange_spinner)
    public Spinner discount_option_spinner;

    String description;


    ShopickApi.SearchService service;


    private Context activityContext = this;

    private SearchResultAdapter locationAdapter;

    private SearchResultAdapter typeAdapter;

    private SearchResultAdapter postCollectionAdapter;

    private int flag;

    private static final int  dimension = 1000;
    public static String EXTRA_CATEGORY_ID = "com.acquire.shopick.ui.PostFeedItem.category_id";
    public static String EXTRA_CATEGORY_NAME = "com.acquire.shopick.ui.PostFeedItem.category_name";
    public static String EXTRA_STORE_ID = "com.acquire.shopick.ui.PostFeedItem.store_id";
    public static String EXTRA_STORE_NAME = "com.acquire.shopick.ui.PostFeedItem.store_name";

    static final int REQUEST_TAKE_PHOTO = 1908;
    static final int REQUEST_PICK_IMAGE = 2008;
    static final int REQUEST_CATEGORY_PICKER = 3008;
    static final int REQUEST_LOCATION_PICKER = 4008;


    @Inject
    JobManager jobManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_post_feed);
        super.onCreate(savedInstanceState);
            if (!AccountUtils.getLoginDone(getApplicationContext())) {
                SnackbarUtil.createStartingLogin(screen1);
                startActivity(DispatchIntentUtils.dispatchLoginIntent(getApplicationContext(), true));
            }


        Intent intent = getIntent();
        if (intent != null) {
            post_type = intent.getIntExtra(MainFeedFragment.POST_REFERENCE_FLAG, -1);
        }


        //Set the back button.
        Toolbar toolbar = getActionBarToolbar();
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_18dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnalyticsManager.sendEvent("Post", "Discarded item", "DiscardedPost", 0L);
                supportFinishAfterTransition();
            }
        });
        List<SearchResult> list = new ArrayList<SearchResult>();
        SnackbarUtil.showYouEarnPicks(screen1, getApplicationContext());
        typeAutoComplete.setTag(0);
        locationAutoComplete.setTag(1);
        View.OnKeyListener onKeyListener = new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == 66) {
                    if ((int)v.getTag() == 0) {
                        locationAutoComplete.requestFocus();
                    } else if ((int)v.getTag() == 1) {
                        discount_option_spinner.requestFocus();
                    }
                }
                return false;
            }
        };
        locationAdapter = new SearchResultAdapter(getApplicationContext(), R.layout.activity_shopick, R.id.type, 0);
        typeAdapter = new SearchResultAdapter(getApplicationContext(), R.layout.activity_shopick, R.id.type, 1);
        postCollectionAdapter = new SearchResultAdapter(getApplicationContext(), R.layout.activity_shopick, R.id.type, 2);
        locationAutoComplete.setAdapter(locationAdapter);
        locationAutoComplete.setOnKeyListener(onKeyListener);
        locationAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SearchResult result = locationAdapter.getItem(position);
                placeId = result._source.id;
                placeIndex = result._index;
                discount_option_spinner.requestFocus();
            }
        });
        typeAutoComplete.setAdapter(typeAdapter);
        typeAutoComplete.setOnKeyListener(onKeyListener);
        typeAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SearchResult result = typeAdapter.getItem(position);
                if (result._index.equalsIgnoreCase("categories")) {
                    categoryId = result._source.id;
                    category = result._source.name;
                    locationAutoComplete.requestFocus();
                }

            }
        });
        postCollectionAutoComplete.setAdapter(postCollectionAdapter);
        postCollectionAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SearchResult result = postCollectionAdapter.getItem(position);
                if (result._index.equalsIgnoreCase("post_collections")) {
                    postCollectionId = result._source.id;
                    postCollectionTitle = result._source.title;

                }

            }
        });
        postCollectionAutoComplete.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == 66) {
                    dialogPostItBox();
                }
                return false;
            }
        });

        if (post_type == 2) {
            offer_or_not.setChecked(true);
        }
        offer_or_not.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    post_type = 2;
                } else {
                    post_type = 1;
                }
            }
        });
        //Set other details.

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.price_range_discount, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        discount_option_spinner.setAdapter(adapter);
        discount_option_spinner.setOnItemSelectedListener(this);
        toolbar.setTitle("Post to Shopick");
        toolbar.setVisibility(View.VISIBLE);
        AnalyticsManager.sendScreenView(SCREEN_LABEL);
    }


    @OnClick({R.id.post_feed_camera, R.id.post_feed_camera_screen_2})
    public void onCamera() {
        AnalyticsManager.sendEvent("POSTING","POST_FEED_ITEM","CAMERA");
        dispatchTakePictureFileIntent();
    }

    @OnClick({R.id.post_feed_gallery, R.id.post_feed_gallery_screen_2})
    public void onGallery() {
        AnalyticsManager.sendEvent("POSTING","POST_FEED_ITEM","GALLERY");
        dispatchPickImageIntent();
    }

    @OnClick(R.id.switch_mode)
    public void switchMode() {
        AnalyticsManager.sendEvent("POSTING","POST_FEED_ITEM","SWITCH_MODE");
        if (cropView) {
            cropView = false;
            mCropView.setVisibility(View.GONE);
            mImageView.setVisibility(View.VISIBLE);
            if (mCurrentPhotouri != null) {
                mCropView.setImageBitmap(imageFile);
                mImageView.setImageBitmap(imageFile);
            }
        } else {
            cropView = true;
            mCropView.setVisibility(View.VISIBLE);
            mImageView.setVisibility(View.GONE);
            if (mCurrentPhotouri != null) {
                mCropView.setImageBitmap(imageFile);
                mImageView.setImageBitmap(imageFile);
            }
        }
    }

    @OnClick(R.id.feed_item_next)
    public void nextItem() {
        if (screen1.getVisibility() == View.VISIBLE) {
            screen2.setVisibility(View.VISIBLE);
            screen1.setVisibility(View.GONE);
            AnalyticsManager.sendEvent("NEXT","POST_FEED_ITEM","FIRST_SCREEN");
        } else if (screen2.getVisibility() == View.VISIBLE) {
            screen3.setVisibility(View.VISIBLE);
            screen2.setVisibility(View.GONE);
            nextButton.setVisibility(View.GONE);
            postButton.setVisibility(View.VISIBLE);
            AnalyticsManager.sendEvent("NEXT","POST_FEED_ITEM","SECOND_SCREEN");
            Toolbar toolbar = getActionBarToolbar();
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    screen3.setVisibility(View.GONE);
                    screen2.setVisibility(View.VISIBLE);
                    nextButton.setVisibility(View.VISIBLE);
                    AnalyticsManager.sendEvent("BACK","POST_FEED_ITEM","WENT_BACK");
                    postButton.setVisibility(View.GONE);
                    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AnalyticsManager.sendEvent("Post", "Discarded item", "DiscardedPost", 0L);
                            AnalyticsManager.sendEvent("BACK","POST_FEED_ITEM","CLOSED_AFTER");
                            supportFinishAfterTransition();
                        }
                    });
                }
            });

        } else if (screen3.getVisibility() == View.VISIBLE) {
            AnalyticsManager.sendEvent("NEXT","POST_FEED_ITEM","THIRED_SCREEN");
            screen3.setVisibility(View.VISIBLE);
            screen2.setVisibility(View.GONE);

        }
    }


    @OnClick(R.id.post_feed_item)
    public void postItem() {
        AnalyticsManager.sendEvent("POSTING","POST_FEED_ITEM","STARTED");
        AnalyticsManager.sendEvent("Post", "Post item", "ClickedPost", 0L);
        CreatePostEvent event = new CreatePostEvent();
        File croppedFile = null;
        if (mCurrentPhotouri != null) {
            if (cropView) {
                try {
                    LOGD(TAG, "creating photo activity");
                    croppedFile = createImageFile();
                    LOGD(TAG, croppedFile.toString() + "photo file");
                } catch (IOException ex) {
                    // Error occurred while creating the File
                }
                // Continue only if the File was successfully created
                if (croppedFile != null) {
                    LOGD(TAG, "Dispatching intent");
                    mCropView.extensions().crop().format(Bitmap.CompressFormat.JPEG).into(croppedFile);
                    event.setImage(croppedFile);
                }
            } else {
                croppedFile = FileUtils.getFile(getApplicationContext(), mCurrentPhotouri);
                event.setImage(croppedFile);
            }
            AnalyticsEvent.postEvenPosted(getApplicationContext(), AccountUtils.getShopickTempProfileId(getApplicationContext()), post_type, placeId, categoryId, 1);
        } else {
            AnalyticsEvent.postEvenPosted(getApplicationContext(), AccountUtils.getShopickTempProfileId(getApplicationContext()), post_type, placeId, categoryId, 0);

        }
        event.setUser_id(AccountUtils.getShopickProfileId(getApplicationContext()));
        if (AccountUtils.getLoginDone(getApplicationContext())) {
            event.setUser_id(AccountUtils.getShopickProfileId(getApplicationContext()));
        } else {
            //SnackbarUtil.createStartingLogin(screen1);
            //DispatchIntentUtils.startActivityWithCondition(this, DispatchIntentUtils.dispatchLoginIntent(getApplicationContext(), true));
            //return;
        }
        event.setType(post_type);
        event.setCategory_id(categoryId);
        event.setStore_id(placeId);
        String text = description;
        if (discount_option_spinner.getVisibility() == View.GONE) {
            text = postCollectionAutoComplete.getText().toString();
        }

        if (text.isEmpty() && croppedFile == null) {
            Snackbar snackbar = Snackbar
                    .make(screen1, "No Content to post!!", Snackbar.LENGTH_SHORT);

            snackbar.show();

            return;
        } else {
            event.setDescription(text);
        }
        BusProvider.getInstance().post(event);
        if (mFileUris.size() == 0) {
            jobManager.addJobInBackground(new ShopickPostJob("", new Long(placeId), placeIndex,
                    new Long(categoryId), new Long(event.getUser_id()), post_type, postCollectionId, text, mCurrentPhotouri == null ? null : mCurrentPhotouri.toString(), croppedFile));
        } else {
            for (int i=0 ; i < mFileUris.size(); i++) {
                jobManager.addJobInBackground(new ShopickPostJob("", new Long(placeId), placeIndex,
                        new Long(categoryId), new Long(event.getUser_id()), post_type, postCollectionId, text, mCurrentPhotouri == null ? null : mCurrentPhotouri.toString(), mFileUris.get(i)));
            }

        }
        postButton.setActivated(false);
        // finish();
    }


    public Bitmap decodeUri(Uri uri, int reqWidth, int reqHeight) throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(getContentResolver().openInputStream(uri)
                , null, o);
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = ImageUtil.calculateInSampleSize(o, reqWidth, reqHeight);
        return BitmapFactory.decodeStream(
                getContentResolver().openInputStream(uri), null, o2);
    }


    Bitmap imageFile;
    ArrayList<File> mFileUris = new ArrayList<File>();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivity Result " + requestCode + "  " + resultCode + "  ");
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {

            try {
                imageFile = decodeUri(mCurrentPhotouri, dimension, dimension);
                mImageView.setImageBitmap(imageFile);
                mCropView.setImageBitmap(imageFile);
                screen1.setVisibility(View.GONE);
                screen2.setVisibility(View.VISIBLE);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                galleryAddPic();
                setPic();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mImageView.setVisibility(View.VISIBLE);

        } else if (requestCode == REQUEST_PICK_IMAGE && resultCode == RESULT_OK && data != null ) {
            try {
                if (data.getData() != null) {
                    Uri uri = data.getData();
                    String path = FileUtils.getPath(getApplicationContext(), uri);
                    LOGD(TAG, " file path : " + path);
                    mCurrentPhotouri = uri;
                    mCurrentPhoto = FileUtils.getFile(getApplicationContext(), uri);
                    Bitmap bitmap = decodeUri(uri, dimension, dimension);
                    imageFeed = bitmap;
                    imageFile = bitmap;
                    mCropView.setImageBitmap(bitmap);
                    mImageView.setImageBitmap(imageFile);
                    screen1.setVisibility(View.GONE);
                    screen2.setVisibility(View.VISIBLE);
                    mCurrentPhotouri = uri;
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    if (data.getClipData() != null) {
                            ClipData mClipData = data.getClipData();
                            for (int i = 0; i < mClipData.getItemCount(); i++) {

                                ClipData.Item item = mClipData.getItemAt(i);
                                Uri uri = item.getUri();
                                mFileUris.add(FileUtils.getFile(getApplicationContext(), uri));
                            }
                            Log.v("LOG_TAG", "Selected Images" + mFileUris.size());
                    }
                }

            } catch (IOException e) {
                Log.e(TAG, e.toString());
            }

        }

    }

    String mCurrentPhotoPath;
    File mCurrentPhoto;
    Uri mCurrentPhotouri = null;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        mCurrentPhoto = image;
        mCurrentPhotouri = Uri.fromFile(mCurrentPhoto);
        LOGD(TAG, "creating photo activity  image" + image.getAbsolutePath());

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        LOGD(TAG, "creating photo activity  image" + image.getAbsolutePath());
        LOGD(TAG, "creating photo activity  image" + mCurrentPhotoPath);

        return image;
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {

        if (mCurrentPhotouri != null)
            savedInstanceState.putString("camera_image", mCurrentPhotouri.toString());
        if (categoryId != -1)
            savedInstanceState.putLong("category_id", categoryId);

        if (placeId != -1)
            savedInstanceState.putLong("place_id", placeId);

        if (!TextUtils.isEmpty(placeIndex))
            savedInstanceState.putString("place_index", placeIndex);

        if (post_type != -1)
            savedInstanceState.putInt("post_type", post_type);

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("camera_image")) {
                mCurrentPhotouri = Uri.parse(savedInstanceState.getString("camera_image"));
            }

            if (savedInstanceState.containsKey("category_id")) {
                categoryId = savedInstanceState.getLong("category_id");
            }

            if (savedInstanceState.containsKey("place_id")) {
                placeId = savedInstanceState.getLong("place_id");
            }
            if (savedInstanceState.containsKey("place_index")) {
                placeIndex = savedInstanceState.getString("place_index");
            }
            if (savedInstanceState.containsKey("post_type")) {
                post_type = savedInstanceState.getInt("post_type");
                if (post_type == 2) {
                    offer_or_not.setChecked(true);
                }
            }

        }

        super.onRestoreInstanceState(savedInstanceState);
    }


    private void dispatchTakePictureFileIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        LOGD(TAG, "dispatch take activity");
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                LOGD(TAG, "creating photo activity");

                photoFile = createImageFile();
                LOGD(TAG, photoFile.toString() + "photo file");
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                LOGD(TAG, "Dispatching intetn");
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }


    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        // File f = new File(mCurrentPhotoPath);
        // Uri contentUri = Uri.fromFile(mCurrentPhoto);
        mediaScanIntent.setData(mCurrentPhotouri);
        this.sendBroadcast(mediaScanIntent);
    }

    private void setPic() throws IOException {
        imageFeed = decodeUri(mCurrentPhotouri, dimension, dimension);
        mImageView.setImageBitmap(imageFeed);
    }



    private void dispatchPickImageIntent() {
        Intent takePickImage = new Intent();
        takePickImage.setType("image/*");
        takePickImage.setAction(Intent.ACTION_GET_CONTENT);
        takePickImage.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

        // Ensure that there's a camera activity to handle the intent
        startActivityForResult(takePickImage, REQUEST_PICK_IMAGE);

    }

    @Subscribe
    public void OnNewPostAddedEvent(NewPostAddedEvent event) {
        dispatchLocalPostIntent(event.getUniqId());
        finish();
    }

    private void dispatchLocalPostIntent(String uniqID) {
        Intent local_post = new Intent(getApplicationContext(), LocalPostActivity.class);
        local_post.putExtra(LocalPostActivity.SHOPICK_LOCAL_POST_UNIQ_ID, uniqID);
        DispatchIntentUtils.startActivityWithCondition(this, local_post);

    }


    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        String[] list = getResources().getStringArray(R.array.price_range_discount);
        description = list[pos];
        if (description.contentEquals("Other")) {
            postCollectionAutoComplete.setVisibility(View.VISIBLE);
            discount_option_spinner.setVisibility(View.GONE);
        }

    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    public void dialogPostItBox() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Would you like to Post?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        postItem();
                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
