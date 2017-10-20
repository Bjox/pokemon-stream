#pragma once

#include <Windows.h>
#include <string>

/* VisualBoyAdvance process reader. */
class VBAProcess
{	
public:
	static VBAProcess* instance;
	const std::string windowTitle = "VisualBoyAdvance";

	HWND wHandle;
	DWORD pid;
	HANDLE pHandle;

	VBAProcess();
	~VBAProcess();

	bool open();
	bool close();
	bool isOpen();

	int WRAMStartAddress();
	void readWRAM(signed char* buffer, size_t size);

	void readByte(int address, signed char* buffer);
	void readBytes(int address, signed char* buffer, size_t length);
	void readInt(int address, int* buffer);

private:
	bool openState;
};

