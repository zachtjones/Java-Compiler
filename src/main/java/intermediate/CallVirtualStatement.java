package intermediate;

import java.util.Arrays;
import java.util.HashMap;

import helper.CompileException;
import helper.UsageCheck;
import main.JavaCompiler;
import x64.X64File;
import x64.X64Function;
import x64.jni.GetObjectClassJNI;
import x64.operands.X64PreservedRegister;

/** Represents a function call via v-table lookup. */
public class CallVirtualStatement implements InterStatement, GetObjectClassJNI {
	private final Register obj;
	String name;
	private final Register[] args;
	private final Register returnVal;
	
	private final String fileName;
	private final int line;
	
	public CallVirtualStatement(Register obj, String name, Register[] args, Register returnVal,
			String fileName, int line) {
		
		this.obj = obj;
		this.name = name;
		this.args = args;
		this.returnVal = returnVal;
		this.fileName = fileName;
		this.line = line;
	}

	@Override
	public String toString() {
		// use the Arrays.toString and remove '[' and ']', also handle no return
		String leftPart = "callVirtual " + obj + " " + name + "(" + 
		Arrays.toString(args).replaceAll("\\[|\\]", "") + ")";
		if (returnVal != null) {
			return leftPart + " -> " + returnVal + ";";	
		} else {
			return leftPart + ";";
		}
	}

	@Override
	public void typeCheck(HashMap<Register, String> regs, HashMap<String, String> locals,
			HashMap<String, String> params, InterFunction func) throws CompileException {
		
		for (Register r : args) {
			UsageCheck.verifyDefined(r, regs, fileName, line);
		}
		
		if (returnVal != null) {
			// fill in the return type
			InterFile e = JavaCompiler.parseAndCompile(obj.typeFull, fileName, line);
			String returnType = e.getReturnType(name, args);
			
			if (returnType == null) {
				StringBuilder signature = new StringBuilder(name + "(");
				for (int i = 0; i < args.length; i++) {
					signature.append(args[i].typeFull);
					if (i != args.length - 1) {
						signature.append(',');
					}
				}
				signature.append(")");
				throw new CompileException("no method found with signature, " + signature.toString()
						+ ", referenced", fileName, line);
			}
			
			returnVal.typeFull = returnType;
			regs.put(returnVal, returnVal.typeFull);
		}
	}

	@Override
	public void compile(X64File assemblyFile, X64Function function) throws CompileException {
		// if the type of the register is java/*, use JNI
		if (obj.typeFull.startsWith("java/")) {
			// clazz = GetClass
			X64PreservedRegister clazz =
				addGetObjectClass(function, X64PreservedRegister.fromILRegister(obj));

			// methodID =  GetMethodID(JNIEnv *env, jclass clazz, char *name, char *sig);
			// 3 options for the method call
			// %result = Call<type>Method(JNIEnv *env, jobject obj, jmethodID methodID, ...);
			// %result = Call<type>MethodA(JNIEnv *env, jobject obj, jmethodID methodID, const jvalue *args);
			// %result = Call<type>MethodV(JNIEnv *env, jobject obj, jmethodID methodID, va_list args);

			// TODO optimization -- use the first one if there are 3 args or less to the method call without A or V
			// const jvalue* args with A is probably the best way to create it otherwise, although pushing the
			//  extra args onto the stack might not be too bad
		} else {
			// TODO requires adding the virtual function tables to the system of files
			throw new CompileException("V-table lookup not implemented yet.", fileName, line);
		}
	}
}
