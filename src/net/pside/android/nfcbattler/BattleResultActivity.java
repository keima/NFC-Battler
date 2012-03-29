
package net.pside.android.nfcbattler;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;

public class BattleResultActivity extends Activity {

    private int mWinCount = 0;
    private SharedPreferences mPreferences = null;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.battleresult);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Bundle extra = getIntent().getExtras();

        if (extra != null) {
            String enemyDataStr = extra.getString("EnemyData");
            String[] enemyData = enemyDataStr.split(",");

            for (int i = 0; i < enemyData.length; i++) {
                if (mPreferences.getInt("PARAM" + i, 0) > Integer.parseInt(enemyData[i])) {
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
}
