// Server
#define _WINSOCK_DEPRECATED_NO_WARNINGS

#pragma comment(lib,"ws2_32.lib")
#include <WinSock2.h>
#include <iostream>

SOCKET Connections[3];
int connectionCounter = 0;

void ClientHandlerThread(int index) {
	char buffer[65536];
	while (true) {
		recv(Connections[index], buffer, sizeof(buffer), NULL);
		for (int i = 0; i < connectionCounter; i++) {
			if (i == index)
				continue;
			else
				send(Connections[i], buffer, sizeof(buffer), NULL);
		}
		Sleep(500);
	}
}

int main() {

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
	addr.sin_addr.s_addr = inet_addr("127.0.0.1"); // Broadcast address
	addr.sin_port = htons(1111); // Port
	addr.sin_family = AF_INET; // IPv4 Socket

	SOCKET sListen = socket(AF_INET, SOCK_STREAM, NULL); // Create a socket to listen for new connections
	bind(sListen, (SOCKADDR *)&addr, sizeof(addr)); // Bind the address to the socket
	listen(sListen, SOMAXCONN); // Places sListen socket in a state in which it is listening for an incoming connection

	SOCKET newConnection;
	for (int i = 0; i < 100; i++) {
		if ((newConnection = accept(sListen, (SOCKADDR *)&addr, &addrlen)) == 0) {
			std::cerr << "Failed to accept the client's connection" << std::endl;
		}
		else {
			std::cout << "Client connected!" << std::endl;
			char MOTD[25] = "Welcome to the Grid App!";
			send(newConnection, MOTD, sizeof(MOTD), NULL);
			Connections[i] = newConnection;
			connectionCounter += 1;
			CreateThread(NULL, NULL, (LPTHREAD_START_ROUTINE)ClientHandlerThread, (LPVOID)(i), NULL, NULL);
		}
	}
	system("pause");
	return 0;
}