package au.edu.jcu.cp3406.educationalgame;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

public class Authenticate_Activity extends BaseActivity {
    private WebView webView;
    private TextView info;
    private Twitter twitter = TwitterFactory.getSingleton();
    private String oauthVerifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticate);
        info = findViewById(R.id.info);
        webView = findViewById(R.id.web_view);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);

                info.setText(getString(R.string.working));
                String message;
                if (url.startsWith("https://cp3406.game.net/")) {
                    Uri uri = Uri.parse(url);
                    oauthVerifier = uri.getQueryParameter("oauth_verifier");
                    if (oauthVerifier != null) {
                        Log.i("Authenticate", "authenticated!");
                        message = "Authenticated!";
                        updateTwitterConfiguration();
                        webView.loadData("done", "text/html", null);
                    } else {
                        message = "Not authenticated...";
                    }
                    info.setText(message);
                }
            }
        });

        // present twitter login for authentication
        Background.run(new Runnable() {
            @Override
            public void run() {
                try {
                    RequestToken requestToken = twitter.getOAuthRequestToken();
                    final String requestUrl = requestToken.getAuthenticationURL();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            webView.loadUrl(requestUrl);
                        }
                    });

                } catch (final Exception e) {
                    Log.i("Authenticate", e.toString());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            info.setText(e.toString());
                        }
                    });
                }
            }
        });
    }

    private void updateTwitterConfiguration() {
        Background.run(new Runnable() {
            @Override
            public void run() {
                try {
                    AccessToken accessToken = twitter.getOAuthAccessToken(oauthVerifier);
                    twitter.setOAuthAccessToken(accessToken);
                } catch (final Exception e) {
                    Log.e("Authenticate", e.toString());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            info.setText(e.toString());
                        }
                    });
                }
            }
        });
    }
}
