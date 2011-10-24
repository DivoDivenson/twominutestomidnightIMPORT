#!/usr/bin/python
import xmlrpclib

proxy = xmlrpclib.ServerProxy("http://localhost:8080/")
print proxy.ls()