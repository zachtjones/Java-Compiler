rm differences.txt
numFailing=0
java -cp "../" JavaCompiler BasicClass.java
diff BasicClass.jil expected/BasicClass.jil >> differences.txt
if [ `echo $?` -eq "1" ] ; then
	echo "BasicClass.java failed!"
	let numFailing=$numFailing+1
fi
rm -f BasicClass.jil
java -cp "../" JavaCompiler BasicClassQualified.java
diff BasicClassQualified.jil expected/BasicClassQualified.jil >> differences.txt
if [ `echo $?` -eq "1" ] ; then
	echo "BasicClassQualified.java failed!"
	let numFailing=$numFailing+1
fi
rm -f BasicClassQualified.jil
java -cp "../" JavaCompiler EnumTest.java
diff EnumTest.jil expected/EnumTest.jil >> differences.txt
if [ `echo $?` -eq "1" ] ; then
	echo "EnumTest.java failed!"
	let numFailing=$numFailing+1
fi
rm -f EnumTest.jil
java -cp "../" JavaCompiler HelloWorld.java
diff HelloWorld.jil expected/HelloWorld.jil >> differences.txt
if [ `echo $?` -eq "1" ] ; then
	echo "HelloWorld.java failed!"
	let numFailing=$numFailing+1
fi
rm -f HelloWorld.jil
java -cp "../" JavaCompiler MorseVowel.java
diff MorseVowel.jil expected/MorseVowel.jil >> differences.txt
if [ `echo $?` -eq "1" ] ; then
	echo "MorseVowel.java failed!"
	let numFailing=$numFailing+1
fi
rm -f MorseVowel.jil
java -cp "../" JavaCompiler OpOrderTest.java
diff OpOrderTest.jil expected/OpOrderTest.jil >> differences.txt
if [ `echo $?` -eq "1" ] ; then
	echo "OpOrderTest.java failed!"
	let numFailing=$numFailing+1
fi
rm -f OpOrderTest.jil
numTotal=6
let passed=$numTotal-$numFailing
echo "$passed / $numTotal tests passed"
