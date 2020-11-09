package iamn5.edpn.framework.utils;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.*;
import com.sun.jna.win32.W32APIOptions;

public class Processes {
    public static boolean isEDRunning() {
        return isProcessRunning("EliteDangerous");
    }

    public static boolean isProcessRunning(String processName) {
        Kernel32 kernel32 = Native.load(Kernel32.class, W32APIOptions.UNICODE_OPTIONS);

        Tlhelp32.PROCESSENTRY32.ByReference pEntry = new Tlhelp32.PROCESSENTRY32.ByReference();
        WinNT.HANDLE hSnap = kernel32.CreateToolhelp32Snapshot(Tlhelp32.TH32CS_SNAPPROCESS, new WinDef.DWORD(0));

        if (kernel32.Process32First(hSnap, pEntry)) {
            do {
                if (String.valueOf(pEntry.szExeFile).contains(processName)) {
                    return true;
                }

            } while (kernel32.Process32Next(hSnap, pEntry));
        }

        return false;
    }
}
