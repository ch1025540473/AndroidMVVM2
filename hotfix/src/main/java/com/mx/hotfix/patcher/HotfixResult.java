package com.mx.hotfix.patcher;


import java.io.Serializable;

/**
 * Created by wwish on 16/11/23.
 */

public class HotfixResult implements Serializable {
    public boolean isUpgradePatch;

    public boolean isSuccess;

    public String rawPatchFilePath;

    public long costTime;

    public Throwable e;

    //@Nullable
    public String patchVersion;

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("\nPatchResult: \n");
        sb.append("isUpgradePatch:" + isUpgradePatch + "\n");
        sb.append("isSuccess:" + isSuccess + "\n");
        sb.append("rawPatchFilePath:" + rawPatchFilePath + "\n");
        sb.append("costTime:" + costTime + "\n");
        if (patchVersion != null) {
            sb.append("patchVersion:" + patchVersion + "\n");
        }

        if (e != null) {
            sb.append("Throwable:" + e.getMessage() + "\n");
        }
        return sb.toString();
    }

}
