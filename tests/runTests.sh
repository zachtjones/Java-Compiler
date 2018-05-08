#!/bin/bash

rm differences.txt

java -cp "../" JavaCompiler EnumTest.java
diff EnumTest.jil expected/EnumTest.jil >> differences.txt
if [ `echo $?` -eq "1" ] ; then
	echo "EnumTest.java failed"
fi
rm -f EnumTest.jil



