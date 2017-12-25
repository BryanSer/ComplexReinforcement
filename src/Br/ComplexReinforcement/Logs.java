/*
 * 开发者:Bryan_lzh
 * QQ:390807154
 * 保留一切所有权
 * 若为Bukkit插件 请前往plugin.yml查看剩余协议
 */
package Br.ComplexReinforcement;

import Br.API.Log;
import java.io.PrintWriter;

/**
 *
 * @author Bryan_lzh
 */
public class Logs {

    private static Log Log;
    private static PrintWriter PW;

    public static void init() {
        Logs.Log = new Log(Main.Plugin, 30, 30);
        PW = Log.toPrintWriter();
        Logs.Log("Logs >> 初始化 >> 完成");
    }

    public static void Log(String s) {
        Logs.Log.Log(s);
    }

    public static void Save() {
        Logs.Log.Save();
    }

    public static void Log(Throwable t) {
        t.printStackTrace(PW);
        Log.Save();
    }
}
