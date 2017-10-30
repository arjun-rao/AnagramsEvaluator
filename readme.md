## Anagrams Evaluator

This repo contains scripts that automatically evaluate submissions for 
g.co/cswithandroid's Anagrams unit. The same can be repurposed for other units as well. The only modification required would be the Package Structure and changes made to the unit test in AnagramTest.java file. This doesn't use a unit testing framework like jUnit, but rather tests code in a more manual way. 

## Setup

* Save student submissions to submissions/ directory, and name the files as '<studentName/ID>.<Filename>.java' where 'Filename' can be the original filename submitted by the student. 

* run `python gradeanagrams.py` in your terminal

## Interpretting the results

When `gradeanagrams.py' completes execution, the list of student IDs whose submissions are correct will be printed on the screen and those which are wrong will have the string 'error:' prefixed to the student ID and printed on the screen. The same results are appended to the verdict.txt file. Make sure to save the verdict file after running the script. 

