#!/usr/bin/python

import SocketServer
import hashlib
from crypto import *
import json
from misc import *

key = ""

#Should really move these into misc
class TCPServer(SocketServer.TCPServer):
	allow_reuse_addrss = True


class DirectoryServer(SocketServer.BaseRequestHandler):

	

if __name__ == "__main__":
	key = (read_config("./config/fs.json"))['key']

	config = (read_config("./config/servers.json"))['servers']['ds']