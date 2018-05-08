rm differences.txt
numFailing=0
java -cp "../" JavaCompiler EnumTest.java
diff EnumTest.jil expected/EnumTest.jil >> differences.txt
if [ `echo $?` -eq "1" ] ; then
	echo "EnumTest.java\ failed!"
	numFailing=$numFailing+1
fi
rm -f EnumTest.jil
numTotal=1
passed=$(($numTotal - $numFailing))
echo "$passed / $numTotal tests passed"
