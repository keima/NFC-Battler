
package net.pside.android.nfcbattler;

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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MainActivity extends Activity implements CreateNdefMessageCallback,
        OnNdefPushCompleteCallback {

    private PreferencesManager mManager;
    private TextView textDataMessage;
    private NfcAdapter mAdapter;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        initializeView();

        findViewById(R.id.button1).setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ScanActivity.class);
                startActivity(intent);

            }
        });
    }

    private void initializeView() {
        mManager = new PreferencesManager(this);
        if (mManager.getIsMyStatus() == false) {
            // データが存在しない状態の時

        } else {
            // データが存在するとき

            textDataMessage = (TextView) findViewById(R.id.textView1);
            textDataMessage.setText("端末同士をかざしてバトル開始！");

            // mAdapter == null だとNFC非対応であると判断できるらしい
            mAdapter = NfcAdapter.getDefaultAdapter(this);
            // NDEFメッセージを作成するためのコールバック登録
            mAdapter.setNdefPushMessageCallback(this, this);
            // メッセージ送信後のコールバック登録
            mAdapter.setOnNdefPushCompleteCallback(this, this);
            // mAdapter.setNdefPushMessage(message, this);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        initializeView();

    }

    /**
     * CreateNdefMessageCallbackインターフェースの実装
     */
    public NdefMessage createNdefMessage(NfcEvent event) {
        mManager = new PreferencesManager(this);

        // データ本体を整形(しなくても良くなった)
        String data = mManager.getMyStatusForString();

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

        // mIsSentMyData = true;
        // createResultActivity(mIsSentMyData, mIsGetEnemyData);

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
