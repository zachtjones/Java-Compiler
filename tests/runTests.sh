rm differences.txt
numFailing=0
java -cp "../" JavaCompiler BasicClass.java
diff BasicClass.jil expected/BasicClass.jil >> differences.txt
if [ `echo $?` -eq "1" ] ; then
	echo "BasicClass.java failed!"
	let numFailing=$numFailing+1
fi
rm -f BasicClass.jil
java -cp "../" JavaCompiler EnumTest.java
diff EnumTest.jil expected/EnumTest.jil >> differences.txt
if [ `echo $?` -eq "1" ] ; then
	echo "EnumTest.java failed!"
	let numFailing=$numFailing+1
fi
rm -f EnumTest.jil
numTotal=2
let passed=$numTotal-$numFailing
echo "$passed / $numTotal tests passed"
