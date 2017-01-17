package il.org.spartan.ispartanizer.tippers;

import com.intellij.psi.*;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.testFramework.PsiTestCase;
import il.org.spartan.ispartanizer.auxilary_layer.Wrapper;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * @author Oren Afek
 * @since 26/12/16
 */
public abstract class TipperTest extends PsiTestCase {

    private static final String dummyTestFileName = "test.java";
    private static final String emptyText = "";

    private PsiFile getTestFile() {
        return createDummyFile(dummyTestFileName, emptyText);
    }

    private PsiElementFactory getTestFactory() {
        return JavaPsiFacade.getElementFactory(getTestFile().getProject());
    }

    protected PsiStatement createTestStatementFromString(String s) {
        return getTestFactory().createStatementFromText(s, getTestFile());
    }

    protected PsiExpression createTestExpressionFromString(String s) {
        return getTestFactory().createExpressionFromText(s, getTestFile());
    }

    protected PsiClass createTestClassFromString(String javadoc, String className, String classBody, String... classModifiers) {
        StringBuilder sb = new StringBuilder();
        Arrays.stream(classModifiers).forEach(m -> sb.append(m));
        sb.append("class");
        sb.append(className);
        sb.append("{");
        sb.append(classBody);
        sb.append("}");
        return getTestFactory().createClassFromText(sb.toString(), getTestFile());
    }

    protected PsiClass createTestInterfaceFromString(String interfaceName, String interfaceBody, String... interfaceModifiers) {
        StringBuilder sb = new StringBuilder();
        Arrays.stream(interfaceModifiers).forEach(m -> sb.append(m));
        sb.append("interface");
        sb.append(interfaceName);
        sb.append("{");
        sb.append(interfaceBody);
        sb.append("}");
        return getTestFactory().createClassFromText(sb.toString(), getTestFile());
    }

    protected PsiClass createEnumClassFromString(String s) {
        return getTestFactory().createEnum(s);
    }

    protected PsiClass createInterfaceClassFromString(String s) {
        return getTestFactory().createInterface(s);
    }

    protected PsiBlockStatement createTestBlockStatementFromString(String s) {
        return (PsiBlockStatement) getTestFactory().createStatementFromText(s, getTestFile());
    }

    /**
     * doesn't work if there are several classes inside each other.
     *
     * @param f
     * @return
     */
    protected PsiClass getClassInFile(PsiFile f) {
        Wrapper<PsiClass> classWrapper = new Wrapper<>(null);
        f.acceptChildren(new JavaElementVisitor() {
            @Override
            public void visitClass(PsiClass aClass) {
                classWrapper.set(aClass);
            }
        });
        return classWrapper.get();
    }

    protected PsiMethod createTestMethodFromString(String s) {
        return getTestFactory().createMethodFromText(s, getTestFile());
    }

    protected PsiEnumConstant createTestEnumFromString(String s) {
        return getTestFactory().createEnumConstantFromText(s, getTestFile());
    }

    protected PsiDeclarationStatement createTestEnumDecFromString(String s) {
        return (PsiDeclarationStatement) getTestFactory().createStatementFromText(s, getTestFile());
    }

    protected PsiAnnotation createTestAnnotationFromString(String s) {
        return getTestFactory().createAnnotationFromText(s, getTestFile());
    }

    protected PsiType createTestTypeFromString(String s) {
        return getTestFactory().createTypeElementFromText(s, getTestFile()).getType();
    }

    protected PsiField createTestFieldDeclarationFromString(String s) {
        return getTestFactory().createFieldFromText(s, getTestFile());
    }

    protected PsiTypeElement createTestTypeElementFromString(String s) {
        return getTestFactory().createTypeElementFromText(s, getTestFile());
    }

    protected PsiIdentifier createTestIdentifierFromString(String s) {
        return getTestFactory().createIdentifier(s);
    }

    protected PsiDocComment createTestDocCommentFromString(String s) {
        return getTestFactory().createDocCommentFromText(s);
    }

    protected PsiComment createTestCommentFromString(String s) {
        return getTestFactory().createCommentFromText(s, getTestFile());
    }

    protected PsiWhileStatement createTestWhileStatementFromString(String s) {
        return (PsiWhileStatement) getTestFactory().createStatementFromText(s, getTestFile());
    }

    protected PsiDoWhileStatement createTestDoWhileStatementFromString(String s) {
        return (PsiDoWhileStatement) getTestFactory().createStatementFromText(s, getTestFile());
    }

    protected PsiSwitchStatement createTestSwitchStatementFromString(String s) {
        return (PsiSwitchStatement) getTestFactory().createStatementFromText(s, getTestFile());
    }

    protected PsiForStatement createTestForStatementFromString(String s) {
        return (PsiForStatement) getTestFactory().createStatementFromText(s, getTestFile());
    }

    protected PsiForeachStatement createTestForeachStatementFromString(String s) {
        return (PsiForeachStatement) getTestFactory().createStatementFromText(s, getTestFile());
    }

    protected PsiImportList createTestImportListFromString(String s) {
        PsiFile file = createTestFileFromString(s +
                "public class A{}");
        PsiElement importList = file.getNavigationElement().getFirstChild();
        return (PsiImportList) importList;
    }

    protected PsiCodeBlock createTestCodeBlockFromString(String s) {
        return getTestFactory().createCodeBlockFromText(s, getTestFile());
    }

    protected PsiFile createTestFileFromString(String s) {
        return createDummyFile(dummyTestFileName, s);
    }

    protected PsiLiteralExpression createTestNullExpression() {
        return (PsiLiteralExpression) getTestFactory().createExpressionFromText("null", getTestFile());
    }

    protected PsiType createTestType(String s) {
        return getTestFactory().createType(getTestFactory().createClass(s));
    }

    protected PsiDeclarationStatement createTestDeclarationStatement(String name, String type, String initializer) {
        PsiType t = createTestType(type);
        PsiExpression i = createTestExpressionFromString(initializer);
        return getTestFactory().createVariableDeclarationStatement(name, t, i);
    }

    protected PsiIfStatement createTestIfStatement(String cond, String then) {
        return (PsiIfStatement) getTestFactory()
                .createStatementFromText("if (" + cond + ") {" + then + "} ", getTestFile());
    }

    protected PsiConditionalExpression createTestConditionalExpression(String cond, String then, String else$) {
        return (PsiConditionalExpression) getTestFactory()
                .createExpressionFromText(cond + " ? " + then + " : " + else$, getTestFile());
    }

    protected void printPsi(PsiElement e) {
        Wrapper<Integer> tabs = new Wrapper<>(0);
        e.accept(new JavaRecursiveElementVisitor() {
            @Override
            public void visitElement(PsiElement element) {
                IntStream.range(0, tabs.get()).forEach(x -> System.out.print("\t"));
                System.out.println(element);
                tabs.set(tabs.get() + 1);
                super.visitElement(element);
            }
        });
    }

    protected PsiMethodCallExpression createTestMethodCallExpression(String methodName, String... args) {
        StringBuilder sb = new StringBuilder();
        sb.append(methodName).append("(");
        Arrays.stream(args).forEach(f -> sb.append(f));
        sb.append(")");
        return (PsiMethodCallExpression) getTestFactory()
                .createExpressionFromText(sb.toString(), getTestFile());
    }

    protected PsiExpression createTestExpression(String expression) {
        return getTestFactory()
                .createExpressionFromText(expression, getTestFile());
    }

    protected PsiTryStatement createTestTryStatement(String try$, String catch$, String do$) {
        return (PsiTryStatement) getTestFactory().createStatementFromText("try {" + try$ + "} catch(" + catch$ + "){" + do$ + "}", getTestFile());
    }

    protected PsiTryStatement createTestTryStatement(String try$, String catch$, String do$, String finally$) {
        return (PsiTryStatement) getTestFactory().createStatementFromText("try {" + try$ + "} catch(" + catch$ + "){" + do$ + "} finally {" + finally$ + "}", getTestFile());
    }

    protected boolean equalsByText(PsiElement e1, PsiElement e2) {
        return (e1 == null && e2 == null) || (e1 != null && e2 != null && e1.getText().equals(e2.getText()));
    }

    protected void assertEqualsByText(PsiElement e1, PsiElement e2) {
        if (e1 == null && e2 == null) {
            return;
        }

        assertNotNull(e1);
        assertNotNull(e2);
        assertEquals(removeWhiteSpaces(e1.getText()), removeWhiteSpaces(e2.getText()));
    }

    protected String removeWhiteSpaces(String s) {
        return s.replaceAll("\\s+", "");
    }

    protected PsiLambdaExpression createTestLambdaExpression(String s) {
        return (PsiLambdaExpression) getTestFactory().createExpressionFromText(s, getTestFile());
    }

    protected PsiNewExpression createTestNewExpression(String typeName, String... parametes) {
        StringBuilder sb = new StringBuilder();
        sb.append("new ")
                .append(typeName)
                .append("(");
        Arrays.stream(parametes).forEach(s -> sb.append(s));
        sb.append(")");
        return (PsiNewExpression) getTestFactory().createExpressionFromText(sb.toString(), getTestFile());
    }

    protected PsiMethodReferenceExpression createTestMethodReferenceEpression(String typeName, String methodName) {
        StringBuilder sb = new StringBuilder();
        sb.append(typeName)
                .append("::")
                .append(methodName);
        return (PsiMethodReferenceExpression) getTestFactory().createExpressionFromText(sb.toString(), getTestFile());
    }

    protected PsiRequiresStatement createTestRequiresStatement(String module) {
        return (PsiRequiresStatement) getTestFactory().createStatementFromText("requires " + module + ";", getTestFile());
    }

    protected class Pair<T, K> {

        public T first;
        public K second;

        public Pair() {
            this(null, null);
        }

        public Pair(T first, K second) {
            this.first = first;
            this.second = second;
        }
    }
}
