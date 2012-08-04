
package net.pside.android.nfcbattler;

/*
 * 参考文献：
 * http://d.hatena.ne.jp/bs-android/20111020/1319107541
 */

import java.nio.charset.Charset;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcAdapter.OnNdefPushCompleteCallback;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

public class BattleActivity extends Activity implements CreateNdefMessageCallback,
        OnNdefPushCompleteCallback {

    private NfcAdapter mAdapter;
    private DataStore mDataStore;

    private String enemyData = "";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.battle);

        mDataStore = new DataStore(this);

        // パラメータ値がないとき
        if (mDataStore.getIsMyStatus() == false) {
            Toast.makeText(this, "さきにスキャンしなはれ", Toast.LENGTH_LONG).show();
            finish();
        } else {

            // mAdapter == null だとNFC非対応であると判断できるらしい
            mAdapter = NfcAdapter.getDefaultAdapter(this);
            // NDEFメッセージを作成するためのコールバック登録
            mAdapter.setNdefPushMessageCallback(this, this);
            // メッセージ送信後のコールバック登録
            mAdapter.setOnNdefPushCompleteCallback(this, this);
        }
    }

    /**
     * CreateNdefMessageCallbackインターフェースの実装
     */
    public NdefMessage createNdefMessage(NfcEvent event) {

        String data = mDataStore.getMyStatusForString();

        // 送信するデータパケットを作成
        NdefMessage msg = new NdefMessage(
                new NdefRecord[] {
                        createMimeRecord("application/net.pside.android.nfcbattler",
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

        NfcTransferTemp transferTemp = new NfcTransferTemp();
        transferTemp.setIsSent(true);

        createResultActivity(transferTemp.getIsSent(), transferTemp.getIsReceived());

    }

    @Override
    protected void onResume() {
        super.onResume();

        /*
         * Beamを受け取ったらよばれるのがこちら
         */

        // アクティビティがAndroid Beamによって開始されたことをチェックする
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            Parcelable[] rawMsgs = getIntent().getParcelableArrayExtra(
                    NfcAdapter.EXTRA_NDEF_MESSAGES);
            // ビームの送信中は一つだけのメッセージ
            NdefMessage msg = (NdefMessage) rawMsgs[0];
            // 現在は、レコード0はMIMEタイプを含む、レコード1はAARを含む
            // textView.setText(new String(msg.getRecords()[0].getPayload()));

            enemyData = new String(msg.getRecords()[0].getPayload());

            NfcTransferTemp transferTemp = new NfcTransferTemp();
            transferTemp.setIsReceived(true);

            createResultActivity(transferTemp.getIsSent(), transferTemp.getIsReceived());
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        Log.i("BattleActivity", "onNewIntent is called.");
    }

    /**
     * 状態を短期保存する
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // outState.putBoolean("IsGetEnemyData", mIsGetEnemyData);
        // outState.putBoolean("IsSentMyData", mIsSentMyData);

        Log.v("foobar", "onSave!!!!!");

    }

    /**
     * 保存した状態をロードする
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // mIsGetEnemyData = savedInstanceState.getBoolean("IsGetEnemyData");
        // mIsSentMyData = savedInstanceState.getBoolean("IsSentMyData");

        Log.v("foobar", "onLoad!!!!!");

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

            intent.putExtra("EnemyData", enemyData);

            Log.v("BattleActivity", "ResultActivity!");
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
