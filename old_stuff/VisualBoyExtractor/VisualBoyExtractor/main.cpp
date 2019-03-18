#include <iostream>
#include <Windows.h>
#include <iomanip>
#include "VBAProcess.h"

#define HEX( x ) std::uppercase << std::setw(2) << std::setfill('0') << std::hex << (unsigned)(x)

void printBuffer(unsigned char* buffer, size_t length)
{
	for (unsigned i = 0; i < length; i++) {
		std::cout << HEX(buffer[i]) << " ";
	}
	std::cout << std::endl;
}


