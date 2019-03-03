package pokemoninfodisplayer.process.windows;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.StdCallLibrary;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public interface Kernel32 extends StdCallLibrary {
	
	public Kernel32 INSTANCE = (Kernel32) Native.loadLibrary("kernel32", Kernel32.class);
	
    //BOOL WINAPI WriteProcessMemory(  
    //__in   HANDLE hProcess,  
    //__in   LPVOID lpBaseAddress,  
    //__in   LPCVOID lpBuffer,  
    //__in   SIZE_T nSize,  
    //__out  SIZE_T *lpNumberOfBytesWritten  
    //);
    public int WriteProcessMemory(Pointer p, long address, Object buffer, int size, IntByReference written);  
      
    //BOOL WINAPI ReadProcessMemory(  
    //__in   HANDLE hProcess,  
    //__in   LPCVOID lpBaseAddress,  
    //__out  LPVOID lpBuffer,  
    //__in   SIZE_T nSize,  
    //__out  SIZE_T *lpNumberOfBytesRead  
    //);  
    public int ReadProcessMemory(Pointer hProcess, long inBaseAddress, Object outputBuffer, int nSize, IntByReference outNumberOfBytesRead);  
     
     
    //HANDLE WINAPI OpenProcess(  
    //__in  DWORD dwDesiredAccess,  
    //__in  BOOL bInheritHandle,  
    //__in  DWORD dwProcessId  
    //);  
    public Pointer OpenProcess(int desired, boolean inherit, int pid);
	
	//BOOL WINAPI CloseHandle(
	//_In_ HANDLE hObject
	//);
	public int CloseHandle(Pointer handle);
}
