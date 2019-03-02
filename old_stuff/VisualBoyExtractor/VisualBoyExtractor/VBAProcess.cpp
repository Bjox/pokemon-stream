#include "VBAProcess.h"


VBAProcess::VBAProcess()
{
}


VBAProcess::~VBAProcess()
{
}


bool VBAProcess::open()
{
	wHandle = FindWindow(NULL, windowTitle.c_str());
	if (!wHandle) {
		return false;
	}

	GetWindowThreadProcessId(wHandle, &pid);
	pHandle = OpenProcess(PROCESS_VM_READ, FALSE, pid);
	if (!pHandle) {
		return false;
	}

	openState = true;
	return true;
}


bool VBAProcess::close()
{
	bool closeRes = CloseHandle(pHandle);
	if (closeRes) {
		openState = false;
	}
	return closeRes;
}


bool VBAProcess::isOpen()
{
	return openState;
}


int VBAProcess::WRAMStartAddress()
{
	int wramPtrAdr = 0x6778E8; // The magic number
	int wramAdr;
	readInt(wramPtrAdr, &wramAdr);
	return wramAdr;
}


void VBAProcess::readWRAM(signed char* buffer, size_t size)
{
	if (size < 0x40000) {
		throw "Provided buffer is too small. Minimum size is 0x40000.";
	}
	readBytes(WRAMStartAddress(), buffer, 0x40000);
}


void VBAProcess::readByte(int address, signed char* buffer)
{
	ReadProcessMemory(pHandle, (LPCVOID)address, buffer, 1UL, NULL);
}


void VBAProcess::readBytes(int address, signed char* buffer, size_t length)
{
	ReadProcessMemory(pHandle, (LPCVOID)address, buffer, length, NULL);
}


void VBAProcess::readInt(int address, int* buffer)
{
	ReadProcessMemory(pHandle, (LPCVOID)address, buffer, sizeof(unsigned), NULL);
}


