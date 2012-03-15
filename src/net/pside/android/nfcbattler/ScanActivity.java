
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
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.nfc.NfcAdapter;
import android.nfc.tech.NfcF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;

public class ScanActivity extends Activity {

    private final String TAG = "NFC Battler";

    private NfcAdapter mAdapter; // 端末のNFCアダプタを提供
    private PendingIntent mPendingIntent;
    private IntentFilter[] mFilters;
    private String[][] mTechLists;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan);

        mAdapter = NfcAdapter.getDefaultAdapter(this);

        mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndef.addDataType("*/*");
        } catch (MalformedMimeTypeException e) {
            throw new RuntimeException("fail", e);
        }

        mFilters = new IntentFilter[] {
                ndef,
        };

        mTechLists = new String[][] {
                new String[] {
                        NfcF.class.getName()
                }
        };

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        Log.v(TAG, "onResume!");

        if (mAdapter != null) {
            mAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters, mTechLists);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(TAG, "onPause!");

        if (mAdapter != null) {
            mAdapter.disableForegroundDispatch(this);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // TODO Auto-generated method stub
        super.onNewIntent(intent);

        // Parcelable[] rawMsgs = intent
        // .getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        byte[] rawMsgs = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);

        Log.v(TAG, "length:" + rawMsgs.length);

        for (int i = 0; i < rawMsgs.length; i++) {
            Log.v(TAG, "rawMsgs[" + i + "]" + rawMsgs[i]);
        }

    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            Log.d(TAG, "  onReceive(" + context + ", " + intent + ")");

            Parcelable[] rawMsgs = intent
                    .getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

            Log.v(TAG, "length:" + rawMsgs.length);

            for (int i = 0; i < rawMsgs.length; i++) {
                Log.v(TAG, "rawMsgs[" + i + "]" + rawMsgs[i]);
            }
        }
    };
}
