#!/usr/bin/env python
import sys, signal, os, subprocess, syslog
from shutil import copyfile

# Place student submissions in the submissions directory and run this script

import os
for filename in os.listdir('submissions'):
	studentid = filename.split('.')[0]
	if 'java' not in filename.split('.'):
		continue;
	copyfile('./submissions/'+filename,'./com/google/engedu/anagrams/AnagramDictionary.java')
	grade = "./checkanagram.py " + studentid +" >> verdict.txt"
	r = os.system(grade)
	if(r):
		print("Error:"+ filename+"\n")
	else: 
		print(filename+"\n")


