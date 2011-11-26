#!/usr/bin/python

import json

def read_config(filename):
	config = open(filename)
	config = json.loads(config.read())
	return config
