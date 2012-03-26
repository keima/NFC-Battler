
package net.pside.android.nfcbattler;

/*
 * 参考文献：
 * http://d.hatena.ne.jp/bs-android/20111020/1319107541
 */

import java.nio.charset.Charset;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcAdapter.OnNdefPushCompleteCallback;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class BattleActivity extends Activity implements CreateNdefMessageCallback,
        OnNdefPushCompleteCallback {

    private NfcAdapter mAdapter;
    private SharedPreferences mPreferences;

    private boolean mIsGetEnemyData = false;
    private boolean mIsSentMyData = false;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.battle);

        // sharedprefからデータ回収
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // パラメータ値がないとき
        if (mPreferences.getInt("PARAM1", 0) == 0) {
            Toast.makeText(this, "さきにスキャンしなはれ", Toast.LENGTH_LONG).show();
            finish();
        } else {

            // mAdapter == null だとNFC非対応であると判断できるらしい
            mAdapter = NfcAdapter.getDefaultAdapter(this);
            // NDEFメッセージを作成するためのコールバック登録
            mAdapter.setNdefPushMessageCallback(this, this);
            // メッセージ送信後のコールバック登録
            mAdapter.setOnNdefPushCompleteCallback(this, this);
            // mAdapter.setNdefPushMessage(message, this);

        }

        String data = "";
        for (int i = 0; i < 7; i++) {
            data += String.valueOf(mPreferences.getInt("PARAM" + i, 0));
            data += ",";
        }
        Log.i("BattleActivity", "data:" + data);

    }

    /**
     * CreateNdefMessageCallbackインターフェースの実装
     */
    public NdefMessage createNdefMessage(NfcEvent event) {
        // TODO Auto-generated method stub

        String data = "";
        for (int i = 0; i < 7; i++) {
            data += String.valueOf(mPreferences.getInt("PARAM" + i, 0));
            data += ",";
        }
        Log.v("BattleActivity", "data:" + data);

        NdefMessage msg = new NdefMessage(
                new NdefRecord[] {
                        createMimeRecord("application/com.example.android.beam",
                                data.getBytes())
                });

        return msg;
    }

    /**
     * OnNdefPushCompleteCallbackインターフェースの実装 (Pushが終わると同時に開始される)
     */
    public void onNdefPushComplete(NfcEvent event) {
        /*
         * Beamを送りつけたら呼ばれるのがこちら
         */
        mIsSentMyData = true;

    }

    @Override
    protected void onResume() {
        super.onResume();

        /*
         * Beamを受け取ったらよばれるのがこちら
         */

        // アクティビティがAndroid Beamによって開始されたことをチェックする
        Intent intent = getIntent();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(
                    NfcAdapter.EXTRA_NDEF_MESSAGES);
            // ビームの送信中は一つだけのメッセージ
            NdefMessage msg = (NdefMessage) rawMsgs[0];
            // 現在は、レコード0はMIMEタイプを含む、レコード1はAARを含む
            // textView.setText(new String(msg.getRecords()[0].getPayload()));

            Toast.makeText(this, new String(msg.getRecords()[0].getPayload()), Toast.LENGTH_LONG)
                    .show();
            Log.v("BattleActivity", new String(msg.getRecords()[0].getPayload()));

            mIsGetEnemyData = true;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // TODO Auto-generated method stub
        // super.onNewIntent(intent);
        setIntent(intent);
    }

    /**
     * 2つのbooleanを比較して、双方とも真であればActivityを切り替える
     * 
     * @param boolean, boolean
     */
    private void createResultActivity(boolean param1, boolean param2) {
        /* 双方とも真であればActivity起動 */
        if (param1 & param2) {
            Intent intent = new Intent(this, BattleResultActivity.class);
            // TODO ここでExtra格納せんとまずい
            startActivity(intent);
        }
    }

    /**
     * NDEFレコード内にカスタムMIMEタイプをカプセル化して生成する
     * 
     * @param mimeType
     */
    public NdefRecord createMimeRecord(String mimeType, byte[] payload) {
        byte[] mimeBytes = mimeType.getBytes(Charset.forName("US-ASCII"));
        NdefRecord mimeRecord = new NdefRecord(
                NdefRecord.TNF_MIME_MEDIA, mimeBytes, new byte[0], payload);
        return mimeRecord;
    }
}
