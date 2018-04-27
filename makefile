all:
	javacc JavaParser.jj
	javac JavaCompiler.java

clean:
	rm -f JavaParserConstants.java
	rm -f JavaParserTokenManager.java
	rm -f ParseException.java
	rm -f SimpleCharStream.java
	rm -f Token.java
	rm -f TokenMgrError.java
	rm -f *.class
	rm -f tree/*.class

