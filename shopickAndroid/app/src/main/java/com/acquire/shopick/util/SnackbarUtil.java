package com.acquire.shopick.util;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

/**
 * Created by gaurav on 10/31/15.
 */
public class SnackbarUtil {

    public static void tryFindanything(View mView) {
        Snackbar snackbar = Snackbar
                .make(mView, "Try #Findanything, if Shopick Can't be added as contact.", Snackbar.LENGTH_SHORT);
        View view = snackbar.getView();
        TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);

        snackbar.show();
    }

    public static void referralCodeCopied(View mView) {
        Snackbar snackbar = Snackbar
                .make(mView, "Referral Code copied to clipboard.", Snackbar.LENGTH_SHORT);
        View view = snackbar.getView();
        TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);

        snackbar.show();
    }

    public static void showYouEarnPicks(View mView, Context context) {
        Snackbar snackbar = Snackbar
                .make(mView, "Earn picks by posting and reedem for cash, shopping at Zara, Levis", Snackbar.LENGTH_LONG);
        View view = snackbar.getView();
        snackbar.setAction("Reedem", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(DispatchIntentUtils.dispatchRedeemPicksIntent(context));
                snackbar.dismiss();
            }
        });
        TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);

        snackbar.show();
    }


    public static void succesRedeemReferral(View mView) {
        Snackbar snackbar = Snackbar
                .make(mView, "Thanks, your reward will be credited shortly.", Snackbar.LENGTH_SHORT);
        View view = snackbar.getView();
        TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);

        snackbar.show();
    }

    public static void failureRedeemReferral(View mView, String message) {
        Snackbar snackbar = Snackbar
                .make(mView, message, Snackbar.LENGTH_SHORT);
        View view = snackbar.getView();
        TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);

        snackbar.show();
    }


    public static void showMessage(View mView, String message) {
        Snackbar snackbar = Snackbar
                .make(mView, message, Snackbar.LENGTH_SHORT);
        View view = snackbar.getView();
        TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);

        snackbar.show();
    }

    public static void showMessageLong(View mView, String message) {
        Snackbar snackbar = Snackbar
                .make(mView, message, Snackbar.LENGTH_LONG);
        snackbar.setDuration(8000);
        View view = snackbar.getView();
        TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);

        snackbar.show();

    }

    public static void pleaseWait(final Context mContext, final View mView) {
        Snackbar snackbar = Snackbar
                .make(mView, "Waiting for WhatsApp to sync Shopick contact. Tell Shopick what you want to be found on WhatsApp.", Snackbar.LENGTH_LONG);

               /* .setAction("Ok", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        GenericUtils.sendWhatsAppMessage(mContext, mView, true);

                    }
                });*/

        View view = snackbar.getView();
        TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        snackbar.setActionTextColor(Color.WHITE);

        snackbar.show();
    }


    public static void createStartingLogin(View mView) {
        Snackbar snackbar = Snackbar
                .make(mView, "Starting Login Process!!", Snackbar.LENGTH_SHORT);
        View view = snackbar.getView();
        TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);

        snackbar.show();
    }

    public static void presentationThatsAll(View mView) {
        Snackbar snackbar = Snackbar
                .make(mView, "That's All Folks!!", Snackbar.LENGTH_SHORT);
        View view = snackbar.getView();
        TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);

        snackbar.show();
    }

    public static void viewEmptyNoItems(View mView) {
        Snackbar snackbar = Snackbar
                .make(mView, "Seems like you hit a dead end, Try something else!!", Snackbar.LENGTH_SHORT);
        View view = snackbar.getView();
        TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);

        snackbar.show();
    }

    public static void viewApplicationNotInstalled(View mView, String application) {
        Snackbar snackbar = Snackbar
                .make(mView, application + " not installed!!", Snackbar.LENGTH_SHORT);
        View view = snackbar.getView();
        TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        snackbar.show();
    }

}
