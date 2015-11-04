package umlparser.umlparser;

import java.io.*;
import java.util.*;

import com.github.javaparser.*;
import com.github.javaparser.ast.*;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.*;

import net.sourceforge.plantuml.SourceStringReader;

public class ParseSeqEngine {
    String pumlCode;
    final String inPath;
    final String outPath;
    final String inFuncName;
    final String inClassName;

    HashMap<String, String> mapMethodClass;
    ArrayList<CompilationUnit> cuArray;
    HashMap<String, ArrayList<MethodCallExpr>> mapMethodCalls;

    ParseSeqEngine(String inPath, String inClassName, String inFuncName,
            String outFile) {
        this.inPath = inPath;
        this.outPath = inPath + "\\" + outFile + ".png";
        this.inClassName = inClassName;
        this.inFuncName = inFuncName;
        mapMethodClass = new HashMap<String, String>();
        mapMethodCalls = new HashMap<String, ArrayList<MethodCallExpr>>();
        pumlCode = "@startuml\n";
    }

    public void start() throws Exception {
        cuArray = getCuArray(inPath);
        buildMaps();
        pumlCode += "actor user #black\n";
        pumlCode += "user" + " -> " + inClassName + " : " + inFuncName + "\n";
        pumlCode += "activate " + mapMethodClass.get(inFuncName) + "\n";
        parse(inFuncName);
        pumlCode += "@enduml";
        generateDiagram(pumlCode);
        System.out.println("Plant UML Code:\n" + pumlCode);
    }

    private void parse(String callerFunc) {

        for (MethodCallExpr mce : mapMethodCalls.get(callerFunc)) {
            String callerClass = mapMethodClass.get(callerFunc);
            String calleeFunc = mce.getName();
            String calleeClass = mapMethodClass.get(calleeFunc);
            if (mapMethodClass.containsKey(calleeFunc)) {
                pumlCode += callerClass + " -> " + calleeClass + " : "
                        + mce.toStringWithoutComments() + "\n";
                pumlCode += "activate " + calleeClass + "\n";
                parse(calleeFunc);
                pumlCode += calleeClass + " -->> " + callerClass + "\n";
                pumlCode += "deactivate " + calleeClass + "\n";
            }
        }
    }

    private void buildMaps() {
        for (CompilationUnit cu : cuArray) {
            String className = "";
            List<TypeDeclaration> td = cu.getTypes();
            for (Node n : td) {
                ClassOrInterfaceDeclaration coi = (ClassOrInterfaceDeclaration) n;
                className = coi.getName();
                for (BodyDeclaration bd : ((TypeDeclaration) coi)
                        .getMembers()) {
                    if (bd instanceof MethodDeclaration) {
                        MethodDeclaration md = (MethodDeclaration) bd;
                        ArrayList<MethodCallExpr> mcea = new ArrayList<MethodCallExpr>();
                        for (Object bs : md.getChildrenNodes()) {
                            if (bs instanceof BlockStmt) {
                                for (Object es : ((Node) bs)
                                        .getChildrenNodes()) {
                                    if (es instanceof ExpressionStmt) {
                                        if (((ExpressionStmt) (es))
                                                .getExpression() instanceof MethodCallExpr) {
                                            mcea.add(
                                                    (MethodCallExpr) (((ExpressionStmt) (es))
                                                            .getExpression()));
                                        }
                                    }
                                }
                            }
                        }
                        mapMethodCalls.put(md.getName(), mcea);
                        mapMethodClass.put(md.getName(), className);
                    }
                }
            }
        }
        //printMaps();
    }

    private ArrayList<CompilationUnit> getCuArray(String inPath)
            throws Exception {
        File folder = new File(inPath);
        ArrayList<CompilationUnit> cuArray = new ArrayList<CompilationUnit>();
        for (final File f : folder.listFiles()) {
            if (f.isFile() && f.getName().endsWith(".java")) {
                FileInputStream in = new FileInputStream(f);
                CompilationUnit cu;
                try {
                    cu = JavaParser.parse(in);
                    cuArray.add(cu);
                } finally {
                    in.close();
                }
            }
        }
        return cuArray;
    }

    private String generateDiagram(String source) throws IOException {

        OutputStream png = new FileOutputStream(outPath);
        SourceStringReader reader = new SourceStringReader(source);
        String desc = reader.generateImage(png);
        return desc;

    }

    @SuppressWarnings("unused")
    private void printMaps() {
        System.out.println("mapMethodCalls:");
        Set<String> keys = mapMethodCalls.keySet(); // get all keys
        for (String i : keys) {
            System.out.println(i + "->" + mapMethodCalls.get(i));
        }
        System.out.println("---");
        keys = null;

        System.out.println("mapMethodClass:");
        keys = mapMethodClass.keySet(); // get all keys
        for (String i : keys) {
            System.out.println(i + "->" + mapMethodClass.get(i));
        }
        System.out.println("---");
    }

}
