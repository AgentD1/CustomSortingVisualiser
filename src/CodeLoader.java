import javax.tools.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.*;
import java.util.*;
import java.util.regex.*;

public class CodeLoader {
	public static SortingAlgorithm load(String code) {
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		
		DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
		
		Pattern namePattern = Pattern.compile(".*public class (.*?) implements SortingAlgorithm *\\{.*");
		
		Matcher matcher = namePattern.matcher(code);
		matcher.find();
		
		String name = matcher.group(1);
		
		System.out.println(name);
		
		JavaFileObject file = new JavaSourceFromString(name, code);
		Iterable<? extends JavaFileObject> compilationUnits = List.of(file);
		JavaCompiler.CompilationTask task = compiler.getTask(null, null, diagnostics, null, null, compilationUnits);
		
		boolean success = task.call();
		for (Diagnostic diagnostic : diagnostics.getDiagnostics()) {
			System.out.println(diagnostic.getCode());
			System.out.println(diagnostic.getKind());
			System.out.println(diagnostic.getPosition());
			System.out.println(diagnostic.getStartPosition());
			System.out.println(diagnostic.getEndPosition());
			System.out.println(diagnostic.getSource());
			System.out.println(diagnostic.getMessage(null));
			
		}
		System.out.println("Success: " + success);
		
		if (success) {
			try {
				URLClassLoader classLoader = URLClassLoader.newInstance(new URL[] { new File("").toURI().toURL() });
				
				SortingAlgorithm loadedAlgorithm = (SortingAlgorithm) Class.forName(name, true, classLoader).newInstance();
				
				return loadedAlgorithm;
			} catch (ClassNotFoundException | IllegalAccessException | MalformedURLException | InstantiationException e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}
}

class JavaSourceFromString extends SimpleJavaFileObject {
	final String code;
	
	JavaSourceFromString(String name, String code) {
		super(URI.create("string:///" + name.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
		this.code = code;
	}
	
	@Override
	public CharSequence getCharContent(boolean ignoreEncodingErrors) {
		return code;
	}
}
