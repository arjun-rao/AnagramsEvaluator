#!/usr/bin/env python
import sys, signal, os, subprocess, syslog


# Verdicts
verdict = {'CORRECT' : 0, 'COMPILE_ERROR' : 1, 'WRONG' : 2, 'TIME_EXCEEDED' : 3, 'ILLEGAL_FILE' : 4}

# Execution time limit
TIME_LIMIT = 2

p = None

# All debug messages come through this
def debug(msg):
	#print(msg)
	pass

def cleanup():
	os.remove("./com/google/engedu/anagrams/AnagramDictionary.java")	


# This is called in case the program takes too long to execute
def alarm_callback(signum, frame):
	p.terminate()
	print("Time out")
	cleanup()
	sys.exit(verdict['TIME_EXCEEDED'])

# Lenient file comparator
def compare_files(a, b):
	fileA = open(a, "r")
	fileB = open(b, "r")

	linesA = fileA.readlines()
	linesB = fileB.readlines()

	fileA.close()
	fileB.close()

	if(len(linesA) != len(linesB)):
		return False

	for i in range(0, len(linesA)):
		lineA = linesA[i].strip()
		lineB = linesB[i].strip()
		if(lineA != lineB):
			return False

	return True

# Parse commandline options
sourcefile = "AnagramTest.java"
problem = ""
path = "./"

ext = sourcefile.split(".")[1]	

# Return ILLEGAL_FILE if extension is not recognized
if ext not in ["java"]:
	print("Illegal file")
	sys.exit(verdict['ILLEGAL_FILE'])

# Make sure it is a java file extension
if(ext == "java"):
	compile = "javac -cp . %s 2> /dev/null" % (sourcefile)

if(ext in ["java"]):
	debug(compile)

	# Compile
	r = os.system(compile)
	if(r):
		print("")
		cleanup()
		sys.exit(verdict['COMPILE_ERROR'])

file = sourcefile.split("/")[-1]
outfile = "out.txt"


#Set up run params
if(ext == "java"):
	file = file.split(".")[0]	
	run = ["java", file]


# Run
debug("Running...")
signal.signal(signal.SIGALRM, alarm_callback)
signal.alarm(TIME_LIMIT)
p = subprocess.Popen(run, stdout=open(outfile,"w"), stderr=open("/dev/null", "w"), cwd=path)
r = os.waitpid(p.pid, 0)[1]
signal.alarm(0)
debug("Exit status : %d" % r)

# Compare output with expected output
cleanup()
outputProduced = "out.txt"
outputExpected = "expected.txt"


if( compare_files(outputProduced, outputExpected) == True ):
	print(sys.argv[1])	
	os.remove("./out.txt")
	sys.exit(verdict['CORRECT'])
else:
	print("Wrong:"+sys.argv[1])	
	os.remove("./out.txt")
	sys.exit(verdict['WRONG'])


