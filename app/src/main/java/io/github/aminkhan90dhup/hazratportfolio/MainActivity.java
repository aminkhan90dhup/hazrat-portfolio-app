package io.github.aminkhan90dhup.hazratportfolio;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends Activity {
    private View pageAbout, pageServices, pageContact;
    private TextView navAbout, navServices, navContact, themeBtn;
    private int currentTab = 0;
    private final ExecutorService io = Executors.newSingleThreadExecutor();
    private final Handler main = new Handler(Looper.getMainLooper());

    @Override protected void attachBaseContext(Context base) { super.attachBaseContext(ThemeHelper.wrap(base)); }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pageAbout = findViewById(R.id.pageAbout);
        pageServices = findViewById(R.id.pageServices);
        pageContact = findViewById(R.id.pageContact);
        navAbout = findViewById(R.id.navAbout);
        navServices = findViewById(R.id.navServices);
        navContact = findViewById(R.id.navContact);
        themeBtn = findViewById(R.id.themeBtn);
        navAbout.setOnClickListener(v -> showTab(0));
        navServices.setOnClickListener(v -> showTab(1));
        navContact.setOnClickListener(v -> showTab(2));
        themeBtn.setOnClickListener(v -> {
            int next = ThemeHelper.isDark(this) ? ThemeHelper.MODE_LIGHT : ThemeHelper.MODE_DARK;
            ThemeHelper.setMode(this, next);
            recreate();
        });
        buildServices();
        wireAbout();
        wireContact();
        showTab(0);
        handleIntent(getIntent());
    }

    @Override protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (intent == null) return;
        if (intent.hasExtra("open_tab")) {
            showTab(intent.getIntExtra("open_tab", 0));
            String subject = intent.getStringExtra("subject");
            if (!TextUtils.isEmpty(subject)) {
                EditText et = findViewById(R.id.etSubject);
                et.setText(subject);
                findViewById(R.id.etName).requestFocus();
                Toast.makeText(this, "Service selected — just fill the form", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showTab(int tab) {
        currentTab = tab;
        pageAbout.setVisibility(tab == 0 ? View.VISIBLE : View.GONE);
        pageServices.setVisibility(tab == 1 ? View.VISIBLE : View.GONE);
        pageContact.setVisibility(tab == 2 ? View.VISIBLE : View.GONE);
        View active = tab == 0 ? pageAbout : tab == 1 ? pageServices : pageContact;
        active.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
        styleNav(navAbout, tab == 0);
        styleNav(navServices, tab == 1);
        styleNav(navContact, tab == 2);
    }

    private void styleNav(TextView t, boolean selected) {
        t.setTextColor(getResources().getColor(selected ? R.color.accent : R.color.navInactive));
        t.setBackgroundResource(selected ? R.drawable.bg_chip : android.R.color.transparent);
    }

    // ---------------- Services ----------------
    private void buildServices() {
        LinearLayout list = findViewById(R.id.servicesList);
        LayoutInflater inf = LayoutInflater.from(this);
        ServiceItem[] items = ServiceItem.all();
        for (int i = 0; i < items.length; i++) {
            final int idx = i;
            View card = inf.inflate(R.layout.item_service, list, false);
            ((TextView) card.findViewById(R.id.sIcon)).setText(items[i].icon);
            ((TextView) card.findViewById(R.id.sTitle)).setText(items[i].title);
            ((TextView) card.findViewById(R.id.sCategory)).setText(items[i].category);
            ((TextView) card.findViewById(R.id.sDesc)).setText(items[i].shortDesc);
            View.OnClickListener open = v -> {
                Intent d = new Intent(this, ServiceDetailActivity.class);
                d.putExtra("index", idx);
                startActivity(d);
            };
            card.setOnClickListener(open);
            card.findViewById(R.id.sLearn).setOnClickListener(open);
            card.findViewById(R.id.sGet).setOnClickListener(v -> {
                showTab(2);
                ((EditText) findViewById(R.id.etSubject)).setText("Service Request: " + items[idx].title);
                findViewById(R.id.etName).requestFocus();
            });
            list.addView(card);
        }
    }

    // ---------------- About ----------------
    private void wireAbout() {
        findViewById(R.id.btnFb).setOnClickListener(v -> openUrl(Config.FACEBOOK_URL));
        findViewById(R.id.btnYt).setOnClickListener(v -> openUrl(Config.YOUTUBE_URL));
        findViewById(R.id.btnWeb).setOnClickListener(v -> openUrl(Config.WEBSITE_URL));
        findViewById(R.id.btnFiverr).setOnClickListener(v -> openUrl(Config.FIVERR_URL));
        findViewById(R.id.btnHire).setOnClickListener(v -> showTab(2));
        findViewById(R.id.btnSeeServices).setOnClickListener(v -> showTab(1));
    }

    // ---------------- Contact ----------------
    private EditText etName, etEmail, etPhone, etSubject, etMessage;
    private ProgressBar progress;
    private TextView tvStatus;
    private Button btnSend;

    private void wireContact() {
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etSubject = findViewById(R.id.etSubject);
        etMessage = findViewById(R.id.etMessage);
        progress = findViewById(R.id.progress);
        tvStatus = findViewById(R.id.tvStatus);
        btnSend = findViewById(R.id.btnSend);
        btnSend.setOnClickListener(v -> sendMessage());
        findViewById(R.id.btnWa).setOnClickListener(v -> openWhatsApp());
        findViewById(R.id.btnMail).setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + Config.EMAIL));
            i.putExtra(Intent.EXTRA_SUBJECT, etSubject.getText().toString().trim());
            i.putExtra(Intent.EXTRA_TEXT, etMessage.getText().toString().trim());
            startActivity(Intent.createChooser(i, "Send email"));
        });
        findViewById(R.id.btnFb2).setOnClickListener(v -> openUrl(Config.FACEBOOK_URL));
        findViewById(R.id.btnYt2).setOnClickListener(v -> openUrl(Config.YOUTUBE_URL));
    }

    private boolean validate() {
        if (etName.getText().toString().trim().length() < 3) {
            etName.setError("Please enter your full name"); etName.requestFocus(); return false;
        }
        String email = etEmail.getText().toString().trim();
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Please enter a valid email address"); etEmail.requestFocus(); return false;
        }
        String phone = etPhone.getText().toString().trim();
        if (!phone.isEmpty() && (phone.length() < 7 || !phone.matches("[+0-9 ()-]+"))) {
            etPhone.setError("Please enter a valid phone number"); etPhone.requestFocus(); return false;
        }
        if (etMessage.getText().toString().trim().length() < 10) {
            etMessage.setError("Please write a message (at least 10 characters)"); etMessage.requestFocus(); return false;
        }
        return true;
    }

    /** Sends the form for real via the FormSubmit AJAX endpoint.
     *  Success is shown ONLY when the server confirms with HTTP 200. */
    private void sendMessage() {
        if (!validate()) return;
        btnSend.setEnabled(false);
        progress.setVisibility(View.VISIBLE);
        tvStatus.setText("Sending…");
        tvStatus.setTextColor(getResources().getColor(R.color.textSub));
        final JSONObject payload = new JSONObject();
        try {
            payload.put("_subject", "App contact: " + etSubject.getText().toString().trim());
            payload.put("name", etName.getText().toString().trim());
            payload.put("email", etEmail.getText().toString().trim());
            payload.put("phone", etPhone.getText().toString().trim());
            payload.put("message", etMessage.getText().toString().trim());
            payload.put("_template", "table");
            payload.put("_captcha", "false");
        } catch (Exception ignored) { }
        io.execute(() -> {
            String result;
            boolean ok = false;
            try {
                HttpURLConnection c = (HttpURLConnection) new URL(Config.FORM_ENDPOINT).openConnection();
                c.setRequestMethod("POST");
                c.setConnectTimeout(15000);
                c.setReadTimeout(20000);
                c.setDoOutput(true);
                c.setRequestProperty("Content-Type", "application/json");
                c.setRequestProperty("Accept", "application/json");
                c.setRequestProperty("Referer", Config.WEBSITE_URL); // FormSubmit requires a web referer
                byte[] body = payload.toString().getBytes(StandardCharsets.UTF_8);
                try (OutputStream os = c.getOutputStream()) { os.write(body); }
                int code = c.getResponseCode();
                BufferedReader br = new BufferedReader(new InputStreamReader(
                    code >= 200 && code < 300 ? c.getInputStream() : c.getErrorStream(), StandardCharsets.UTF_8));
                StringBuilder sb = new StringBuilder(); String line;
                while ((line = br.readLine()) != null) sb.append(line);
                JSONObject resp = new JSONObject(sb.toString());
                ok = code == 200 && "true".equalsIgnoreCase(resp.optString("success"));
                result = resp.optString("message", ok ? "Message sent successfully." : "The service did not confirm delivery.");
            } catch (Exception e) {
                result = "Could not send (network error). Please check your internet, or use the Email button below.";
            }
            final boolean fok = ok; final String fmsg = result;
            main.post(() -> {
                progress.setVisibility(View.GONE);
                btnSend.setEnabled(true);
                tvStatus.setText(fmsg);
                tvStatus.setTextColor(getResources().getColor(fok ? R.color.primary : R.color.error));
                if (fok) {
                    etMessage.setText("");
                    etSubject.setText("");
                    Toast.makeText(this, "Message sent ✔", Toast.LENGTH_LONG).show();
                }
            });
        });
    }

    private void openWhatsApp() {
        try {
            if (!Config.WHATSAPP_NUMBER.isEmpty()) {
                openUrl("https://wa.me/" + Config.WHATSAPP_NUMBER);
            } else {
                Intent i = getPackageManager().getLaunchIntentForPackage("com.whatsapp");
                if (i != null) startActivity(i);
                else openUrl("https://www.whatsapp.com/");
            }
        } catch (Exception e) {
            Toast.makeText(this, "WhatsApp is not installed on this device", Toast.LENGTH_SHORT).show();
        }
    }

    private void openUrl(String url) {
        try { startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url))); }
        catch (Exception e) { Toast.makeText(this, "No app found to open this link", Toast.LENGTH_SHORT).show(); }
    }
}
