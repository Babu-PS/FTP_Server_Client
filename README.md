# FTP_Server_Client
This Server Client is simply programmed to Transfer a file from server to client upon request.
The Server takes arguments as Port Number(Port address the client can connect) and Access Folder(Directory for the client to search his required file)
The Client takes arguments as IPaddress(Required Server), Port Number(Address on which Server is Listening), Number of Files Required, Names of the Files and provide the last Argument as the "Directory to Which the files should be downloaded".
Multiple Clients can connect to the server because of the Use of Threading concept in Server
Every Client should have a Security store that should match with the Server's one in order to proceed the Transfer.
The Information of CLient and Server are logged on to the log file by using Log4j
