package com.acquire.shopick.util;

import android.content.ActivityNotFoundException;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by gaurav on 2/25/16.
 */
public class GenericUtils {

    public static String phonenumber= "8826489685";

    public static boolean sendMessageShopick(final Context mContext, final View mView) {
        if (GenericUtils.contactExists(mContext, phonenumber)) {
            return sendWhatsAppMessage(mContext, mView, true);
        } else {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            //Yes button clicked
                            GenericUtils.WritePhoneContact("Shopick", phonenumber, mContext);
                            SnackbarUtil.pleaseWait(mContext, mView);
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            SnackbarUtil.tryFindanything(mView);
                            break;
                    }
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setMessage("We would add our contact as Shopick ? ").setPositiveButton("Allow", dialogClickListener)
                    .setNegativeButton("Deny", dialogClickListener).show();
            return false;
        }
    }

    public static boolean sendWhatsAppMessage(final Context mContext, final View mview, final boolean interactive) {
        try {
            if (GenericUtils.identifyWhatsappContact(mContext, phonenumber, "Shopick")){
                Uri mUri = Uri.parse("smsto:"+phonenumber);
                Intent mIntent = new Intent(Intent.ACTION_SENDTO, mUri);
                mIntent.setPackage("com.whatsapp");
                mIntent.putExtra("sms_body", "Hi");
                mIntent.putExtra("chat", true);
                mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(mIntent);
                return true;
            } else {
                if (interactive) {
                    SnackbarUtil.pleaseWait(mContext, mview);
                }
                return false;
            }
        } catch (ActivityNotFoundException noactivity) {
            if (interactive) {
                SnackbarUtil.viewApplicationNotInstalled(mview, "WhatsApp");
            }
            return true;
        }
    }



    //Utility Functions


    public static void WritePhoneContact(String displayName, String number,Context cntx /*App or Activity Ctx*/)
    {
        Context contetx 	= cntx; //Application's context or Activity's context
        String strDisplayName 	=  displayName; // Name of the Person to add
        String strNumber 	=  number; //number of the person to add with the Contact

        ArrayList<ContentProviderOperation> cntProOper = new ArrayList<ContentProviderOperation>();
        int contactIndex = cntProOper.size();//ContactSize

        //Newly Inserted contact
        // A raw contact will be inserted ContactsContract.RawContacts table in contacts database.
        cntProOper.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)//Step1
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).build());

        //Display name will be inserted in ContactsContract.Data table
        cntProOper.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)//Step2
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, contactIndex)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, strDisplayName) // Name of the contact
                .build());
        //Mobile number will be inserted in ContactsContract.Data table
        cntProOper.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)//Step 3
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, contactIndex)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, strNumber) // Number to be added
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE).build()); //Type like HOME, MOBILE etc
        try
        {
            // We will do batch operation to insert all above data
            //Contains the output of the app of a ContentProviderOperation.
            //It is sure to have exactly one of uri or count set
            ContentProviderResult[] contentProresult = null;
            contentProresult = contetx.getContentResolver().applyBatch(ContactsContract.AUTHORITY, cntProOper); //apply above data insertion into contacts list
        }
        catch (RemoteException exp)
        {
            //logs;
        }
        catch (OperationApplicationException exp)
        {
            //logs
        }
    }

    public static boolean contactExists(Context _activity, String number) {
        if (number != null) {

            Uri lookupUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
            String[] mPhoneNumberProjection = { ContactsContract.PhoneLookup._ID, ContactsContract.PhoneLookup.NUMBER, ContactsContract.PhoneLookup.DISPLAY_NAME };
            Cursor cur = _activity.getContentResolver().query(lookupUri, mPhoneNumberProjection, null, null, null);
            try {
                if (cur.moveToFirst()) {
                    return true;
                }
            } finally {
                if (cur != null)
                    cur.close();
            }
            return false;
        } else {
            return false;
        }
    }


    public static boolean identifyWhatsappContact(final Context mContexts, final String phonenumber, final String contactName) {
        ContentResolver contentResolver = mContexts.getContentResolver();

        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phonenumber));

        String[] projection = new String[] {ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup._ID};

        Cursor cursor =
                contentResolver.query(
                        uri,
                        projection,
                        null,
                        null,
                        null);
        String contactId,contactName1;
        contactId = "Shopick";
        if(cursor!=null) {
            while(cursor.moveToNext()){
                contactName1 = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup.DISPLAY_NAME));
                contactId = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup._ID));
                Log.d("fsdf", "contactMatch name: " + contactName1);
                Log.d("dfdsfds", "contactMatch id: " + contactId);
            }
            cursor.close();
        }
        String[] projection1 = new String[] { ContactsContract.RawContacts._ID };
        String selection = ContactsContract.Data.CONTACT_ID + " = ? AND account_type IN (?)";
        String[] selectionArgs = new String[] { contactId, "com.whatsapp" };
        Cursor cursor1 = mContexts.getContentResolver().query(ContactsContract.RawContacts.CONTENT_URI, projection1, selection, selectionArgs, null);
        boolean hasWhatsApp = cursor1.moveToNext();
        if (hasWhatsApp){
            return true;
        }
        return false;
    }
}
