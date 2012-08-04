
package net.pside.android.nfcbattler;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class BattleResultActivity extends Activity {

    private int mWinCount = 0;
    private DataStore mDataStore;

    private static final int[] textMyID = {
            R.id.textMyParam1,
            R.id.textMyParam2,
            R.id.textMyParam3,
            R.id.textMyParam4,
            R.id.textMyParam5,
            R.id.textMyParam6,
            R.id.textMyParam7,
    };

    private static final int[] textEnemyID = {
            R.id.textEnemyParam1,
            R.id.textEnemyParam2,
            R.id.textEnemyParam3,
            R.id.textEnemyParam4,
            R.id.textEnemyParam5,
            R.id.textEnemyParam6,
            R.id.textEnemyParam7,
    };

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.battleresult);

        mDataStore = new DataStore(this);

        resetTransferLog();

        Bundle extra = getIntent().getExtras();

        if (extra != null) {
            // EnemyDataの整形
            String enemyDataStr = extra.getString("EnemyData");
            String[] enemyData = enemyDataStr.split(",");

            // MyDataの整形
            String myDataStr = mDataStore.getMyStatusForString();
            String[] myData = myDataStr.split(",");

            for (int i = 0; i < enemyData.length; i++) {

                TextView textMy = (TextView) findViewById(textMyID[i]);
                TextView textEnemy = (TextView) findViewById(textEnemyID[i]);

                textMy.setText(myData[i]);
                textEnemy.setText(enemyData[i]);

                if (Integer.parseInt(myData[i]) > Integer.parseInt(enemyData[i])) {
                    mWinCount++;
                } else {
                    // nothing
                }
            }

            TextView text = (TextView) findViewById(R.id.textWinLose);
            if (mWinCount >= 4) {

                text.setText("勝");
            } else {
                text.setText("負");
            }

        }
    }

    private void resetTransferLog() {
        NfcTransferTemp transferTemp = new NfcTransferTemp();
        transferTemp.setIsSent(false);
        transferTemp.setIsReceived(false);
    }
}
