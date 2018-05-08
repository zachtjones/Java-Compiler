#!/bin/bash

echo "rm differences.txt" > runTests.sh
total=0
echo "numFailing=0" >> runTests.sh

# get the files in the current directory
for file in *.java; do
	echo "java -cp \"../\" JavaCompiler $file" >> runTests.sh
	jilName=${file/.java/.jil} # replace .java with .jil
	echo "diff $jilName expected/$jilName >> differences.txt" >> runTests.sh
	echo "if [ \`echo \$?\` -eq \"1\" ] ; then" >> runTests.sh
	echo "	echo \"$file\ failed!\"" >> runTests.sh
	echo "	numFailing=\$numFailing+1" >> runTests.sh
	echo "fi" >> runTests.sh
	echo "rm -f \$jilName" >> runTests.sh
	total=$(($total + 1))
done

echo "numTotal=$total" >> runTests.sh

echo "passed=\$((\$numTotal - \$numFailing))" >> runTests.sh
echo "echo \"\$passed / \$numTotal tests passed\"" >> runTests.sh

chmod +x runTests.sh

