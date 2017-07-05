// Client
#define _WINSOCK_DEPRECATED_NO_WARNINGS 

#pragma comment(lib, "ws2_32.lib")
#include <WinSock2.h>
#include <iostream>
#include <sstream>

SOCKET Connection;

void GetBuffer() {
	char buffer[256];
	while (true) {
		recv(Connection, buffer, sizeof(buffer), NULL);
		std::cout << buffer << std::endl;
	}
}

int main() {

	char *name;
	std::cout << "Please enter your name: ";
	std::cin >> name;
	// Winsock Startup
	WSAData wsaData;
	WORD DllVersion = MAKEWORD(2, 1);
	if (WSAStartup(DllVersion, &wsaData) != 0) // if WSAStartup returns anything other than 0, then pop an error
	{
		MessageBoxA(NULL, "Winsock startup failed", "Error", MB_OK | MB_ICONERROR);
		exit(EXIT_FAILURE);
	}

	SOCKADDR_IN addr; // Address that we will bind our listening socket to
	int addrlen = sizeof(addr); // Length of the address (required for accept call)
	addr.sin_addr.s_addr = inet_addr("127.0.0.1"); // Server address = localhost (this pc)
	addr.sin_port = htons(1111); // Port
	addr.sin_family = AF_INET; // IPv4 Socket

	Connection = socket(AF_INET, SOCK_STREAM, NULL); // Set Connection socket
	if (connect(Connection, (SOCKADDR*)&addr, addrlen) != 0) {
		MessageBoxA(NULL, "Failed to connect", "Error" , MB_OK | MB_ICONERROR);
		exit(EXIT_FAILURE);
	} 

	std::cout << "Connected!" << std::endl; 
	char MOTD[25];
	recv(Connection, MOTD, sizeof(MOTD), NULL);
	std::cout << MOTD << std::endl;
	char buffer[65536];
	do{
		std::cin.getline(buffer, sizeof(buffer));
		send(Connection, buffer, sizeof(buffer), NULL);
		CreateThread(NULL, NULL, (LPTHREAD_START_ROUTINE)GetBuffer, NULL, NULL, NULL);
	}while (buffer != "exit");
	system("pause");

	return 0;
}