
package net.pside.android.nfcbattler;

/* 
 * 参考文献：
 * http://d.hatena.ne.jp/chun_ryo/20110226/1298730955
 * https://sites.google.com/a/gclue.jp/android-nfc/androidno-n
 * http://d.hatena.ne.jp/chun_ryo/20110226/1298730955
 * API Demos#NFC
 * https://sites.google.com/a/topgate.co.jp/systemsolution/android/nfc-startup
 * http://www.adamrocker.com/blog/313/nfc-write-with-nexuss.html
 * http://wiki.osdev.info/?FeliCa
 */

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.nfc.NfcAdapter;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.os.Bundle;

public class ScanActivity extends Activity {

    private final String TAG = "NFC Battler";

    private NfcAdapter mAdapter; // 端末のNFCアダプタを提供
    private Intent mIntent;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mFilters;
    private String[][] mTechLists;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan);

        mAdapter = NfcAdapter.getDefaultAdapter(this);

        mIntent = new Intent(this, ScanResultActivity.class);

        mPendingIntent = PendingIntent.getActivity(this, 0, mIntent, 0);

        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndef.addDataType("*/*");
        } catch (MalformedMimeTypeException e) {
            throw new RuntimeException("fail", e);
        }

        // どのBroadcastを受け取るか(上で詳細を決めている)
        mFilters = new IntentFilter[] {
                ndef,
        };

        // どのタイプのカードを読むようにするか
        mTechLists = new String[][] {
                new String[] {
                        NfcA.class.getName(), // MIFARE
                        NfcB.class.getName(), // B
                        NfcF.class.getName(), // FeliCa
                }
        };

    }

    @Override
    protected void onResume() {
        // NFC読むスイッチみたいなのをONにする
        super.onResume();
        // Log.v(TAG, "onResume!");

        if (mAdapter != null) {
            mAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters, mTechLists);
        }
    }

    @Override
    protected void onPause() {
        // NFC読むスイッチみたいなのをOFFにする
        super.onPause();
        // Log.v(TAG, "onPause!");

        if (mAdapter != null) {
            mAdapter.disableForegroundDispatch(this);
        }
    }
}
