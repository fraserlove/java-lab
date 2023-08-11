# P2P File Sharing
A simple P2P file sharing application, allowing multiple peers running separate instances of this application to send and recieve files over a network using TCP. The application features a client to provide a simple text-based UI and a server to handle ingoing and outgoing requests. A list of the features of this application are given below:
- Allows for files of any file type to be shared between peers.
- Directories in which to download and share files are configurable.
- Provide a dynamic list of current peers on the network and request a list of downloadable files from each peer.
- Reliably closes connections with clients which exit the network undexpectedly.
- Search over all peers in the network for a file by its file name and return a list of peers sharing the file.

### Usage
The application can be compiled and ran using the following commands inside the `/src` directory.
```bash
javac FileShareMain.java
```
Then ran with the following command:
```bash
java FileShareMain
```
The program has default `saved` and `shared` directories at the directories `../p2p-files/saved` and `../p2p-files/shared`
respectively. Files are saved and shared here. Therefore in order to share a file, the file must be present within the 
peers `shared` directory.

The program supports file transfer of any file type since it sends the file over in byte arrays.

A peer that is running an instance of the same application can be connected to as follows:
```bash
connect "255.255.255.255"
connect "ffff::ffff::ffff::ffff::ffff"
connect "host.cs.st-andrews.ac.uk"
```
Where `255.255.255.255` and `xxxx::xxxx::xxxx::xxxx::xxxx` can be any IPv4 and IPv6 address respectively.
Either way of connecting is acceptable, however assure that you have included either single or double quotation
marks around the peer address for it to be accepted as an argument.

All arguments, in fact, should be wrapped in quotation marks, this allows the application to parse them correctly.

To cleanly exit the application the `exit` command can be ran.

### Commands
To see a list of available commands and a description of their function, type `list` into the command line. The commands
shown by `list` will change between connecting and disconnecting to a peer. For instance the `disconnect` command will 
only be available if connected to a peer.

#### Base commands (can be ran at any point)
|Command|Usage|Function|
|:---|:---|:---|
|`exit`| |Quit the application.|
|`downloads`| |List all files downloaded that are present in the saved directory.|
|`dirs`| |Show the location of the shared and saved directories.|
|`change-dirs`|`change-dirs "../saved_dir" "../shared_dir"`| Changes the location where files are saved and shared.|
|`serving`| |List peers that are currently connected to this client.|
|`peers`| |List all peers on the local network.|
|`search`|`search "file.ext"`|List all peers on the network hosting a specific file.|
|`list`| |list all commands available to the user.|

#### Connected commands (can only be ran when connected to a peer)
|Command|Usage|Function|
|:---|:---|:---|
|`disconnect`| |Disconnect the peer currently connected to.|
|`get`|`get "file.ext"`|Download the specified file from the peer currently connected to.|
|`available`| |List files shared by the peer and are available to download.|

#### Disconnected commands (can only be ran when disconnected)
|Command|Usage|Function|
|:---|:---|:---|
|`connect`| `connect "255.255.255.255"` |Connect to a peer with the specified address.|

### Notes
The `search` and `peers` commands scan for instances of the application of files on the local network. It 
only scans computers the netmask `255.255.255.0`. Therefore, when running these commands, make sure that the application is running on two computers with the same IPv4 address up to the last byte.