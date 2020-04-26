package cloud.musdey.corona;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import cloud.musdey.corona.mobile.AdsController;
import cloud.musdey.corona.mobile.ShareController;

public class AndroidLauncher extends AndroidApplication implements AdsController, ShareController {

	private static final String BANNER_AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111";
	private static final String INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712";

	AdView bannerAd;
	InterstitialAd interstitialAd;
	private boolean adLoaded = false;
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		//initialize(new CoronaGame(), config);
		View gameView = initializeForView(new CoronaGame(this,this), config);
		setupAds();
		RelativeLayout layout = new RelativeLayout(this);
		layout.addView(gameView, ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		layout.addView(bannerAd, params);

		setContentView(layout);
	}

	public void setupAds() {
		bannerAd = new AdView(getApplicationContext());
		bannerAd.setVisibility(View.INVISIBLE);
		bannerAd.setBackgroundColor(0xff000000); // black
		bannerAd.setAdUnitId(BANNER_AD_UNIT_ID);
		bannerAd.setAdSize(AdSize.SMART_BANNER);

		interstitialAd = new InterstitialAd(getApplicationContext());
		interstitialAd.setAdUnitId(INTERSTITIAL_AD_UNIT_ID);
		interstitialAd.loadAd(new AdRequest.Builder().build());
		interstitialAd.setAdListener(new AdListener() {
			@Override
			public void onAdLoaded() {
				adLoaded = true;
				// Code to be executed when an ad finishes loading.
			}

			@Override
			public void onAdFailedToLoad(int errorCode) {
				// Code to be executed when an ad request fails.
			}

			@Override
			public void onAdOpened() {
				// Code to be executed when the ad is displayed.
			}

			@Override
			public void onAdClicked() {
				// Code to be executed when the user clicks on an ad.
			}

			@Override
			public void onAdLeftApplication() {
				// Code to be executed when the user has left the app.
			}

			@Override
			public void onAdClosed() {
				// Code to be executed when the interstitial ad is closed.
			}
		});

		/*MobileAds.initialize(this, new OnInitializationCompleteListener() {
			@Override
			public void onInitializationComplete(InitializationStatus initializationStatus) {

				bannerAd = new AdView(getApplicationContext());
				bannerAd.setVisibility(View.INVISIBLE);
				bannerAd.setBackgroundColor(0xff000000); // black
				bannerAd.setAdUnitId(BANNER_AD_UNIT_ID);
				bannerAd.setAdSize(AdSize.SMART_BANNER);
			}
		});*/

	}

	@Override
	public void showInterstitialAd() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if(adLoaded){
					interstitialAd.show();

				}
			}
		});
	}

	@Override
	public void hideInterstitialAd() {

	}

	@Override
	public void showBannerAd() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				System.out.println("show is called brateee");
				bannerAd.setVisibility(View.VISIBLE);
				AdRequest.Builder builder = new AdRequest.Builder();
				AdRequest ad = builder.build();
				bannerAd.loadAd(ad);
			}
		});
	}

	@Override
	public void hideBannerAd() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				bannerAd.setVisibility(View.INVISIBLE);
			}
		});
	}

	@Override
	public void shareWithFriends(String data) {
		Intent sendIntent = new Intent();
		sendIntent.setAction(Intent.ACTION_SEND);
		sendIntent.putExtra(Intent.EXTRA_TEXT, data);
		sendIntent.setType("text/plain");

		Intent shareIntent = Intent.createChooser(sendIntent, null);
		startActivity(shareIntent);
	}
}
