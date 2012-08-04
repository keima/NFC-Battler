
package net.pside.android.nfcbattler;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class DataStore extends Object {

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    public DataStore(Context context) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * ログをはき出す（ずぼら用）
     * 
     * @param String
     */
    private void LogV(String msg) {
        Log.v("DataStore", msg);

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

        LogV("setIsMyStatus:" + bool);
    }

    /**
     * 自分のステータス値の有無を引き出す
     * 
     * @return
     */
    public Boolean getIsMyStatus() {
        Boolean ret = mPreferences.getBoolean("IS_MY_STATUS", false);

        LogV("getIsMyStatus:" + ret);

        return ret;
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
