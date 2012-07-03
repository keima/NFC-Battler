
package net.pside.android.nfcbattler;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class ScanResultActivity extends Activity {

    private static final String TAG = "ScanResultActivity";

    private Bundle mBundle;
    private PreferencesManager pm;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scanresult);

        int[] viewId = {
                R.id.text_param1,
                R.id.text_param2,
                R.id.text_param3,
                R.id.text_param4,
                R.id.text_param5,
                R.id.text_param6,
                R.id.text_param7,
        };

        TextView[] textViews = new TextView[7];
        for (int i = 0; i < 7; i++) {
            textViews[i] = (TextView) findViewById(viewId[i]);
        }

        mBundle = getIntent().getExtras();

        if (mBundle != null) {
            byte[] nfcIDm = mBundle.getByteArray(NfcAdapter.EXTRA_ID);

            // IDmを8桁と固定する(そうでないカードがきたらエラーを返す)
            if (nfcIDm.length == 8) {
                // 最下位1桁をそれ以外に足し算して（多少だけど）不可逆性を与える。
                // ＊＊＊アルゴリズムがザルなのでIDm漏れる危険性アリ＊＊＊

                int[] param = new int[8];

                for (int i = 0; i < nfcIDm.length; i++) {
                    param[i] = nfcIDm[i] & 0xff; // byte 2 int 変換
                }

                /* ここで一桁足している */
                for (int i = 0; i < param.length - 1; i++) {
                    param[i] += param[7];
                }
                param[7] = 0;// 意味あるのかどうか分からない。

                pm = new PreferencesManager(this);
                pm.setMyStatus(param);

                for (int i = 0; i < 7; i++) {
                    textViews[i].append(String.valueOf(param[i]));
                }

            } else {
                Toast.makeText(this, "申し訳ありませんが別のカードをスキャンしてください", Toast.LENGTH_LONG).show();
                finish();
            }

        }

        findViewById(R.id.button_gotomain).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScanResultActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
