package com.ubiqlog.ubiqlogwear.Util;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.gc.android.market.api.MarketSession;
import com.gc.android.market.api.model.Market;

import java.io.IOException;

/**
 * Created by User on 3/7/15.
 */
public class PkgLookupUtil {


    public static void lookup(Context context) {
        Log.d("LOOKUP", "Looking up");
        AccountManager am = AccountManager.get(context);
        Account[] accounts = am.getAccountsByType("com.google");
        if (accounts.length > 0) {
            try {
                AccountManagerFuture<Bundle> accountManagerFuture =
                        am.getAuthToken(accounts[0], "android", null, (Activity) context, null, null);
                Bundle authTokenBundle = accountManagerFuture.getResult();
                String authToken =
                        authTokenBundle.getString(AccountManager.KEY_AUTHTOKEN).toString();

                MarketSession session = new MarketSession();
                session.setAuthSubToken(authToken);
                Market.AppsRequest appsRequest = Market.AppsRequest.newBuilder()
                        .setQuery("maps")
                        .setStartIndex(0).setEntriesCount(10)
                        .setWithExtendedInfo(true)
                        .build();
                Log.d("LOOKUP", "finished looking up");
                session.append(appsRequest, new MarketSession.Callback<Market.AppsResponse>() {
                    @Override
                    public void onResult(Market.ResponseContext responseContext, Market.AppsResponse appsResponse) {
                        Log.d("Lookup", "in response");
                        for (int i = 0; i < appsResponse.getEntriesCount(); i++) {
                            Log.d("LOOKUP", appsResponse.getApp(i).getTitle());
                        }
                    }
                });


            } catch (AuthenticatorException e) {
                e.printStackTrace();
            } catch (OperationCanceledException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
