
package net.pside.android.nfcbattler;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferencesManager extends Object {

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    public PreferencesManager(Context context) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * 自分のステータス値の有無をセットする
     * 
     * @param bool
     */
    public void setIsMyStatus(Boolean bool) {
        mEditor = mPreferences.edit();

        mEditor.putBoolean("IS_MY_STATUS", bool);
        mEditor.commit();
    }

    /**
     * 自分のステータス値の有無を引き出す
     * 
     * @return
     */
    public Boolean getIsMyStatus() {
        return mPreferences.getBoolean("IS_MY_STATUS", false);
    }

    /**
     * 自分のステータス値を格納する
     * 
     * @param status int配列
     */
    public void setMyStatus(int[] status) {
        mEditor = mPreferences.edit();

        String value = "";
        for (int i = 0; i < 7; i++) {
            value += status[i] + ",";
        }

        mEditor.putString("MY_STATUS_PARAMETER", value);
        mEditor.commit();

        setIsMyStatus(true);// データが存在することをセットする
    }

    /**
     * 自分のステータス値を引き出す
     * 
     * @return int配列のステータス値
     */
    public int[] getMyStatus() {
        String[] value = mPreferences.getString("MY_STATUS_PARAMETER", null).split(",");

        int[] retValue = new int[7];
        for (int i = 0; i < 7; i++) {
            retValue[i] = Integer.parseInt(value[i]);
        }

        return retValue;
    }

    public String getMyStatusForString() {
        return mPreferences.getString("MY_STATUS_PARAMETER", null);
    }

}
