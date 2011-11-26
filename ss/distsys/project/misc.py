#!/usr/bin/python

import json
msg_size = 1024


def read_config(filename):
	config = open(filename)
	config = json.loads(config.read())
	return config
