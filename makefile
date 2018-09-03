all:
	cd main; javacc JavaParser.jj
	javac main/JavaCompiler.java

clean:
	rm -f main/JavaParserConstants.java
	rm -f main/JavaParserTokenManager.java
	rm -f main/JavaParser.java
	rm -f main/ParseException.java
	rm -f main/SimpleCharStream.java
	rm -f main/Token.java
	rm -f main/TokenMgrError.java
	rm -f main/*.class
	rm -f tree/*.class
	rm -f helper/*.class
	rm -f intermediate/*.class
