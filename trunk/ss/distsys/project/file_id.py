#!/usr/bin/python

#Can be used to ID file or directory
class file_identifier(object):

	def __init__(self, path, name):
		self.path = path
		self.name = name
	

	#Parse list back into object
	@classmethod
	def fromOne(cls, rpcDict):
		return cls(rpcDict['path'], rpcDict['name'])

	#No error checking for a slash between paht and name, done in client for the moment
	def fileString(self):
		return self.path + self.name
		
	#For debugging
	def __repr__(self):
		return  "File ID = " + self.path + " : " + self.name