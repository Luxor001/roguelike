package com.panacea.RufusPyramid.android;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.utils.Array;
import com.panacea.RufusPyramid.Main;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import de.tomgrill.gdxfacebook.core.GDXFacebook;
import de.tomgrill.gdxfacebook.core.GDXFacebookCallback;
import de.tomgrill.gdxfacebook.core.GDXFacebookConfig;
import de.tomgrill.gdxfacebook.core.GDXFacebookError;
import de.tomgrill.gdxfacebook.core.GDXFacebookSystem;
import de.tomgrill.gdxfacebook.core.SignInMode;
import de.tomgrill.gdxfacebook.core.SignInResult;

public class AndroidLauncher extends AndroidApplication {
	public static GDXFacebook facebook;
	private Array<String> permissionsRead = new Array<String>();
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new Main(new DatabaseAndroid(this.getBaseContext())), config);

// questo pezzo di codice ritorna l'hash key dell'applicazione, necessario per facebook.
/*		PackageInfo info;
		try {
			info = getPackageManager().getPackageInfo(getBaseContext().getPackageName(), PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {
				MessageDigest md;
				md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				String something = new String(Base64.encode(md.digest(), 0));
				//String something = new String(Base64.encodeBytes(md.digest()));
				Log.e("hash key", something);
			}
		} catch (PackageManager.NameNotFoundException e1) {
			Log.e("name not found", e1.toString());
		} catch (NoSuchAlgorithmException e) {
			Log.e("no such an algorithm", e.toString());
		} catch (Exception e) {
			Log.e("exception", e.toString());
		}*/
	}
}
