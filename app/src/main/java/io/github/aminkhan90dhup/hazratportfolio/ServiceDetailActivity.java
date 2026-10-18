package io.github.aminkhan90dhup.hazratportfolio;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ServiceDetailActivity extends Activity {
    @Override protected void attachBaseContext(Context base) { super.attachBaseContext(ThemeHelper.wrap(base)); }
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_detail);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.fade_out);
        int idx = getIntent().getIntExtra("index", 0);
        ServiceItem s = ServiceItem.all()[idx];
        ((TextView) findViewById(R.id.dIcon)).setText(s.icon);
        ((TextView) findViewById(R.id.dTitle)).setText(s.title);
        ((TextView) findViewById(R.id.dCategory)).setText(s.category);
        ((TextView) findViewById(R.id.dDesc)).setText(s.longDesc);
        ((TextView) findViewById(R.id.dPrice)).setText(s.priceLine);
        LinearLayout box = findViewById(R.id.dFeatures);
        for (String f : s.features) {
            TextView t = new TextView(this);
            t.setText("✓  " + f);
            t.setTextSize(14.5f);
            t.setTextColor(getResources().getColor(R.color.textMain));
            t.setPadding(0, 6, 0, 6);
            box.addView(t);
        }
        findViewById(R.id.dBack).setOnClickListener(v -> finish());
        ((Button) findViewById(R.id.dGet)).setOnClickListener(v -> {
            Intent i = new Intent(this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            i.putExtra("open_tab", 2);
            i.putExtra("subject", "Service Request: " + s.title);
            startActivity(i);
            finish();
        });
    }
}
